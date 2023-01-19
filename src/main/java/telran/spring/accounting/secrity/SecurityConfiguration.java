package telran.spring.accounting.secrity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Value("${app.admin.username:admin}")
	private String admin;
	@Value("${app.admin.password:${ADMIN_PASSWORD}}")
	private String adminPassword;

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeHttpRequests(requests -> requests.anyRequest().authenticated()).httpBasic();
		return http.build();

	}

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsManager userDetailsService(PasswordEncoder bCryptPasswordEncoder) {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(
				User.withUsername(admin).password(bCryptPasswordEncoder.encode(adminPassword)).roles("ADMIN").build());

		return manager;
	}

}
