package api.avaliadin.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import details.UserDetailsServiceImpl;
 
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 
	
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
    
	/*@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    auth.inMemoryAuthentication()
	        .passwordEncoder(new BCryptPasswordEncoder())
	            .withUser("namhm")
	            .password("$2a$10$fCY89aAmJCp9kQ5Ejz0HveVzKSBCyGVk6YmgqSp2uzL2kqwJD/zCm")
	            .roles("USER")
	        .and()
	            .withUser("admin")
	            .password("$2a$10$K65x7/TZEpXlnDGBZSC.5u0R.iO7U1CbkZ.VIIKjSkY8uOBNmeqzK")
	            .roles("ADMIN")
	        ;
	}
 */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        	.antMatchers("/").permitAll()
        	.antMatchers("/login").permitAll()
        	.antMatchers("/cadastro").permitAll()
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
        		         
        		        System.out.println("Logged user: " + authentication.getName());
        		         
        		        response.sendRedirect("/listaUser");
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
    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        	.antMatchers("/").permitAll()
        	.antMatchers("/login").permitAll()
        	.antMatchers("/cadastro").permitAll()
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
        		         
        		        System.out.println("Logged user: " + authentication.getName());
        		         
        		        response.sendRedirect("/listaUser");
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
    }  */
}