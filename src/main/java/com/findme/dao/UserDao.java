package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.exception.ObjectNotFoundException;
import com.findme.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    User save(User user) throws InternalServerException;

    User findById(long id) throws ObjectNotFoundException, InternalServerException;

    User update(User user) throws InternalServerException;

    void delete(User user) throws InternalServerException;
}
