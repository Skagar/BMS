package com.cfs.BookMyShow.Service;

import com.cfs.BookMyShow.Exception.ResourceNotFoundException;
import com.cfs.BookMyShow.Exception.SeatUnavailableException;
import com.cfs.BookMyShow.Model.*;
import com.cfs.BookMyShow.dto.*;
import com.cfs.BookMyShow.repository.BookingRepository;
import com.cfs.BookMyShow.repository.ShowRepository;
import com.cfs.BookMyShow.repository.ShowSeatRepository;
import com.cfs.BookMyShow.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional //To maintain ACID properties i.e. if any of the functionalities will fail than all the precomputed functionalities will roll back and partial saving will not be there
public class BookingService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private ShowSeatRepository showSeatRepository;
    @Autowired
    private BookingRepository bookingRepository;
    public BookingDto createBooking(BookingRequestDto bookingRequest)
    {
        User user=userRepository.findById(bookingRequest.getUserId()).orElseThrow(()-> new ResourceNotFoundException(("User Not found")));
        Show show=showRepository.findById(bookingRequest.getShowId()).orElseThrow(()-> new ResourceNotFoundException(("Show Not found")));
        List<ShowSeat> selectedSeats=showSeatRepository.findAllById(bookingRequest.getSeatIds());
        if(selectedSeats.size() != bookingRequest.getSeatIds().size())
        {
            throw new ResourceNotFoundException(
                    "One or more seats not found"
            );
        }

        for(ShowSeat seat : selectedSeats)
        {
            if(!seat.getShow().getId().equals(show.getId()))
            {
                throw new SeatUnavailableException(
                        "Seat does not belong to selected show"
                );
            }
        }
        for(ShowSeat seat:selectedSeats)
        {
            if(!"Available".equalsIgnoreCase(seat.getStatus()))
            {
                throw new SeatUnavailableException("Seat"+seat.getSeat().getSeatNumber()+"is not Available");
            }
            seat.setStatus("Locked");
        }
        showSeatRepository.saveAll(selectedSeats);
        Double totalAmount=selectedSeats.stream().mapToDouble(ShowSeat::getPrice).sum();
        Payment payment=new Payment();
        payment.setAmount(totalAmount);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setPaymentMethod(bookingRequest.getPaymentMethod());
        payment.setStatus("Success");
        payment.setTransactionId(UUID.randomUUID().toString());

        Booking booking=new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus("Confirmed");
        booking.setTotalAmount(totalAmount);
        booking.setBookingNumber(UUID.randomUUID().toString());
        booking.setPayment(payment);
        Booking saveBooking =bookingRepository.save(booking);
        selectedSeats.forEach(seat->
        {
            seat.setStatus("Booked");
            seat.setBooking(saveBooking);
        });
        showSeatRepository.saveAll(selectedSeats);
        return mapToBookingDto(saveBooking,selectedSeats);
    }

    public BookingDto getBookingById(Long id)
    {
        Booking booking=bookingRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Booking not found"));
     List<ShowSeat>seats=showSeatRepository.findByBookingId(id);
    return mapToBookingDto(booking,seats);
    }

    public BookingDto getBookingByNumber(String bookingNumber)
    {
        Booking booking=bookingRepository.findByBookingNumber(bookingNumber).orElseThrow(()->new ResourceNotFoundException("Booking Not Found"));
        List<ShowSeat>seats=showSeatRepository.findAll().stream().filter(seat->seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId())).collect(Collectors.toList());
        return mapToBookingDto(booking,seats);
    }
    public List<BookingDto> getBookingByUserId(Long userId)
    {
        List<Booking> bookings=bookingRepository.findByUserId(userId);
        return bookings.stream().map(booking -> {
            List<ShowSeat> seats=showSeatRepository.findAll()
                    .stream().filter(seat->seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId()))
                    .collect(Collectors.toList());
            return mapToBookingDto(booking,seats);
        })
                .collect(Collectors.toList());
    }
    public BookingDto cancelBooking(Long id)
    {
        Booking booking=bookingRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Booking Not Found"));
        booking.setStatus("Booking Cancelled");
        List<ShowSeat> seats=showSeatRepository.findAll()
                .stream().filter(seat->seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());
       seats.forEach(seat->{
           seat.setStatus("Available");
           seat.setBooking(null);
       });
       if(booking.getPayment()!=null) {
         booking.getPayment().setStatus("Refunded");
       }
       Booking updatebooking=bookingRepository.save(booking);
       showSeatRepository.saveAll(seats);
       return mapToBookingDto(updatebooking,seats);
    }
    private BookingDto mapToBookingDto(Booking booking,List<ShowSeat>seats)
    {
        BookingDto bookingDto=new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setBookingNumber(booking.getBookingNumber());
        bookingDto.setBookingTime(booking.getBookingTime());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setTotalAmount(booking.getTotalAmount());

        UserDto userDto=new UserDto();
        userDto.setId(booking.getUser().getId());
        userDto.setName(booking.getUser().getName());
        userDto.setEmail(booking.getUser().getEmail());
        userDto.setPhoneNumber(booking.getUser().getPhoneNumber());
        bookingDto.setUser(userDto);

        ShowDto showDto=new ShowDto();
        showDto.setId(booking.getShow().getId());
        showDto.setStartTime(booking.getShow().getStartTime());
        showDto.setEndTime(booking.getShow().getEndTime());

        MovieDto movieDto=new MovieDto();
        movieDto.setId(booking.getShow().getMovie().getId());
        movieDto.setTitle(booking.getShow().getMovie().getTitle());
        movieDto.setDescription(booking.getShow().getMovie().getDescription());
        movieDto.setLanguage(booking.getShow().getMovie().getLanguage());
        movieDto.setGenre(booking.getShow().getMovie().getGenre());
        movieDto.setDurationMins(booking.getShow().getMovie().getDurationMins());
        movieDto.setReleaseDate(booking.getShow().getMovie().getReleaseDate());
        movieDto.setPosterUrl(booking.getShow().getMovie().getPosterUrl());
        showDto.setMovie(movieDto);

        ScreenDto screenDto=new ScreenDto();
        screenDto.setId(booking.getShow().getScreen().getId());
        screenDto.setName(booking.getShow().getScreen().getName());
        screenDto.setTotalSeats(booking.getShow().getScreen().getTotalSeats());

        TheaterDto theaterDto=new TheaterDto();
        theaterDto.setId(booking.getShow().getScreen().getTheater().getId());
        theaterDto.setName(booking.getShow().getScreen().getTheater().getName());
        theaterDto.setAddress(booking.getShow().getScreen().getTheater().getAddress());
        theaterDto.setCity(booking.getShow().getScreen().getTheater().getCity());
        theaterDto.setTotalScreens(booking.getShow().getScreen().getTheater().getTotalScreen());

        screenDto.setTheater(theaterDto);
        showDto.setScreen(screenDto);
        bookingDto.setShow(showDto);

       List<ShowSeatDto> seatDtos= seats.stream().map(seat->{
            ShowSeatDto seatDto=new ShowSeatDto();
            seatDto.setId(seat.getId());
            seatDto.setStatus(seat.getStatus());
            seatDto.setPrice(seat.getPrice());
            SeatDto baseseatdto=new SeatDto();
            baseseatdto.setId(seat.getSeat().getId());
            baseseatdto.setSeatNumber(seat.getSeat().getSeatNumber());
            baseseatdto.setSeatType(seat.getSeat().getSeatType());
            baseseatdto.setBasePrice(seat.getSeat().getBasePrice());
            seatDto.setSeat(baseseatdto);
            return  seatDto;

        }).collect(Collectors.toList());
       bookingDto.setSeats((seatDtos));
       if(booking.getPayment()!=null)
       {
           PaymentDto paymentDto=new PaymentDto();
           paymentDto.setId(booking.getPayment().getId());
           paymentDto.setAmount(booking.getPayment().getAmount());
           paymentDto.setPaymentMethod(booking.getPayment().getPaymentMethod());
           paymentDto.setPaymentTime(booking.getPayment().getPaymentTime());
           paymentDto.setStatus(booking.getPayment().getStatus());
           paymentDto.setTransactionId(booking.getPayment().getTransactionId());
           bookingDto.setPayment(paymentDto);
       }
        return bookingDto;
    }
}
