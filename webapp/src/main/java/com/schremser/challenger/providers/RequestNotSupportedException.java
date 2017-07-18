package com.schremser.challenger.providers;

public class RequestNotSupportedException extends RuntimeException {
	private String i_resource;
	private String i_request;

	public RequestNotSupportedException( String resource, String request) {
		super(makeMessage( resource, request ) );
		i_resource = resource;
		i_request = request;
	}
	
	public String getResource( ) {
		return i_resource;
	}
	
	public String getRequest( ) {
		return i_request;
	}

	private static String makeMessage( String resource, String request ) {
		return "Request [" + request + "] not support for resource [" + resource + "]";
	}
}
