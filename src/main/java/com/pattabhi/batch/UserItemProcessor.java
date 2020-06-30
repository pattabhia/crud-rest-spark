package com.pattabhi.batch;

import com.pattabhi.model.User;
import org.springframework.batch.item.ItemProcessor;

public class UserItemProcessor implements ItemProcessor<User, User> {

    @Override
    public User process(User user) throws Exception {
        System.out.println(user.getEmail());
        System.out.println(user.getName());
        return user;
    }

}