package ru.smv.system.restaurant.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smv.system.restaurant.models.db.RestaurantEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class RestaurantDTO {
    private Long id;
    private String name;
    private String phone;
    private String address;
    private String note;
    private String email;
    private String timeWork;
    private Set<MenuDTO> menus;

    public RestaurantDTO(RestaurantEntity restaurant){
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.phone = restaurant.getPhone();
        this.address = restaurant.getAddress();
        this.note = restaurant.getNote();
        this.email = restaurant.getEmail();
        this.timeWork = restaurant.getTimeWork();
        this.menus = restaurant.getMenus().stream().map(m -> new MenuDTO(m)).collect(Collectors.toSet());
    }
}
