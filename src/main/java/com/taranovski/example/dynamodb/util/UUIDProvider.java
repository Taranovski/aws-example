package com.taranovski.example.dynamodb.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by Alyx on 10.02.2020.
 */
@Component
public class UUIDProvider {

    public String getUUID(){
        return UUID.randomUUID().toString();
    }
}
