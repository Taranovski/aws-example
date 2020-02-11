package com.taranovski.example.dynamodb;

import com.taranovski.example.dynamodb.configuration.DynamoDbConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Created by Alyx on 09.02.2020.
 */
@SpringBootApplication
@Import(value = {
        DynamoDbConfiguration.class,
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
