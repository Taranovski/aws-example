package com.taranovski.example.dynamodb.configuration.database;

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

import javax.annotation.PreDestroy;
import java.io.File;
import java.util.Objects;

/**
 * Created by Alyx on 09.02.2020.
 */
@Configuration
@ConditionalOnExpression("#{environment.getActiveProfiles().?[#this == 'embedded-dynamo-db'].length == 1}")
public class EmbeddedAmazonDynamoDbConfiguration {

    @Value("${application.connectivity.dynamodb.service.protocol}")
    private String serviceProtocol;
    @Value("${application.connectivity.dynamodb.service.host}")
    private String serviceHost;
    @Value("${application.connectivity.dynamodb.service.port}")
    private String servicePort;
    @Value("${application.connectivity.dynamodb.signin.region}")
    private String signingRegion;
    @Value("${application.connectivity.dynamodb.awsAccessKeyId}")
    private String awsAccessKeyId;
    @Value("${application.connectivity.dynamodb.awsSecretKey}")
    private String awsSecretKey;
    @Value("${application.connectivity.dynamodb.embedded.persistence.type}")
    private String localServicePersistenceType;

    private AmazonDynamoDB amazonDynamoDB;
    private DynamoDBProxyServer dynamoDBProxyServer;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() throws Exception {
        System.setProperty("sqlite4java.library.path", "native-libs");

        dynamoDBProxyServer = ServerRunner.createServerFromCommandLineArgs(
                new String[]{
                        Objects.equals(localServicePersistenceType, "in-memory") ?
                                "-inMemory" :
                                "-dbPath", System.getProperty("user.dir") + File.separator + "target",
                        "-port", servicePort,
                });

        dynamoDBProxyServer.start();

        AwsClientBuilder.EndpointConfiguration endpointConfiguration =
                new AwsClientBuilder.EndpointConfiguration(getServiceEndpoint(), signingRegion);

        AWSCredentials credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretKey);

        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);

        amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(credentialsProvider)
                .build();

        return amazonDynamoDB;
    }

    private String getServiceEndpoint() {
        return serviceProtocol + "://" + serviceHost + ":" + servicePort;
    }

    @PreDestroy
    public void preDestroy() throws Exception {
        if (amazonDynamoDB != null) {
            amazonDynamoDB.shutdown();
        }

        if (dynamoDBProxyServer != null) {
            dynamoDBProxyServer.stop();
        }
    }
}
