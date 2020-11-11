package com.findme.service;

import com.findme.dao.UserDao;
import com.findme.exception.InternalServerException;
import com.findme.exception.ObjectNotFoundException;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {

    private final UserDao userdao;

    @Autowired
    public UserServiceImpl(UserDao userdao) {
        this.userdao = userdao;
    }

    public User findById(long id) throws ObjectNotFoundException, InternalServerException {
        return userdao.findById(id);
    }
}
