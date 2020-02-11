package com.taranovski.example.dynamodb.rest;

import com.taranovski.example.dynamodb.converter.PersonDtoConverter;
import com.taranovski.example.dynamodb.domain.Person;
import com.taranovski.example.dynamodb.dto.PersonDto;
import com.taranovski.example.dynamodb.repository.PersonRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Alyx on 09.02.2020.
 */
@RestController
@RequestMapping(path = "/persons")
public class PersonController {

    private final PersonRepository personRepository;
    private final PersonDtoConverter personDtoConverter;

    public PersonController(PersonRepository personRepository,
                            PersonDtoConverter personDtoConverter) {
        this.personRepository = personRepository;
        this.personDtoConverter = personDtoConverter;
    }

    @GetMapping
    public List<PersonDto> findPage(@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                 @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

        List<Person> allOnPage = personRepository.findAllOnPage(pageSize, pageNumber);

        return personDtoConverter.toDtos(allOnPage);
    }

    @GetMapping(path = "/{id}")
    public PersonDto findById(@PathVariable(name = "id") String id) {

        Person byId = personRepository.findById(id);

        return personDtoConverter.toDto(byId);
    }

    @PostMapping
    public String createPerson(@RequestBody PersonDto personDto) {

        Person person = personDtoConverter.fromDto(personDto);

        return personRepository.create(person);
    }

    @PutMapping(path = "/{id}")
    public void updatePerson(@PathVariable(name = "id") String id, @RequestBody PersonDto personDto) {

        Person person = personDtoConverter.fromDto(personDto);

        personRepository.update(id, person);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteById(@PathVariable(name = "id") String id) {
        personRepository.delete(id);
    }


}
