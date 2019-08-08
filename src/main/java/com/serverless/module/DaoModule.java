package com.serverless.module;

import com.serverless.dao.CommentRepository;
import com.serverless.dao.DynamoCommentRepository;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class DaoModule {
    @Binds
    abstract CommentRepository bindCommentRepository(DynamoCommentRepository repo);
}
