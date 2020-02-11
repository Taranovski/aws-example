package com.taranovski.example.dynamodb.support;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
})
public abstract class IntegrationTestRunSupport {

}
