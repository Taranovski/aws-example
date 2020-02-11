package com.taranovski.example.dynamodb.repository.impl;

import com.taranovski.example.dynamodb.domain.Person;
import com.taranovski.example.dynamodb.repository.PersonRepository;
import com.taranovski.example.dynamodb.support.JMHIntegrationTestRunSupport;
import org.junit.Ignore;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * Created by Alyx on 10.02.2020.
 */
@Ignore
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 5)
@Measurement(iterations = 10, time = 5)
public class PersonRepositoryImplJMHTest extends JMHIntegrationTestRunSupport {

    private static volatile PersonRepository personRepository;

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        PersonRepositoryImplJMHTest.personRepository = personRepository;
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        public Person person;

        @Setup(Level.Trial)
        public void setupTrial() {
            person = new Person();
            person.setName("Alex");
            person.setSalary(new BigDecimal("100000.00"));
        }
    }

    @Benchmark
    public Object microBenchmarkSave(BenchmarkState benchmarkState) throws Exception {
        return PersonRepositoryImplJMHTest.personRepository.create(benchmarkState.person);
    }


}