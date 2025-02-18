package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.model.Response;
import com.serverless.model.User;
import com.serverless.service.UserService;
import com.serverless.utils.JsonUtil;


public class CreateUser implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>{

    private UserService userService = new UserService();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String body = event.getBody();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        Response message = new Response("No user data provided");

        if (body == null || body.isEmpty()) {
            response.setStatusCode(400);
            response.setBody(JsonUtil.toJson(message));
            return response;
        }

        userService.initDynamoDbClient();

        try {
            User newUser = new ObjectMapper().readValue(body, User.class);
            String userId = userService.persistData(newUser);

            response.setStatusCode(201);
            message.setMessage("User created successfully, id: " + userId);
            response.setBody(JsonUtil.toJson(message));
        } catch (Exception e) {
            response.setStatusCode(500);
            message.setMessage("Error" + e.getMessage());
            response.setBody(JsonUtil.toJson(message));
        }
        return response;
    }

}