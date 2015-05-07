package com.watchrabbit.crawler.driver.factory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.PreDestroy;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mariusz
 */
@Service
public class FirefoxWebDriverFactory implements RemoteWebDriverFactory {

    private final Queue<RemoteWebDriver> drivers = new ConcurrentLinkedQueue<>();

    @Value("${crawler.driver.maxWaitingDriverSessions:10}")
    private int maxWaitingDriverSessions;

    @PreDestroy
    void cleanup() {
        drivers.forEach(driver -> driver.quit());
    }

    @Override
    public synchronized RemoteWebDriver produceDriver() {
        if (drivers.isEmpty()) {
            FirefoxProfile profile = new FirefoxProfile();
            profile.setAcceptUntrustedCertificates(true);
            RemoteWebDriver ff = new FirefoxDriver(profile);
            return ff;
        } else {
            return drivers.poll();
        }
    }

    @Override
    public void returnWebDriver(RemoteWebDriver driver) {
        if (driver != null) {
            if (drivers.size() > maxWaitingDriverSessions) {
                driver.quit();
            } else {
                driver.manage().deleteAllCookies();
                drivers.add(driver);
            }
        }
    }

}
