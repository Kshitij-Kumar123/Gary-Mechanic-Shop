package com.example.user_microservice.Filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
//    whenever the user is trying to login
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        this is just request and response like seen before
        log.info("attempting authentication");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String requestData = (String) request.getAttribute("username");

        log.info(requestData);
//        this would create a token the authentication manager will use
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

//        this actually authentications the user
        return authenticationManager.authenticate(authenticationToken);
    }

//    after the authentication is successful - first it will try the attemptAuthentication funct and then if
//    everyhting is successful only then this is called
//    access token is created and sent to the frontend (user) - this is done by auth0 maven dependencyu
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
//        the user that has beeen authenticated
        User user = (User)authentication.getPrincipal();

//        algorithm which created the token string
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
//        current system time + whatever time addition in milliseconds
        String accessToken = JWT.create()
                .withSubject(user.getUsername()) // something unique about user
                .withExpiresAt(new Date(System.currentTimeMillis() + 100 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
//                this just means what claims or roles every specific user is assigned
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

//        for fresh token with longer time
        String refreshToken = JWT.create()
                .withSubject(user.getUsername()) // something unique about user
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        response.setHeader("accessToken", accessToken);
        response.setHeader("refreshToken", refreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
//       this allows the request body be sent as json with accesstoken and refresh token
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
