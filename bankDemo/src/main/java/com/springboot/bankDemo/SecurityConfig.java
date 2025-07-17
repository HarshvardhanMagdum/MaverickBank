package com.springboot.bankDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	@Autowired
	private JwtFilter jwtFilter;
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf((csrf) -> csrf.disable())
			.authorizeHttpRequests((authorize) -> authorize
					.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
					// Account
					.requestMatchers("/api/account/post/").permitAll()
					.requestMatchers("/api/account/post/{customerId}/{branchId}").hasAnyAuthority("EXECUTIVE","MANAGER")
					.requestMatchers("/api/account/get-all").hasAuthority("MANAGER")
					.requestMatchers("/api/account/put/status/{accountId}/").hasAnyAuthority("CUSTOMER","EXECUTIVE")
					.requestMatchers("/api/account/put/balance/{accountId}").permitAll()
					.requestMatchers("/api/account/get-one").hasAuthority("CUSTOMER")
					.requestMatchers("/api/account/get/id/{accountId}").hasAnyAuthority("EXECUTIVE","MANAGER")
					.requestMatchers("/api/account/get/customerId/{customerId}").hasAnyAuthority("EXECUTIVE","MANAGER")
					.requestMatchers("/api/account/get/{branchId}").hasAnyAuthority("EXECUTIVE","MANAGER")
					.requestMatchers("/api/account/get/status").hasAnyAuthority("EXECUTIVE","MANAGER")
					// AccountType
					.requestMatchers("/api/accountType/post").hasAuthority("EXECUTIVE")
					.requestMatchers("/api/accountType/put/{id}").hasAuthority("EXECUTIVE")
					.requestMatchers("/api/accountType/get-all").permitAll()
					.requestMatchers("/api/accountType/get/type").permitAll()
					.requestMatchers("/api/accountType/get-one/{id}").permitAll()
					//Beneficiary
					.requestMatchers("/api/beneficiary/post").hasAuthority("CUSTOMER")
					.requestMatchers("/api/beneficiary/put/{id}/{customerId}").hasAuthority("CUSTOMER")
					.requestMatchers("/api/beneficiary/delete/{id}").hasAuthority("CUSTOMER")
					.requestMatchers("/api/beneficiary/get").hasAuthority("CUSTOMER")
					.requestMatchers("/api/beneficiary/get-one/{id}").hasAuthority("CUSTOMER")
					// Branch
					.requestMatchers("/api/branch/post").hasAuthority("MANAGER")
					.requestMatchers("/api/branch/put/{id}").hasAuthority("MANAGER")
					.requestMatchers("/api/branch/get-all").permitAll()
					.requestMatchers("/api/branch/get/ifscCode").permitAll()
					.requestMatchers("/api/branch/get-one/{id}").permitAll()
					// Customer
					.requestMatchers("/api/customer/post").permitAll()
					.requestMatchers("/api/customer/get").hasAuthority("CUSTOMER")
					.requestMatchers("/api/customer/get-one/{customerId}").hasAuthority("EXECUTIVE")
					.requestMatchers("/api/customer/get-all").hasAuthority("MANAGER")
					.requestMatchers("/api/customer/put").permitAll()
					// Enums
					.requestMatchers("/api/enum/account/status/get").permitAll()
					.requestMatchers("/api/enum/loanApply/status/get").permitAll()
					.requestMatchers("/api/enum/loan/status/get").permitAll()
					.requestMatchers("/api/enum/loan/type/get").permitAll()
					.requestMatchers("/api/enum/transfer/type/get").permitAll()
					// Executive
					.requestMatchers("/api/executive/post/{branchId}").hasAnyAuthority("MANAGER","ADMIN")
					.requestMatchers("/api/executive/put").hasAuthority("EXECUTIVE")
					.requestMatchers("/api/executive/get-one").hasAuthority("EXECUTIVE")
					.requestMatchers("/api/executive/get/{branchId}").hasAuthority("MANAGER")
					.requestMatchers("/api/executive/get-all").hasAuthority("MANAGER")
					// Loan
					.requestMatchers("/api/loan/post").hasAnyAuthority("EXECUTIVE","MANAGER")
					.requestMatchers("/api/loan/put/status/{id}").hasAnyAuthority("EXECUTIVE","MANAGER")
					.requestMatchers("/api/loan/get-by/branchId/{branchId}").hasAnyAuthority("EXECUTIVE","MANAGER")
					.requestMatchers("/api/loan/get-by/id/{id}").hasAnyAuthority("EXECUTIVE","MANAGER")
					.requestMatchers("/api/loan/get-one").hasAuthority("CUSTOMER")
					.requestMatchers("/api/loan/get-all").hasAuthority("MANAGER")
					// Loan Application
					.requestMatchers("/api/loanApply/post/{loanDetailsId}/{accountId}").hasAuthority("CUSTOMER")
					.requestMatchers("/api/loanApply/put/status/cancelled/{id}").hasAuthority("CUSTOMER")
					.requestMatchers("/api/loanApply/put/status/{id}").hasAuthority("EXECUTIVE")
					.requestMatchers("/api/loanApply/get-one").hasAuthority("CUSTOMER")
					.requestMatchers("/api/loanApply/get-by/status").hasAnyAuthority("EXECUTIVE","MANAGER")
					.requestMatchers("/api/loanApply/get-by/branchId/{branchId}").hasAnyAuthority("EXECUTIVE","MANAGER")
					.requestMatchers("/api/loanApply/get-all").hasAuthority("MANAGER")
					// Loan Details
					.requestMatchers("/api/loanDetails/post").permitAll()
					.requestMatchers("/api/loanDetails/put/{id}").permitAll()
					.requestMatchers("/api/loanDetails/get/all").permitAll()
					// Loan Repayment
					.requestMatchers("/api/loanRepay/poat/{laonId}").hasAuthority("CUSTOMER")
					.requestMatchers("/api/loanRepay/get-by/loanId/{loanId}").authenticated()
					// Manager 
					.requestMatchers("/api/manager/post").hasAuthority("ADMIN")
					.requestMatchers("/api/manager/put").hasAuthority("MANAGER")
					.requestMatchers("/api/manager/get-one").hasAuthority("MANAGER")
					.requestMatchers("/api/manager/get-all").hasAuthority("MANAGER")
					// Transaction
					.requestMatchers("/api/transaction/post/deposit/{accountId}").hasAuthority("CUSTOMER")
					.requestMatchers("/api/transaction/post/withdraw/{accountId}").hasAuthority("CUSTOMER")
					.requestMatchers("/api/transaction/post/transfer/{accountId}/{beneficiaryId}").hasAuthority("CUSTOMER")
					.requestMatchers("/api/transaction/get-btw/{accountId}").hasAnyAuthority("CUSTOMER","EXECUTIVE","MANAGER")
					.requestMatchers("/api/transaction/get-from/{accountId}").hasAnyAuthority("CUSTOMER","EXECUTIVE","MANAGER")
					.requestMatchers("/api/transaction/get-10/{accountId}").hasAnyAuthority("CUSTOMER","EXECUTIVE","MANAGER")
					.requestMatchers("/api/transaction/get/statement/{accountId}").hasAnyAuthority("CUSTOMER","EXECUTIVE","MANAGER")
					.requestMatchers("/api/transaction/get-btw/branchId/{branchId}").hasAnyAuthority("EXECUTIVE","MANAGER")
					.requestMatchers("/api/transaction/get-from/branchId/{branchId}").hasAnyAuthority("EXECUTIVE","MANAGER")
					.requestMatchers("/api/transaction/get-btw").hasAuthority("MANAGER")
					.requestMatchers("/api/transaction/get-from").hasAuthority("MANAGER")
					.requestMatchers("/api/transaction/get-all").hasAuthority("MANAGER")
					// User
					.requestMatchers("/api/user/signup").permitAll()
					.requestMatchers("/api/user/put/status/{id}").permitAll()
					.requestMatchers("/api/user/get/all").permitAll()
					.requestMatchers("/api/user/token").permitAll()
					.requestMatchers("/api/user/details").permitAll()
					.anyRequest().authenticated()
			 )
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.httpBasic(Customizer.withDefaults());
		return http.build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager getAuthManager(AuthenticationConfiguration auth) throws Exception {
		return auth.getAuthenticationManager();
	}
}
