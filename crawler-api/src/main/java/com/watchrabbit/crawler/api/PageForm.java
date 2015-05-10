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
public class PageForm {

    private String url;

    private String gatewayUrl;

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static class Builder {

        private final PageForm item;

        public Builder() {
            this.item = new PageForm();
        }

        public Builder withUrl(final String url) {
            this.item.url = url;
            return this;
        }

        public Builder withGatewayUrl(final String gatewayUrl) {
            this.item.gatewayUrl = gatewayUrl;
            return this;
        }

        public PageForm build() {
            return this.item;
        }
    }

}
