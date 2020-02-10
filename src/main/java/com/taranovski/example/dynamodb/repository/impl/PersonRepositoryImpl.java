package com.taranovski.example.dynamodb.repository.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConsumedCapacity;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.Select;
import com.taranovski.example.dynamodb.domain.Person;
import com.taranovski.example.dynamodb.monitoring.DynamoDbMonitoringService;
import com.taranovski.example.dynamodb.repository.PersonRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;

/**
 * Created by Alyx on 09.02.2020.
 */
@Repository
public class PersonRepositoryImpl implements PersonRepository {

    private final AmazonDynamoDB amazonDynamoDB;
    private final DynamoDBMapper mapper;

    private final DynamoDbMonitoringService dynamoDbMonitoringService;

    public PersonRepositoryImpl(AmazonDynamoDB amazonDynamoDB,
                                DynamoDbMonitoringService dynamoDbMonitoringService) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.mapper = new DynamoDBMapper(amazonDynamoDB);

        this.dynamoDbMonitoringService = dynamoDbMonitoringService;
    }

    @Override
    public Person findById(String id) {
        Map<String, AttributeValue> attributes = singletonMap(Person.ID, new AttributeValue(id));
        GetItemResult getItemResult = amazonDynamoDB.getItem(Person.TABLE_NAME, attributes);

        ConsumedCapacity consumedCapacity = getItemResult.getConsumedCapacity();

        dynamoDbMonitoringService.record(consumedCapacity, "findById", id);

        return mapper.marshallIntoObject(Person.class, getItemResult.getItem());
    }

    @Override
    public void persist(Person person) {
        //todo add consumed capacity meter
        mapper.save(person);
    }

    @Override
    public String create(Person person) {
        //todo add consumed capacity meter
        mapper.save(person);
        //todo does not return id
        return person.getId();
    }

    @Override
    public void delete(String id) {
        Map<String, AttributeValue> attributes = singletonMap(Person.ID, new AttributeValue(id));
        DeleteItemResult deleteItemResult = amazonDynamoDB.deleteItem(Person.TABLE_NAME, attributes);

        ConsumedCapacity consumedCapacity = deleteItemResult.getConsumedCapacity();

        dynamoDbMonitoringService.record(consumedCapacity, "delete", id);
    }

    @Override
    public List<Person> findAllOnPage(Integer pageSize, Integer pageNumber) {
        ScanRequest scanRequest = new ScanRequest(Person.TABLE_NAME);

        scanRequest.setLimit((pageNumber + 1) * pageSize);
        scanRequest.setSelect(Select.ALL_ATTRIBUTES);

        ScanResult scanResult = amazonDynamoDB.scan(scanRequest);

        ConsumedCapacity consumedCapacity = scanResult.getConsumedCapacity();

        dynamoDbMonitoringService.record(consumedCapacity, "findAllOnPage", pageSize, pageNumber);

        return mapper.marshallIntoObjects(Person.class, scanResult.getItems());
    }
}
