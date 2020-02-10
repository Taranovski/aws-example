package com.taranovski.example.dynamodb.monitoring.impl;

import com.amazonaws.services.dynamodbv2.model.ConsumedCapacity;
import com.taranovski.example.dynamodb.monitoring.DynamoDbMonitoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Alyx on 10.02.2020.
 */
@Component
public class DynamoDbMonitoringServiceImpl implements DynamoDbMonitoringService {

    private static final Logger LOGGER  = LoggerFactory.getLogger(DynamoDbMonitoringServiceImpl.class);

    @Override
    public void record(ConsumedCapacity consumedCapacity, String methodName, Object[] objects) {
        if (consumedCapacity != null) {
            LOGGER.info("method: {} params {} capacity: {}", methodName, objects, consumedCapacity);
        }
    }
}
