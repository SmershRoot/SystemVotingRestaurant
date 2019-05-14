package ru.smv.system.restaurant.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.smv.system.restaurant.constants.AccessPath;
import ru.smv.system.restaurant.exception.ForbiddenException;
import ru.smv.system.restaurant.exception.NotFoundException;
import ru.smv.system.restaurant.models.db.UserEntity;
import ru.smv.system.restaurant.models.dto.UserDTO;
import ru.smv.system.restaurant.repository.UserRepository;
import ru.smv.system.restaurant.security.AuthorizedUser;
import ru.smv.system.restaurant.security.SecurityRole;
import ru.smv.system.restaurant.security.SecurityUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public UserController(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Получение всех пользователей
     *
     * @return перечень пользователей системы
     */
    @RequestMapping(path = AccessPath.API_USERS, method = RequestMethod.GET)
    public List<UserDTO> getUsers(
            @RequestParam(name = "filter", required = false) String jsonFilter,
            @RequestParam(name = "sort", required = false) String jsonSort,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "pageSize", required = false) Integer pageSize
    ) throws IOException {
//        PageRequest.of(page, pageSize, sort);
        AuthorizedUser authorizedUser = SecurityUtils.currentAuthentication();
        if(!authorizedUser.getAuthorities().contains(SecurityRole.ADMIN)){
            throw new ForbiddenException();
        }

        Sort sort;
        if(jsonSort == null || jsonSort.isEmpty()){
            sort = new Sort(Sort.DEFAULT_DIRECTION, "login");
        } else {
            sort = objectMapper.readValue(jsonSort, Sort.class);
        }

        List<UserEntity> users = userRepository.findAll(sort);
        return users.stream().map(UserDTO::new).collect(Collectors.toList());
    }

    @RequestMapping(path = AccessPath.API_USERS, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody UserDTO userDto) {
        AuthorizedUser authorizedUser = SecurityUtils.currentAuthentication();
        if(!authorizedUser.getAuthorities().contains(SecurityRole.ADMIN) &&
                authorizedUser.getAuthorities().contains(SecurityRole.USER)){
            throw new ForbiddenException();
        }

        Assert.notNull(userDto, "Пользователь не заполнен.");
        Assert.notNull(userDto.getPassword(), "У пользователя не задан пароль.");

        UserEntity user = addDataUserEntityFromDto(new UserEntity(), userDto);

        return new UserDTO(userRepository.save(user));
    }

    @RequestMapping(path = AccessPath.API_USERS_SUD, method = RequestMethod.GET)
    public UserDTO getUser(@PathVariable Long userId) throws NotFoundException {
        AuthorizedUser authorizedUser = SecurityUtils.currentAuthentication();
        UserEntity authorizedUserEntity = userRepository.findByLogin(authorizedUser.getUsername())
                .orElseThrow(() -> new NotFoundException("Пользователь под которым осуществлен вход не найден в системе"));

        if(!authorizedUser.getAuthorities().contains(SecurityRole.ADMIN) &&
                (!authorizedUserEntity.getId().equals(userId))){
            throw new ForbiddenException();
        }

        Assert.notNull(userId, "Параметр строки обращения не корректен.");
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()){
            return new UserDTO(optionalUser.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
    }

    @RequestMapping(path = AccessPath.API_USERS, method = RequestMethod.PUT)
    public UserDTO updateUser(
            @RequestBody UserDTO userDto
    ) throws NotFoundException {
        AuthorizedUser authorizedUser = SecurityUtils.currentAuthentication();
        UserEntity authorizedUserEntity = userRepository.findByLogin(authorizedUser.getUsername())
                .orElseThrow(() -> new NotFoundException("Пользователь под которым осуществлен вход не найден в системе"));

        if(!authorizedUser.getAuthorities().contains(SecurityRole.ADMIN) &&
                !authorizedUserEntity.getId().equals(userDto.getId())){
            throw new ForbiddenException();
        }

        Assert.notNull(userDto, "Пользователь не заполнен.");
        Optional<UserEntity> optionalUser = userRepository.findById(userDto.getId());
        if(!optionalUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }

        UserEntity user = addDataUserEntityFromDto(optionalUser.get(), userDto);

        return new UserDTO(userRepository.save(user));
    }

    @RequestMapping(path = AccessPath.API_USERS_SUD, method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) throws NotFoundException {
        AuthorizedUser authorizedUser = SecurityUtils.currentAuthentication();
        UserEntity authorizedUserEntity = userRepository.findByLogin(authorizedUser.getUsername())
                .orElseThrow(() -> new NotFoundException("Пользователь под которым осуществлен вход не найден в системе"));

        if(!authorizedUser.getAuthorities().contains(SecurityRole.ADMIN) &&
                !authorizedUserEntity.getId().equals(userId)){
            throw new ForbiddenException();
        }

        Assert.notNull(userId, "Параметр строки обращения не корректен.");

        userRepository.deleteById(userId);
    }

    private UserEntity addDataUserEntityFromDto(UserEntity userEntity, UserDTO userDto) {
        if(userRepository.findByLogin(userDto.getLogin()).isPresent()){
            throw new ForbiddenException("Пользователь "+ userDto.getLogin() + " уже существует");
        }
        if(userRepository.findByEmail(userDto.getEmail()).isPresent()){
            throw new ForbiddenException("Пользователь c email "+ userDto.getEmail() + " уже существует");
        }

        userEntity.setLogin(userDto.getLogin().toLowerCase());
        userEntity.setEmail(userDto.getEmail().toLowerCase());
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setPatronymic(userDto.getPatronymic());
        userEntity.setSecurityRoleId(SecurityRole.USER.getId());
        userEntity.setModifiedDate(LocalDateTime.now());
        if(userDto.getPassword()!=null){
            userEntity.setPassword(
                    PasswordEncoderFactories
                            .createDelegatingPasswordEncoder()
                            .encode(userDto.getPassword()));
        }

        return userEntity;
    }

    @RequestMapping(path = AccessPath.API_USERS_PASSWORD, method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @PathVariable Long userId,
            @RequestParam(name = "current", required = false) String currentPass,
            @RequestParam(name = "new") String newPass
    ) throws NotFoundException {
        AuthorizedUser authorizedUser = SecurityUtils.currentAuthentication();
        UserEntity authorizedUserEntity = userRepository.findByLogin(authorizedUser.getUsername())
                .orElseThrow(() -> new NotFoundException("Пользователь под которым осуществлен вход не найден в системе"));

        if(!authorizedUser.getAuthorities().contains(SecurityRole.ADMIN) &&
                !authorizedUserEntity.getId().equals(userId)){
            throw new ForbiddenException();
        }

        Assert.notNull(userId, "Параметр строки обращения не корректен.");
        Assert.notNull(newPass, "Пароль не может быть пустым.");
        Assert.isTrue(!newPass.isEmpty(), "Пароль не может быть пустым.");

        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if(!optionalUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        UserEntity user = optionalUser.get();

        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        //Проверяем права пользователя
        //Если не Админ
        if(!authorizedUser.getAuthorities().contains(SecurityRole.ADMIN)) {
            if(passwordEncoder.matches(currentPass, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPass));
            }
        } else {
            //Если Админ
            user.setPassword(passwordEncoder.encode(newPass));
        }

        userRepository.save(user);
    }

}
