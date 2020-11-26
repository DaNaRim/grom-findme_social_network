package com.findme.service;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.ObjectNotFoundException;
import com.findme.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User findById(long id) throws ObjectNotFoundException, InternalServerException;

    User registerUser(User user) throws BadRequestException, InternalServerException;

    User loginUser(String mail, String password) throws BadRequestException, InternalServerException, ObjectNotFoundException;
}
