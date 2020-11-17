package com.findme.dao;

import com.findme.model.Post;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PostDaoImpl extends Dao<Post> implements PostDao {

    public PostDaoImpl() {
        super(Post.class);
    }

}
