package com.serverless.dagger;

import javax.inject.Singleton;

import com.serverless.handler.ApiEchoHandler;
import com.serverless.handler.ApiGetHandler;
import com.serverless.handler.ApiPostHandler;
import com.serverless.module.DaoModule;
import com.serverless.module.DynamoDbModule;
import com.serverless.module.LambdaModule;

import dagger.Component;

@Singleton
@Component(
        modules = {
                LambdaModule.class,
                DynamoDbModule.class,
                DaoModule.class})
public interface LambdaComponent {
    void inject(ApiGetHandler handler);

    void inject(ApiPostHandler handler);

    void inject(ApiEchoHandler handler);
}
