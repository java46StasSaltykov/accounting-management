package telran.accounting.controller;

import java.io.*;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import telran.accounting.dto.Account;
import telran.accounting.service.AccountingServiceImpl;

@RestController
@RequestMapping("accounts")
public class AccountingController {
	
	AccountingServiceImpl accountingService;
	
	public AccountingController(AccountingServiceImpl accountingService) {
		this.accountingService = accountingService;
	}
	
	@PostConstruct
	void restoreAccountsData() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("accounts.txt"));
			Account account = new Account();
			String accountData = "";
			while ((accountData = reader.readLine()) != null) {
				String[] data = accountData.split(" ");
				account.username = data[0];
				account.password = data[1];
				account.role = data[2];
				accountingService.accounts.put(data[0], account);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@PostMapping
	boolean addAccount(@RequestBody @Valid Account account) {
		String addRes = accountingService.addAccount(account);
		if (!(addRes.contains("exists") || addRes.contains("cannot"))) {
			return true;
		} else {
			return false;
		}
	}
	
	@PutMapping
	boolean updateAccount(@RequestBody @Valid Account account) {
		String updateRes = accountingService.updateAccount(account);
		if (!(updateRes.contains("exist") || updateRes.contains("cannot"))) {
			return true;
		} else {
			return false;
		}
	}
	
	@DeleteMapping("/{username}")
	boolean deleteUser(@PathVariable("username") String username) {
		String deleteRes = accountingService.deleteAccount(username);
		if (!(deleteRes.contains("exist") || deleteRes.contains("cannot"))) {
			return true;
		} else {
			return false;
		}
	}
	
	@GetMapping("/{username}")
	boolean userExists(@PathVariable("username") String username) {
		return accountingService.isExist(username);
	}
	
}
