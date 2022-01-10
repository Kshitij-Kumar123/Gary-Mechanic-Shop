package com.example.user_microservice.security;

import com.example.user_microservice.Filter.CustomAuthenticationFilter;
import com.example.user_microservice.Filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// spring security recognizes that this configuration needs to be used
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    this comes from spring secuirty we did not make this
//     it has the username, password etc etc
//     we will override the find and save function of this service to fit our needs of the app
    private final UserDetailsService userDetailsService;

//    the bean for the password encoder in the main application file
//    whenever application runs then spring security will automatically use bcrypt
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

//    different ways to tell spring to find users - this one uses memory
//    here we will pass the username and password
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    userDetailService is a bean we need to override
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        eariler, we provided user and random password and that session is kept in track with cookie - this needs to be disabled
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//        order matters here, here i say all endpoints are authorized except for a few below
//        http.authorizeRequests().antMatchers(HttpMethod.POST, "").permitAll();

        http.authorizeRequests().antMatchers(HttpMethod.POST, "/").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/").permitAll();

////        this allows us to choose what api endpoints are authorized to what roles
//        http.authorizeRequests().antMatchers(HttpMethod.POST, "http://TICKET-SERVICE/api/ticket/save").hasAnyAuthority("ROLE_ADMIN");
//        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/**").hasAnyAuthority("ROLE_USER");
//
//
//        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/user/save/**").hasAnyAuthority("ROLE_ADMIN");


////        allow anyone to access this api regardless of role
//        http.authorizeRequests().anyRequest().permitAll();
//        this will take care of authentication
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
