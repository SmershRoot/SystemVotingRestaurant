package ru.smv.system.restaurant.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.smv.system.restaurant.constants.AccessPath;
import ru.smv.system.restaurant.models.db.UserEntity;
import ru.smv.system.restaurant.models.dto.UserDTO;
import ru.smv.system.restaurant.repository.UserRepository;
import ru.smv.system.restaurant.security.SecurityRole;
import ru.smv.system.restaurant.security.SecurityUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * Получение всех пользователей
     *
     * @return
     */
    @RequestMapping(path = AccessPath.API_USERS, method = RequestMethod.GET)
    public List<UserDTO> getUsers(
            @RequestParam(name = "filter", required = false) String jsonFilter,
            @RequestParam(name = "sort", required = false) String jsonSort,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "pageSize", required = false) Integer pageSize
    ) throws IOException {
//        PageRequest.of(page, pageSize, sort);
        SecurityUtils.currentAuthentication();

        Sort sort;
        if(jsonSort == null || jsonSort.isEmpty()){
            sort = new Sort(Sort.DEFAULT_DIRECTION, "login");
        } else {
            sort = objectMapper.readValue(jsonSort, Sort.class);
        }

        List<UserEntity> users = userRepository.findAll(sort);
        return users.stream().map(u -> new UserDTO(u)).collect(Collectors.toList());
    }

    @RequestMapping(path = AccessPath.API_USERS, method = RequestMethod.POST)
    public UserDTO createUser(@RequestBody UserDTO userDto){
        Assert.notNull(userDto, "Пользователь не заполнен.");
        UserEntity user = addDataUserEntityFromDto(new UserEntity(), userDto);

        return new UserDTO(userRepository.save(user));
    }

    @RequestMapping(path = AccessPath.API_USERS_SUD, method = RequestMethod.GET)
    public UserDTO getUser(@PathVariable(name = "userId") Long userId){
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
    ){
        Assert.notNull(userDto, "Пользователь не заполнен.");
        Optional<UserEntity> optionalUser = userRepository.findById(userDto.getId());
        if(!optionalUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        UserEntity user = addDataUserEntityFromDto(optionalUser.get(), userDto);
        //TODO Если по себе меняем или админ

        return new UserDTO(userRepository.save(user));
    }

    @RequestMapping(path = AccessPath.API_USERS_SUD, method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(name = "userId") Long userId){
        Assert.notNull(userId, "Параметр строки обращения не корректен.");
        //TODO Если по себе меняем или админ
        userRepository.deleteById(userId);
    }

    private UserEntity addDataUserEntityFromDto(UserEntity userEntity, UserDTO userDto){
        userEntity.setLogin(userDto.getLogin());
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setPatronymic(userDto.getPatronymic());
        userEntity.setSecurityRoleId(SecurityRole.USER.getId());

        return userEntity;
    }

    @RequestMapping(path = AccessPath.API_USERS_SUD_PASSWORD)
    public void changePassword(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "current", required = false) String currentPass,
            @RequestParam(name = "new") String newPass
    ) throws NoSuchAlgorithmException {
        Assert.notNull(userId, "Параметр строки обращения не корректен.");
        Assert.notNull(newPass, "Пароль не может быть пустым.");
        Assert.isTrue(!newPass.isEmpty(), "Пароль не может быть пустым.");

        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if(!optionalUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        UserEntity user = optionalUser.get();
        //TODO Если по себе меняем или админ

        //Проверяем права пользователя
        //Если не Админ
        if(!(SecurityRole.ADMIN).equals(user.getSecurityRoleId())) {
            if (user.getPassword().equals(createMd5FromText(currentPass))) {
                user.setPassword(createMd5FromText(newPass));
            }
        } else {
            //Если Админ
            user.setPassword(createMd5FromText(newPass));
        }

        userRepository.save(user);
    }

    private String createMd5FromText(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(text.getBytes());
        byte[] digest = md.digest();
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);
        while(hashtext.length() < 32 ){
            hashtext = "0"+hashtext;
        }
        return hashtext;
    }


}
