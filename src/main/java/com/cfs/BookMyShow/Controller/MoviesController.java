package com.cfs.BookMyShow.Controller;

import com.cfs.BookMyShow.Service.MovieService;
import com.cfs.BookMyShow.dto.MovieDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")

public class MoviesController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@Valid @RequestBody MovieDto movieDto)
    {
        return  new ResponseEntity<>(movieService.createMovie(movieDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id)
    {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies()
    {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/language/{language}")
    public ResponseEntity<List<MovieDto>> getAllMoviesByLanguage(@PathVariable String language)
    {
        return ResponseEntity.ok(movieService.getMovieByLanguage(language));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<MovieDto>> getAllMoviesByTitle(@PathVariable String title)
    {
        return ResponseEntity.ok(movieService.getMovieByTitle(title));
    }


    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MovieDto>> getAllMoviesByGenre(@PathVariable String genre)
    {
        return ResponseEntity.ok(movieService.getMovieByGenre(genre));
    }

   @PutMapping("/{id}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long id,@Valid @RequestBody MovieDto movieDto)
   {
       return new ResponseEntity<>(movieService.updateMovie(id,movieDto),HttpStatus.OK);
   }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id)
    {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

}
