package com.schremser.challenger.app;

import com.schremser.challenger.providers.IExpenseProvider;

public interface IProviderFactory {
	IExpenseProvider createExpenseProvider();
}
