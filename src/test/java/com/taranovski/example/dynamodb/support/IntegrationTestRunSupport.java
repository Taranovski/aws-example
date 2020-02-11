package com.taranovski.example.dynamodb.support;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.taranovski.example.dynamodb.changelog.AmazonDynamoDbChangeLog;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Alyx on 10.02.2020.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles(profiles = {
        "integration-test",
})
@TestPropertySource(properties = {
        "logging.level.org.springframework=WARN",
        "logging.level.com.taranovski.example.dynamodb=WARN",
})
public abstract class IntegrationTestRunSupport {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private AmazonDynamoDbChangeLog amazonDynamoDbChangeLog;

    @Before
    public void before() {
        List<String> tableNames = amazonDynamoDB.listTables().getTableNames();
        for (String tableName : tableNames) {
            amazonDynamoDB.deleteTable(tableName);
        }

        amazonDynamoDbChangeLog.applyChangeLog();
    }
}
