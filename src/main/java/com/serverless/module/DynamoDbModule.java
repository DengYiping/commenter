package com.serverless.module;

import javax.inject.Named;
import javax.inject.Singleton;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;

import dagger.Module;
import dagger.Provides;

@Module
public class DynamoDbModule {
    @Provides
    @Singleton
    static DynamoDB dynamoDB(final AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDB(amazonDynamoDB);
    }

    @Provides
    @Singleton
    static DynamoDBMapper providesDynamoDBMapper(final AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB);
    }

    @Provides
    @Singleton
    static AmazonDynamoDB providesAmazonDynamoDB(Regions regions) {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(regions)
                .build();
    }


    @Provides
    @Singleton
    static Table providesTable(
            @Named("tableName") String tableName,
            DynamoDB dynamoDB) {
        return dynamoDB.getTable(tableName);
    }
}
