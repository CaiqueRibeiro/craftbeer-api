package com.beerhouse.controllers;

import java.net.URI;

import com.beerhouse.dtos.BeerDTO;
import com.beerhouse.models.Beer;
import com.beerhouse.services.BeersService;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.validation.Valid;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/beers")
public class BeersController {

    final BeersService beersService;

    public BeersController(BeersService beersService) {
        this.beersService = beersService;
    }

    @PostMapping(consumes = { "application/json" }, produces = { "application/json" })
    public ResponseEntity<Void> save(@RequestBody @Valid BeerDTO beerDTO) {
        if(this.beersService.exists(beerDTO)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Beer beer = this.beersService.save(beerDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(beer.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<Page<Beer>> findAll(@PageableDefault(page = 0, size = 50, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(beersService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Beer> getById(@PathVariable(value = "id") Integer id) {
        Beer beer = beersService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(beer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") Integer id) {
        beersService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable(value = "id") Integer id,
                                       @RequestBody @Valid BeerDTO beer) {
        beer.setId(id);
        this.beersService.update(beer);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePartial(@PathVariable(value = "id") Integer id,
                                       @RequestBody BeerDTO beer) {
        beer.setId(id);
        this.beersService.update(beer);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
