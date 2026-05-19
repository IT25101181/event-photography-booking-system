package com.photobooking.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
    /*
    OOP Concept: Class & Object
    Explanation:
    Functions as a defined Object with specific responsibilities in the application.
    
    Related Files:
    - Classes that import this file
    
    Relationship:
    Provides utility, configuration, or initialization logic.
    
    Reason:
    Organizes code systematically.
    */


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
