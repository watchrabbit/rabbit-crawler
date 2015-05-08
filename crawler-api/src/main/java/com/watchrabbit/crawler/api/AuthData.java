/*
 * Copyright 2015 Mariusz.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.watchrabbit.crawler.api;

/**
 *
 * @author Mariusz
 */
public class AuthData {

    private String authEndpointUrl;

    private String login;

    private String password;

    private String domain;

    private int sessionDuration;

    public int getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(int sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public String getAuthEndpointUrl() {
        return authEndpointUrl;
    }

    public void setAuthEndpointUrl(String authEndpointUrl) {
        this.authEndpointUrl = authEndpointUrl;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public static class Builder {

        private final AuthData item;

        public Builder() {
            this.item = new AuthData();
        }

        public Builder withAuthEndpointUrl(final String authEndpointUrl) {
            this.item.authEndpointUrl = authEndpointUrl;
            return this;
        }

        public Builder withLogin(final String login) {
            this.item.login = login;
            return this;
        }

        public Builder withPassword(final String password) {
            this.item.password = password;
            return this;
        }

        public Builder withDomain(final String domain) {
            this.item.domain = domain;
            return this;
        }

        public Builder withSessionDuration(final int sessionDuration) {
            this.item.sessionDuration = sessionDuration;
            return this;
        }

        public AuthData build() {
            return this.item;
        }
    }

}
