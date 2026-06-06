package com.cfs.BookMyShow.repository;

import com.cfs.BookMyShow.Model.Show;
import com.cfs.BookMyShow.Model.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat,Long> {

    List<ShowSeat>findByShowId(Long showId);
    List<ShowSeat>findByShowIdAndStatus(Long showId,String status);

}
