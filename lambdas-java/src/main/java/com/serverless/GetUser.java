package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.serverless.model.Response;
import com.serverless.model.User;
import com.serverless.service.UserService;
import com.serverless.utils.JsonUtil;

import java.util.HashMap;
import java.util.List;

public class GetUser implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>{

	private UserService userService = new UserService();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
		String pathParameter = event.getPathParameters().get("id");

		if (pathParameter == null || pathParameter.isEmpty()) {
			return createErrorResponse(400, "ID is required");
		}

		User user = userService.getUserById(pathParameter);

		if (user == null) {
			return createErrorResponse(404, "User not found");
		}

		return createSuccessResponse(user);
	}

	private APIGatewayProxyResponseEvent createErrorResponse(int statusCode, String message) {
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		response.setStatusCode(statusCode);
		Response errorResponse = new Response(message);
		response.setBody(JsonUtil.toJson(errorResponse));
		return response;
	}

	private APIGatewayProxyResponseEvent createSuccessResponse(User user) {
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		response.setStatusCode(200);
		response.setBody(JsonUtil.toJson(user));
		return response;
	}
}