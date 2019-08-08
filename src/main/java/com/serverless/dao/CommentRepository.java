package com.serverless.dao;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.serverless.types.Comment;

public interface CommentRepository {
    void insertOrUpdate(Comment comment);
    boolean delete(Comment comment);
    ImmutableSet<Comment> all();
    ImmutableSet<Comment> getAllForPost(String postTitle);
}
