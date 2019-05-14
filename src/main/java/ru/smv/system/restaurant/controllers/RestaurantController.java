package ru.smv.system.restaurant.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import ru.smv.system.restaurant.constants.AccessPath;
import ru.smv.system.restaurant.exception.ForbiddenException;
import ru.smv.system.restaurant.exception.NotFoundException;
import ru.smv.system.restaurant.models.db.MenuEntity;
import ru.smv.system.restaurant.models.db.RestaurantEntity;
import ru.smv.system.restaurant.models.dto.MenuDTO;
import ru.smv.system.restaurant.models.dto.RestaurantDTO;
import ru.smv.system.restaurant.repository.RestaurantRepository;
import ru.smv.system.restaurant.security.AuthorizedUser;
import ru.smv.system.restaurant.security.SecurityRole;
import ru.smv.system.restaurant.security.SecurityUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public RestaurantController(RestaurantRepository restaurantRepository, ObjectMapper objectMapper) {
        this.restaurantRepository = restaurantRepository;
        this.objectMapper = objectMapper;
    }

    @RequestMapping(path = AccessPath.API_RESTAURANTS, method = RequestMethod.GET)
    public List<RestaurantDTO> getRestaurants(
            @RequestParam(name = "filter", required = false) String jsonFilter,
            @RequestParam(name = "sort", required = false) String jsonSort,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "pageSize", required = false) Integer pageSize
    ) throws IOException {
        AuthorizedUser authorizedUser = SecurityUtils.currentAuthentication();
        if(!authorizedUser.getAuthorities().contains(SecurityRole.ADMIN) &&
                !authorizedUser.getAuthorities().contains(SecurityRole.USER)){
            throw new ForbiddenException();
        }

        Sort sort;
        if(jsonSort == null || jsonSort.isEmpty()) {
            sort = new Sort(Sort.DEFAULT_DIRECTION, "name");
        } else {
            sort = objectMapper.readValue(jsonSort, Sort.class);
        }

        List<RestaurantEntity> restaurants = restaurantRepository.findAll(sort);
        return restaurants.stream().map(RestaurantDTO::new).collect(Collectors.toList());
    }

    @RequestMapping(path = AccessPath.API_RESTAURANTS_SUD, method = RequestMethod.GET)
    public RestaurantDTO getRestaurant(
            @PathVariable Long restaurantId
            ) throws NotFoundException {
//        AuthorizedUser authorizedUser = SecurityUtils.currentAuthentication();
//        if(!authorizedUser.getAuthorities().contains(SecurityRole.ADMIN) &&
//                !authorizedUser.getAuthorities().contains(SecurityRole.USER)){
//            throw new ForbiddenException();
//        }

        Assert.notNull(restaurantId, "Параметр строки обращения не корректен.");
        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(
                        "Ресторан не найден. Возможно данный ресторан отсутствует в системе."));

        return new RestaurantDTO(restaurantEntity);
    }

    @RequestMapping(path = AccessPath.API_RESTAURANTS, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantDTO createRestaurant(
            @RequestBody RestaurantDTO restaurant
    ){
        AuthorizedUser authorizedUser = SecurityUtils.currentAuthentication();
        if(!authorizedUser.getAuthorities().contains(SecurityRole.ADMIN)){
            throw new ForbiddenException();
        }

        Assert.notNull(restaurant, "Ресторан не заполнен.");
        RestaurantEntity restaurantEntity = addDataRestaurantEntityFromDto(new RestaurantEntity(), restaurant, false);
        return new RestaurantDTO(restaurantRepository.save(restaurantEntity));
    }

    @RequestMapping(path = AccessPath.API_RESTAURANTS, method = RequestMethod.PUT)
    public RestaurantDTO updateRestaurant(
            @RequestParam(name = "updateMenu" ,required = false, defaultValue = "false") boolean updateMenu,
            @RequestBody RestaurantDTO restaurant
    ) throws NotFoundException {
        AuthorizedUser authorizedUser = SecurityUtils.currentAuthentication();
        if(!authorizedUser.getAuthorities().contains(SecurityRole.ADMIN)){
            throw new ForbiddenException();
        }

        Assert.notNull(restaurant, "Ресторан не заполнен.");
        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurant.getId())
                .orElseThrow(() -> new NotFoundException(
                        "Ресторан не найден. Возможно данный ресторан отсутствует в системе."));
        restaurantEntity =  addDataRestaurantEntityFromDto(restaurantEntity, restaurant, updateMenu);
        return new RestaurantDTO(restaurantRepository.save(restaurantEntity));
    }

    private RestaurantEntity addDataRestaurantEntityFromDto(RestaurantEntity restaurantEntity,
                                                            RestaurantDTO restaurantDTO,
                                                            boolean updateMenu){
        restaurantEntity.setName(restaurantDTO.getName());
        restaurantEntity.setPhone(restaurantDTO.getPhone());
        restaurantEntity.setAddress(restaurantDTO.getAddress());
        restaurantEntity.setNote(restaurantDTO.getNote());
        restaurantEntity.setEmail(restaurantDTO.getEmail());
        restaurantEntity.setTimeWork(restaurantDTO.getTimeWork());
        restaurantEntity.setModifiedDate(LocalDateTime.now());

        if(updateMenu) {
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
    public void deleteRestaurant(@PathVariable Long restaurantId){
        AuthorizedUser authorizedUser = SecurityUtils.currentAuthentication();
        if(!authorizedUser.getAuthorities().contains(SecurityRole.ADMIN)){
            throw new ForbiddenException();
        }

        Assert.notNull(restaurantId, "Параметр строки обращения не корректен.");
        restaurantRepository.deleteById(restaurantId);
    }
    
}
