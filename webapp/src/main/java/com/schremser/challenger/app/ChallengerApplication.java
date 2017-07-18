package com.schremser.challenger.app;

import com.schremser.challenger.providers.CORSFilter;
import com.schremser.challenger.providers.GsonJerseyProvider;
import com.schremser.challenger.services.ExpenseResource;
import com.schremser.challenger.services.LoginResource;
import com.schremser.challenger.services.SignupResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath( "/api" )
public class ChallengerApplication extends Application {
	private final static Logger log = LoggerFactory.getLogger(ChallengerApplication.class);
	public ChallengerApplication( ) {
		log.info("ChallengerApplication created");
	}

	@Override
	public Set<Class<?>> getClasses( ) {
		Set<Class<?>> classes = new HashSet<Class<?>>( );
		return classes;
	}
	
	@Override
	public Set<Object> getSingletons( ) {
		log.debug( "ChallengerApplication: getSingletons" );

		// Initialize ProviderRegistry
		ProviderRegistry.instance( new CloudantProviderFactory() );

		Set<Object> singletons = new HashSet<>( );

		singletons.add(new ExpenseResource());
		singletons.add(new SignupResource());
		singletons.add(new LoginResource());
		singletons.add(new GsonJerseyProvider());
		singletons.add(new CORSFilter());

		log.info( "Number of singletons: " + singletons.size( ) );
		
		return singletons;
	}
	
}

