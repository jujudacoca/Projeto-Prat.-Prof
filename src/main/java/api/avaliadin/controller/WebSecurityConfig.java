package api.avaliadin.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import api.avaliadin.model.User;
import api.avaliadin.repository.UserRepository;
import details.UserDetailsServiceImpl;
 
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 
	@Autowired
	private UserRepository userRepository;
	
	@Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
        
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	 String staticResources  = "/image/**";
        http.authorizeRequests()
        	.antMatchers("/").permitAll()
        	.antMatchers("/login").permitAll()
        	.antMatchers("/cadastro").permitAll()
        	.antMatchers(staticResources).permitAll()
        	.anyRequest().authenticated()
        	.and()
        	.formLogin().loginPage("/login")
        		.usernameParameter("username")
        		.passwordParameter("senha")
        		.loginProcessingUrl("/log")
        		.successHandler(new AuthenticationSuccessHandler() {
        		    @Override
        		    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        		            Authentication authentication) throws IOException, ServletException {
        		         
        		        String username = authentication.getName();
        		        User t = userRepository.findByUsername(username);
        		        if(t.getRole().equals("ROLE USER")) {
        		        	response.sendRedirect("/indexmembro");
        		        }else if(t.getRole().equals("ROLE ADMIN")) {
        		        	response.sendRedirect("/indexadmin");
        		        }else if(t.getRole().equals("ROLE GM")) {
        		        	response.sendRedirect("/indexgerente");
        		        }
        		    }
        		})
        		.failureHandler(new AuthenticationFailureHandler() {
        		     
        		    @Override
        		    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        		            AuthenticationException exception) throws IOException, ServletException {
        		        System.out.println("Login failed");
        		        System.out.println(exception);
        		         
        		        response.sendRedirect("/login?error");
        		    }
        		})
        	.and()
        	.logout().permitAll()
        	.and()
        	.exceptionHandling().accessDeniedPage("/403");
    }  

}