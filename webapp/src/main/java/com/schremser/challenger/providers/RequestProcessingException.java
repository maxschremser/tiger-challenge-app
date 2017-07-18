package com.schremser.challenger.providers;

/*
 * RequestProcessingException - thrown on any failures of request processing performed by the provider
 * 
 */
public class RequestProcessingException extends RuntimeException {
	private String i_resource;
	private String i_request;
	private String i_id;

	public RequestProcessingException( String resource, String request, Throwable cause ) {
		super( makeMessage( resource, request, cause ) );
		i_resource = resource;
		i_request = request;
	}
	
	public RequestProcessingException( String resource, String request, String id, Throwable cause ) {
		super( makeMessage( resource, request, id, cause ) );
		i_resource = resource;
		i_request = request;
		i_id = id;
	}
	
	public String getResource( ) {
		return i_resource;
	}
	
	public String getRequest( ) {
		return i_request;
	}
	
	public String getId( ) {
		return i_id;
	}

	private static String makeMessage( String resource, String request, Throwable cause ) {
		return "Request [" + request + "] processing failed for resource [" + resource + "]: " + cause.getMessage();
	}
	
	private static String makeMessage( String resource, String request, String id, Throwable cause ) {
		return "Request [" + request + "] processing failed for resource [" + resource + "] instance [" + id + "]: " + cause.getMessage();
	}
	
}
