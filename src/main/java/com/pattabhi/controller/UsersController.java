package com.pattabhi.controller;

import com.pattabhi.exception.UserExistException;
import com.pattabhi.model.User;
import com.pattabhi.repository.UserJpaRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/")
public class UsersController {

	/** The JPA repository */
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

    /**
	 * Used to find and return a user by name
	 * 
	 * @param name refers to the name of the user
	 * @return {@link User} object
	 */
    @GetMapping(value = "/{name}")
    public User findByName(@PathVariable final String name){
        return userJpaRespository.findByName(name);
    }

    /**
	 * Used to create a User in the DB
	 * 
	 * @param users refers to the User needs to be saved
	 * @return the {@link User} created
	 */
    @PostMapping(value = "/load")
    public User load(@RequestBody final User users) throws UserExistException {
        if(userJpaRespository.exists(users.getEmail())) {
            throw new UserExistException("User already exists with email = "+ users.getEmail());
        }
        userJpaRespository.save(users);
        return userJpaRespository.findByName(users.getEmail());
    }
}
