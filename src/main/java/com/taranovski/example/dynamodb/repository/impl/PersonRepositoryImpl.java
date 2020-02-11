package com.taranovski.example.dynamodb.repository.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.ConversionSchema;
import com.amazonaws.services.dynamodbv2.datamodeling.ConversionSchemas;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperTableModel;
import com.amazonaws.services.dynamodbv2.datamodeling.ItemConverter;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ConsumedCapacity;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ReturnConsumedCapacity;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.Select;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import com.taranovski.example.dynamodb.domain.Person;
import com.taranovski.example.dynamodb.monitoring.DynamoDbMonitoringService;
import com.taranovski.example.dynamodb.repository.PersonRepository;
import com.taranovski.example.dynamodb.util.UUIDProvider;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;

/**
 * Created by Alyx on 09.02.2020.
 */
@Repository
public class PersonRepositoryImpl implements PersonRepository {

    public static final List<String> ALL_ATTRIBUTE_NAMES = asList(Person.ID, Person.NAME, Person.DATE_OF_BIRTH, Person.HOURS_PER_WEEK, Person.SALARY);
    public static final List<String> ALL_ATTRIBUTE_NAMES_WITHOUT_KEYS = asList(Person.NAME, Person.DATE_OF_BIRTH, Person.HOURS_PER_WEEK, Person.SALARY);
    private final DynamoDbMonitoringService dynamoDbMonitoringService;
    private final UUIDProvider uuidProvider;

    private final AmazonDynamoDB amazonDynamoDB;
    private final DynamoDBMapper mapper;

    private final DynamoDBMapperTableModel<Person> tableModel;

    public PersonRepositoryImpl(DynamoDbMonitoringService dynamoDbMonitoringService,
                                UUIDProvider uuidProvider,
                                AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.uuidProvider = uuidProvider;
        this.mapper = new DynamoDBMapper(amazonDynamoDB);

        this.tableModel = mapper.getTableModel(Person.class);

        this.dynamoDbMonitoringService = dynamoDbMonitoringService;
    }

    @Override
    public Person findById(String id) {
        Map<String, AttributeValue> attributes = singletonMap(Person.ID, new AttributeValue(id));

        GetItemRequest getItemRequest = new GetItemRequest(Person.TABLE_NAME, attributes);

        getItemRequest.setReturnConsumedCapacity(ReturnConsumedCapacity.TOTAL);
        getItemRequest.setAttributesToGet(ALL_ATTRIBUTE_NAMES);

        GetItemResult getItemResult = amazonDynamoDB.getItem(getItemRequest);

        ConsumedCapacity consumedCapacity = getItemResult.getConsumedCapacity();

        dynamoDbMonitoringService.record(consumedCapacity, "findById", id);

        return mapper.marshallIntoObject(Person.class, getItemResult.getItem());
    }

    @Override
    public void update(String id, Person person) {

        Map<String, AttributeValue> keyAttributes = singletonMap(Person.ID, new AttributeValue(id));

        Map<String, AttributeValue> attributeValues = tableModel.convert(person);

        Map<String, AttributeValueUpdate> attributeValueUpdates = new HashMap<>();

        for (String columnName : ALL_ATTRIBUTE_NAMES_WITHOUT_KEYS) {
            if (attributeValues.containsKey(columnName)) {
                attributeValueUpdates.put(columnName, new AttributeValueUpdate(attributeValues.get(columnName), AttributeAction.PUT));
            } else {
                AttributeValue attributeValue = new AttributeValue();
                attributeValue.setNULL(true);
                attributeValueUpdates.put(columnName, new AttributeValueUpdate(attributeValue, AttributeAction.PUT));
            }
        }

        UpdateItemRequest updateItemRequest = new UpdateItemRequest(Person.TABLE_NAME, keyAttributes, attributeValueUpdates);

        updateItemRequest.setReturnConsumedCapacity(ReturnConsumedCapacity.TOTAL);

        UpdateItemResult updateItemResult = amazonDynamoDB.updateItem(updateItemRequest);

        ConsumedCapacity consumedCapacity = updateItemResult.getConsumedCapacity();

        dynamoDbMonitoringService.record(consumedCapacity, "update", person);
    }

    @Override
    public String create(Person person) {
        String uuid = uuidProvider.getUUID();

        person.setId(uuid);

        Map<String, AttributeValue> attributeValueMap = tableModel.convert(person);

        PutItemRequest putItemRequest = new PutItemRequest(Person.TABLE_NAME, attributeValueMap);

        putItemRequest.setReturnConsumedCapacity(ReturnConsumedCapacity.TOTAL);

        putItemRequest.setReturnValues(ReturnValue.ALL_OLD);

        PutItemResult putItemResult = amazonDynamoDB.putItem(putItemRequest);

        ConsumedCapacity consumedCapacity = putItemResult.getConsumedCapacity();

        dynamoDbMonitoringService.record(consumedCapacity, "create", person);

        return uuid;
    }

    @Override
    public void delete(String id) {
        Map<String, AttributeValue> attributes = singletonMap(Person.ID, new AttributeValue(id));

        DeleteItemRequest deleteItemRequest = new DeleteItemRequest(Person.TABLE_NAME, attributes);

        deleteItemRequest.setReturnConsumedCapacity(ReturnConsumedCapacity.TOTAL);

        DeleteItemResult deleteItemResult = amazonDynamoDB.deleteItem(deleteItemRequest);

        ConsumedCapacity consumedCapacity = deleteItemResult.getConsumedCapacity();

        dynamoDbMonitoringService.record(consumedCapacity, "delete", id);
    }

    @Override
    public List<Person> findAllOnPage(Integer pageSize, Integer pageNumber) {
        ScanRequest scanRequest = new ScanRequest(Person.TABLE_NAME);

        scanRequest.setLimit((pageNumber + 1) * pageSize);
        scanRequest.setSelect(Select.ALL_ATTRIBUTES);

        scanRequest.setReturnConsumedCapacity(ReturnConsumedCapacity.TOTAL);

        ScanResult scanResult = amazonDynamoDB.scan(scanRequest);

        ConsumedCapacity consumedCapacity = scanResult.getConsumedCapacity();

        dynamoDbMonitoringService.record(consumedCapacity, "findAllOnPage", pageSize, pageNumber);

        return mapper.marshallIntoObjects(Person.class, scanResult.getItems());
    }
}
