package com.vitaquest.challengeservice.Authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AuthenticationConfiguration {

    @Bean
    @Profile(value = {"development", "production"})
    public IAuthenticationValidator authenticationContextValidator(){
        return new AuthenticationContextValidator();
    }
}
