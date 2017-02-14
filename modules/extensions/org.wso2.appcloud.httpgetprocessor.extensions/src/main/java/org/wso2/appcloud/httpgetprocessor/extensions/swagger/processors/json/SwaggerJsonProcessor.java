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
package org.wso2.appcloud.httpgetprocessor.extensions.swagger.processors.json;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.rest.API;
import org.json.JSONObject;
import org.wso2.appcloud.httpgetprocessor.extensions.swagger.utils.GenericApiObjectDefinition;
import org.wso2.appcloud.httpgetprocessor.extensions.swagger.utils.SwaggerConstants;
import org.wso2.appcloud.httpgetprocessor.extensions.swagger.utils.SwaggerUtils;
import org.wso2.carbon.core.transports.CarbonHttpRequest;
import org.wso2.carbon.core.transports.CarbonHttpResponse;
import org.wso2.carbon.core.transports.HttpGetRequestProcessor;

/**
 * Processes incoming http request and provide Swagger definition for the API in JSON format
 */
public class SwaggerJsonProcessor implements HttpGetRequestProcessor {
    private static final Log log = LogFactory.getLog(SwaggerJsonProcessor.class);

    /**
     * Process incoming CarbonHttpRequest and update CarbonHttpResponse with generated swagger definition in json
     * format
     *
     * @param request              CarbonHttpRequest representing the incoming request
     * @param response             Updated CarbonHttpResponse with swagger definition
     * @param configurationContext Global configuration context
     * @throws Exception Will be thrown if any exception occurs during update
     */
    public void process(CarbonHttpRequest request, CarbonHttpResponse response,
                        ConfigurationContext configurationContext) throws Exception {
        API api = SwaggerUtils.getAPIFromSynapseConfig(request);

        if (api == null) {
            log.error("API not found for the request : " + request.getRequestURI());
            throw new Exception("API not found by SwaggerJsonProcessor");
        } else {
            JSONObject jsonDefinition = new JSONObject(new GenericApiObjectDefinition(api).getDefinitionMap());
            String responseString = jsonDefinition.toString(SwaggerConstants.DEFAULT_INDENT_FACTOR);
            SwaggerUtils.updateResponse(response, responseString, SwaggerConstants.CONTENT_TYPE_JSON);
        }
    }
}
