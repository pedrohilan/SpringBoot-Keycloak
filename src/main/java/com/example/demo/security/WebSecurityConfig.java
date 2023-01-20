package com.example.demo.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig{

    private AuthenticationConfiguration authenticationConfiguration;

    public WebSecurityConfig(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","OPTIONS","PATCH"));

        http
                .authorizeHttpRequests((authz) -> {
                    try {
                        authz//.requestMatchers(HttpMethod.POST, "/login").permitAll()
                                .requestMatchers(HttpMethod.GET, "/hello")
                                .hasAnyAuthority("default-roles-master")
                                .anyRequest().authenticated().and()
                                .oauth2ResourceServer().jwt().jwtAuthenticationConverter(getJwtAuthenticationConverter());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).disable()
                .cors().configurationSource(request -> corsConfiguration.applyPermitDefaultValues())
                .and()
                .httpBasic();
        return http.build();
    }

    JwtAuthenticationConverter getJwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthoritiesClaimName("authorities");
        converter.setAuthorityPrefix("");
        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
        return authenticationConverter;
    }

     /*@Bean
     public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("user")
            .password(passwordEncoder().encode("password"))
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(user);
    }*/

    /*
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/
}
