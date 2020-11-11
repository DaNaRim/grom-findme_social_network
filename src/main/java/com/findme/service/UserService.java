package com.findme.service;

import com.findme.exception.InternalServerException;
import com.findme.exception.ObjectNotFoundException;
import com.findme.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User findById(long id) throws ObjectNotFoundException, InternalServerException;
}
