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

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.appcloud.integration.test.utils.AppCloudIntegrationTestConstants;
import org.wso2.appcloud.integration.test.utils.AppCloudIntegrationTestException;
import org.wso2.appcloud.integration.test.utils.AppCloudIntegrationTestUtils;
import org.wso2.carbon.automation.test.utils.http.client.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationClient extends BaseClient{
    private static final Log log = LogFactory.getLog(ApplicationClient.class);
    protected static final String CREATE_APPLICATION_ACTION = "createApplication";
    protected static final String DELETE_APPLICATION_ACTION = "deleteApplication";
    protected static final String STOP_APPLICATION_ACTION = "stopApplication";
    protected static final String START_APPLICATION_ACTION = "startApplication";
    protected static final String GET_APPLICATION_ACTION = "getApplication";
    protected static final String GET_VERSION_HASH_ACTION = "getVersionHashId";
    protected static final String GET_APPLICATION_HASH_ACTION = "getApplicationHashIdByName";
    protected static final String GET_ENV_VAR_ACTION = "getEnvVariablesOfVersion";
    protected static final String ADD_ENV_VAR_ACTION = "addRuntimeProperty";
    protected static final String UPDATE_ENV_VAR_ACTION = "updateRuntimeProperty";
    protected static final String DELETE_ENV_VAR_ACTION = "deleteRuntimeProperty";
    protected static final String GET_TAG_ACTION = "getTags";
    protected static final String ADD_TAG_ACTION = "addTag";
    protected static final String UPDATE_TAG_ACTION = "updateTag";
    protected static final String DELETE_TAG_ACTION = "deleteTag";
    protected static final String DELETE_REVISION_ACTION = "deleteVersion";
    protected static final String GET_REVISIONS_ACTION = "getExistingRevisions";
    protected static final String CHANGE_APP_ICON_ACTION = "changeAppIcon";
    protected static final String PARAM_NAME_APPLICATION_NAME = "applicationName";
    protected static final String PARAM_NAME_APPLICATION_HASH_ID = "applicationKey";
    protected static final String PARAM_NAME_APPLICATION_DESCRIPTION = "applicationDescription";
    protected static final String PARAM_NAME_RUNTIME = "runtime";
    protected static final String PARAM_NAME_APP_TYPE_NAME = "appTypeName";
    protected static final String PARAM_NAME_APPLICATION_REVISION = "applicationRevision";
    protected static final String PARAM_NAME_UPLOADED_FILE_NAME = "uploadedFileName";
    protected static final String PARAM_NAME_PROPERTIES = "runtimeProperties";
    protected static final String PARAM_NAME_TAGS = "tags";
    protected static final String PARAM_NAME_VERSION_KEY = "versionKey";
    protected static final String PARAM_NAME_KEY = "key";
    protected static final String PARAM_NAME_PREVIOUS_KEY = "prevKey";
    protected static final String PARAM_NAME_NEW_KEY = "newKey";
    protected static final String PARAM_NAME_VALUE = "value";
    protected static final String PARAM_NAME_NEW_VALUE = "newValue";
    protected static final String PARAM_NAME_IS_FILE_ATTACHED = "isFileAttached";
    protected static final String PARAM_NAME_CHANGE_ICON = "changeIcon";
    protected static final String PARAM_NAME_FILE_UPLOAD = "fileupload";
    protected static final String PARAM_NAME_IS_NEW_VERSION = "isNewVersion";
    protected static final String PARAM_NAME_CONTAINER_SPEC = "conSpec";
    protected static final String PARAM_NAME_GIT_REPO_URL = "gitRepoUrl";
    protected static final String PARAM_NAME_GIT_REPO_BRANCH = "gitRepoBranch";
    protected static final String PARAM_NAME_PROJECT_ROOT = "projectRoot";
    public static final String PARAM_NAME_APP_CREATION_METHOD = "appCreationMethod";
    public static final String PARAM_NAME_APP_CONTEXT = "applicationContext";
    public static final String DEFAULT = "default";
    public static final String GITHUB = "github";
    public static final String PARAM_NAME_SET_DEFAULT_VERSION = "setDefaultVersion";
    protected static final String UPDATE_DEFAULT_VERSION_ACTION = "updateDefaultVersion";
    protected static final String PARAM_NAME_DEFAULT_VERSION = "defaultVersion";

    private String endpoint;
    private String settingsEndpoint;


	/**
     * Construct authenticates REST client to invoke appmgt functions.
     *
     * @param backEndUrl backend url
     * @param username   username
     * @param password   password
     * @throws Exception
     */
    public ApplicationClient(String backEndUrl, String username, String password) throws Exception {
	    super(backEndUrl, username, password);
	    this.endpoint = backEndUrl + AppCloudIntegrationTestConstants.APPMGT_URL_SURFIX
	                    + AppCloudIntegrationTestConstants.REST_APPLICATION_ENDPOINT;
        this.settingsEndpoint = backEndUrl + AppCloudIntegrationTestConstants.APPMGT_URL_SURFIX
                + AppCloudIntegrationTestConstants.REST_SETTINGS_ENDPOINT;
    }

    public void createNewApplication(String applicationName, String runtime, String appTypeName,
                                     String applicationRevision, String applicationDescription, String uploadedFileName,
                                     String runtimeProperties, String tags, File uploadArtifact, boolean isNewVersion,
                                     String applicationContext, String conSpec, boolean setDefaultVersion, String appCreationMethod,
                                     String gitRepoUrl, String gitRepoBranch, String projectRoot)
            throws AppCloudIntegrationTestException {

        HttpClient httpclient = null;
        org.apache.http.HttpResponse response = null;
        int timeout = (int) AppCloudIntegrationTestUtils.getTimeOutPeriod();
        try {
            httpclient = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
                    .build();
            HttpPost httppost = new HttpPost(this.endpoint);
            httppost.setConfig(requestConfig);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            builder.addPart(PARAM_NAME_ACTION, new StringBody(CREATE_APPLICATION_ACTION, ContentType.TEXT_PLAIN));
            builder.addPart(PARAM_NAME_APP_CREATION_METHOD, new StringBody(appCreationMethod, ContentType.TEXT_PLAIN));
            if (GITHUB.equals(appCreationMethod)) {
                builder.addPart(PARAM_NAME_GIT_REPO_URL, new StringBody(gitRepoUrl, ContentType.TEXT_PLAIN));
                builder.addPart(PARAM_NAME_GIT_REPO_BRANCH, new StringBody(gitRepoBranch, ContentType.TEXT_PLAIN));
                builder.addPart(PARAM_NAME_PROJECT_ROOT, new StringBody(projectRoot, ContentType.TEXT_PLAIN));
            } else if(DEFAULT.equals(appCreationMethod)) {
                builder.addPart(PARAM_NAME_FILE_UPLOAD, new FileBody(uploadArtifact));
                builder.addPart(PARAM_NAME_UPLOADED_FILE_NAME, new StringBody(uploadedFileName, ContentType.TEXT_PLAIN));
                builder.addPart(PARAM_NAME_IS_FILE_ATTACHED, new StringBody(Boolean.TRUE.toString(),
                        ContentType.TEXT_PLAIN));//Setting true to send the file in request
            }
	    builder.addPart(PARAM_NAME_CONTAINER_SPEC, new StringBody(conSpec, ContentType.TEXT_PLAIN));
            builder.addPart(PARAM_NAME_APPLICATION_NAME, new StringBody(applicationName, ContentType.TEXT_PLAIN));
            builder.addPart(PARAM_NAME_APPLICATION_DESCRIPTION,
                    new StringBody(applicationDescription, ContentType.TEXT_PLAIN));
            builder.addPart(PARAM_NAME_RUNTIME, new StringBody(runtime, ContentType.TEXT_PLAIN));
            builder.addPart(PARAM_NAME_APP_TYPE_NAME, new StringBody(appTypeName, ContentType.TEXT_PLAIN));
            builder.addPart(PARAM_NAME_APP_CONTEXT, new StringBody(applicationContext, ContentType.TEXT_PLAIN));
            builder.addPart(PARAM_NAME_APPLICATION_REVISION,
                    new StringBody(applicationRevision, ContentType.TEXT_PLAIN));

            builder.addPart(PARAM_NAME_PROPERTIES, new StringBody(runtimeProperties, ContentType.TEXT_PLAIN));
            builder.addPart(PARAM_NAME_TAGS, new StringBody(tags, ContentType.TEXT_PLAIN));

            builder.addPart(PARAM_NAME_IS_NEW_VERSION,
                    new StringBody(Boolean.toString(isNewVersion), ContentType.TEXT_PLAIN));
            builder.addPart(PARAM_NAME_SET_DEFAULT_VERSION,
                    new StringBody(Boolean.toString(setDefaultVersion), ContentType.TEXT_PLAIN));

            httppost.setEntity(builder.build());
            httppost.setHeader(HEADER_COOKIE, getRequestHeaders().get(HEADER_COOKIE));
            response = httpclient.execute(httppost);

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity());
                throw new AppCloudIntegrationTestException("CreateNewApplication failed " + result);
            }

        } catch (ConnectTimeoutException | java.net.SocketTimeoutException e1) {
            // In most of the cases, even though connection is timed out, actual activity is completed.
            // If application is not created, in next test case, it will be identified.
            log.warn("Failed to get 200 ok response from endpoint:" + endpoint, e1);
        } catch (IOException e) {
            log.error("Failed to invoke application creation API.", e);
            throw new AppCloudIntegrationTestException("Failed to invoke application creation API.", e);
        } finally {
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpclient);
        }
    }

	public void stopApplicationRevision(String applicationName, String applicationRevision, String versionHash) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_ACTION, STOP_APPLICATION_ACTION));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_APPLICATION_NAME, applicationName));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_APPLICATION_REVISION, applicationRevision));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_VERSION_KEY, versionHash));
        HttpResponse response = doPostRequest(this.endpoint, nameValuePairs);

		if (response.getResponseCode() != HttpStatus.SC_OK && response.getResponseCode() != HttpStatus.SC_REQUEST_TIMEOUT) {
			throw new AppCloudIntegrationTestException("Application stop failed " + response.getData());
		}
	}

	public void startApplicationRevision(String applicationName, String applicationRevision, String versionHash) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_ACTION, START_APPLICATION_ACTION));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_APPLICATION_NAME, applicationName));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_APPLICATION_REVISION, applicationRevision));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_VERSION_KEY, versionHash));
        HttpResponse response = doPostRequest(this.endpoint, nameValuePairs);

		if (response.getResponseCode() != HttpStatus.SC_OK && response.getResponseCode() != HttpStatus.SC_REQUEST_TIMEOUT) {
			throw new AppCloudIntegrationTestException("Application start failed " + response.getData());
		}
	}

	public boolean deleteApplication(String applicationHashId) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_ACTION, DELETE_APPLICATION_ACTION));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_APPLICATION_HASH_ID, applicationHashId));
        HttpResponse response = doPostRequest(this.endpoint, nameValuePairs);

		if (response.getResponseCode() == HttpStatus.SC_OK && response.getData().equals("true")) {
			return true;
        } else if (response.getResponseCode() == HttpStatus.SC_REQUEST_TIMEOUT) {
            //todo: check if application is there yet
            return true;
        } else {
            throw new AppCloudIntegrationTestException("Application deletion failed " + response.getData());
        }
    }

	public JSONObject getApplicationBean(String applicationName) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_ACTION, GET_APPLICATION_ACTION));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_APPLICATION_NAME, applicationName));
        HttpResponse response = doPostRequest(this.endpoint, nameValuePairs);

		if (response.getResponseCode() == HttpStatus.SC_OK) {
			checkErrors(response);
            return new JSONObject((response.getData()));
		} else {
			throw new AppCloudIntegrationTestException("Get Application Bean failed " + response.getData());
		}
	}

	public String getApplicationHash(String applicationName) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_ACTION, GET_APPLICATION_HASH_ACTION));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_APPLICATION_NAME, applicationName));
        HttpResponse response = doPostRequest(this.endpoint, nameValuePairs);

		if (response.getResponseCode() == HttpStatus.SC_OK) {
			return response.getData();
		} else {
			throw new AppCloudIntegrationTestException("Get Application Hash value failed " + response.getData());
		}
	}

	public String getVersionHash(String applicationName, String applicationRevision) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_ACTION, GET_VERSION_HASH_ACTION));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_APPLICATION_NAME, applicationName));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_APPLICATION_REVISION, applicationRevision));
        HttpResponse response = doPostRequest(this.endpoint, nameValuePairs);

		if (response.getResponseCode() == HttpStatus.SC_OK) {
			return response.getData();
		} else {
			throw new AppCloudIntegrationTestException("Get Application Version Hash value failed " + response.getData());
		}
	}

	public String addRuntimeProperty(String versionKey, String key, String value) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_ACTION, ADD_ENV_VAR_ACTION));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_VERSION_KEY, versionKey));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_KEY, key));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_VALUE, value));
        HttpResponse response = doPostRequest(this.endpoint, nameValuePairs);

		if (response.getResponseCode() == HttpStatus.SC_OK) {
			return response.getData();
		} else {
			throw new AppCloudIntegrationTestException("Add Application Runtime Property failed " + response.getData());
		}
	}

	public JSONArray getRuntimeProperties(String versionKey) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_ACTION, GET_ENV_VAR_ACTION));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_VERSION_KEY, versionKey));
        HttpResponse response = doPostRequest(this.endpoint, nameValuePairs);

		if (response.getResponseCode() == HttpStatus.SC_OK) {
            return new JSONArray(response.getData());
		} else {
			throw new AppCloudIntegrationTestException("Get Application Runtime Properties failed " + response.getData());
		}
	}

	public void updateRuntimeProperty(String versionKey, String previousKey, String newKey, String newValue) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_ACTION, UPDATE_ENV_VAR_ACTION));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_VERSION_KEY, versionKey));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_PREVIOUS_KEY, previousKey));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_NEW_KEY, newKey));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_NEW_VALUE, newValue));
        HttpResponse response = doPostRequest(this.endpoint, nameValuePairs);

		if (response.getResponseCode() != HttpStatus.SC_OK) {
			throw new AppCloudIntegrationTestException("Update Application Runtime Properties failed " + response.getData());
		}
	}

	public void deleteRuntimeProperty(String versionKey, String key) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_ACTION, DELETE_ENV_VAR_ACTION));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_VERSION_KEY, versionKey));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_KEY, key));
        HttpResponse response = doPostRequest(this.endpoint, nameValuePairs);

		if (response.getResponseCode() != HttpStatus.SC_OK) {
			throw new AppCloudIntegrationTestException("Delete Application Runtime Properties failed " + response.getData());
		}
	}

	public String addTag(String versionKey, String key, String value) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_ACTION, ADD_TAG_ACTION));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_VERSION_KEY, versionKey));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_KEY, key));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_VALUE, value));
        HttpResponse response = doPostRequest(this.endpoint, nameValuePairs);

		if (response.getResponseCode() == HttpStatus.SC_OK) {
			return response.getData();
		} else {
			throw new AppCloudIntegrationTestException("Add Application Tag failed " + response.getData());
		}
	}

	public JSONArray getTags(String versionKey) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_ACTION, GET_TAG_ACTION));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_VERSION_KEY, versionKey));
        HttpResponse response = doPostRequest(this.endpoint, nameValuePairs);

		if (response.getResponseCode() == HttpStatus.SC_OK) {
            return new JSONArray(response.getData());
		} else {
			throw new AppCloudIntegrationTestException("Get Application Tags failed " + response.getData());
		}
	}

	public void updateTag(String versionKey, String previousKey, String newKey, String newValue) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_ACTION, UPDATE_TAG_ACTION));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_VERSION_KEY, versionKey));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_PREVIOUS_KEY, previousKey));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_NEW_KEY, newKey));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_NEW_VALUE, newValue));
        HttpResponse response = doPostRequest(this.endpoint, nameValuePairs);

		if (response.getResponseCode() != HttpStatus.SC_OK) {
			throw new AppCloudIntegrationTestException("Update Application Tag failed " + response.getData());
		}
	}

	public void deleteTag(String versionKey, String key) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_ACTION, DELETE_TAG_ACTION));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_VERSION_KEY, versionKey));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_KEY, key));
        HttpResponse response = doPostRequest(this.endpoint, nameValuePairs);

		if (response.getResponseCode() != HttpStatus.SC_OK) {
			throw new AppCloudIntegrationTestException("Delete Application Tag failed " + response.getData());
		}
	}

	public void deleteVersion(String versionKey) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_ACTION, DELETE_REVISION_ACTION));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_VERSION_KEY, versionKey));
        HttpResponse response = doPostRequest(this.endpoint, nameValuePairs);

		if (response.getResponseCode() != HttpStatus.SC_OK && response.getResponseCode() != HttpStatus.SC_REQUEST_TIMEOUT) {
			throw new AppCloudIntegrationTestException("Delete Application Version failed " + response.getData());
		}
	}

	public JSONArray getVersions(String applicationName) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_ACTION, GET_REVISIONS_ACTION));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_APPLICATION_NAME, applicationName));
        HttpResponse response = doPostRequest(this.endpoint, nameValuePairs);

		if (response.getResponseCode() == HttpStatus.SC_OK) {
            return new JSONArray(response.getData());
		} else {
			throw new AppCloudIntegrationTestException("Get Application Versions failed " + response.getData());
		}
	}

	public boolean launchApplication(String launchURL, String sampleAppContent) throws Exception {
		HttpResponse response = doGetRequest(launchURL, new Header[0]);
        return response.getResponseCode() == HttpStatus.SC_OK && response.getData().contains(sampleAppContent);
	}

	public void changeAppIcon(String applicationHash, File appIcon) throws AppCloudIntegrationTestException {
        HttpClient httpclient = null;
        org.apache.http.HttpResponse response = null;
        try {
            httpclient = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
            int timeout = (int) AppCloudIntegrationTestUtils.getTimeOutPeriod();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
                    .build();

            HttpPost httppost = new HttpPost(this.endpoint);
            httppost.setConfig(requestConfig);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart(PARAM_NAME_CHANGE_ICON, new FileBody(appIcon));
            builder.addPart(PARAM_NAME_ACTION, new StringBody(CHANGE_APP_ICON_ACTION, ContentType.TEXT_PLAIN));
            builder.addPart(PARAM_NAME_APPLICATION_HASH_ID, new StringBody(applicationHash, ContentType.TEXT_PLAIN));
            httppost.setEntity(builder.build());
            httppost.setHeader(HEADER_COOKIE, getRequestHeaders().get(HEADER_COOKIE));
            response = httpclient.execute(httppost);

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity());
                throw new AppCloudIntegrationTestException("Update app icon failed " + result);
            }
        } catch (ConnectTimeoutException | java.net.SocketTimeoutException e1) {
            // In most of the cases, even though connection is timed out, actual activity is completed.
            // And this will be asserted so if it failed due to a valid case, it will be captured.
            log.warn("Failed to get 200 ok response from endpoint:" + endpoint, e1);
        } catch (IOException e) {
            log.error("Failed to invoke app icon update API.", e);
            throw new AppCloudIntegrationTestException("Failed to invoke app icon update API.", e);
        } finally {
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpclient);
        }
    }

    //TODO This method should uncomment when we removed the free tire restriction.
    /*
    public void updateDefaultVersion(String applicationName, String newDefaultVersion)
            throws AppCloudIntegrationTestException {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_ACTION, UPDATE_DEFAULT_VERSION_ACTION));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_APPLICATION_NAME, applicationName));
        nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_DEFAULT_VERSION, newDefaultVersion));

        HttpResponse response = doPostRequest(this.settingsEndpoint, nameValuePairs);

        if (response.getResponseCode() != HttpStatus.SC_OK) {
            throw new AppCloudIntegrationTestException("Update Application Tag failed " + response.getData());
        }
    }
    */

}
