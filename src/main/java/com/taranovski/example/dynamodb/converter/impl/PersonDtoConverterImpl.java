package com.taranovski.example.dynamodb.converter.impl;

import com.taranovski.example.dynamodb.converter.PersonDtoConverter;
import com.taranovski.example.dynamodb.domain.Person;
import com.taranovski.example.dynamodb.dto.PersonDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alyx on 11.02.2020.
 */
@Component
public class PersonDtoConverterImpl implements PersonDtoConverter {

    @Override
    public Person fromDto(PersonDto personDto) {
        if (personDto == null) {
            return null;
        } else {
            Person person = new Person();

            person.setId(personDto.getId());
            person.setDateOfBirth(personDto.getDateOfBirth());
            person.setHoursPerWeek(personDto.getHoursPerWeek());
            person.setName(personDto.getName());
            person.setSalary(personDto.getSalary());

            return person;
        }
    }

    @Override
    public PersonDto toDto(Person person) {
        if (person == null) {
            return null;
        } else {
            PersonDto personDto = new PersonDto();

            personDto.setId(person.getId());
            personDto.setDateOfBirth(person.getDateOfBirth());
            personDto.setHoursPerWeek(person.getHoursPerWeek());
            personDto.setName(person.getName());
            personDto.setSalary(person.getSalary());

            return personDto;
        }
    }

    @Override
    public List<PersonDto> toDtos(List<Person> allOnPage) {
        List<PersonDto> personDtos = new ArrayList<>(allOnPage.size());

        for (Person person : allOnPage) {
            personDtos.add(toDto(person));
        }

        return personDtos;
    }
}
