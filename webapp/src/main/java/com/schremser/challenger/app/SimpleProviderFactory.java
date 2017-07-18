package com.schremser.challenger.app;

import com.schremser.challenger.providers.IExpenseProvider;
import com.schremser.challenger.providers.MockExpenseProvider;

import java.text.ParseException;

public class SimpleProviderFactory implements IProviderFactory {

	public SimpleProviderFactory() {
	}

	@Override
	public IExpenseProvider createExpenseProvider() {
		IExpenseProvider provider = null;
		try {
			provider = MockExpenseProvider.instance();
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return provider;
	}
}
