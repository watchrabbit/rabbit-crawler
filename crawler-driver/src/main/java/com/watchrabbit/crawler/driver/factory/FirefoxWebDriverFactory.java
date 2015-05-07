package com.watchrabbit.crawler.driver.factory;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mariusz
 */
@Service
public class FirefoxWebDriverFactory implements RemoteWebDriverFactory {

    @Override
    public RemoteWebDriver produceDriver() {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setAcceptUntrustedCertificates(true);
        RemoteWebDriver ff = new FirefoxDriver(profile);
        return ff;
    }

}
