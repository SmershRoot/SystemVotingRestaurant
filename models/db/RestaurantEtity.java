package ru.smv.system.restaurant.models.db;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "RESTAURANT")
public class RestaurantEtity extends AbstractEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "TIME_WORK")
    private String timeWork;


}
