package com.schremser.challenger.app;

import com.schremser.challenger.providers.CloudantExpenseProvider;
import com.schremser.challenger.providers.IExpenseProvider;

import java.text.ParseException;

public class CloudantProviderFactory implements IProviderFactory {

	public CloudantProviderFactory() {
	}

	@Override
	public IExpenseProvider createExpenseProvider() {
		IExpenseProvider provider = null;
		try {
			provider = CloudantExpenseProvider.instance();
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return provider;
	}
}
