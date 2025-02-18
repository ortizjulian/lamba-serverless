package com.serverless.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.serverless.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserService {

    private AmazonDynamoDB amazonDynamoDB;
    private String DYNAMODB_TABLE_NAME = "user";
    private Regions REGION = Regions.US_EAST_1;

    public void initDynamoDbClient() {
        this.amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withRegion(REGION)
                .build();
    }

    public String persistData(User personRequest) throws ConditionalCheckFailedException {

        String userId = UUID.randomUUID().toString();
        Map<String, AttributeValue> attributesMap = new HashMap<>();

        attributesMap.put("id", new AttributeValue(userId));
        attributesMap.put("name", new AttributeValue(personRequest.getName()));
        attributesMap.put("email", new AttributeValue(personRequest.getEmail()));

        amazonDynamoDB.putItem(DYNAMODB_TABLE_NAME, attributesMap);

        return userId;
    }

    public User getUserById(String userId) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue(userId));

        GetItemRequest request = new GetItemRequest()
                .withTableName(DYNAMODB_TABLE_NAME)
                .withKey(key);

        try {
            Map<String, AttributeValue> result = amazonDynamoDB.getItem(request).getItem();
            if (result == null || result.isEmpty()) {
                return null;
            }

            String id = result.get("id").getS();
            String name = result.get("name").getS();
            String email = result.get("email").getS();
            return new User(id, name, email);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get user from DynamoDB", e);
        }
    }
}
