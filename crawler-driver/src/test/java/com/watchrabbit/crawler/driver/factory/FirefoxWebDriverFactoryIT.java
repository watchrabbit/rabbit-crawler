package com.watchrabbit.crawler.driver.factory;

import com.watchrabbit.crawler.driver.ContextTestBase;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author Mariusz
 */
public class FirefoxWebDriverFactoryIT extends ContextTestBase {

    @Autowired
    @Qualifier("firefoxWebDriverFactory")
    RemoteWebDriverFactory firefoxFactory;

    @Test
    public void shouldGetDriver() {
        RemoteWebDriver driver = firefoxFactory.produceDriver();
        driver.get("http://google.com");

        assertThat(driver.getCurrentUrl()).contains("google");
        driver.close();
    }

    @Test
    public void shouldExtractCookie() {
        RemoteWebDriver driver = null;
        RemoteWebDriver driver2 = null;
        try {
            driver = firefoxFactory.produceDriver();
            driver.get("https://watchrabbit.com:4848/");

            driver2 = firefoxFactory.produceDriver();
            driver2.get("https://watchrabbit.com:4848/");

            driver2.manage().deleteAllCookies();
            driver2.manage().addCookie(driver.manage().getCookieNamed("JSESSIONID"));

            assertThat(driver.manage().getCookieNamed("JSESSIONID").getValue()).isEqualTo(driver2.manage().getCookieNamed("JSESSIONID").getValue());
        } finally {
            driver.quit();
            driver2.quit();
        }
    }

}