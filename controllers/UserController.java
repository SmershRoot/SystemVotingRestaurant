package ru.smv.system.restaurant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.smv.system.restaurant.constants.AccessPath;
import ru.smv.system.restaurant.models.db.UserEntity;
import ru.smv.system.restaurant.repository.UserRepository;

import java.util.List;

@RestController
public class UserController {
    
    @Autowired
    UserRepository userRepository;

    @RequestMapping(path = AccessPath.API_USERS, method = RequestMethod.GET)
    public List<UserEntity> getUsers(){
        List<UserEntity> users = userRepository.findAll();
        return users;
    }

    @RequestMapping(path = AccessPath.API_USERS, method = RequestMethod.POST)
    public UserEntity createUser(@RequestBody UserEntity userDto){
        return userRepository.save(userDto);
    }

    @RequestMapping(path = AccessPath.API_USERS_SUD, method = RequestMethod.GET)
    public UserEntity getUser(@PathVariable(name = "userId") Long userId){
        return userRepository.getOne(userId);
    }

    @RequestMapping(path = AccessPath.API_USERS, method = RequestMethod.PUT)
    public UserEntity updateUser(
            @RequestBody UserEntity userDto
    ){
        UserEntity user = userRepository.getOne(userDto.getId());

        if(user==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }

        return userRepository.save(userDto);
    }

    @RequestMapping(path = AccessPath.API_USERS_SUD, method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable(name = "userId") Long userId){
        userRepository.deleteById(userId);
    }
}
