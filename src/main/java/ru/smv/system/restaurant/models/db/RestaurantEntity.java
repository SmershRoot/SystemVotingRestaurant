package ru.smv.system.restaurant.models.db;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "RESTAURANT")
public class RestaurantEntity extends AbstractEntity implements Serializable {

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "NOTE")
    private String note;

    @Email
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "TIME_WORK")
    private String timeWork;

    @JsonManagedReference("restaurant-list-task")
    @OneToMany(mappedBy = "restaurant", cascade=CascadeType.ALL, orphanRemoval=true)
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 1)
    private List<MenuEntity> menus = new ArrayList<>();

}
