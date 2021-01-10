package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    User save(User user) throws InternalServerException;

    User findById(long id) throws InternalServerException;

    User update(User user) throws InternalServerException;

    boolean isUserMissing(long id) throws InternalServerException;

    User findByMail(String mail) throws InternalServerException;

    void updateDateLastActive(long userId) throws InternalServerException;


    boolean areThePhoneAndMailBusy(String phone, String mail) throws InternalServerException;
}
