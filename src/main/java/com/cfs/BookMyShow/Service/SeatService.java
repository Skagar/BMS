package com.cfs.BookMyShow.Service;

import com.cfs.BookMyShow.Exception.ResourceNotFoundException;
import com.cfs.BookMyShow.Model.Screen;
import com.cfs.BookMyShow.Model.Seat;
import com.cfs.BookMyShow.Model.Theater;
import com.cfs.BookMyShow.dto.ScreenDto;
import com.cfs.BookMyShow.dto.SeatDto;
import com.cfs.BookMyShow.dto.TheaterDto;
import com.cfs.BookMyShow.repository.ScreenRepository;
import com.cfs.BookMyShow.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService {
    @Autowired
    private SeatRepository seatRepository;
   @Autowired
   private ScreenRepository screenRepository;
    public SeatDto createSeat(SeatDto seatDto)
    {
        Screen screen=screenRepository.findById(seatDto.getScreenDto().getId()).orElseThrow(()-> new ResourceNotFoundException("Screen not found"));
       Seat seat=new Seat();
       seat.setSeatType(seatDto.getSeatType());
       seat.setSeatNumber(seatDto.getSeatNumber());
       seat.setBasePrice(seatDto.getBasePrice());
       seat.setScreen(screen);
       Seat savedseat=seatRepository.save(seat);
       return mapToDto(savedseat);
    }
    public SeatDto getSeatById(Long id)
    {
        Seat seat=seatRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Seat not found with id"+id));
        return mapToDto(seat);
    }

    public List<SeatDto> getAllSeats()
    {
        List<Seat>seats=seatRepository.findAll();
       return seats.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<SeatDto> getSeatByScreenId(Long id)
    {
        List<Seat>seats=seatRepository.findByScreenId(id);
        return seats.stream().map(this::mapToDto).collect(Collectors.toList());
    }
    public SeatDto updateSeat(Long id,SeatDto seatDto)
    {
        Seat seat=seatRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Seat not found with id"+id));
        Screen screen=screenRepository.findById(seatDto.getScreenDto().getId()).orElseThrow(()-> new ResourceNotFoundException("Screen not found"));
        seat.setSeatType(seatDto.getSeatType());
        seat.setSeatNumber(seatDto.getSeatNumber());
        seat.setBasePrice(seatDto.getBasePrice());
        seat.setScreen(screen);
        Seat savedseat=seatRepository.save(seat);
        return mapToDto(savedseat);
    }
    public void deleteSeat(Long id)
    {
        Seat seat=seatRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Seat not found with id"+id));
        seatRepository.delete(seat);
    }
    private SeatDto mapToDto(Seat seat)
    {
        SeatDto seatDto=new SeatDto();
        seatDto.setId(seat.getId());
        seatDto.setSeatType(seat.getSeatType());
        seatDto.setSeatNumber(seat.getSeatNumber());
        seatDto.setBasePrice(seat.getBasePrice());
        Theater theater=seat.getScreen().getTheater();
        TheaterDto theaterDto=new TheaterDto(theater.getId(), theater.getName(), theater.getAddress(), theater.getCity(), theater.getTotalScreen());
        ScreenDto screenDto=new ScreenDto(seat.getScreen().getId(),
                seat.getScreen().getName(),seat.getScreen().getTotalSeats(),theaterDto);
        seatDto.setScreenDto(screenDto);
        return seatDto;
    }
}
