//package com.example.MeetingMinder.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig {
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
//                        .requestMatchers("/login", "/resources/**", "/css/**", "/js/**", "/images/**").permitAll()  // Autoriser l'accès à la page de login et aux ressources statiques
//                        .anyRequest().authenticated()
//                )
//                .formLogin(formLogin -> formLogin
//                        .loginPage("/meeting-minder/login")  // Assurez-vous que le chemin de la page de login est correct
//                        .permitAll()
//                )
//                .logout(LogoutConfigurer::permitAll);
//
//        return http.build();
//    }
//
//}
