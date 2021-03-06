package com.example.carrental.config;

import static com.example.carrental.constants.ApplicationConstants.ADMIN;

import com.example.carrental.service.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Web Security configurations.
 *
 * @author Yury Raichonak
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final AuthenticationEntryPoint authenticationEntryPoint;
  private final BCryptPasswordEncoder passwordEncoder;
  private final JwtTokenHelper jwtTokenHelper;
  private final UserSecurityService userSecurityService;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder);
  }

  @Bean
  public OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
    return new OpenEntityManagerInViewFilter();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable().cors().and().headers().frameOptions().disable();
    http
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint)
        .and()
        .authorizeRequests()
        .antMatchers("/paymentbills", "/repairbills").hasAuthority(ADMIN)
        .antMatchers(HttpMethod.POST, "/brands/**", "/carclasses", "/cars",
            "/cars/search/admin", "/models/**", "/faqs", "/locations", "/cars/**/upload/image",
            "/orders/search", "/users").hasAuthority(ADMIN)
        .antMatchers(HttpMethod.PUT, "/users/status/**", "/brands/**", "/carclasses/**",
            "/cars/**", "/users/{id}/user", "/users/{id}/admin", "/models/**",
            "/passports/download", "/drivinglicenses/download", "/faqs/**", "/locations/**",
            "/messages/**", "/orders/**", "/paymentbills/{id}", "/details",
            "/requests/approve/{id}", "/requests/reject/{id}").hasAuthority(ADMIN)
        .antMatchers(HttpMethod.GET, "/details", "/messages/**", "/users",
            "/requests/**", "/orders/new/**", "/orders/future/**", "/orders/current/**",
            "/orders/amount", "/orders/amount/day" , "/requests/new/**", "/details/admin",
            "/users/amount").hasAuthority(ADMIN)
        .antMatchers(HttpMethod.DELETE, "/faqs/**").hasAuthority(ADMIN)
        .antMatchers(HttpMethod.DELETE, "/notifications/**").authenticated()
        .antMatchers("/brands/all", "/cars/profitable", "/cars/search/**",
            "users/email/confirm/**").permitAll()
        .antMatchers(HttpMethod.GET, "/brands", "/carclasses", "/cars", "/models", "/models/list",
            "/models/brand/**", "/faqs", "/locations/**", "/details/contact").permitAll()
        .antMatchers(HttpMethod.POST, "/cars/search", "/messages", "/users/auth/registration",
            "/users/auth/login", "/users/auth/forgot/**").permitAll()
        .antMatchers(HttpMethod.POST, "/orders/**", "/requests", "/paymentbills/pay/**",
            "/repairbills/pay/**").authenticated()
        .antMatchers(HttpMethod.PUT, "/users/**", "/notifications/**").authenticated()
        .antMatchers(HttpMethod.GET, "/users/profile", "/users/auth/userinfo",
            "/notifications/**", "/details/user", "/orders/user/**", "/orders/**/export",
            "/paymentbills/user/**", "/repairbills/user/**").authenticated()
        .antMatchers("/drivinglicenses/**", "/passports/**", "/users/phone").authenticated()
        .and()
        .addFilterBefore(new JwtAuthenticationFilter(userSecurityService, jwtTokenHelper),
            UsernamePasswordAuthenticationFilter.class)
        .rememberMe();
  }
}
