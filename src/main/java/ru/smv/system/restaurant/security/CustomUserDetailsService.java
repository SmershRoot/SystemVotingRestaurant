package ru.smv.system.restaurant.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.smv.system.restaurant.models.db.UserEntity;
import ru.smv.system.restaurant.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByLogin(name)
                .orElseThrow(() -> new UsernameNotFoundException("Ошибка загрузки пользователя из базы"));
        return new AuthorizedUser(userEntity);
    }
}
