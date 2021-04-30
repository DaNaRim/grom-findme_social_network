package com.findme.util;

import com.findme.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UpdateUserDateLastActiveInterceptor implements HandlerInterceptor {

    private final UserService userService;

    private static final Logger logger = LogManager.getLogger(UpdateUserDateLastActiveInterceptor.class);

    @Autowired
    public UpdateUserDateLastActiveInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        long userId = SecurityUtil.getAuthorizedUserId();

        if (userId != 0) {
            try {
                userService.updateDateLastActive(userId);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        return true;
    }
}
