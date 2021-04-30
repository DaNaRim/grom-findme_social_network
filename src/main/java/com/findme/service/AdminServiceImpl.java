package com.findme.service;

import com.findme.dao.PostDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AdminServiceImpl implements AdminService {

    private final PostDao postDao;

    @Autowired
    public AdminServiceImpl(PostDao postDao) {
        this.postDao = postDao;
    }

    @Override
    public void deletePost(long postId) throws BadRequestException, InternalServerException {

        if (postDao.isPostMissing(postId)) {
            throw new BadRequestException("Missing post with this id");
        }
        postDao.delete(postDao.findById(postId));
    }
}
