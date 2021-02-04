package com.findme.util;

import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

@Component
public class UpdateUserDateLastActiveInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Autowired
    public UpdateUserDateLastActiveInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        Long userId = (Long) request.getSession().getAttribute("userId");

        if (userId != null) {
            try {
                userService.updateDateLastActive(userId);
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                System.err.println(sw.toString());
            }
        }
        return true;
    }
}
