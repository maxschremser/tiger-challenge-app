package com.schremser.challenger.app;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppContext implements ServletContextListener {
	private static final String ENV_LOGIN_SESSION_TIMEOUT = "LOGIN_SESSION_TIMEOUT";
	private static final String PARAM_LOGIN_SESSION_TIMEOUT = "login-session-timeout";

	private static String s_contextRoot = null;
	private static int s_loginSessionTimeout = 10*60; // default 10 minutes
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		s_contextRoot = sc.getContextPath();
		
		// read timeout from the environment (server.env) if provided use it, otherwise read it from the context
		//
		String[] labels = new String[] { ENV_LOGIN_SESSION_TIMEOUT, PARAM_LOGIN_SESSION_TIMEOUT };
		String[] strValues = new String[] { System.getenv( ENV_LOGIN_SESSION_TIMEOUT ), sc.getInitParameter( PARAM_LOGIN_SESSION_TIMEOUT ) }; 
		for( int i=0; i<strValues.length; i++ ) {
			String str = strValues[i];
			String label = labels[i];
			if( str != null ) {
				try {
					s_loginSessionTimeout = Integer.parseInt( str );
					break;
				} catch( Exception e ) {
					logMinor( "Expected " + label + " value [" + str + "] to be numeric" );
				}
			}
		}

		logInfo( "ServletContext initialized: contextRoot=" + s_contextRoot + " login-session-timeout=" + s_loginSessionTimeout	);
	
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println( "ServletContext: destroyed" );
	}

	public static String getContextRoot( ) {
		return s_contextRoot;
	}
	
	public static int getLoginSessionTimeout( ) {
		return s_loginSessionTimeout;
	}

	private void logMinor( String msg ) {
		System.out.println( "[MINOR] " + msg );
	}

	private void logInfo( String msg ) {
		System.out.println( "[MINOR] " + msg );
	}

}
