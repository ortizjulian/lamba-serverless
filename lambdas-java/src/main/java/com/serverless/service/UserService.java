package com.serverless.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.serverless.model.User;
import com.amazonaws.services.sqs.AmazonSQS;
import com.serverless.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserService {

    private AmazonDynamoDB amazonDynamoDB;

    private String DYNAMODB_TABLE_NAME;
    private Regions REGION;
    private AmazonSQS amazonSQS;
    private String SQS_QUEUE_URL;

    public UserService() {

        REGION = Regions.US_EAST_1;
        amazonSQS = AmazonSQSClient.builder().withRegion(REGION).build();
        Map<String, String> environment = System.getenv();
        DYNAMODB_TABLE_NAME = environment.get("DYNAMODB_TABLE_NAME");
        SQS_QUEUE_URL = environment.get("SQS_QUEUE_URL");

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
        personRequest.setId(userId);
        sendSqs(personRequest);
        return userId;
    }

    public void sendSqs(User person) {

        String userJson = JsonUtil.toJson(person);

        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(SQS_QUEUE_URL)
                .withMessageBody(userJson);

        amazonSQS.sendMessage(sendMessageRequest);
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
