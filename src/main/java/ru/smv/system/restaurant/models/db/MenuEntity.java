package ru.smv.system.restaurant.models.db;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "MENU")
public class MenuEntity extends AbstractEntity implements Serializable {

    @Column(name = "NAME")
    private String name;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "PHOTO_SRC")
    private String photoSrc;

    @Column(name = "DATE_MENU")
    private LocalDate dateMenu;

    @JsonBackReference("restaurant-list-task")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL})
    @JoinColumn(name = "RESTAURANT_ID")
    private RestaurantEntity restaurant;

}
