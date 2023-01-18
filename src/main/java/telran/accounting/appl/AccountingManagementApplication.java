package telran.accounting.appl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import jakarta.annotation.PreDestroy;
import telran.accounting.service.AccountingServiceImpl;

@SpringBootApplication
public class AccountingManagementApplication {

	private static final String SHUTDOWN = "shutdown";

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(AccountingManagementApplication.class, args);
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("To stop server type " + SHUTDOWN);
			String line = scanner.nextLine();
			if (line.equals(SHUTDOWN)) {
				break;
			}
		}
		ctx.close();
	}

	@PreDestroy
	void saveAccountsData() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("accounts.txt"));
			for (String user : AccountingServiceImpl.accounts.keySet()) {
				writer.write(String.format("%s %s %s\n", user, AccountingServiceImpl.accounts.get(user).password, AccountingServiceImpl.accounts.get(user).role));
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
