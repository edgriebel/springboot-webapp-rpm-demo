package com.edgriebel.rpmdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/index.html").permitAll()       
                .antMatchers("/rest/test").hasRole("USER")
                .and()
            .httpBasic()
//            .formLogin().loginPage("/login").failureUrl("/login-error")
                .and()
            .logout()
                // From logoutUrl(): "It is considered best practice to use an HTTP POST on any action that changes state (i.e. log out) to protect against CSRF attacks."
                // We're not going to be so pedantic as a DoS from a forced logout from CSRF fail is not problematic 
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // default is "/logout"
                .logoutSuccessUrl("/")
            ;    
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
                .antMatchers("/*.css", "/*.js", "/manage/*");
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER")
        ;
    }
}
