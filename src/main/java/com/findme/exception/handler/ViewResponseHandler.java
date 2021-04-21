package com.findme.exception.handler;

import com.findme.exception.BadRequestException;
import com.findme.exception.NotFoundException;
import com.findme.exception.UnauthorizedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ViewResponseHandler {

    private static final Logger logger = LogManager.getLogger(ViewResponseHandler.class);

    @ExceptionHandler(value = BadRequestException.class)
    public ModelAndView badRequestHandler(Exception e) {
        ModelAndView mav = new ModelAndView("400");
        mav.addObject("error", e.getMessage());
        return mav;
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ModelAndView badRequestHandler2() {
        ModelAndView mav = new ModelAndView("400");
        mav.addObject("error", "Fields filed incorrect");
        return mav;
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ModelAndView unauthorizedHandler(Exception e) {
        ModelAndView mav = new ModelAndView("401");
        mav.addObject("error", e.getMessage());
        return mav;
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ModelAndView notFoundHandler(Exception e) {
        ModelAndView mav = new ModelAndView("404");
        mav.addObject("error", e.getMessage());
        return mav;
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView internalServerErrorHandler(Exception e) {
        logger.error(e);
        ModelAndView mav = new ModelAndView("500");
        mav.addObject("error", "Something went wrong");
        return mav;
    }
}
