package com.beerhouse.repositories;

import com.beerhouse.models.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeersRepository extends JpaRepository<Beer, Integer> {
}
