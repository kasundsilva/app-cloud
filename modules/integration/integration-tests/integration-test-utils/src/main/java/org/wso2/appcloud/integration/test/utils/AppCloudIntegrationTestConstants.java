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

package org.wso2.appcloud.integration.test.utils;

public class AppCloudIntegrationTestConstants {

    // Product groups
    public static final String APPCLOUD_PRODUCT_GROUP = "AppCloud";

	// Automation Config Paths
    public static final String DEFAULT_TENANT_ADMIN = "//appCloudProperties/defaultTenant/admin";
    public static final String DEFAULT_TENANT_ADMIN_PASSWORD = "//appCloudProperties/defaultTenant/adminPassword";
    public static final String DEFAULT_TENANT_TENANT_DOMAIN = "//appCloudProperties/defaultTenant/tenantDomain";
    public static final String TIMEOUT_PERIOD = "//appCloudProperties/timeOutSetting/period";
    public static final String TIMEOUT_RETRY_COUNT = "//appCloudProperties/timeOutSetting/retryCount";
    public static final String URLS_APPCLOUD = "//appCloudProperties/urls/appCloud";
	public static final String DATABASE_NAME = "//appCloudProperties/databaseProperties/databaseName";
	public static final String DATABASE_USER_ONE = "//appCloudProperties/databaseProperties/dbUserNameOne";
	public static final String DATABASE_USER_TWO = "//appCloudProperties/databaseProperties/dbUserNameTwo";
	public static final String DATABASE_PASSWORD = "//appCloudProperties/databaseProperties/dbPassword";


	// Default Application Details
	public static final String APP_NAME_KEY = "//appCloudProperties/application/applicationName";
	public static final String APP_DESC_KEY = "//appCloudProperties/application/applicationDescription";
	public static final String APP_REVISION_KEY = "//appCloudProperties/application/defaultRevision";
	public static final String APP_NEW_REVISION_KEY = "//appCloudProperties/application/newRevision";
	public static final String TOMCAT_APP_RUNTIME_ID_KEY = "//appCloudProperties/application/runtimeIDs/runtimeID[@key='tomcat']";
	public static final String TOMCAT_AS600M2_APP_RUNTIME_ID_KEY = "//appCloudProperties/application/runtimeIDs/runtimeID[@key='tomcat-as600m2']";
	public static final String TOMCAT_AS600M3_APP_RUNTIME_ID_KEY = "//appCloudProperties/application/runtimeIDs/runtimeID[@key='tomcat-as600m3']";
	public static final String MSS_APP_RUNTIME_ID_KEY = "//appCloudProperties/application/runtimeIDs/runtimeID[@key='msf4j']";
	public static final String MSS200_APP_RUNTIME_ID_KEY = "//appCloudProperties/application/runtimeIDs/runtimeID[@key='msf4j200']";
	public static final String PHP_APP_RUNTIME_ID_KEY = "//appCloudProperties/application/runtimeIDs/runtimeID[@key='php']";
	public static final String JAGGERY_APP_RUNTIME_ID_KEY = "//appCloudProperties/application/runtimeIDs/runtimeID[@key='jaggery']";
	public static final String JAGGERY_AS600M3_APP_RUNTIME_ID_KEY = "//appCloudProperties/application/runtimeIDs/runtimeID[@key='jaggery-as600m3']";
	public static final String DSS_APP_RUNTIME_ID_KEY = "//appCloudProperties/application/runtimeIDs/runtimeID[@key='wso2dataservice']";
	public static final String ESB_APP_RUNTIME_ID_KEY = "//appCloudProperties/application/runtimeIDs/runtimeID[@key='wso2esb']";
	public static final String TOMCAT_APP_FILE_NAME_KEY = "//appCloudProperties/application/files/file[@type='war']";
	public static final String MSS_APP_FILE_NAME_KEY = "//appCloudProperties/application/files/file[@type='msf4j']";
	public static final String MSS200_APP_FILE_NAME_KEY = "//appCloudProperties/application/files/file[@type='msf4j200']";
	public static final String PHP_APP_FILE_NAME_KEY = "//appCloudProperties/application/files/file[@type='php']";
	public static final String JAGGERY_APP_FILE_NAME_KEY = "//appCloudProperties/application/files/file[@type='jaggery']";
	public static final String JAGGERY_AS600M3_APP_FILE_NAME_KEY = "//appCloudProperties/application/files/file[@type='jaggery-as600m3']";
	public static final String DSS_APP_FILE_NAME_KEY = "//appCloudProperties/application/files/file[@type='wso2dataservice']";
	public static final String ESB_APP_FILE_NAME_KEY = "//appCloudProperties/application/files/file[@type='wso2esb']";
	public static final String TOMCAT_APP_CONTENT = "//appCloudProperties/application/sampleAppContents/sampleAppContent[@key='war']";
	public static final String MSS_APP_CONTENT = "//appCloudProperties/application/sampleAppContents/sampleAppContent[@key='msf4j']";
	public static final String PHP_APP_CONTENT = "//appCloudProperties/application/sampleAppContents/sampleAppContent[@key='php']";
	public static final String JAGGERY_APP_CONTENT = "//appCloudProperties/application/sampleAppContents/sampleAppContent[@key='jaggery']";
	public static final String JAGGERY_AS600M3_APP_CONTENT = "//appCloudProperties/application/sampleAppContents/sampleAppContent[@key='jaggery-as600m3']";
	public static final String DSS_APP_CONTENT = "//appCloudProperties/application/sampleAppContents/sampleAppContent[@key='wso2dataservice']";
	public static final String ESB_APP_CONTENT = "//appCloudProperties/application/sampleAppContents/sampleAppContent[@key='wso2esb']";
	public static final String TOMCAT_RUNTIME_START_TIMEOUT = "//appCloudProperties/application/runtimeStartTimeouts/runtimeStartTimeout[@key='tomcat']";
	public static final String MSS_RUNTIME_START_TIMEOUT = "//appCloudProperties/application/runtimeStartTimeouts/runtimeStartTimeout[@key='msf4j']";
	public static final String PHP_RUNTIME_START_TIMEOUT = "//appCloudProperties/application/runtimeStartTimeouts/runtimeStartTimeout[@key='php']";
	public static final String JAGGERY_RUNTIME_START_TIMEOUT = "//appCloudProperties/application/runtimeStartTimeouts/runtimeStartTimeout[@key='jaggery']";
	public static final String DSS_RUNTIME_START_TIMEOUT = "//appCloudProperties/application/runtimeStartTimeouts/runtimeStartTimeout[@key='wso2dataservice']";
	public static final String ESB_RUNTIME_START_TIMEOUT = "//appCloudProperties/application/runtimeStartTimeouts/runtimeStartTimeout[@key='wso2esb']";
	public static final String JAGGERY_APPLICATION_CONTEXT = "//appCloudProperties/application/applicationContexts/context[@key='jaggery']";
	public static final String JAGGERY_AS600M3_APPLICATION_CONTEXT = "//appCloudProperties/application/applicationContexts/context[@key='jaggery-as600m3']";
	public static final String PHP_APPLICATION_CONTEXT = "//appCloudProperties/application/applicationContexts/context[@key='php']";
	public static final String MSS_APPLICATION_CONTEXT = "//appCloudProperties/application/applicationContexts/context[@key='msf4j']";
	public static final String DSS_APPLICATION_CONTEXT = "//appCloudProperties/application/applicationContexts/context[@key='wso2dataservice']";
	public static final String ESB_APPLICATION_CONTEXT = "//appCloudProperties/application/applicationContexts/context[@key='wso2esb']";
	public static final String TOMCAT_APPLICATION_CONTEXT = "//appCloudProperties/application/applicationContexts/context[@key='war']";
	public static final String JAGGERY_CONTAINER_SPEC = "//appCloudProperties/application/containerSpecifications/spec[@type='jaggery']";
	public static final String PHP_CONTAINER_SPEC = "//appCloudProperties/application/containerSpecifications/spec[@type='php']";
	public static final String MSS_CONTAINER_SPEC = "//appCloudProperties/application/containerSpecifications/spec[@type='msf4j']";
	public static final String DSS_CONTAINER_SPEC = "//appCloudProperties/application/containerSpecifications/spec[@type='wso2dataservice']";
	public static final String ESB_CONTAINER_SPEC = "//appCloudProperties/application/containerSpecifications/spec[@type='wso2esb']";
	public static final String TOMCAT_CONTAINER_SPEC = "//appCloudProperties/application/containerSpecifications/spec[@type='war']";
	public static final String TOMCAT_SET_DEFAULT_VERSION = "//appCloudProperties/application/defaultVersion/set[@key='war']";
	public static final String DSS_SET_DEFAULT_VERSION = "//appCloudProperties/application/defaultVersion/set[@key='wso2dataservice']";
	public static final String ESB_SET_DEFAULT_VERSION = "//appCloudProperties/application/defaultVersion/set[@key='wso2esb']";
	public static final String MSS_SET_DEFAULT_VERSION = "//appCloudProperties/application/defaultVersion/set[@key='msf4j']";
	public static final String PHP_SET_DEFAULT_VERSION = "//appCloudProperties/application/defaultVersion/set[@key='php']";
	public static final String JAGGERY_SET_DEFAULT_VERSION = "//appCloudProperties/application/defaultVersion/set[@key='jaggery']";
	public static final String TOMCAT_DEFAULT_VERSION = "//appCloudProperties/application/defaultVersion/name[@key='war']";
	public static final String DSS_DEFAULT_VERSION = "//appCloudProperties/application/defaultVersion/name[@key='wso2dataservice']";
	public static final String ESB_DEFAULT_VERSION = "//appCloudProperties/application/defaultVersion/name[@key='wso2esb']";
	public static final String MSS_DEFAULT_VERSION = "//appCloudProperties/application/defaultVersion/name[@key='msf4j']";
	public static final String PHP_DEFAULT_VERSION = "//appCloudProperties/application/defaultVersion/name[@key='php']";
	public static final String JAGGERY_DEFAULT_VERSION = "//appCloudProperties/application/defaultVersion/name[@key='jaggery']";

	public static final String APP_PROPERTIES_KEY = "//appCloudProperties/application/properties/property";
	public static final String APP_NEW_PROPERTIES_KEY = "//appCloudProperties/application/newProperties/property";
	public static final String APP_TAGS_KEY = "//appCloudProperties/application/tags/tag";
	public static final String APP_NEW_TAGS_KEY = "//appCloudProperties/application/newTags/tag";


	// container specifications
	public static final String LARGE_CPU = "//appCloudProperties/containerSpecifications/cpuValues/cpu[@key='large']";
	public static final String EXTRA_LARGE_CPU = "//appCloudProperties/containerSpecifications/cpuValues/cpu[@key='extraLarge']";
	public static final String LARGE_MEMORY = "//appCloudProperties/containerSpecifications/memoryValues/memory[@key='large']";
	public static final String EXTRA_LARGE_MEMORY = "//appCloudProperties/containerSpecifications/memoryValues/memory[@key='extraLarge']";

	// Rest endpoints
	public static final String REST_APPLICATION_ENDPOINT = "application/application.jag";
	public static final String REST_SETTINGS_ENDPOINT = "settings/settings.jag";
	public static final String REST_LOGS_ENDPOINT = "runtimeLogs/ajax/runtimeLogs.jag";
	public static final String APPMGT_USER_LOGIN = "user/login/ajax/login.jag";
	public static final String APPMGT_URL_SURFIX = "appmgt/site/blocks/";
	// Rest endpoints - Database
	public static final String DATABASE_ADD = "database/add/ajax/add.jag";
	public static final String DATABASE_USERS = "database/users/ajax/list.jag";
	public static final String DATABASE_DROP = "database/drop/ajax/drop.jag";
	public static final String DATABASE_LIST = "database/list/ajax/list.jag";

	public static final String RESPONSE_MESSAGE_NAME = "message";
	public static final String ATTRIBUTE_KEY = "key";
	public static final String STATUS_RUNNING = "running";
	public static final String STATUS_STOPPED = "stopped";
	public static final String PROPERTY_STATUS_NAME = "status";
	public static final String PROPERTY_VERSIONS_NAME = "versions";
	public static final String PROPERTY_APPLICATION_NAME = "applicationName";
	public static final String TRUE_STRING = "true";
	public static final String PROPERTY_DEPLOYMENT_URL = "deploymentURL";
	public static final String PARAM_UNDERSCORE = "_";
	public static final String PARAM_ICON = "icon";
	public static final String DEFAULT_VERSION = "defaultVersion";
	public static final String DEFAULT_VERSION_URL = "defaultURL";
}
