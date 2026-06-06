package com.cfs.BookMyShow.repository;

import com.cfs.BookMyShow.Model.ShowSeat;
import com.cfs.BookMyShow.Model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterRepository extends JpaRepository<Theater,Long> {

   List<Theater>findByCity(String city);
}
