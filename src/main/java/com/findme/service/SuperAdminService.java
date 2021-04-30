package com.findme.service;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import org.springframework.stereotype.Service;

@Service
public interface SuperAdminService {

    void makeAdmin(long userId) throws BadRequestException, InternalServerException;

    void removeAdmin(long userId) throws BadRequestException, InternalServerException;
}
