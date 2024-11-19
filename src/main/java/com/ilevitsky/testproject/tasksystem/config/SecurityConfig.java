package com.ilevitsky.testproject.tasksystem.config;

import com.ilevitsky.testproject.tasksystem.entity.auth.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final AppConfig appConfig;

  private static final String ADMIN = Role.ADMIN.name();
  private static final String USER = Role.USER.name();

  private static final String[] WHITELIST = {
    "/auth/login", "/auth/register", "/api-docs/**", "/swagger-ui.html", "/swagger-ui/**"
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            request ->
                request
                    .requestMatchers(WHITELIST)
                    .permitAll()
                    .requestMatchers("/tasks/**")
                    .hasAnyAuthority(ADMIN, USER)
                    .requestMatchers("/users/**")
                    .hasAuthority(ADMIN)
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(appConfig.authenticationProvider())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
