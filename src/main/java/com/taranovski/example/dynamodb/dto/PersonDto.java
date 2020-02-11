package com.taranovski.example.dynamodb.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Alyx on 11.02.2020.
 */
public class PersonDto {
    private String name;
    private LocalDate dateOfBirth;
    private BigDecimal salary;
    private int hoursPerWeek;
    private String id;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setHoursPerWeek(int hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
    }

    public int getHoursPerWeek() {
        return hoursPerWeek;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
