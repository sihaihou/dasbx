package com.reyco.dasbx.open.core.service;

import com.reyco.dasbx.open.core.model.domain.Accounts;

public interface AccountsService {

	Accounts getAccounts(String username) ;
	
	Boolean login(String username,String password);
	
}
