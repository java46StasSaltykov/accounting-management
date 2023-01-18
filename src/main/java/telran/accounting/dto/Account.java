package telran.accounting.dto;

import jakarta.validation.constraints.*;

public class Account {
	
	@Email @NotEmpty
	public String username;
	@Min(6) @NotEmpty
	public String password;
	@NotNull @NotEmpty 	@Pattern(regexp = "USER|ADMIN")
	public String role;

}
