package com.watchrabbit.crawler.driver;

import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Mariusz
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@EnableWebDrivers
@SpringBootApplication
@SpringApplicationConfiguration(classes = {ContextTestBase.class})
public class ContextTestBase {

}
