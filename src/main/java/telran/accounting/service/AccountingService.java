package telran.accounting.service;

import telran.accounting.dto.Account;

public interface AccountingService {
	
	String addAccount(Account account);
	
	String deleteAccount(String username);
	
	String updateAccount(Account account);
	
	boolean isExist(String username);

}
