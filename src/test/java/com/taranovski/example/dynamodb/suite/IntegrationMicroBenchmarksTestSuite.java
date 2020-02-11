package com.taranovski.example.dynamodb.suite;

import com.taranovski.example.dynamodb.repository.impl.PersonRepositoryImplJMHTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Alyx on 10.02.2020.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(value = {
        PersonRepositoryImplJMHTest.class,
})
public class IntegrationMicroBenchmarksTestSuite {
}
