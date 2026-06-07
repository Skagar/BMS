package com.cfs.BookMyShow.Service;

import com.cfs.BookMyShow.Exception.ResourceNotFoundException;
import com.cfs.BookMyShow.Model.Theater;
import com.cfs.BookMyShow.dto.TheaterDto;
import com.cfs.BookMyShow.repository.TheaterRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.chrono.ThaiBuddhistChronology;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheaterService {
    @Autowired
    private TheaterRepository theaterRepository;

    public TheaterDto createTheater(TheaterDto theaterDto)
    {
        Theater theater=mapToEntity(theaterDto);
        Theater savedTheater=theaterRepository.save(theater);
        return mapToDto(savedTheater);
    }

    public TheaterDto updateTheater(Long id,TheaterDto theaterDto)
    {
        Theater theater=theaterRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Theater Not found with id ="+id));
        theater.setName(theaterDto.getName());
        theater.setCity(theaterDto.getCity());
        theater.setAddress(theaterDto.getAddress());
        theater.setTotalScreen(theaterDto.getTotalScreens());
        Theater updateTheater=theaterRepository.save(theater);
        return mapToDto(updateTheater);
    }

    public void deleteTheater(Long id)
    {
        Theater theater=theaterRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Theater Not found with id ="+id));
        theaterRepository.delete(theater);
    }

    public TheaterDto getTheaterById(Long id)
    {
        Theater theater=theaterRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Theater not Found with id: "+id));
        return mapToDto(theater);
    }

    public List<TheaterDto> getAllTheaters()
    {
        List<Theater> theaters=theaterRepository.findAll();
        return theaters.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<TheaterDto> getAllTheatersByCity(String city)
    {
        List<Theater>theaters=theaterRepository.findByCity(city);
        return theaters.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private TheaterDto mapToDto(Theater theater)
    {
        TheaterDto theaterDto=new TheaterDto();
        theaterDto.setId(theater.getId());
        theaterDto.setCity(theater.getCity());
        theaterDto.setName(theater.getName());
        theaterDto.setAddress(theater.getAddress());
        theaterDto.setTotalScreens(theater.getTotalScreen());
        return theaterDto;
    }
    private Theater mapToEntity(TheaterDto theaterDto)
    {
        Theater theater=new Theater();
        theater.setName(theaterDto.getName());
        theater.setCity(theaterDto.getCity());
        theater.setAddress(theaterDto.getAddress());
        theater.setTotalScreen(theaterDto.getTotalScreens());
        return theater;
    }
}
