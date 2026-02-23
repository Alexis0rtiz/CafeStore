/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.logisticaMovil.config;

import com.example.logisticaMovil.util.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author Sistemas
 */
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtService jwtService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/login", "/api/register").permitAll()
                        .requestMatchers("/", "/index.html", "/login.html", "/css/**", "/js/**", "/error").permitAll()
                        .requestMatchers("/api/host/**").hasRole("HOST")
                        .requestMatchers("/api/admin/**").hasAnyRole("ADMIN", "HOST")
                        .requestMatchers("/api/tareas/**").hasAnyRole("ADMIN", "HOST", "EMPLEADO")
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").hasAnyRole("USUARIO", "ADMIN", "HOST")
                        .requestMatchers("/api/productos/**").hasAnyRole("ADMIN", "HOST")
                        .requestMatchers(HttpMethod.GET, "/api/promociones/**").hasAnyRole("USUARIO", "ADMIN", "HOST")
                        .requestMatchers("/api/promociones/**").hasAnyRole("ADMIN", "HOST")
                        .requestMatchers(HttpMethod.GET, "/api/recompensas/mis-puntos")
                        .hasAnyRole("USUARIO", "ADMIN", "HOST")
                        .requestMatchers("/api/recompensas/**").hasAnyRole("ADMIN", "HOST")
                        .requestMatchers("/api/user/**").hasAnyRole("USUARIO", "ADMIN", "HOST")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean

    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtService);
    }
}