package com.watchrabbit.crawler.auth.model;

/**
 *
 * @author Mariusz
 */
public class AuthData {

    private String authEndpointUrl;

    private String login;

    private String password;

    private String domain;

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

        public AuthData build() {
            return this.item;
        }
    }

}
