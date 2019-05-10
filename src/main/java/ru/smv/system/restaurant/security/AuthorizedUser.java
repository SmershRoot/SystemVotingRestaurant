package ru.smv.system.restaurant.security;

import ru.smv.system.restaurant.models.db.UserEntity;
import java.util.Collections;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User{

    public AuthorizedUser(UserEntity user) {
        super(user.getLogin(), user.getPassword(), Collections.singleton(SecurityRole.fromId(user.getSecurityRoleId())));
    }
}
