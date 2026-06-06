package com.cfs.BookMyShow.Service;

import com.cfs.BookMyShow.Exception.ResourceNotFoundException;
import com.cfs.BookMyShow.Model.Movie;
import com.cfs.BookMyShow.dto.MovieDto;
import com.cfs.BookMyShow.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public MovieDto createMovie(MovieDto movieDto)
    {
      Movie movie=maptoEntity(movieDto);
      Movie savemovie=movieRepository.save(movie);
     return maptoDto(savemovie);
    }
    public MovieDto getMovieById(Long id)
    {
        Movie movie=movieRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Movie Not found with id:"+id));
        return maptoDto(movie);
    }
    public List<MovieDto> getAllMovies()
    {
        List<Movie>movies=movieRepository.findAll();
        return movies.stream().map(this::maptoDto).collect(Collectors.toList());
    }
    public List<MovieDto> getMovieByLanguage(String language)
    {
        List<Movie>movies=movieRepository.findByLanguage(language);
        return movies.stream().map(this::maptoDto).collect(Collectors.toList());
    }
    public List<MovieDto> getMovieByGenre(String genre)
    {
        List<Movie>movies=movieRepository.findByGenre(genre);
        return movies.stream().map(this::maptoDto).collect(Collectors.toList());
    }
    public List<MovieDto> getMovieByTitle(String title)
    {
        List<Movie>movies=movieRepository.findByTitle(title);
        return movies.stream().map(this::maptoDto).collect(Collectors.toList());
    }
    public MovieDto updateMovie(Long id, MovieDto movieDto)
    {
        Movie movie=movieRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Movie Not Found with "+id));
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setLanguage(movieDto.getLanguage());
        movie.setGenre(movieDto.getGenre());
        movie.setDurationMins(movieDto.getDurationMins());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());
        Movie updatedMovie=movieRepository.save(movie);
        return maptoDto(updatedMovie);
    }

    public void deleteMovie(Long id)
    {
        Movie movie=movieRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Movie Not found with id :" +id));
        movieRepository.delete(movie);
    }

    private MovieDto maptoDto(Movie movie)
    {
        MovieDto movieDto=new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setGenre(movie.getGenre());
        movieDto.setLanguage(movie.getLanguage());
        movieDto.setDescription(movie.getDescription());
        movieDto.setPosterUrl(movie.getPosterUrl());
        movieDto.setTitle(movie.getTitle());
        movieDto.setReleaseDate(movie.getReleaseDate());
        movieDto.setDurationMins(movie.getDurationMins());
        return movieDto;
    }
    public Movie maptoEntity(MovieDto movieDto)
    {
        Movie movie=new Movie();
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setLanguage(movieDto.getLanguage());
        movie.setGenre(movieDto.getGenre());
        movie.setDurationMins(movieDto.getDurationMins());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());
        return movie;

    }
}
