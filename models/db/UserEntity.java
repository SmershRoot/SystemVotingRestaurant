package ru.smv.system.restaurant.models.db;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "USER")
public class UserEntity extends AbstractEntity {

    @Column(name = "LOGIN")
    private Long login;

    @Column(name = "PASSWORD")
    private Long password;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "PATRONYC")
    private String patronyc;
}
