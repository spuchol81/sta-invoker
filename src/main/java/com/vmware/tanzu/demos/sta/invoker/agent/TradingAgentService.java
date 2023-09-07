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

package com.vmware.tanzu.demos.sta.invoker.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
class TradingAgentService {
    private final Logger logger = LoggerFactory.getLogger(TradingAgentService.class);
    private final RestTemplate client;
    private final Map<String, TradingAgent> agents = new ConcurrentHashMap<>(4);

    public TradingAgentService(RestTemplate client, TradingAgentConfig config) {
        this.client = client;
        config.getUrls().forEach(url -> agents.put(url, new TradingAgent(url, "(no output available)", TradingAgent.Status.PENDING, null)));
    }

    public List<TradingAgent> list() {
        return agents.values().stream().sorted(Comparator.comparing(TradingAgent::url)).toList();
    }

    @Scheduled(fixedDelayString = "${app.invoker.period}", timeUnit = TimeUnit.SECONDS, initialDelay = 10)
    public void invokeAll() {
        logger.info("About to invoke trading agents");
        agents.keySet().parallelStream().forEach(this::invokeTradingAgent);
        logger.info("Trading agents invocation done");
    }

    public void invokeTradingAgent(String url) {
        logger.info("Invoking trading agent: {}", url);
        agents.put(url, new TradingAgent(url, "(invoking trading agent)", TradingAgent.Status.RUNNING, null));

        try {
            doInvokeTradingAgent(url);
        } catch (Exception e) {
            logger.warn("Trading agent invocation failed: {}", url, e);
            agents.put(url, new TradingAgent(url, "(trading agent invocation failed)", TradingAgent.Status.ERROR, null));
        }
    }

    private void doInvokeTradingAgent(String url) {
        logger.debug("Calling URL: {}", url);
        final StopWatch sw = new StopWatch();
        sw.start();

        final var resp = client.getForEntity(url, String.class);
        sw.stop();

        logger.debug("Got HTTP status from URL after {} ms: {}->{}", sw.getTotalTimeMillis(), url, resp.getStatusCode());
        var output = resp.getBody();
        if (!StringUtils.hasLength(output)) {
            output = "(no output available)";
        }
        agents.put(url, new TradingAgent(url, output, TradingAgent.Status.SUCCESS, Duration.ofMillis(sw.getTotalTimeMillis())));
    }
}
