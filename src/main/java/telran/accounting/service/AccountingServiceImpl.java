package telran.accounting.service;

import java.util.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import telran.accounting.dto.Account;

@Service
public class AccountingServiceImpl implements AccountingService {
	
	public static HashMap<String, Account> accounts;
	PasswordEncoder encoder;
	public UserDetailsManager manager;

	public AccountingServiceImpl(PasswordEncoder encoder, UserDetailsManager manager) {
		this.encoder = encoder;
		this.manager = manager;
	}
	
	static {
		accounts = new HashMap<String, Account>();
	}

	@Override
	public String addAccount(Account account) {
		if (account.username.equals("admin")) {
			return "cannot add this user";
		}
		String res = String.format("User with name %s already exists", account.username);
		if (!manager.userExists(account.username)) {
			manager.createUser(User.withUsername(account.username)
					.password(encoder.encode(account.password))
					.roles(account.role).build());
			res = String.format("User %s has been added", account.username);
			accounts.putIfAbsent(account.username, account);
		}
		return res;
	}

	@Override
	public String deleteAccount(String username) {
		if (username.equals("admin")) {
			return "cannot delete this user";
		}
		String res = String.format("User with name %s doesn't exist", username);
		if (manager.userExists(username)) {
			manager.deleteUser(username);
			res = String.format("User %s has been deleted", username);
			accounts.remove(username);
		}
		return res;
	}

	@Override
	public String updateAccount(Account account) {
		if (account.username.equals("admin")) {
			return "cannot update this user";
		}
		String res = String.format("User with name %s doesn't exist", account.username);
		if (manager.userExists(account.username)) {
			manager.updateUser(User.withUsername(account.username)
					.password(encoder.encode(account.password))
					.roles(account.role).build());
			res = String.format("User %s has been updated", account.username);
			accounts.put(account.username, account);
		}
		return res;
	}

	@Override
	public boolean isExist(String username) {
		return manager.userExists(username);
	}

}
