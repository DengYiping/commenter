package com.serverless.dagger;

import com.serverless.module.DynamoDbModule;
import com.serverless.module.LambdaModule;

public class Injector {
    private static LambdaComponent component;
    public static LambdaComponent getInjector() {
        if (component == null) {
            synchronized (Injector.class) {
                if (component == null) {
                    component = DaggerLambdaComponent.create();
                }
            }
        }
        return component;
    }
}
