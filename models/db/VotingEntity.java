package ru.smv.system.restaurant.models.db;

import javax.persistence.JoinColumn;

public class VotingEntity extends AbstractEntity {

    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @JoinColumn(name = "RESTAURANT_ID")
    private RestaurantEtity restaurant;
}
