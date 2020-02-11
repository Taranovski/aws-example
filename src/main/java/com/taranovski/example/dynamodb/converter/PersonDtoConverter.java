package com.taranovski.example.dynamodb.converter;

import com.taranovski.example.dynamodb.domain.Person;
import com.taranovski.example.dynamodb.dto.PersonDto;

import java.util.List;

/**
 * Created by Alyx on 11.02.2020.
 */
public interface PersonDtoConverter {
    Person fromDto(PersonDto personDto);

    PersonDto toDto(Person byId);

    List<PersonDto> toDtos(List<Person> allOnPage);
}
