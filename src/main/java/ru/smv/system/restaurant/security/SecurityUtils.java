package ru.smv.system.restaurant.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.smv.system.restaurant.exception.ForbiddenException;

public class SecurityUtils {

    public static AuthorizedUser currentAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null){
            throw new ForbiddenException("Пользователь не авторизован");
        }
        return (AuthorizedUser) authentication.getPrincipal();
    }
}
