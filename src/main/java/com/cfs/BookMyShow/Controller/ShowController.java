package com.cfs.BookMyShow.Controller;

import com.cfs.BookMyShow.Service.ShowService;
import com.cfs.BookMyShow.dto.ShowDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {
    @Autowired
    private ShowService showService;

    @GetMapping
    public ResponseEntity<List<ShowDto>> getAllShows()
    {
        return  ResponseEntity.ok(showService.getAllShows());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ShowDto> getShowById(@PathVariable Long id)
    {
        return ResponseEntity.ok(showService.getShowById(id));
    }

    @GetMapping("/movie/{movieid}")
    public ResponseEntity<List<ShowDto>> getShowsByMovieId(@PathVariable Long movieid)
    {
        return ResponseEntity.ok(showService.getShowsByMovie(movieid));
    }

    @PostMapping
    public ResponseEntity<ShowDto> createShow(@Valid @RequestBody ShowDto showDto)
    {
        return new ResponseEntity<>(showService.createShow(showDto),HttpStatus.CREATED);
    }

    @GetMapping("/movie/{movieid}/city/{city}")
    public ResponseEntity<List<ShowDto>> getShowsByMovieIdAndCity(@PathVariable Long movieid,@PathVariable String city)
    {
        return ResponseEntity.ok(showService.getShowsByMovieAndCity(movieid,city));
    }

    @GetMapping("/daterange")
    public ResponseEntity<List<ShowDto>> getMovieByDateRange(@RequestParam ("start") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)LocalDateTime start, @RequestParam("end")
    @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime end)
    {
        return ResponseEntity.ok(showService.getShowsDateRange(start,end));
    }

}
