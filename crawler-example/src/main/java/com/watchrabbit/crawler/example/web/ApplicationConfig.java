package com.watchrabbit.crawler.example.web;

import com.watchrabbit.crawler.auth.EnableAuthService;
import com.watchrabbit.crawler.batch.EnableBatchService;
import com.watchrabbit.crawler.executor.EnableExecutorService;
import com.watchrabbit.crawler.manager.EnableManagerService;
import com.watchrabbit.crawler.manager.policy.LinkFilter;
import com.watchrabbit.crawler.manager.util.InternetAddress;
import static java.util.stream.Collectors.toList;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author Mariusz
 */
@SpringBootApplication
@EnableAuthService
@EnableExecutorService
@EnableManagerService
@EnableBatchService
public class ApplicationConfig {

    @Bean
    public LinkFilter linkFilter() {
        return (links) -> links.stream().filter(link -> InternetAddress.getDomainName(link).equals("scalingapp.com")
                || InternetAddress.getDomainName(link).equals("watchrabbit.com")
                || InternetAddress.getDomainName(link).equals("api.watchrabbit.com")).collect(toList());
    }
}
