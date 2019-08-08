package com.serverless.handler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.serverless.dagger.Injector;
import com.serverless.dao.CommentRepository;
import com.serverless.model.ApiGatewayResponse;
import com.serverless.service.ContextService;

public class ApiGetHandler extends AbstractApiHandler {
    @Inject CommentRepository repo;
    @Inject ContextService contextService;

    public ApiGetHandler() {
        Injector.getInjector().inject(this);
    }

    @Override
    protected ContextService getContextService() {
        return contextService;
    }

    @Override
    protected ApiGatewayResponse handle(APIGatewayProxyRequestEvent request) {
        checkArgument(request.getHttpMethod().equals("GET"), "input is not get method");
        checkNotNull(request.getQueryStringParameters(), "please specify the title");
        return ApiGatewayResponse
                .builder()
                .setStatusCode(200)
                .setObjectBody(repo.getAllForPost(request.getQueryStringParameters().get("title")))
                .build();
    }
}
