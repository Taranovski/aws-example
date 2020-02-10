package com.taranovski.example.dynamodb.changelog;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.taranovski.example.dynamodb.domain.Person;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * Created by Alyx on 09.02.2020.
 */
@Component
public class AmazonDynamoDbChangeLog {

    private final AmazonDynamoDB amazonDynamoDB;

    public AmazonDynamoDbChangeLog(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
    }

    @PostConstruct
    public void postConstruct() {
        ListTablesResult listTablesResult = amazonDynamoDB.listTables();
        List<String> tableNames = listTablesResult.getTableNames();
        Set<String> tableNamesSet = new HashSet<>(tableNames);

        createPersonsTableIfAbsent(tableNamesSet);

    }

    private void createPersonsTableIfAbsent(Set<String> tableNamesSet) {
        if (!tableNamesSet.contains(Person.TABLE_NAME)) {

            List<KeySchemaElement> keySchema = asList(
                    new KeySchemaElement(Person.ID, KeyType.HASH),
                    new KeySchemaElement(Person.NAME, KeyType.RANGE)
            );

            List<AttributeDefinition> attributeDefinitions = asList(
                    new AttributeDefinition(Person.ID, ScalarAttributeType.S),
                    new AttributeDefinition(Person.NAME, ScalarAttributeType.S)
            );

            ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(100L, 100L);

            CreateTableRequest createTableRequest = new CreateTableRequest(attributeDefinitions, Person.TABLE_NAME, keySchema, provisionedThroughput);

            amazonDynamoDB.createTable(createTableRequest);
        }
    }
}
