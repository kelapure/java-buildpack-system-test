/*
 * Copyright 2013 the original author or authors.
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

package com.gopivotal.cloudfoundry.test.support;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryOperations;
import org.cloudfoundry.client.lib.domain.CloudDomain;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

/**
 * Configuration used by all tests
 */
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@ComponentScan
public class TestConfiguration {

    @Value("${cf.org}")
    private volatile String organization;

    @Value("${cf.password}")
    private volatile String password;

    @Value("${cf.space}")
    private volatile String space;

    @Value("${cf.target:https://api.run.pivotal.io}")
    private volatile String target;

    @Value("${cf.username}")
    private volatile String username;

    @Bean(initMethod = "login", destroyMethod = "logout")
    CloudFoundryOperations cloudFoundryOperations() throws MalformedURLException {
        CloudCredentials credentials = new CloudCredentials(this.username, this.password);
        return new CloudFoundryClient(credentials, URI.create(this.target).toURL(), this.organization, this.space);
    }

    @Bean
    String domain() throws MalformedURLException {
        for (CloudDomain domain : cloudFoundryOperations().getDomainsForOrg()) {
            if (domain.getOwner().getName().equals("none")) {
                return domain.getName();
            }
        }

        throw new IllegalStateException("No default domain found");
    }

    @Bean
    RestOperations restOperations() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new NoErrorResponseErrorHandler());

        return restTemplate;
    }

    @Bean
    RuleChain ruleChain(List<TestRule> testRules) {
        RuleChain ruleChain = RuleChain.emptyRuleChain();

        for (TestRule testRule : testRules) {
            ruleChain = ruleChain.around(testRule);
        }

        return ruleChain;
    }

    private static final class NoErrorResponseErrorHandler extends DefaultResponseErrorHandler {

        @Override
        protected boolean hasError(HttpStatus statusCode) {
            return false;
        }
    }

}
