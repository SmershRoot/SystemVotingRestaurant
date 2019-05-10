package ru.smv.system.restaurant.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.smv.system.restaurant.exception.ForbiddenException;
import ru.smv.system.restaurant.models.dto.UserDTO;

public class SecurityUtils {

    public final static AuthorizedUser currentAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null){
            throw new ForbiddenException("Пользователь не авторизован");
        }
        AuthorizedUser user = (AuthorizedUser) authentication.getPrincipal();
        return user;
    }
}
