package com.pattabhi.controller;

import com.pattabhi.exception.UserAlreadyExistException;
import com.pattabhi.model.User;
import com.pattabhi.model.UserStatus;
import com.pattabhi.repository.UserJpaRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    /**
     * The JPA repository
     */
    @Autowired
    private UserJpaRespository userJpaRespository;

    /**
     * Used to fetch all the users from DB
     *
     * @return list of {@link User}
     */
    @GetMapping(value = "/all")
    public List<User> findAll() {
        return userJpaRespository.findAll();
    }

    /*   *//**
     * Used to find and return a user by name
     *
     * @param name refers to the name of the user
     * @return {@link User} object
     *//*
    @GetMapping(value = "/{name}")
    public User findByName(@PathVariable final String name){
        return userJpaRespository.findByName(name);
    }*/

    /**
     * Used to find and return a user by email
     *
     * @param name refers to the name of the user
     * @return {@link User} object
     */
    @GetMapping(value = "/{name}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> findByName(@PathVariable final String name) {
        List<User> user = userJpaRespository.findByName(name);
        return user;
    }

    /**
     * Used to create a User in the DB
     *
     * @param users refers to the User needs to be saved
     * @return the {@link User} created
     */
    @PostMapping(value = "/load", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public User load(@RequestBody final User users) {
        User savedUser = null;
        try {
            if (userJpaRespository.exists(users.getEmail())) {
                throw new UserAlreadyExistException("User already exists with email = " + users.getEmail());
            }
            users.getAddress().setEmail(users.getEmail());
            savedUser = userJpaRespository.save(users);
        } catch (UserAlreadyExistException e) {
            User user = userJpaRespository.findByEmail(users.getEmail());
           return new UserStatus(user,e.getMessage());
        }  catch (Exception e) {
            return new UserStatus(new User(),HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
        return savedUser;
    }
}
