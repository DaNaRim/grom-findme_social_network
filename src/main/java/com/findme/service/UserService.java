package com.findme.service;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User findById(long id) throws NotFoundException, InternalServerException;

    User registerUser(User user) throws BadRequestException, InternalServerException;

    User login(String mail, String password) throws NotFoundException, BadRequestException, InternalServerException;

    void updateDateLastActive(User user) throws InternalServerException;
}
