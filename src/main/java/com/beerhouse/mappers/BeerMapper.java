package com.beerhouse.mappers;

import com.beerhouse.dtos.BeerDTO;
import com.beerhouse.mappers.exceptions.MappingException;
import com.beerhouse.models.Beer;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
public class BeerMapper {
    public Beer fromDTOToModel(BeerDTO beerDTO) {
        Beer beer = new Beer();
        beer.setId(beerDTO.getId());
        beer.setName(beerDTO.getName());
        beer.setCategory(beerDTO.getCategory());
        beer.setPrice(beerDTO.getPrice());
        beer.setAlcoholContent(beerDTO.getAlcoholContent());
        beer.setIngredients(beerDTO.getIngredients());

        return beer;
    }

    public void fromDTOToModel(BeerDTO beerDTO, Beer beer) {
        Method[] dtoMethods = beerDTO.getClass().getMethods();

        for (Method method : dtoMethods) {
            String methodName = method.getName();
            try{
                if(methodName.startsWith("get") && !methodName.equals("getClass")){
                    if(method.invoke(beerDTO, null) != null) {
                        beer.getClass().getMethod(methodName.replaceFirst("get", "set"), method.getReturnType()).invoke(beer, method.invoke(beerDTO, null));
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | IllegalArgumentException e) {
                e.printStackTrace();
                throw new MappingException("Erro no mapeamento dos DTOS");
            }
        }
    }
}
