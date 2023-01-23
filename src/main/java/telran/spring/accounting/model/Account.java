package telran.spring.accounting.model;


import java.io.Serializable;

import jakarta.validation.constraints.*;

public class Account implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@NotEmpty @Email
	public String username;
	 @Size(min = 6) @NotEmpty
	public String password;
	@NotEmpty 
	public String role;
}
