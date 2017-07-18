package com.schremser.challenger.app;

import com.schremser.challenger.providers.IExpenseProvider;

public class ProviderRegistry {
	private static ProviderRegistry s_instance;
	private IProviderFactory i_factory;
	
	private IExpenseProvider i_expenseProvider;

	public synchronized static ProviderRegistry instance( ) {
		if( s_instance == null )
			s_instance = new ProviderRegistry( );
		return s_instance;
	}
	
	public synchronized static ProviderRegistry instance(IProviderFactory factory ) {
		if( s_instance == null )
			s_instance = new ProviderRegistry( factory );
		return s_instance;
	}
	
	public synchronized void setProviderFactory( IProviderFactory factory ) {
		if( factory != null )
			i_factory = factory;
	}
	
	public IProviderFactory getProviderFactory( ) {
		return i_factory;
	}
	
	private ProviderRegistry( ) {
		// set default factory: SimpleProviderFactory in mock mode
		//
		this( new SimpleProviderFactory() );
	}
	
	private ProviderRegistry(IProviderFactory factory ) {
		i_factory = factory;
	}
	
	public synchronized IExpenseProvider getExpenseProvider( ) {
		if( i_expenseProvider == null )
			i_expenseProvider = i_factory.createExpenseProvider();
		return i_expenseProvider;
	}
}
