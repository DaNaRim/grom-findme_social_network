package com.findme.dao;

import com.findme.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {

    User save(User user);

    User findById(long id);

    User update(User user);

    User findByUsername(String username);

    boolean isUserMissing(long id);

    boolean areUsersMissing(List<Long> usersIds);

    void updateDateLastActive(long userId);


    boolean arePhoneAndMailBusy(String phone, String mail);

    boolean isPhoneBusy(String phone);

    boolean isMailBusy(String mail);

    String findPhone(long id);

    String findMail(long id);
}
