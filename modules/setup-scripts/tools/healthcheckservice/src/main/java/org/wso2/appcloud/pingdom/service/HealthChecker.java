/*
* Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.wso2.appcloud.pingdom.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.appcloud.pingdom.service.util.Utils;

import java.util.Date;
import java.util.concurrent.Callable;

public class HealthChecker implements Callable<Boolean> {
    private static final Logger log = LoggerFactory.getLogger(HealthChecker.class);
    private String endpoint;

    public HealthChecker(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public Boolean call() throws Exception {
        Date start = new Date();
        Boolean isHealthy = Utils.isApplicationHealthy(this.endpoint);
        Date end = new Date();
        log.info("Endpoint: " + this.endpoint + " invocation completed in " + ((end.getTime() - start.getTime()) / 1000 % 60) +
                         "s and returned: " + (isHealthy ? "200" : "500"));
        return isHealthy;
    }
}
