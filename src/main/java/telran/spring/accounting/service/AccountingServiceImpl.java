package telran.spring.accounting.service;

import java.util.HashMap;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.io.*;

import telran.spring.accounting.model.Account;
@Service
public class AccountingServiceImpl implements AccountingService {
	private static Logger LOG = LoggerFactory.getLogger(AccountingService.class);
	@Value("${app.admin.username:admin}")
	private String admin;
private PasswordEncoder passwordEncoder;
private UserDetailsManager userDetailsManager;
private HashMap<String, Account> accounts;
@Value("${app.file.name:accounts.data}")
private String fileName;

	@Override
	public boolean addAccount(Account account) {
		boolean res = false;
		if(!account.username.equals(admin) && !accounts.containsKey(account.username)) {
			res = true;
			account.password = passwordEncoder.encode(account.password);
			accounts.put(account.username, account);
			userDetailsManager.createUser(User.withUsername(account.username)
					.password(account.password).roles(account.role).build());
		}
		return res;
	}

	@Override
	public boolean deleteAccount(String username) {
		boolean res = false;
		if(accounts.containsKey(username)) {
			res = true;
			accounts.remove(username);
			userDetailsManager.deleteUser(username);
		}
		return res;
	}

	@Override
	public boolean updateAccount(Account account) {
		boolean res = false;
		if(accounts.containsKey(account.username)) {
			res = true;
			account.password = passwordEncoder.encode(account.password);
			accounts.put(account.username, account);
			userDetailsManager.updateUser(User.withUsername(account.username)
					.password(account.password).roles(account.role).build());
		}
		return res;
	}

	@Override
	public boolean isExists(String username) {
		return accounts.containsKey(username);
	}

	public AccountingServiceImpl(PasswordEncoder passwordEncoder, UserDetailsManager userDetailsManager) {
		this.passwordEncoder = passwordEncoder;
		this.userDetailsManager = userDetailsManager;
	}
	@PreDestroy
	void saveAccounts() {
		try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName))){
			output.writeObject(accounts);
			LOG.debug("accounts saved to file {}", fileName);
		} catch(Exception e) {
			LOG.error("saving to file caused exception {}", e.getMessage());
		}
	}
	@PostConstruct
	void restoreAccounts() {
		try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName))){
			accounts = (HashMap<String, Account>) input.readObject();
			for(Account acc: accounts.values()) {
				userDetailsManager.createUser(User.withUsername(acc.username)
						.password(acc.password).roles(acc.role).build());
			}
			LOG.debug("accounts {} has been restored", accounts.keySet());
		}catch(FileNotFoundException e) {
			LOG.warn("file {} doesn't exists", fileName);
			accounts = new HashMap<>();
		}catch (Exception e) {
			LOG.error("error at restoring accounts {}", e.getMessage());
		}
	}
}
