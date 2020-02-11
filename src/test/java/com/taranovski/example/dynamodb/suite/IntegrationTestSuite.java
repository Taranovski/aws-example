package com.taranovski.example.dynamodb.suite;

import com.taranovski.example.dynamodb.repository.impl.PersonRepositoryImplPerformanceTest;
import com.taranovski.example.dynamodb.rest.PersonControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Alyx on 10.02.2020.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(value = {
        PersonControllerTest.class,
        PersonRepositoryImplPerformanceTest.class,

})
public class IntegrationTestSuite {
}
