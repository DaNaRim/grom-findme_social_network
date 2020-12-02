package com.findme.service;

import com.findme.exception.InternalServerException;
import com.findme.exception.ServiceException;
import com.findme.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User findById(long id) throws ServiceException, InternalServerException;

    User registerUser(User user) throws ServiceException, InternalServerException;

    User login(String mail, String password) throws ServiceException, InternalServerException;

    void logout(long userId) throws InternalServerException;

    void updateDateLastActive(long userId) throws InternalServerException;
}
