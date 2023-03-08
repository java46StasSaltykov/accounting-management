package telran.spring.accounting.service;

import java.util.*;
import telran.spring.accounting.model.Account;

public interface AccountingService {
	
	boolean addAccount(Account account);

	boolean deleteAccount(String username);

	boolean updateAccount(Account account);

	boolean isExists(String username);
	
	List<String> getAccountsRole(String role);
	
	List<String> getActiveAccounts();
	
	long getMaxRoles();
	
	List<String> getAllAccountsWithMaxRoles();
	
	int getMaxRolesOccurrenceCount();
	
	List<String> getAllRolesWithMaxOccurrrence();
	
	int getActiveMinRolesOccurrenceCount();
	
}
