package com.cfs.BookMyShow.repository;

import com.cfs.BookMyShow.Model.Booking;
import com.cfs.BookMyShow.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {
    List<Movie> findByLanguage(String language);
    List<Movie> findByGenre(String genre);
    List<Movie> findByTitle(String title);
}
