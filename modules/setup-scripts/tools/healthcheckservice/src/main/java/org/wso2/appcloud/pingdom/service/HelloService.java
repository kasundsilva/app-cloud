/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mysql.jdbc.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.appcloud.pingdom.service.util.Utils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.Inflater;

/**
 * Hello service resource class.
 */
@Path("/hello")
public class HelloService {
    private static final Logger log = LoggerFactory.getLogger(HelloService.class);
    private ExecutorService executorService;

    public HelloService() {
        String threadPoolSizeEnv = System.getenv("PINGDOM_THREAD_POOL_SIZE");
        int threadPoolSize = 10;
        if (!StringUtils.isNullOrEmpty(threadPoolSizeEnv)) {
            threadPoolSize = Integer.parseInt(threadPoolSizeEnv);
        }
        executorService = Executors.newScheduledThreadPool(threadPoolSize);
    }

    @GET
    @Path("/")
    public Response hello() {
        String dbURL = System.getenv("PINGDOM_SERVICE_DB_URL");
        String dbUser = System.getenv("PINGDOM_SERVICE_DB_USER");
        String dbUserPassword = System.getenv("PINGDOM_SERVICE_DB_USER_PASSWORD");
        boolean areAppsHealthy = true;
        String msg = null;

        Set<Future<Boolean>> futures = new HashSet<>();
        try {
            Date start = new Date();
            Set<String> launchURLs = Utils.getApplicationLaunchURLs(dbURL, dbUser, dbUserPassword);
            Date end = new Date();
            log.info("Completed database query in " + ((end.getTime() - start.getTime()) / 1000 % 60) + "s.");
            for (String url : launchURLs) {
                Future<Boolean> future = executorService.submit(new HealthChecker(url));
                futures.add(future);
            }
        } catch (HeartbeatServiceException e) {
            areAppsHealthy = false;
            msg = "Failed to connect to database and get launch URLs of health check applications.";
        }

        for (Future<Boolean> future : futures) {
            try {
                if (!future.get()) {
                    areAppsHealthy = false;
                    msg = "Can not access the backend applications.";
                    break;
                }
            } catch (InterruptedException | ExecutionException e) {
                areAppsHealthy = false;
                msg = "Internal error occurred in health checker service.";
                log.error(msg, e);
            }
        }
        if (areAppsHealthy) {
            return Response.ok("Health check applications responded properly.").build();
        } else {
            log.error(msg);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

}
