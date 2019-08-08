package com.serverless.module;

import javax.inject.Named;
import javax.inject.Singleton;

import com.amazonaws.regions.Regions;

import dagger.Module;
import dagger.Provides;

@Module
public class LambdaModule {
    @Singleton @Provides
    static Regions getRegions() {
        return Regions.fromName(System.getenv("AWS_REGION"));
    }

    @Provides
    @Singleton
    @Named("tableName")
    static String providesTableName() {
        return System.getenv("DYNAMODB_TABLE");
    }
}
