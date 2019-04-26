package ru.smv.system.restaurant.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.smv.system.restaurant.models.dto.UserDTO;

public class SecurityUtils {

    public final static UserDTO currentAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDto = (UserDTO) authentication.getPrincipal();
        return userDto;
    }
}
