package com.cfs.BookMyShow.Service;

import com.cfs.BookMyShow.Exception.ResourceNotFoundException;
import com.cfs.BookMyShow.Model.Movie;
import com.cfs.BookMyShow.Model.Screen;
import com.cfs.BookMyShow.Model.Show;
import com.cfs.BookMyShow.Model.ShowSeat;
import com.cfs.BookMyShow.dto.*;
import com.cfs.BookMyShow.repository.MovieRepository;
import com.cfs.BookMyShow.repository.ScreenRepository;
import com.cfs.BookMyShow.repository.ShowRepository;
import com.cfs.BookMyShow.repository.ShowSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;
    public ShowDto createShow(ShowDto showDto)
    {
        Show show=new Show();
        Movie movie=movieRepository.findById(showDto.getMovie().getId()).orElseThrow(()->new ResourceNotFoundException("Movie Not Found"));
        Screen screen=screenRepository.findById(showDto.getScreen().getId()).orElseThrow(()->new ResourceNotFoundException("Screen Not Found"));
        show.setMovie(movie);
        show.setScreen(screen);
        show.setStartTime(showDto.getStartTime());
        show.setEndTime(showDto.getEndTime());
        Show savedshow=showRepository.save(show);

        List<ShowSeat> availableseats=
                showSeatRepository.findByShowIdAndStatus(savedshow.getId(),"Available");
        return maptoDto(savedshow,availableseats);

    }
    public ShowDto getShowById(Long id)
    {
        Show show=showRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Show Not Found With id: "+id));
        List<ShowSeat> availableSeats=showSeatRepository.findByShowIdAndStatus(show.getId(),"Available");
        return maptoDto(show,availableSeats);
    }

    public List<ShowDto> getAllShows()
    {
        List<Show>shows=showRepository.findAll();
        return shows.stream()
                .map(show->
                {
                    List<ShowSeat>availableSeats=showSeatRepository.findByShowIdAndStatus(
                            show.getId(),"Available"
                    );
                            return maptoDto(show,availableSeats);
                })
                .collect(Collectors.toList());
    }

    public List<ShowDto>getShowsByMovie(Long MovieId)
    {
        List<Show>shows=showRepository.findByMovieId(MovieId);
        return shows.stream()
                .map(show->
                {
                    List<ShowSeat>availableSeats=showSeatRepository.findByShowIdAndStatus(
                            show.getId(),"Available"
                    );
                    return maptoDto(show,availableSeats);
                })
                .collect(Collectors.toList());
    }
    public List<ShowDto> getShowsByMovieAndCity(Long MovieId,String city)
    {
        List<Show>shows=showRepository.findByMovie_IdAndScreen_Theater_City(MovieId,city);
        return shows.stream()
                .map(show->
                {
                    List<ShowSeat>availableSeats=showSeatRepository.findByShowIdAndStatus(
                            show.getId(),"Available"
                    );
                    return maptoDto(show,availableSeats);
                })
                .collect(Collectors.toList());
    }
    public List<ShowDto> getShowsDateRange(LocalDateTime startDate, LocalDateTime endTime)
    {
        List<Show>shows=showRepository.findByStartTimeBetween(startDate,endTime);
        return shows.stream()
                .map(show->
                {
                    List<ShowSeat>availableSeats=showSeatRepository.findByShowIdAndStatus(
                            show.getId(),"Available"
                    );
                    return maptoDto(show,availableSeats);
                })
                .collect(Collectors.toList());
    }

    private ShowDto maptoDto(Show show,List<ShowSeat>availableSeats)
    {
        ShowDto showDto=new ShowDto();
        showDto.setId(show.getId());
        showDto.setStartTime(show.getStartTime());
        showDto.setEndTime(show.getEndTime());

        showDto.setMovie(new MovieDto(
                show.getMovie().getId(),
                show.getMovie().getTitle(),
                show.getMovie().getDescription(),
                show.getMovie().getLanguage(),
                show.getMovie().getGenre(),
                show.getMovie().getDurationMins(),
                show.getMovie().getReleaseDate(),
                show.getMovie().getPosterUrl()
        ));

        TheaterDto theaterDto=new TheaterDto(
                show.getScreen().getTheater().getId(),
                show.getScreen().getTheater().getName(),
                show.getScreen().getTheater().getAddress(),
                show.getScreen().getTheater().getCity(),
                show.getScreen().getTheater().getTotalScreen()
        );

        showDto.setScreen(new ScreenDto(
                show.getScreen().getId(),
                show.getScreen().getName(),
                show.getScreen().getTotalSeats(),
                theaterDto
        ));
      List<ShowSeatDto>seatDtos=  availableSeats.stream()
                .map(seat->{
                    ShowSeatDto seatDto=new ShowSeatDto();
                    seatDto.setId(seat.getId());
                    seatDto.setStatus(seat.getStatus());
                    seatDto.setPrice(seat.getPrice());

                    SeatDto baseSeatDto =new SeatDto();
                    baseSeatDto.setId(seat.getSeat().getId());
                    baseSeatDto.setSeatNumber((seat.getSeat().getSeatNumber()));
                    baseSeatDto.setSeatType((seat.getSeat().getSeatType()));
                    baseSeatDto.setBasePrice((seat.getSeat().getBasePrice()));
                    seatDto.setSeat(baseSeatDto);
                    return seatDto;
                })
              .collect(Collectors.toList());
      showDto.setAvailableSeats((seatDtos));
      return showDto;
    }
}
