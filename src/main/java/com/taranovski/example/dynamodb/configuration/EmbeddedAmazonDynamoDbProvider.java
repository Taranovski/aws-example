package com.taranovski.example.dynamodb.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PreDestroy;
import java.io.File;

/**
 * Created by Alyx on 09.02.2020.
 */
@Profile("local")
@Configuration
@ConditionalOnExpression("#{environment.getActiveProfiles().?[#this == 'local'].length == 1}")
public class EmbeddedAmazonDynamoDbProvider {

    @Value("${application.connectivity.dynamodb.service.endpoint}")
    private String serviceEndpoint;
    @Value("${application.connectivity.dynamodb.signin.region}")
    private String signingRegion;
    @Value("${application.connectivity.dynamodb.awsAccessKeyId}")
    private String awsAccessKeyId;
    @Value("${application.connectivity.dynamodb.awsSecretKey}")
    private String awsSecretKey;
    @Value("${application.connectivity.dynamodb.local.port}")
    private String localServicePort;

    private AmazonDynamoDB amazonDynamoDB;
    private DynamoDBProxyServer dynamoDBProxyServer;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() throws Exception {
        System.setProperty("sqlite4java.library.path", "native-libs");

        dynamoDBProxyServer = ServerRunner.createServerFromCommandLineArgs(
                new String[]{
//                        "-inMemory",
                        "-dbPath", System.getProperty("user.dir") + File.separator + "target",
                        "-port", localServicePort,
                });

        dynamoDBProxyServer.start();

        AwsClientBuilder.EndpointConfiguration endpointConfiguration =
                new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, signingRegion);

        AWSCredentials credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretKey);

        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);

        amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(credentialsProvider)
                .build();

        return amazonDynamoDB;
    }

    @PreDestroy
    public void preDestroy() throws Exception {
        if (dynamoDBProxyServer != null) {
            dynamoDBProxyServer.stop();
        }

        if (amazonDynamoDB != null) {
            amazonDynamoDB.shutdown();
        }
    }
}
