package com.watchrabbit.crawler.auth.service;

import com.watchrabbit.crawler.auth.ContextTestBase;
import com.watchrabbit.crawler.auth.exception.LoginFormLocalizationException;
import com.watchrabbit.crawler.auth.model.AuthData;
import com.watchrabbit.crawler.auth.repository.AuthDataRepository;
import com.watchrabbit.crawler.driver.factory.RemoteWebDriverFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Mariusz
 */
public class AuthServiceIT extends ContextTestBase {

    @Autowired
    AuthDataRepository authDataRepository;

    @Autowired
    AuthService authService;

    @Autowired
    RemoteWebDriverFactory firefoxFactory;

    @Test
    public void shouldLogInIntoAngularApp() throws LoginFormLocalizationException {
        authDataRepository.addNewAuthData(new AuthData.Builder()
                .withDomain("api.watchrabbit.com")
                .withLogin("mariusz.luciow@gmail.com")
                .withPassword("wkswks12")
                .withAuthEndpointUrl("https://api.watchrabbit.com/signin")
                .build()
        );

        authService.getSession("api.watchrabbit.com");
    }

}
