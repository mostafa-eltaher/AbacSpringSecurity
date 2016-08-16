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
	@Autowired
	InMemoryUserDetailsService usersSvc;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.anyRequest()
				.authenticated()
					.and()
				.httpBasic()
			.authenticationEntryPoint(entryPoint);
		
		http.csrf().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usersSvc);
	}	
}
