package edu.mostafa.abac.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan("edu.mostafa.abac")
@PropertySource(value="classpath:application.properties")
@EnableWebMvc
@EnableWebSecurity
public class RootConfiguration  extends WebSecurityConfigurerAdapter {
	@Autowired
	HttpAuthenticationEntryPoint entryPoint;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.anyRequest()
				.authenticated()
					.and()
				.httpBasic()
			.authenticationEntryPoint(entryPoint);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("user").password("password").roles("USER").and()
		.withUser("admin").password("password").roles("USER", "ADMIN").and()
		.withUser("mostafa").password("password").roles("USER", "ADMIN");

	}	
}
