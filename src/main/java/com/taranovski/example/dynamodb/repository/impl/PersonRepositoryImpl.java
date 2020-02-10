package com.taranovski.example.dynamodb.repository.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.datamodeling.ScanResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.Select;
import com.taranovski.example.dynamodb.domain.Person;
import com.taranovski.example.dynamodb.repository.PersonRepository;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Created by Alyx on 09.02.2020.
 */
@Repository
public class PersonRepositoryImpl implements PersonRepository {

    private final AmazonDynamoDB amazonDynamoDB;
    private final DynamoDBMapper mapper;

    public PersonRepositoryImpl(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.mapper = new DynamoDBMapper(amazonDynamoDB);
    }

    @Override
    public Person findById(Long id) {
        return mapper.load(Person.class, id);
    }

    @Override
    public void persist(Person person) {
        mapper.save(person);
    }

    @Override
    public String create(Person person) {
        mapper.save(person);
        return person.getId();
    }

    @Override
    public void delete(Long id) {
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setN(String.valueOf(id));
        DeleteItemResult deleteItemResult = amazonDynamoDB.deleteItem(Person.TABLE_NAME, Collections.singletonMap(Person.ID, attributeValue));
    }

    @Override
    public List<Person> findAllOnPage(Integer pageSize, Integer pageNumber) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

        scanExpression.setLimit((pageNumber + 1) * pageSize);

        scanExpression.setSelect(Select.ALL_ATTRIBUTES);

        ScanResultPage<Person> tScanResultPage = mapper.scanPage(Person.class, scanExpression);
        return tScanResultPage.getResults();
    }
}
