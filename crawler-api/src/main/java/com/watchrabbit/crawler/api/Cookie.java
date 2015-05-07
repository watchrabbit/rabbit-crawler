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

import java.util.Date;

/**
 *
 * @author Mariusz
 */
public class Cookie {

    private String name;

    private String value;

    private String path;

    private String domain;

    private Date expiry;

    private boolean isSecure;

    private boolean isHttpOnly;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public boolean isIsSecure() {
        return isSecure;
    }

    public void setIsSecure(boolean isSecure) {
        this.isSecure = isSecure;
    }

    public boolean isIsHttpOnly() {
        return isHttpOnly;
    }

    public void setIsHttpOnly(boolean isHttpOnly) {
        this.isHttpOnly = isHttpOnly;
    }

    public static class Builder {

        private final Cookie item;

        public Builder() {
            this.item = new Cookie();
        }

        public Builder withName(final String name) {
            this.item.name = name;
            return this;
        }

        public Builder withValue(final String value) {
            this.item.value = value;
            return this;
        }

        public Builder withPath(final String path) {
            this.item.path = path;
            return this;
        }

        public Builder withDomain(final String domain) {
            this.item.domain = domain;
            return this;
        }

        public Builder withExpiry(final Date expiry) {
            this.item.expiry = expiry;
            return this;
        }

        public Builder withIsSecure(final boolean isSecure) {
            this.item.isSecure = isSecure;
            return this;
        }

        public Builder withIsHttpOnly(final boolean isHttpOnly) {
            this.item.isHttpOnly = isHttpOnly;
            return this;
        }

        public Cookie build() {
            return this.item;
        }
    }

}
