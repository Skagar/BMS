package com.cfs.BookMyShow.Controller;

import com.cfs.BookMyShow.Service.BookingService;
import com.cfs.BookMyShow.dto.BookingDto;
import com.cfs.BookMyShow.dto.BookingRequestDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
public ResponseEntity<BookingDto> crateBooking(@Valid @RequestBody BookingRequestDto bookingRequest)
{
  return new ResponseEntity<>(bookingService.createBooking(bookingRequest), HttpStatus.CREATED);
}

@GetMapping("/{id}")
public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id)
{
    return ResponseEntity.ok(bookingService.getBookingById(id));
}
}
