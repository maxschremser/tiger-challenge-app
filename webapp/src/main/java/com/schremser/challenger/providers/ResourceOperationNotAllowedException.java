package com.schremser.challenger.providers;

public class ResourceOperationNotAllowedException extends IllegalArgumentException {
	private String i_resourceType;
	private String i_operation;
	private String i_id;
	
	public ResourceOperationNotAllowedException( String resourceType, String operation, String id, String reason ) {
		super( makeMessage( resourceType, operation, id, reason ) );
		i_resourceType = resourceType;
		i_operation = operation;
		i_id = id;
	}

	public static String makeMessage( String resourceType, String operation, String id, String reason ) {
		return "Operation '" + operation + "' not allowed for resource '" + resourceType + "' with id '" + id + "': " + reason;
	}
}
