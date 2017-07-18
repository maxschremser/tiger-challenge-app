package com.schremser.challenger.providers;

/*
 * 
 */
public class ResourceNotFoundException extends IllegalArgumentException {
	private String i_resourceType;
	private String i_id;
	
	public ResourceNotFoundException( String resourceType, String id ) {
		super( makeMessage( resourceType, id ) );
		i_resourceType = resourceType;
		i_id = id;
	}
	
	public String getResourceType( ) {
		return i_resourceType;
	}
	
	public String getId( ) {
		return i_id;
	}

	private static String makeMessage( String resourceType, String id ) {
		return resourceType + " with id [" + id + "] not found";
	}
}
