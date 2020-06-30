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
    @GetMapping(value = "/all",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
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
    public ResponseEntity<User> load(@RequestBody final User users) {
        ResponseEntity<User> responseEntity = null;
        try {
            if (userJpaRespository.exists(users.getEmail())) {
                throw new UserAlreadyExistException("User already exists with email = " + users.getEmail());
            }
            users.getAddress().setEmail(users.getEmail());
            User savedUser = userJpaRespository.save(users);
            responseEntity = new ResponseEntity<>(savedUser, HttpStatus.ACCEPTED);
        } catch (UserAlreadyExistException e) {
            responseEntity = new ResponseEntity<>(new UserStatus(userJpaRespository.findByEmail(users.getEmail()),e.getMessage()), HttpStatus.BAD_REQUEST);
        }  catch (Exception e) {
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}
