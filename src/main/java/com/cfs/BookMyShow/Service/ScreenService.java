package com.cfs.BookMyShow.Service;

import com.cfs.BookMyShow.Exception.ResourceNotFoundException;
import com.cfs.BookMyShow.Model.Screen;
import com.cfs.BookMyShow.Model.Theater;
import com.cfs.BookMyShow.dto.ScreenDto;
import com.cfs.BookMyShow.dto.TheaterDto;
import com.cfs.BookMyShow.repository.ScreenRepository;
import com.cfs.BookMyShow.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScreenService {
    @Autowired
    private ScreenRepository  screenRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    public  ScreenDto createScreen(ScreenDto screenDto)
    {
        Theater theater = theaterRepository.findById(
                screenDto.getTheater().getId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Theater Not Found")
        );

        Screen screen = new Screen();
        screen.setName(screenDto.getName());
        screen.setTotalSeats(screenDto.getTotalSeats());
        screen.setTheater(theater);
        Screen saveScreen=screenRepository.save(screen);
        return mapToDto(saveScreen);
    }
    public ScreenDto getScreenById(Long screenid)
    {
        Screen screen=screenRepository.findById(screenid).orElseThrow(()->new ResourceNotFoundException("Screen Not Found With screenid="+screenid));
        return mapToDto(screen);
    }

    public List<ScreenDto> getScreenByTheaterId(Long theaterId)
    {
        List<Screen> screens=screenRepository.findByTheaterId(theaterId);
        return screens.stream().map(this::mapToDto).collect(Collectors.toList());
    }
    public List<ScreenDto> getAllScreens()
    {
        List<Screen>screens =screenRepository.findAll();
      return screens.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public ScreenDto updateScreen(Long screenid,ScreenDto screenDto)
    {
        Screen screen=screenRepository.findById(screenid).orElseThrow(()-> new ResourceNotFoundException("Screen Not found with screen id"+screenid));
        Theater theater = theaterRepository.findById(
                screenDto.getTheater().getId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Theater Not Found")
        );

        screen.setTheater(theater);
        screen.setName(screenDto.getName());
        screen.setTotalSeats(screenDto.getTotalSeats());
        Screen updatedScreen=screenRepository.save(screen);
        return  mapToDto(updatedScreen);
    }
    public void deleteScreen(Long screenid)
    {
        screenRepository.delete(screenRepository.findById(screenid).orElseThrow(()->new ResourceNotFoundException("Screen Not found with id "+screenid)));
    }
    private ScreenDto mapToDto(Screen screen)
    {
        ScreenDto screenDto=new ScreenDto();
        screenDto.setName(screen.getName());
        screenDto.setTotalSeats(screen.getTotalSeats());
        screenDto.setId(screen.getId());
        TheaterDto theaterDto = new TheaterDto();

        theaterDto.setId(screen.getTheater().getId());
        theaterDto.setName(screen.getTheater().getName());
        theaterDto.setAddress(screen.getTheater().getAddress());
        theaterDto.setCity(screen.getTheater().getCity());
        theaterDto.setTotalScreens(
                screen.getTheater().getTotalScreen()
        );

        screenDto.setTheater(theaterDto);
        return screenDto;
    }

}
