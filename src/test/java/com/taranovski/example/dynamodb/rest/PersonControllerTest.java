package com.taranovski.example.dynamodb.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taranovski.example.dynamodb.dto.PersonDto;
import com.taranovski.example.dynamodb.support.IntegrationTestRunSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Alyx on 10.02.2020.
 */
public class PersonControllerTest extends IntegrationTestRunSupport {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCreateAndListAndFindPerson() {
        List emptyPersonsList = testRestTemplate.getForObject("/persons", List.class);

        assertTrue(emptyPersonsList.isEmpty());

        PersonDto person = new PersonDto();
        person.setName("Alex");
        person.setDateOfBirth(LocalDate.of(1234, 2, 3));
        person.setSalary(new BigDecimal("123456.78"));
        person.setHoursPerWeek(40);

        String personId = testRestTemplate.postForObject("/persons", person, String.class);

        assertNotNull(personId);

        List list = testRestTemplate.getForObject("/persons", List.class);
        List<PersonDto> personsList = objectMapper.convertValue(list, new TypeReference<List<PersonDto>>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        });

        assertEquals(1, personsList.size());

        PersonDto firstPerson = personsList.get(0);

        assertEquals(personId, firstPerson.getId());
        assertEquals(person.getName(), firstPerson.getName());
        assertEquals(person.getDateOfBirth(), firstPerson.getDateOfBirth());
        assertEquals(person.getHoursPerWeek(), firstPerson.getHoursPerWeek());
        assertEquals(person.getSalary(), firstPerson.getSalary());


        PersonDto objectFromDb = testRestTemplate.getForObject("/persons/" + personId, PersonDto.class);

        assertEquals(person.getName(), objectFromDb.getName());
        assertEquals(person.getDateOfBirth(), objectFromDb.getDateOfBirth());
        assertEquals(person.getHoursPerWeek(), objectFromDb.getHoursPerWeek());
        assertEquals(person.getSalary(), objectFromDb.getSalary());

        person.setDateOfBirth(null);

        testRestTemplate.put("/persons/" + personId, person);

        PersonDto objectFromDb1 = testRestTemplate.getForObject("/persons/" + personId, PersonDto.class);

        assertEquals(person.getName(), objectFromDb1.getName());
        assertNull(objectFromDb1.getDateOfBirth());
        assertEquals(person.getHoursPerWeek(), objectFromDb1.getHoursPerWeek());
        assertEquals(person.getSalary(), objectFromDb1.getSalary());

        testRestTemplate.delete("/persons/" + personId);

        List emptyPersonsList1 = testRestTemplate.getForObject("/persons", List.class);

        assertTrue(emptyPersonsList1.isEmpty());

    }

}