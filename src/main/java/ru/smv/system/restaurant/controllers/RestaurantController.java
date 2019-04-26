package ru.smv.system.restaurant.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import ru.smv.system.restaurant.constants.AccessPath;
import ru.smv.system.restaurant.exception.NotFoundException;
import ru.smv.system.restaurant.models.db.MenuEntity;
import ru.smv.system.restaurant.models.db.RestaurantEntity;
import ru.smv.system.restaurant.models.dto.MenuDTO;
import ru.smv.system.restaurant.models.dto.RestaurantDTO;
import ru.smv.system.restaurant.repository.RestaurantRepository;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class RestaurantController {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping(path = AccessPath.API_RESTAURANTS, method = RequestMethod.GET)
    public List<RestaurantDTO> getRestaurants(
            @RequestParam(name = "filter", required = false) String jsonFilter,
            @RequestParam(name = "sort", required = false) String jsonSort,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "withMenu", required = false) Boolean withMenu
    ) throws IOException {
        Sort sort;
        if(jsonSort == null || jsonSort.isEmpty()) {
            sort = new Sort(Sort.DEFAULT_DIRECTION, "name");
        } else {
            sort = objectMapper.readValue(jsonSort, Sort.class);
        }

        List<RestaurantEntity> restaurants = restaurantRepository.findAll(sort);
        return restaurants.stream().map(r -> new RestaurantDTO(r)).collect(Collectors.toList());
    }

    @RequestMapping(path = AccessPath.API_RESTAURANTS_SUD, method = RequestMethod.GET)
    public RestaurantDTO getRestaurant(
            @PathVariable(name = "restaurantId") Long restaurantId
            ) throws NotFoundException {
        Assert.notNull(restaurantId, "Параметр строки обращения не корректен.");
        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(
                        "Ресторан не найден. Возможно данный ресторан отсутствует в системе."));

        return new RestaurantDTO(restaurantEntity);
    }

    @RequestMapping(path = AccessPath.API_RESTAURANTS, method = RequestMethod.POST)
    public RestaurantDTO createRestaurant(
            @RequestBody RestaurantDTO restaurant
    ){
        //TODO только администратор
        Assert.notNull(restaurant, "Ресторан не заполнен.");
        RestaurantEntity restaurantEntity = addDataRestaurantEntityFromDto(new RestaurantEntity(), restaurant);
        return new RestaurantDTO(restaurantRepository.save(restaurantEntity));
    }

    @RequestMapping(path = AccessPath.API_RESTAURANTS, method = RequestMethod.PUT)
    public RestaurantDTO updateRestaurant(
            //TODO Думаю сделать обновление ресторана по 2-м типам:
            //- если массив меню пустой - удаляем меню
            //- если null - не трогаем
    //        @PathVariable(name = "restaurantId") Long restaurantId,
            @RequestBody RestaurantDTO restaurant
    ) throws NotFoundException {
        //TODO только администратор
        Assert.notNull(restaurant, "Ресторан не заполнен.");
        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurant.getId())
                .orElseThrow(() -> new NotFoundException(
                        "Ресторан не найден. Возможно данный ресторан отсутствует в системе."));
        restaurantEntity =  addDataRestaurantEntityFromDto(restaurantEntity, restaurant);
        return new RestaurantDTO(restaurantRepository.save(restaurantEntity));
    }

    private RestaurantEntity addDataRestaurantEntityFromDto(RestaurantEntity restaurantEntity, RestaurantDTO restaurantDTO){
        restaurantEntity.setName(restaurantDTO.getName());
        restaurantEntity.setPhone(restaurantDTO.getPhone());
        restaurantEntity.setAddress(restaurantDTO.getAddress());
        restaurantEntity.setNote(restaurantDTO.getNote());
        restaurantEntity.setEmail(restaurantDTO.getEmail());
        restaurantEntity.setTimeWork(restaurantDTO.getTimeWork());

        if(restaurantEntity.getId()!=null) {
            Set<MenuDTO> menus = restaurantDTO.getMenus();
            for (MenuDTO menu : menus) {
                MenuEntity menuEntity = new MenuEntity();
                menuEntity.setDateMenu(menu.getDateMenu());
                menuEntity.setName(menu.getName());
                menuEntity.setNote(menu.getNote());
                menuEntity.setPhotoSrc(menu.getPhotoSrc());
                menuEntity.setRestaurant(restaurantEntity);
                restaurantEntity.getMenus().add(menuEntity);
            }
        }

        return restaurantEntity;
    }

    @RequestMapping(path = AccessPath.API_RESTAURANTS_SUD, method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRestaurant(@PathVariable(name = "restaurantId") Long restaurantId){
        Assert.notNull(restaurantId, "Параметр строки обращения не корректен.");
        //TODO только администратор
        restaurantRepository.deleteById(restaurantId);
    }




}
