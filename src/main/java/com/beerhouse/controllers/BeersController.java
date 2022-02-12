package com.beerhouse.controllers;

import com.beerhouse.dtos.BeerDTO;
import com.beerhouse.models.Beer;
import com.beerhouse.services.BeersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/beers")
public class BeersController {

    final BeersService beersService;

    public BeersController(BeersService beersService) {
        this.beersService = beersService;
    }

    @PostMapping(consumes = { "application/json" }, produces = { "application/json" })
    public ResponseEntity<String> save(@RequestBody @Valid BeerDTO beerDTO) {
        if(this.beersService.exists(beerDTO)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!");
        }
        this.beersService.save(beerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Beer created");
    }
}
