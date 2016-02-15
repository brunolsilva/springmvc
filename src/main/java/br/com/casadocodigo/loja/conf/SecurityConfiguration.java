package br.com.casadocodigo.loja.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import br.com.casadocodigo.loja.daos.UserDAO;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService users;
	
	@Autowired
	private UserDAO userDAO;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(users).passwordEncoder(new BCryptPasswordEncoder());
		auth.authenticationProvider(new CustomAuthenticationProvider(userDAO));
//		auth.authenticationProvider(authenticationProvider)
//		auth.authenticationProvider(authenticationProvider)
	}
	
	//Note, we don't register this as a bean as we don't want it to be added to the main Filter chain, just the spring security filter chain
    protected AbstractAuthenticationProcessingFilter createCustomFilter() throws Exception {
      CustomFilter filter = new CustomFilter( new RegexRequestMatcher("^/.*", null));
      System.out.println("vkdfngdkfnl");
      filter.setAuthenticationManager(this.authenticationManagerBean());
      return filter;
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.addFilterBefore(createCustomFilter(), AnonymousAuthenticationFilter.class)
			.authorizeRequests()
			.antMatchers("/produtos/form").hasRole("ADMIN")
//			.and().addFilterBefore(new Demo(), BasicAuthenticationFilter.class)
			.antMatchers("/shopping/**").permitAll()
			.antMatchers(HttpMethod.POST, "/produtos").hasRole("ADMIN")
			.antMatchers("/produtos/**").hasRole("ADMIN")
			.anyRequest().authenticated()
//			.and().formLogin().loginPage("/login").permitAll()
			.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.and().exceptionHandling().accessDeniedPage("/WEB-INF/views/errors/403.jsp");
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}
	
	@Bean
	public AuthenticationProvider createCustomAuthenticationProvider()  {
	    return new CustomAuthenticationProvider(userDAO);
	}
}
