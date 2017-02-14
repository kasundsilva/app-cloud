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

package org.wso2.appcloud.pingdom.service.util;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.appcloud.pingdom.service.HeartbeatServiceException;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class Utils {
    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    public static Connection getDatabaseConnection(String dbURL, String dbUser, String dbUserPassword)
            throws HeartbeatServiceException {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dbURL, dbUser, dbUserPassword);
        } catch (SQLException e) {
            String errorMsg = "Failed to get database connection to database:" + dbURL;
            log.error(errorMsg, e);
            throw new HeartbeatServiceException(errorMsg, e);
        } catch (ClassNotFoundException e) {
            String errorMsg = "Failed to load jdbc driver class.";
            log.error(errorMsg, e);
            throw new HeartbeatServiceException(errorMsg, e);
        }
        return connection;
    }

    public static Set<String> getApplicationLaunchURLs(String dbURL, String dbUser, String dbUserPassword)
            throws HeartbeatServiceException {
        Set<String> applicationURLs = new HashSet<>();
        Connection dbConnection = getDatabaseConnection(dbURL, dbUser, dbUserPassword);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = dbConnection.prepareStatement("SELECT * FROM APP_CLOUD_LAUNCH_URLS");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                applicationURLs.add(resultSet.getString("LAUNCH_URL"));
            }
        } catch (SQLException e) {
            String msg = "Error while retrieving application launch URLs from database.";
            log.error(msg, e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException e) {
                log.error("Failed to close database connection properly.", e);
            }
        }

        return applicationURLs;

    }

    public static boolean isApplicationHealthy(String launchURL) throws HeartbeatServiceException {
        boolean isApplicationHealthy = true;
        // Create an instance of HttpClient.
        HttpClient client = new HttpClient();

        // Create a method instance.
        GetMethod method = new GetMethod(launchURL);

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                isApplicationHealthy = false;
                byte[] responseBody = method.getResponseBody();
                log.error(
                        "Failed to get 200 ok response from url:" + launchURL + " due to " + new String(responseBody));
            }

        } catch (IOException e) {
            isApplicationHealthy = false;
            log.error("Failed to invoke url:" + launchURL + " and get proper response.", e);
        } finally {
            method.releaseConnection();
        }
        return isApplicationHealthy;
    }

}
