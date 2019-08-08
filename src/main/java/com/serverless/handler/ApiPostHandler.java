package com.serverless.handler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.serverless.handler.util.HandlerUtils.isBlank;

import javax.inject.Inject;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.dagger.Injector;
import com.serverless.dao.CommentRepository;
import com.serverless.model.ApiGatewayResponse;
import com.serverless.service.ContextService;
import com.serverless.types.Comment;

public class ApiPostHandler extends AbstractApiHandler {
    @Inject ContextService contextService;
    @Inject CommentRepository repo;

    public ApiPostHandler() {
        Injector.getInjector().inject(this);
    }

    @Override
    protected ApiGatewayResponse handle(APIGatewayProxyRequestEvent request) throws Exception {
        checkArgument(request.getHttpMethod().equals("POST"), "please only post data");
        Comment comment = parseComment(request);
        repo.insertOrUpdate(comment);
        return ApiGatewayResponse
                .builder()
                .setStatusCode(200)
                .setRawBody("success")
                .build();
    }

    @Override
    protected ContextService getContextService() {
        return contextService;
    }

    private static Comment parseComment(APIGatewayProxyRequestEvent request) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = request.getBody();
        checkArgument(!isBlank(requestBody), "comment body cannot be empty");
        return mapper.readValue(requestBody, Comment.class);
    }
}
