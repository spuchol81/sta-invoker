/*
 * Copyright (c) 2023 VMware, Inc. or its affiliates
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

package com.vmware.tanzu.demos.sta.invoker.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration(proxyBeanMethods = false)
class RestTemplateConfig {
    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    RestTemplateBuilder restTemplateBuilder(@Value("${spring.application.name}") String appName,
                                            @Value("${app.invoker.connect-timeout}") Duration connectTimeout,
                                            @Value("${app.invoker.read-timeout}") Duration readTimeout) {
        return new RestTemplateBuilder()
                .setConnectTimeout(connectTimeout)
                .setReadTimeout(readTimeout)
                .defaultHeader(HttpHeaders.USER_AGENT, appName);
    }
}
