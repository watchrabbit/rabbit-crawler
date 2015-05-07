package com.watchrabbit.crawler.auth.repository;

import com.watchrabbit.crawler.auth.model.AuthData;

/**
 *
 * @author Mariusz
 */
public interface AuthDataRepository {

    AuthData findByDomain(String domain);

    void addNewAuthData(AuthData authData);
}
