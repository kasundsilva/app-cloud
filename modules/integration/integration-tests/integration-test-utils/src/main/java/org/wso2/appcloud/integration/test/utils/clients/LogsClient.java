/*
 *   Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.appcloud.integration.test.utils.clients;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.wso2.appcloud.integration.test.utils.AppCloudIntegrationTestConstants;
import org.wso2.appcloud.integration.test.utils.AppCloudIntegrationTestException;
import org.wso2.appcloud.integration.test.utils.AppCloudIntegrationTestUtils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogsClient extends BaseClient{
	private static final Log log = LogFactory.getLog(LogsClient.class);
	protected static final String PARAM_NAME_APPLICATION_HASH_ID = "applicationKey";
	protected static final String PARAM_NAME_APPLICATION_REVISION = "selectedRevision";
	public static final String UTF_8_ENCODING = "UTF-8";

    private String endpoint;


	/**
     * Construct authenticates REST client to invoke appmgt functions.
     *
     * @param backEndUrl backend url
     * @param username   username
     * @param password   password
     * @throws Exception
     */
    public LogsClient(String backEndUrl, String username, String password) throws Exception {
	    super(backEndUrl, username, password);
	    this.endpoint = backEndUrl + AppCloudIntegrationTestConstants.APPMGT_URL_SURFIX
	                    + AppCloudIntegrationTestConstants.REST_LOGS_ENDPOINT;
    }

    public String[] getSnapshotLogs(String applicationKey, String applicationRevision)
            throws AppCloudIntegrationTestException {
        HttpClient httpclient = null;
        org.apache.http.HttpResponse response = null;
        try {
            httpclient = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
            int timeout = (int) AppCloudIntegrationTestUtils.getTimeOutPeriod();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
                    .build();
            HttpPost httppost = new HttpPost(this.endpoint);
            httppost.setConfig(requestConfig);
            List<NameValuePair> params = new ArrayList<NameValuePair>(3);
            params.add(new BasicNameValuePair(PARAM_NAME_ACTION, "getSnapshotLogs"));
            params.add(new BasicNameValuePair(PARAM_NAME_APPLICATION_HASH_ID, applicationKey));
            params.add(new BasicNameValuePair(PARAM_NAME_APPLICATION_REVISION, applicationRevision));
            httppost.setEntity(new UrlEncodedFormEntity(params, UTF_8_ENCODING));
            httppost.setHeader(HEADER_COOKIE, getRequestHeaders().get(HEADER_COOKIE));
            response = httpclient.execute(httppost);
            String[] resultArray = new String[2];
            resultArray[0] = String.valueOf(response.getStatusLine().getStatusCode());
            resultArray[1] = EntityUtils.toString(response.getEntity());
            return resultArray;
        } catch (IOException e) {
            log.error("Failed to invoke app icon update API.", e);
            throw new AppCloudIntegrationTestException("Failed to invoke app icon update API.", e);
        } finally {
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpclient);
        }

    }

}
