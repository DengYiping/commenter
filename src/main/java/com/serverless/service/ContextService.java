package com.serverless.service;

import static lombok.Lombok.sneakyThrow;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import lombok.Getter;
import lombok.experimental.Delegate;

@Singleton
public class ContextService implements Context {
    @Delegate
    private Context handlerContext;

    @Delegate
    private LambdaLogger lambdaLogger;

    @Inject
    public ContextService() {
    }

    public void setHandlerContext(Context delegate) {
        this.handlerContext = delegate;
        this.lambdaLogger = handlerContext.getLogger();
    }

    public void reportException(Throwable ex) {
        try (
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter)){
            ex.printStackTrace(printWriter);
            log(stringWriter.toString());
        } catch (Throwable ignored) {
        }
    }
}
