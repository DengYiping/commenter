package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.serverless.model.ApiGatewayResponse;
import com.serverless.service.ContextService;

public abstract class AbstractApiHandler implements RequestHandler<APIGatewayProxyRequestEvent, ApiGatewayResponse> {
    @Override
    public final ApiGatewayResponse handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        getContextService().setHandlerContext(context);
        try {
            return handle(input);
        } catch (Throwable ex) {
            getContextService().reportException(ex);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(ex.getMessage())
                    .build();
        }
    }

    protected abstract ApiGatewayResponse handle(APIGatewayProxyRequestEvent request) throws Exception;

    protected abstract ContextService getContextService();
}
