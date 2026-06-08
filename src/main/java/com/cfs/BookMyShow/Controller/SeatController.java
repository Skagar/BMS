package com.cfs.BookMyShow.Controller;

import com.cfs.BookMyShow.Service.SeatService;
import com.cfs.BookMyShow.dto.SeatDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {
    @Autowired
    private SeatService seatService;

    @GetMapping
    public ResponseEntity<List<SeatDto>> getAllSeats()
    {
        return ResponseEntity.ok(seatService.getAllSeats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatDto> getSeatById(@PathVariable Long id)
    {
        return ResponseEntity.ok(seatService.getSeatById(id));
    }

    @GetMapping("/screen/{id}")
    public ResponseEntity<List<SeatDto>> getSeatsByScreenId(@PathVariable Long id)
    {
        return ResponseEntity.ok(seatService.getSeatByScreenId(id));
    }

    @PostMapping
    public ResponseEntity<SeatDto> createSeat(@Valid @RequestBody SeatDto seatDto)
    {
        return new  ResponseEntity<>(seatService.createSeat(seatDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatDto> updateSeat(@PathVariable Long id,@Valid @RequestBody SeatDto seatDto)
    {
        return ResponseEntity.ok(seatService.updateSeat(id,seatDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id)
    {
        seatService.deleteSeat(id);
        return ResponseEntity.noContent().build();
    }
}
