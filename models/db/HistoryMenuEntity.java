package ru.smv.system.restaurant.models.db;

import javax.persistence.Column;
import java.time.LocalDate;

public class HistoryMenuEntity extends AbstractEntity {

    @Column(name = "RESTAURANT_ID")
    private RestaurantEtity restaurant;

    @Column(name = "MEAL_NAME")
    private String mealName;

    @Column(name = "price")
    private Double price;

    @Column(name = "date_menu")
    private LocalDate dateMenu;
}
