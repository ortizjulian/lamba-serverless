package com.serverless.model;

public class Response {

	private String message;

	public Response() {}

	public Response(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message){
		this.message = message;
	}
}
