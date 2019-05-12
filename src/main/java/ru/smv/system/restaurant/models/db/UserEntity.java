package ru.smv.system.restaurant.models.db;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "USER")
public class UserEntity extends AbstractEntity {

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "PASSWORD")
    private String password;

    @Email
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "PATRONYMIC")
    private String patronymic;

    @Column(name = "SECURITY_ROLE_ID")
    private Integer securityRoleId;
}
