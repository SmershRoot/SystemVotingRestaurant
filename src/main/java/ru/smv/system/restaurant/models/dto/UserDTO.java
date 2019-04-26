package ru.smv.system.restaurant.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smv.system.restaurant.models.db.UserEntity;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String patronymic;

    public UserDTO (UserEntity user){
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.patronymic = user.getPatronymic();
    }
}
