package org.bitbucket.shevchenkod.restaurant.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletResponse;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.userDetailsService(userDetailsService)
				//.passwordEncoder(new Md5PasswordEncoder())
		;
	}


	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
 				.and().authorizeRequests().antMatchers("/api/votes/**").hasAnyAuthority(new String[]{"USER", "ADMIN"})
				.and().authorizeRequests().antMatchers(HttpMethod.GET, "/api/admin/**").hasAnyAuthority(new String[]{"ADMIN"})
				.and().authorizeRequests().antMatchers(HttpMethod.GET, "/api/**").hasAnyAuthority(new String[]{"USER", "ADMIN"})
				.and().authorizeRequests().antMatchers(HttpMethod.POST, "/api/**").hasAnyAuthority(new String[]{"ADMIN"})
				.and().authorizeRequests().antMatchers(HttpMethod.PUT, "/api/**").hasAnyAuthority(new String[]{"ADMIN"})
				.and().authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/**").hasAnyAuthority(new String[]{"ADMIN"})
				.and().authorizeRequests().antMatchers(HttpMethod.DELETE, "/doc/user/**").hasAnyAuthority(new String[]{"USER"})
				.and().authorizeRequests().antMatchers(HttpMethod.DELETE, "/doc/admin/**").hasAnyAuthority(new String[]{"ADMIN"})
				.and().csrf().disable()
				.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint())
				.and().httpBasic()
		;

	}


	@Bean
	public AuthenticationEntryPoint unauthorizedEntryPoint() {
		return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

}
