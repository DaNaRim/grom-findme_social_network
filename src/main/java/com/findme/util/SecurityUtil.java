package com.findme.util;

import com.findme.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static long getAuthorizedUserId() {

        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user instanceof User) {
            User user1 = (User) user;
            return user1.getId();
        } else {
            return 0;
        }
    }
}
