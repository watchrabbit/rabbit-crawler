package com.watchrabbit.crawler.auth.service;

import com.watchrabbit.crawler.auth.exception.LoginFormLocalizationException;
import java.util.Collection;
import org.openqa.selenium.Cookie;

/**
 *
 * @author Mariusz
 */
public interface AuthService {

    Collection<Cookie> getSession(String domain) throws LoginFormLocalizationException;
}
