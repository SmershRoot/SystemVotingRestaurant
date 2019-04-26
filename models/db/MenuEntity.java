package ru.smv.system.restaurant.models.db;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "MENU")
public class MenuEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RESTAURANT_ID")
    private RestaurantEtity restaurant;

    @Column(name = "MEAL_NAME")
    private String mealName;

    @Column(name = "price")
    private Double price;

    @Column(name = "date_menu")
    private LocalDate dateMenu;
}
