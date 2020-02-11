package com.taranovski.example.dynamodb.repository.impl;

import com.taranovski.example.dynamodb.domain.Person;
import com.taranovski.example.dynamodb.repository.PersonRepository;
import com.taranovski.example.dynamodb.support.IntegrationTestRunSupport;
import com.taranovski.example.dynamodb.utils.MacroBenchmarkUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Repeat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

/**
 * Created by Alyx on 10.02.2020.
 */
@Ignore
public class PersonRepositoryImplPerformanceTest extends IntegrationTestRunSupport {

    @Autowired
    private PersonRepository personRepository;

    @Repeat(value = 10)
    @Test
    public void shouldNotThrottle() throws Exception {

        int parallelism = Runtime.getRuntime().availableProcessors() * 2;
        int numberOfTasks = parallelism * 500;
        List<Callable<String>> tasks = new ArrayList<>(numberOfTasks);

        for (int i = 0; i < numberOfTasks; i++) {
            tasks.add(getCallable(i));
        }

        ExecutorService executorService = Executors.newWorkStealingPool(parallelism);
        List<String> actualResults = new ArrayList<>(numberOfTasks);

        MacroBenchmarkUtils.measureAndLogExecutionTime(() -> {
            List<Future<String>> submittedOperations = executorService.invokeAll(tasks);

            for (Future<String> singleResult : submittedOperations) {
                String result = singleResult.get();
                actualResults.add(result);
            }
        });

        List<Runnable> runnables = executorService.shutdownNow();
        assertTrue(runnables.isEmpty());

        assertEquals(numberOfTasks, actualResults.size());

    }

    private Callable<String> getCallable(int i) {
        return () -> {
            Person person = new Person();
            person.setHoursPerWeek(i);

            return personRepository.create(person);
        };
    }

}