/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.appcloud.httpgetprocessor.extensions.swagger.utils;

import org.apache.axiom.ext.io.StreamCopyException;
import org.apache.axiom.util.blob.BlobOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.protocol.HTTP;
import org.apache.synapse.config.SynapseConfigUtils;
import org.apache.synapse.rest.API;
import org.wso2.carbon.core.transports.CarbonHttpRequest;
import org.wso2.carbon.core.transports.CarbonHttpResponse;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

/**
 * Util methods used in Swagger definition generation
 */
public class SwaggerUtils {
    private static final Log log = LogFactory.getLog(SwaggerUtils.class);

    /**
     * Returns API instance related to the URI in provided request
     *
     * @param request CarbonHttpRequest which contains the request URI info
     * @return API instance with respect to the request
     */
    public static API getAPIFromSynapseConfig(CarbonHttpRequest request) {
        String tenantDomain = MultitenantUtils.getTenantDomainFromRequestURL(request.getRequestURI());
        String apiName = SwaggerUtils.getApiNameFromRequestUri(request.getRequestURI());
        return SynapseConfigUtils.getSynapseConfiguration((tenantDomain != null) ? tenantDomain
                : MultitenantConstants.SUPER_TENANT_DOMAIN_NAME).getAPI(apiName);
    }

    /**
     * Extract API Name from the given URI
     *
     * @param requestUri URI String of the request
     * @return API Name extracted from the URI provided
     */
    public static String getApiNameFromRequestUri(String requestUri) {
        String tenantDomain = MultitenantConstants.SUPER_TENANT_DOMAIN_NAME;
        String pathSeparator = "/";
        String apiName = null;

        if (requestUri.contains(pathSeparator + MultitenantConstants.TENANT_AWARE_URL_PREFIX + pathSeparator)) {
            String[] paths = requestUri.split(pathSeparator);
            boolean foundTenantDelimiter = false;
            boolean foundNewTenant = false;

            for (String pathString : paths) {
                if (!foundTenantDelimiter && MultitenantConstants.TENANT_AWARE_URL_PREFIX.equals(pathString)) {
                    foundTenantDelimiter = true;
                } else if (foundTenantDelimiter && MultitenantConstants.SUPER_TENANT_DOMAIN_NAME.equals(tenantDomain)) {
                    tenantDomain = pathString;
                    foundNewTenant = true;
                } else if (foundNewTenant) {
                    apiName = pathString;
                    break;
                }
            }
        } else {
            apiName = requestUri.substring(1);
        }
        return apiName;
    }

    /**
     * Update the response with provided response string
     *
     * @param response       CarbonHttpResponse which will be updated
     * @param responseString String response to be updated in response
     * @param contentType    Content type of the response to be updated in response headaers
     * @throws Exception Any exception occured during the update
     */
    public static void updateResponse(CarbonHttpResponse response, String responseString, String contentType) throws
            Exception {
        try {
            ((BlobOutputStream) response.getOutputStream()).getBlob().readFrom(new ByteArrayInputStream
                    (responseString.getBytes(SwaggerConstants.DEFAULT_ENCODING)), responseString.length());
        } catch (StreamCopyException streamCopyException) {
            log.error("Error in generating Swagger definition : fail to copy data to response ",
                    streamCopyException);
            throw new Exception(streamCopyException);
        } catch (UnsupportedEncodingException encodingException) {
            log.error("Error in generating Swagger definition : exception in encoding ", encodingException);
            throw new Exception(encodingException);
        }
        response.setStatus(SwaggerConstants.HTTP_OK);
        response.getHeaders().put(HTTP.CONTENT_TYPE, contentType);
    }
}
