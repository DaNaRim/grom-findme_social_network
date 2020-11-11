package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.exception.ObjectNotFoundException;
import com.findme.model.Post;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDao {

    Post save(Post post) throws InternalServerException;

    Post findById(long id) throws ObjectNotFoundException, InternalServerException;

    Post update(Post post) throws InternalServerException;

    void delete(Post post) throws InternalServerException;
}
