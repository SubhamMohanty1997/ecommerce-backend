package com.ecommerce.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfig {

    @Bean
    public SecurityFilterChain authSeverSecurity(HttpSecurity http) throws Exception{
        OAuth2AuthorizationServerConfigurer auth2AuthorizationServerConfigurer
                = new OAuth2AuthorizationServerConfigurer();
        http.securityMatcher(auth2AuthorizationServerConfigurer.getEndpointsMatcher())
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .with(auth2AuthorizationServerConfigurer, Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                //.formLogin(form->form.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        //.requestMatchers("/api/v1/auth/hello").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                ).oauth2ResourceServer(oauth2 -> oauth2.jwt());;
                //.formLogin(Customizer.withDefaults());

        return http.build();
    }
}
