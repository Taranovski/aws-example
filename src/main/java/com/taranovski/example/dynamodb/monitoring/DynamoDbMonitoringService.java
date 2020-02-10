package com.taranovski.example.dynamodb.monitoring;

import com.amazonaws.services.dynamodbv2.model.ConsumedCapacity;

/**
 * Created by Alyx on 10.02.2020.
 */
public interface DynamoDbMonitoringService {
    void record(ConsumedCapacity consumedCapacity, String methodName, Object... objects);
}
