package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.HashMap;
import java.util.List;

public class GetUser implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>{

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {

		List<Person> users = List.of(
				new Person(1, "Julian", "julian.ortixs@gmail.com"),
				new Person(2, "Julio", "julian.ortixs@gmail.com"),
				new Person(3, "Juli", "julian.ortixs@gmail.com")
		);

		String pathParameter = event.getPathParameters().get("id");
		if (pathParameter == null) {
			return createErrorResponse(400, "ID is required");
		}

		int userId = Integer.parseInt(pathParameter);

		Person user = findUserById(users, userId);

		if (user == null) {
			return createErrorResponse(404, "Usuario no encontrado");
		}

		return createSuccessResponse(user);
	}

	private Person findUserById(List<Person> users, int userId) {
		return users.stream()
				.filter(u -> u.getId() == userId)
				.findFirst()
				.orElse(null);
	}

	private APIGatewayProxyResponseEvent createErrorResponse(int statusCode, String message) {
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		response.setStatusCode(statusCode);
		response.setBody("{\"message\": \"" + message + "\"}");
		response.setHeaders(new HashMap<>());
		return response;
	}

	private APIGatewayProxyResponseEvent createSuccessResponse(Person user) {
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		response.setStatusCode(200);
		response.setBody("{\"id\": " + user.getId() + ", \"name\": \"" + user.getName() + "\", \"email\": \"" + user.getEmail() + "\"}");
		response.setHeaders(new HashMap<>());
		return response;
	}
}