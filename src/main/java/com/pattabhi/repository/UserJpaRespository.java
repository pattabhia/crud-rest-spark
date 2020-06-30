package com.pattabhi.repository;

import com.pattabhi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserJpaRespository extends JpaRepository<User, String>{

    User findByName(String name);
}
