package telran.accounting.service;

import telran.accounting.model.Account;

public interface AccountingService {
	
	boolean addAccount(Account account);

	boolean deleteAccount(String username);

	boolean updateAccount(Account account);

	boolean isExists(String username);
}
