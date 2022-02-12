package com.beerhouse.services;

import com.beerhouse.dtos.BeerDTO;
import com.beerhouse.models.Beer;
import com.beerhouse.repositories.BeersRepository;
import org.springframework.beans.BeanUtils;
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

    public BeersService(BeersRepository beersRepository) {
        this.beersRepository = beersRepository;
    }

    @Transactional
    public void save(BeerDTO beerDTO) {
        Beer beer = new Beer();
        BeanUtils.copyProperties(beerDTO, beer);
        beer.setRegistrationDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        this.beersRepository.save(beer);
    }

    public Page<Beer> findAll(Pageable pageable) {
        return this.beersRepository.findAll(pageable);
    }

    public Optional<Beer> findById(Integer id) {
        return this.beersRepository.findById(id);
    }

    @Transactional
    public void delete(BeerDTO beerDTO) {
        Beer beer = new Beer();
        BeanUtils.copyProperties(beerDTO, beer);
        this.beersRepository.delete(beer);
    }

    public boolean exists(BeerDTO beerDTO) {
        Beer beer = new Beer();
        BeanUtils.copyProperties(beerDTO, beer);
        return this.beersRepository.existsById(beer.getId());
    }
}
