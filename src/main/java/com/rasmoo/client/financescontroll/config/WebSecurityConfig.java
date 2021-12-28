package com.rasmoo.client.financescontroll.config;

import com.rasmoo.client.financescontroll.v1.services.UserInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    public void configuracaoGlobal(final AuthenticationManagerBuilder auth, final UserInfoService userInfoService) throws Exception {
       auth.userDetailsService(userInfoService).passwordEncoder(this.encoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        final String[] allowed = new String[] {"/webjars", "/api/v1/usuario", "/static/**"};
        
        http
            .csrf().disable().authorizeRequests()
            .antMatchers(allowed).permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}
