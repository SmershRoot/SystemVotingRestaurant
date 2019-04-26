package ru.smv.system.restaurant.models.db;

import javax.persistence.Column;
import java.time.LocalDate;

public class HistoryVotingEntity extends AbstractEntity {

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "login")
    private String userLogin;

    @Column(name = "RESTAURANT_ID")
    private Long restaurantId;

    @Column(name = "DATE_VOTING")
    private LocalDate dateVoting;

}
