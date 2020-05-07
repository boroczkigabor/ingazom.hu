package org.atos.commutermap.network.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web.debug(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http  .authorizeRequests().mvcMatchers(HttpMethod.GET, "/baseStations", "/destinationsForMap/**").permitAll()
        .and().authorizeRequests().mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        .and().authorizeRequests().mvcMatchers(HttpMethod.POST, "/user/login").permitAll();

        http.authorizeRequests().anyRequest().authenticated();

        http.cors();
        http.csrf().disable();
    }

}
