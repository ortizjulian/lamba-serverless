package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.HashMap;
import java.util.List;

public class CreateUser implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>{

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
		List<Person> users = new ArrayList<>(
				List.of(
					new Person(1, "Julian", "julian.ortixs@gmail.com"),
					new Person(2, "Julio", "julian.ortixs@gmail.com"),
					new Person(3, "Juli", "julian.ortixs@gmail.com")
				)
		);

        String body = event.getBody();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        if (body == null || body.isEmpty()) {
            response.setStatusCode(400);
            response.setBody("{\"message\": \"No user data provided\"}");
            return response;
        }

        try {
            Person newUser = new ObjectMapper().readValue(body, Person.class);

            users.add(newUser);

            response.setStatusCode(201); 
            response.setBody("{\"message\": \"User created successfully\"}");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setBody("{\"message\": \"Failed to create user\", \"error\": \"" + e.getMessage() + "\"}");
        }

        return response;
    }

}