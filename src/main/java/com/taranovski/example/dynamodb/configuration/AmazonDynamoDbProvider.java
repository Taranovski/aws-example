package com.taranovski.example.dynamodb.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Alyx on 09.02.2020.
 */
@Configuration
@ConditionalOnExpression("#{environment.getActiveProfiles().?[#this == 'embedded-dynamo-db'].length == 0}")
public class AmazonDynamoDbProvider {

    @Value("${application.connectivity.dynamodb.service.endpoint}")
    private String serviceEndpoint;
    @Value("${application.connectivity.dynamodb.signin.region}")
    private String signingRegion;
    @Value("${application.connectivity.dynamodb.awsAccessKeyId}")
    private String awsAccessKeyId;
    @Value("${application.connectivity.dynamodb.awsSecretKey}")
    private String awsSecretKey;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AwsClientBuilder.EndpointConfiguration endpointConfiguration =
                new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, signingRegion);

        AWSCredentials credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretKey);

        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);

        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(credentialsProvider)
                .build();
    }
}
