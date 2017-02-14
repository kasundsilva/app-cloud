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

package org.wso2.appcloud.integration.test.scenarios;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.testng.Assert;
import org.wso2.appcloud.integration.test.utils.AppCloudIntegrationTestConstants;
import org.wso2.appcloud.integration.test.utils.AppCloudIntegrationTestException;
import org.wso2.appcloud.integration.test.utils.AppCloudIntegrationTestUtils;

public class ESBApplicationTestCase extends AppCloudIntegrationBaseTestCase {

    private static final Log log = LogFactory.getLog(DSSApplicationTestCase.class);
    public static final String ESB_SERVER_STARTED_MESSAGE = "Mgt Console URL  :";
    public static final String ESB_APPLICATION_TYPE = "wso2esb";
    public static final String ESB_LAUNCH_CONTEXT = "/services/HelloProxy?wsdl";

    public ESBApplicationTestCase() {
        super(AppCloudIntegrationTestUtils.getPropertyValue(AppCloudIntegrationTestConstants.ESB_APP_RUNTIME_ID_KEY),
                AppCloudIntegrationTestUtils.getPropertyValue(AppCloudIntegrationTestConstants.ESB_APP_FILE_NAME_KEY),
                ESB_APPLICATION_TYPE, AppCloudIntegrationTestUtils.getPropertyValue(AppCloudIntegrationTestConstants.
                        ESB_APP_CONTENT), Long.parseLong(AppCloudIntegrationTestUtils
                        .getPropertyValue(AppCloudIntegrationTestConstants.ESB_RUNTIME_START_TIMEOUT)),
                AppCloudIntegrationTestUtils.getPropertyValue(AppCloudIntegrationTestConstants.ESB_APPLICATION_CONTEXT),
                AppCloudIntegrationTestUtils.getPropertyValue(AppCloudIntegrationTestConstants.ESB_CONTAINER_SPEC),
                Boolean.parseBoolean(AppCloudIntegrationTestUtils
                        .getPropertyValue(AppCloudIntegrationTestConstants.ESB_SET_DEFAULT_VERSION)),
                AppCloudIntegrationTestUtils.getPropertyValue(AppCloudIntegrationTestConstants.ESB_DEFAULT_VERSION));
    }

    @Override
    protected void assertLogContent(String logContent) {
        Assert.assertTrue(logContent.contains(ESB_SERVER_STARTED_MESSAGE),
                "Received log:" + logContent + " but expected line: " + ESB_SERVER_STARTED_MESSAGE);
    }

    @Override
    public void testLaunchApplication() throws Exception {

        log.info("Waiting " + runtimeStartTimeout + "milliseconds before trying application launch...");
        Thread.sleep(runtimeStartTimeout);
        JSONObject applicationBean = applicationClient.getApplicationBean(applicationName);
        String launchURL = ((JSONObject) ((JSONObject) applicationBean
                .get(AppCloudIntegrationTestConstants.PROPERTY_VERSIONS_NAME))
                .get(applicationRevision)).getString(AppCloudIntegrationTestConstants.PROPERTY_DEPLOYMENT_URL);
        launchURL = launchURL + ESB_LAUNCH_CONTEXT;
        //make the launch url http
        launchURL = launchURL.replace("https", "http");
        Boolean isLaunchSuccessfull = applicationClient.launchApplication(launchURL, sampleAppContent);
        Assert.assertTrue(isLaunchSuccessfull, "Application launch failed!");

        log.info("Testing default version launch...");
        Assert.assertTrue(isDefaultVersionLaunch(), "Default version is not launch");
    }

    @Override
    protected boolean isDefaultVersionLaunch() throws AppCloudIntegrationTestException {
        try {
            JSONObject applicationBean = applicationClient.getApplicationBean(applicationName);
            String defaultVersionLaunchURL =
                    applicationBean.getString(AppCloudIntegrationTestConstants.DEFAULT_VERSION_URL)
                            + ESB_LAUNCH_CONTEXT;

            //make the launch url http
            defaultVersionLaunchURL = defaultVersionLaunchURL.replace("https", "http");
            return applicationClient.launchApplication(defaultVersionLaunchURL, sampleAppContent);
        } catch (InterruptedException e) {
            throw new AppCloudIntegrationTestException("Error while sleep the thread", e);
        } catch (Exception e) {
            throw new AppCloudIntegrationTestException(
                    "Error while default version launch for application : " + applicationName, e);
        }
    }

}
