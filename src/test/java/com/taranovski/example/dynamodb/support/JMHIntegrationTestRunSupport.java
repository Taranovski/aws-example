package com.taranovski.example.dynamodb.support;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.taranovski.example.dynamodb.changelog.AmazonDynamoDbChangeLog;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by Alyx on 11.02.2020.
 */
@RunWith(SpringRunner.class)
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
public abstract class JMHIntegrationTestRunSupport {

    private volatile static AmazonDynamoDB amazonDynamoDB;

    private volatile static AmazonDynamoDbChangeLog amazonDynamoDbChangeLog;

    @Autowired
    public void setAmazonDynamoDB(AmazonDynamoDB amazonDynamoDB) {
        JMHIntegrationTestRunSupport.amazonDynamoDB = amazonDynamoDB;
    }

    @Autowired
    public void setAmazonDynamoDbChangeLog(AmazonDynamoDbChangeLog amazonDynamoDbChangeLog) {
        JMHIntegrationTestRunSupport.amazonDynamoDbChangeLog = amazonDynamoDbChangeLog;
    }

    @Before
    public void before() {
        List<String> tableNames = amazonDynamoDB.listTables().getTableNames();
        for (String tableName : tableNames) {
            amazonDynamoDB.deleteTable(tableName);
        }

        amazonDynamoDbChangeLog.applyChangeLog();
    }

    @Test
    public void runMicroBenchmarks() throws RunnerException {
        Options jmhRunnerOptions = new OptionsBuilder()
                .include(this.getClass().getSimpleName())
                //requires forks(0) to have spring functional
                .forks(0)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .resultFormat(ResultFormatType.JSON)
                .result("/dev/null")
                .shouldFailOnError(true)
                .jvmArgs("-server")
                .build();
        new Runner(jmhRunnerOptions).run();
    }

}
