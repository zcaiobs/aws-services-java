package com.aws.java.demoawsservices.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsServicesConfig {

    @Bean
    AmazonDynamoDB service() {
        var dynamodbClient = AmazonDynamoDBClient.builder();
        dynamodbClient.setCredentials(new DefaultAWSCredentialsProviderChain());
        dynamodbClient.setRegion("us-east-1");
        return dynamodbClient.build();
    }
}
