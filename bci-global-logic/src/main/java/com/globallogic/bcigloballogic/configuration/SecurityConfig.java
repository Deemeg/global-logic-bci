package com.globallogic.bcigloballogic.configuration;

import com.globallogic.bcigloballogic.util.JwtAuthEntryPoint;
import com.globallogic.bcigloballogic.util.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        private final JwtAuthEntryPoint jwtAuthEntryPoint;

        @Autowired
        public SecurityConfig( JwtAuthEntryPoint jwtAuthEntryPoint) {
                this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http.csrf().disable()
                        .exceptionHandling()
                        .authenticationEntryPoint(jwtAuthEntryPoint)
                        .and()
                        .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                        .authorizeRequests()
                        .antMatchers("/api/v1/auth/sign-up").permitAll()
                        .antMatchers("/api/v1/auth/login").authenticated()
                        .anyRequest().authenticated()
                        .and()
                        .httpBasic();

                http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager( AuthenticationConfiguration authenticationConfiguration) throws  Exception{
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public JwtAuthFilter jwtAuthFilter() {
                return new JwtAuthFilter();
        }
}
