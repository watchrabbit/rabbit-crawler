package com.watchrabbit.crawler.auth.service;

import com.watchrabbit.crawler.auth.exception.LoginFormLocalizationException;
import com.watchrabbit.crawler.auth.model.AuthData;
import com.watchrabbit.crawler.auth.repository.AuthDataRepository;
import com.watchrabbit.crawler.driver.factory.RemoteWebDriverFactory;
import com.watchrabbit.crawler.driver.util.WaitFor;
import java.util.Collection;
import static java.util.Collections.emptyList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mariusz
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    AuthDataRepository authDataDao;

    @Autowired
    RemoteWebDriverFactory remoteWebDriverFactory;

    @Override
    public Collection<Cookie> getSession(String domain) throws LoginFormLocalizationException {
        RemoteWebDriver driver = remoteWebDriverFactory.produceDriver();
        try {
            AuthData authData = authDataDao.findByDomain(domain);
            if (authData == null) {
                LOGGER.info("Cannot find auth data for {}", domain);
                return emptyList();
            }
            driver.get(authData.getAuthEndpointUrl());
            WaitFor.load(driver);

            WebElement loginForm = locateLoginForm(driver);
            WebElement password = findPasswordInput(loginForm);
            WebElement login = findLoginInput(loginForm);

            login.sendKeys(authData.getLogin());
            password.sendKeys(authData.getPassword());
            loginForm.submit();
            WaitFor.load(driver);

            return driver.manage().getCookies();
        } finally {
            remoteWebDriverFactory.returnWebDriver(driver);
        }
    }

    private WebElement locateLoginForm(RemoteWebDriver driver) throws LoginFormLocalizationException {
        for (WebElement form : driver.findElements(By.xpath("//form"))) {
            List<WebElement> inputs = form.findElements(By.xpath("//input")).stream()
                    .filter(input -> isLoginInput(input) || isPasswordInput(input))
                    .collect(toList());
            if (inputs.size() == 2) {
                return form;
            }
        }
        throw new LoginFormLocalizationException("Cannot locate any form that matches criteria");
    }

    private boolean isLoginInput(WebElement input) {
        return input.getAttribute("type").equals("email") || input.getAttribute("type").equals("text");
    }

    private boolean isPasswordInput(WebElement input) {
        return input.getAttribute("type").equals("password");
    }

    private WebElement findLoginInput(WebElement loginForm) {
        try {
            return loginForm.findElement(By.xpath("//input[@type='email']"));
        } catch (NoSuchElementException ex) {
            return loginForm.findElement(By.xpath("//input[@type='text']"));
        }
    }

    private WebElement findPasswordInput(WebElement loginForm) {
        return loginForm.findElement(By.xpath("//input[@type='password']"));
    }

}
