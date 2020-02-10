package com.taranovski.example.dynamodb.repository;

import com.taranovski.example.dynamodb.domain.Person;

import java.util.List;

/**
 * Created by Alyx on 09.02.2020.
 */
public interface PersonRepository {
    Person findById(Long id);

    void persist(Person person);

    String create(Person person);

    void delete(Long id);

    List<Person> findAllOnPage(Integer pageSize, Integer pageNumber);
}
