package com.findme.service;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.exception.UnauthorizedException;
import com.findme.model.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public interface UserService {

    User findById(long id) throws NotFoundException, InternalServerException;

    User registerUser(User user) throws BadRequestException, InternalServerException;

    User login(String mail, String password, HttpSession session)
            throws NotFoundException, BadRequestException, InternalServerException;

    void logout(HttpSession session) throws UnauthorizedException, InternalServerException;

    void updateDateLastActive(long userId) throws InternalServerException;
}
