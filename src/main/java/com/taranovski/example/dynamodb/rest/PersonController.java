package com.taranovski.example.dynamodb.rest;

import com.taranovski.example.dynamodb.domain.Person;
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

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping
    public List<Person> findPage(@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                 @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {
        return personRepository.findAllOnPage(pageSize, pageNumber);
    }

    @GetMapping(path = "/{id}")
    public Person findById(@PathVariable(name = "id") String id) {
        return personRepository.findById(id);
    }

    @PostMapping
    public String createPerson(@RequestBody Person person) {
        return personRepository.create(person);
    }

    @PutMapping(path = "/{id}")
    public void updatePerson(@PathVariable(name = "id") String id, @RequestBody Person person) {
        personRepository.update(id, person);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteById(@PathVariable(name = "id") String id) {
        personRepository.delete(id);
    }


}
