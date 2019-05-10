package ru.smv.system.restaurant.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import ru.smv.system.restaurant.constants.AccessPath;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
//https://o7planning.org/ru/11705/create-a-login-application-with-spring-boot-spring-security-jpa
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder(){
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return passwordEncoder;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        // Setting Service to find User in the database.
        // And Setting PassswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/resources/**").permitAll();

        http
                .antMatcher(AccessPath.REGISTER)
                .authorizeRequests()
                .anyRequest().anonymous();

//Доступно для всех
/*
* главная страница,
* страница логирования
* страница выхода
* страница просмотра результатов голосования
* страница детальной информации выбранного ресторана*/
        http.authorizeRequests().antMatchers("/",
                AccessPath.LOGIN,
                AccessPath.LOGOUT,
                AccessPath.API_RESTAURANTS_VOTING,
                AccessPath.API_RESTAURANTS_SUD
                )
              .permitAll();
//Принял решение здесь не закрывать никакие адреса, полагаясь только на проверку прав в контроллерах
//Причина: Если надо будет изменить условия доступа теперь не придется менять в 2-х местах
//        http.authorizeRequests().antMatchers(AccessPath.API_RESTAURANTS).authenticated();


        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

        // Config Remember Me.
        http.authorizeRequests().and() //
                .rememberMe().tokenRepository(this.persistentTokenRepository()) //
                .tokenValiditySeconds(1 * 24 * 60 * 60); // 24h
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }
}
