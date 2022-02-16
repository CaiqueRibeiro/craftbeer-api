package com.beerhouse.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.beerhouse.dtos.BeerDTO;
import com.beerhouse.models.Beer;
import com.beerhouse.repositories.BeersRepository;
import com.beerhouse.services.exceptions.ObjectNotFoundException;
import com.beerhouse.services.exceptions.ObjectAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BeersServiceTest {

    @MockBean
    private BeersRepository beersRepository;
    @Autowired
    private BeersService beersService;

    @BeforeEach
    public void init() {
        Mockito.when(this.beersRepository.save(Mockito.any(Beer.class))).thenReturn(new Beer());
        Mockito.when(this.beersRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(new Beer()));
        Mockito.when(this.beersRepository.findById(Mockito.eq(0))).thenReturn(Optional.empty());
    }

    @Test
    public void shouldCreateBeerIfNotExists() {
        BeerDTO beerDTO = new BeerDTO();
        beerDTO.setId(1);
        beerDTO.setName("Cerveja Teste");
        beerDTO.setAlcoholContent("7%");
        beerDTO.setCategory("Lagger");
        beerDTO.setIngredients("água, malte, lúpulo");
        beerDTO.setPrice(new BigDecimal("23.99"));

        Beer beer = this.beersService.save(beerDTO);
        assertNotNull(beer);
    }

    @Test
    public void shouldThrowExceptionWhenBeerIdAlreadyExists() {
        Mockito.when(this.beersRepository.existsById(0)).thenReturn(true);

        BeerDTO beerDTO = new BeerDTO();
        beerDTO.setId(0);
        beerDTO.setName("Cerveja Duplicada");
        assertThrows(ObjectAlreadyExistsException.class, () -> this.beersService.save(beerDTO));
    }

    @Test
    public void shouldThrowExceptionWhenBeerNameAlreadyExists() {
        Mockito.when(this.beersRepository.existsByName("Cerveja Duplicada")).thenReturn(true);

        BeerDTO beerDTO = new BeerDTO();
        beerDTO.setId(0);
        beerDTO.setName("Cerveja Duplicada");
        assertThrows(ObjectAlreadyExistsException.class, () -> this.beersService.save(beerDTO));
    }

    @Test
    public void shouldGetBeerByExistingId() {
        Beer beer = this.beersService.findById(1);

        assertNotNull(beer);
    }

    @Test
    public void shouldThrowObjectNotFoundExceptionWhenCantFindBeer() {
        assertThrows(ObjectNotFoundException.class, () -> this.beersService.findById(0));
    }

    @Test
    public void shouldUpdateBeerPartially() {
        BeerDTO beerDTO = new BeerDTO();
        beerDTO.setId(10);
        beerDTO.setName("Cerveja Teste");
        beerDTO.setIngredients("água, malte, lúpulo");
        beerDTO.setPrice(new BigDecimal("23.99"));

        this.beersService.update(beerDTO);

        ArgumentCaptor<Beer> argument = ArgumentCaptor.forClass(Beer.class);
        Mockito.verify(beersRepository, Mockito.times(1)).save(argument.capture());
        assertEquals("Cerveja Teste", argument.getValue().getName());
        assertEquals("água, malte, lúpulo", argument.getValue().getIngredients());
        assertEquals(new BigDecimal("23.99"), argument.getValue().getPrice());
    }
}
