package com.serverless.handler;

import javax.inject.Inject;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.serverless.dagger.Injector;
import com.serverless.dao.CommentRepository;
import com.serverless.model.ApiGatewayResponse;
import com.serverless.service.ContextService;

public class ApiEchoHandler extends AbstractApiHandler {
    @Inject CommentRepository repo;
    @Inject ContextService contextService;

    public ApiEchoHandler() {
        Injector.getInjector().inject(this);
    }

    @Override
    protected ApiGatewayResponse handle(APIGatewayProxyRequestEvent request) throws Exception {
        return ApiGatewayResponse
                .builder()
                .setStatusCode(200)
                .setObjectBody(request)
                .build();
    }

    @Override
    protected ContextService getContextService() {
        return contextService;
    }
}
