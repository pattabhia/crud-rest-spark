package com.pattabhi.repository;

import com.pattabhi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserJpaRespository extends JpaRepository<User, String>{

    List<User> findByName(String name);

    User findByEmail(String email);
}
