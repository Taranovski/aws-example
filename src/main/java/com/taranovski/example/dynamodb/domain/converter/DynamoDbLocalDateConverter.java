package com.taranovski.example.dynamodb.domain.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by Alyx on 09.02.2020.
 */
public class DynamoDbLocalDateConverter implements DynamoDBTypeConverter<String, LocalDate> {

    private final DateTimeFormatter dateFormat = DateTimeFormatter.BASIC_ISO_DATE;

    @Override
    public String convert(LocalDate localDate) {
        if (localDate == null) {
            return null;
        } else {
            return dateFormat.format(localDate);
        }
    }

    @Override
    public LocalDate unconvert(String s) {
        if (s == null) {
            return null;
        } else {
            return LocalDate.parse(s, dateFormat);
        }
    }
}
