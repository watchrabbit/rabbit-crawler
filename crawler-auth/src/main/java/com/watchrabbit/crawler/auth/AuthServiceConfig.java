package com.watchrabbit.crawler.auth;

import com.watchrabbit.crawler.auth.repository.AuthDataRepository;
import com.watchrabbit.crawler.auth.repository.InMemoryAuthDataRepository;
import com.watchrabbit.crawler.driver.EnableWebDrivers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Mariusz
 */
@Configuration
@ComponentScan
@EnableWebDrivers
public class AuthServiceConfig {

    @Bean
    @ConditionalOnMissingBean
    AuthDataRepository getAuthDataRepository() {
        return new InMemoryAuthDataRepository();
    }
}
