package com.beerhouse.services;

import com.beerhouse.dtos.BeerDTO;
import com.beerhouse.models.Beer;
import com.beerhouse.repositories.BeersRepository;
import com.beerhouse.services.exceptions.ObjectAlreadyExistsException;
import com.beerhouse.services.exceptions.ObjectNotFoundException;
import com.beerhouse.mappers.BeerMapper;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class BeersService {

    final BeersRepository beersRepository;
    final BeerMapper mapper;

    public BeersService(BeersRepository beersRepository, BeerMapper mapper) {
        this.beersRepository = beersRepository;
        this.mapper = mapper;
    }

    @Transactional
    public Beer save(BeerDTO beerDTO) {
        if(this.exists(beerDTO)) {
            throw new ObjectAlreadyExistsException("This beer already exists");
        } else {
            Beer beer = mapper.fromDTOToModel(beerDTO);
            beer.setRegistrationDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
            this.beersRepository.save(beer);
            return beer;
        }
    }

    public Page<Beer> findAll(Pageable pageable) {
        return this.beersRepository.findAll(pageable);
    }

    public Beer findById(Integer id) {
        Optional<Beer> beer = this.beersRepository.findById(id);
        return beer.orElseThrow(() -> new ObjectNotFoundException(
                "No beer found with this ID"));
    }

    @Transactional
    public void delete(Integer id) {
        Optional<Beer> beer = this.beersRepository.findById(id);
        if(!beer.isPresent()) {
            throw new ObjectNotFoundException("No beer found with this ID");
        }
        this.beersRepository.delete(beer.get());
    }

    public void update(BeerDTO beerDTO) {
        Optional<Beer> foundBeer = this.beersRepository.findById(beerDTO.getId());

        if(!foundBeer.isPresent()) {
            throw new ObjectNotFoundException("No beer found with this ID");
        }
        Beer beerToBeUpdated = foundBeer.get();
        mapper.fromDTOToModel(beerDTO, beerToBeUpdated);
        this.beersRepository.save(beerToBeUpdated);
    }

    public boolean exists(BeerDTO beerDTO) {
        if(this.beersRepository.existsById(beerDTO.getId())){
            return true;
        }

        if(this.beersRepository.existsByName(beerDTO.getName())){
            return true;
        }

        return false;
    }
}
