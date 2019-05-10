package ru.smv.system.restaurant.utils;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import ru.smv.system.restaurant.models.db.UserEntity;
import ru.smv.system.restaurant.models.dto.UserDTO;
import ru.smv.system.restaurant.security.AuthorizedUser;
import ru.smv.system.restaurant.security.SecurityRole;

public class TestUtils {

    public static MockHttpSession getMockHttpSession(){
        MockHttpSession session = new MockHttpSession();
        UserEntity userEntity = new UserEntity();

        userEntity.setId(2L);
        userEntity.setLogin("I");
        userEntity.setSecurityRoleId(SecurityRole.ADMIN.getId());
        userEntity.setPassword("1");

        AuthorizedUser user = new AuthorizedUser(userEntity);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, "");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(usernamePasswordAuthenticationToken);

        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        return  session;
    }
}
