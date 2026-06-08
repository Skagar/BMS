package com.cfs.BookMyShow.Controller;

import com.cfs.BookMyShow.Service.ScreenService;
import com.cfs.BookMyShow.dto.ScreenDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screens")
public class ScreenController {
    @Autowired
    private ScreenService screenService;

    @GetMapping
    public ResponseEntity<List<ScreenDto>> getAllScreens()
    {
        return ResponseEntity.ok(screenService.getAllScreens());
    }

    @GetMapping("/{screenId}")
    public ResponseEntity<ScreenDto> getScreenById(@PathVariable Long screenId)
    {
     return ResponseEntity.ok(screenService.getScreenById(screenId));
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<ScreenDto>> getAllScreens( @PathVariable Long theaterId)
    {
        return ResponseEntity.ok(screenService.getScreenByTheaterId(theaterId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScreen(@PathVariable Long id)
    {
        screenService.deleteScreen(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping
    public ResponseEntity<ScreenDto> createScreen(@Valid @RequestBody ScreenDto screenDto)
    {
        return new ResponseEntity<>(screenService.createScreen(screenDto), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ScreenDto> updateScreen(@PathVariable Long id,@Valid @RequestBody ScreenDto screenDto)
    {
        return ResponseEntity.ok(screenService.updateScreen(id,screenDto));
    }
}
