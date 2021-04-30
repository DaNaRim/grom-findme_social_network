package com.findme.service;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {

    void deletePost(long postId) throws BadRequestException, InternalServerException;

}
