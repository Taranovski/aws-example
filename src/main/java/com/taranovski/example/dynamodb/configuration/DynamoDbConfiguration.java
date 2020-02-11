package com.taranovski.example.dynamodb.configuration;

import com.taranovski.example.dynamodb.configuration.database.ExternalAmazonDynamoDbConfiguration;
import com.taranovski.example.dynamodb.configuration.database.EmbeddedAmazonDynamoDbConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Alyx on 10.02.2020.
 */
@Configuration
@Import({
        ExternalAmazonDynamoDbConfiguration.class,
        EmbeddedAmazonDynamoDbConfiguration.class,
})
public class DynamoDbConfiguration {
}
