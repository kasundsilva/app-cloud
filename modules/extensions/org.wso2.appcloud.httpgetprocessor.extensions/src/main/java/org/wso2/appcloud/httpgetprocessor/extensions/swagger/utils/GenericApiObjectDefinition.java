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

import org.apache.synapse.rest.API;
import org.apache.synapse.rest.Resource;
import org.apache.synapse.rest.dispatch.DispatcherHelper;
import org.apache.synapse.rest.dispatch.URLMappingBasedDispatcher;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Generalized object structure for Swagger definitions of APIs. This structure contains set of Maps which compatible
 * with both JSON and YAML formats
 */
public class GenericApiObjectDefinition {
    private API api;

    public GenericApiObjectDefinition(API api) {
        this.api = api;
    }

    /**
     * Returns Map containing the structure of swagger definition created based on API instance properties
     *
     * @return Map of the swagger definition structure
     */
    public Map<String, Object> getDefinitionMap() {
        Map<String, Object> apiMap = new LinkedHashMap<>();
        Map<String, Object> pathMap;

        //Create main elements in definition
        apiMap.put(SwaggerConstants.SWAGGER, SwaggerConstants.SWAGGER_VERSION);
        if (SwaggerConstants.HOST_NAME != null) {
            apiMap.put(SwaggerConstants.HOST, SwaggerConstants.HOST_NAME);
        }
        apiMap.put(SwaggerConstants.BASE_PATH, getUrlWithoutTenant(api.getContext()));
        apiMap.put(SwaggerConstants.INFO, getInfoMap());
        //Create paths
        pathMap = getPathMap();
        if (!pathMap.isEmpty()) {
            apiMap.put(SwaggerConstants.PATHS, getPathMap());
        }
        //Create scehemes
        apiMap.put(SwaggerConstants.SCHEMES, getSchemes());
        return apiMap;
    }

    private Map<String, Object> getResponsesMap() {
        Map<String, Object> responsesMap = new LinkedHashMap<>();
        Map<String, Object> responseDetailsMap = new LinkedHashMap<>();
        responseDetailsMap.put(SwaggerConstants.DESCRIPTION, SwaggerConstants.DEFAULT_RESPONSE);
        responsesMap.put(SwaggerConstants.DEFAULT_VALUE, responseDetailsMap);
        return responsesMap;
    }

    private Map<String, Object> getInfoMap() {
        Map<String, Object> infoMap = new LinkedHashMap<>();
        infoMap.put(SwaggerConstants.DESCRIPTION, (SwaggerConstants.API_DESC_PREFIX + api.getAPIName()));
        infoMap.put(SwaggerConstants.TITLE, api.getName());
        infoMap.put(SwaggerConstants.VERSION, (api.getVersion() != null && !api.getVersion().equals(""))
                ? api.getVersion() : SwaggerConstants.DEFAULT_API_VERSION);
        return infoMap;
    }

    private Map<String, Object> getPathMap() {
        Map<String, Object> pathsMap = new LinkedHashMap<>();
        //Process each resource in the API
        for (Resource resource : api.getResources()) {
            Map<String, Object> methodMap = new LinkedHashMap<>();
            DispatcherHelper resourceDispatcherHelper = resource.getDispatcherHelper();

            //Populate properties for each method (GET, POST, PUT ect) available
            for (String method : resource.getMethods()) {
                if (method != null) {
                    Map<String, Object> methodInfoMap = new LinkedHashMap<>();
                    methodInfoMap.put(SwaggerConstants.RESPONSES, getResponsesMap());
                    if (resourceDispatcherHelper != null) {
                        Object[] parameters = getResourceParameters(resource);
                        if (parameters.length > 0) {
                            methodInfoMap.put(SwaggerConstants.PARAMETERS, parameters);
                        }
                    }
                    methodMap.put(method.toLowerCase(), methodInfoMap);
                }
            }
            pathsMap.put(getPathFromUrl(resourceDispatcherHelper == null ? SwaggerConstants.PATH_SEPARATOR
                    : resourceDispatcherHelper.getString()), methodMap);
        }
        return pathsMap;
    }

    private String[] getSchemes() {
        String[] protocols;
        switch (api.getProtocol()) {
            case SwaggerConstants.PROTOCOL_HTTP_ONLY:
                protocols = new String[]{SwaggerConstants.PROTOCOL_HTTP};
                break;
            case SwaggerConstants.PROTOCOL_HTTPS_ONLY:
                protocols = new String[]{SwaggerConstants.PROTOCOL_HTTPS};
                break;
            default:
                protocols = new String[]{SwaggerConstants.PROTOCOL_HTTP, SwaggerConstants.PROTOCOL_HTTPS};
                break;
        }
        return protocols;
    }

    private Object[] getResourceParameters(Resource resource) {
        ArrayList<Map<String, Object>> parameterList = new ArrayList<>();
        String uri = resource.getDispatcherHelper().getString();

        if (resource.getDispatcherHelper() instanceof URLMappingBasedDispatcher) {
            generateParameterList(parameterList, uri, false);
        } else {
            //If it is uri-template, process both path and query parameters
            generateParameterList(parameterList, uri, true);
        }
        return parameterList.toArray();
    }

    private void generateParameterList(ArrayList<Map<String, Object>> parameterList, String uriString, boolean
            generateBothTypes) {
        if (uriString == null) {
            return;
        }
        //Process query parameters
        if (generateBothTypes) {
            String[] params = getQueryStringFromUrl(uriString).split("\\&");
            for (String parameter : params) {
                if (parameter != null) {
                    int pos = parameter.indexOf('=');
                    if (pos > 0) {
                        parameterList.add(getParametersMap(parameter.substring(0, pos), SwaggerConstants.PARAMETER_IN_QUERY));
                    }
                }
            }
        }
        //Process path parameters
        Matcher matcher = SwaggerConstants.PATH_PARAMETER_PATTERN.matcher(getPathFromUrl(uriString));
        while (matcher.find()) {
            parameterList.add(getParametersMap(matcher.group(1), SwaggerConstants.PARAMETER_IN_PATH));
        }
    }

    private Map<String, Object> getParametersMap(String parameterName, String parameterType) {
        Map<String, Object> parameterMap = new LinkedHashMap<>();
        parameterMap.put(SwaggerConstants.PARAMETER_DESCRIPTION, parameterName);
        parameterMap.put(SwaggerConstants.PARAMETER_IN, parameterType);
        parameterMap.put(SwaggerConstants.PARAMETER_NAME, parameterName);
        parameterMap.put(SwaggerConstants.PARAMETER_REQUIRED, true);
        //Consider parameter type as 'String' by default since no such information available in API config
        parameterMap.put(SwaggerConstants.PARAMETER_TYPE, SwaggerConstants.PARAMETER_TYPE_STRING);
        return parameterMap;
    }

    private String getPathFromUrl(String uri) {
        int pos = uri.indexOf("?");
        if (pos > 0) {
            return uri.substring(0, pos);
        }
        return uri;
    }

    private String getQueryStringFromUrl(String uri) {
        int pos = uri.indexOf("?");
        if (pos > 0) {
            return uri.substring(pos + 1);
        }
        return "";
    }

    private String getUrlWithoutTenant(String pathUrl) {
        String updatedUrl = null;
        if (pathUrl != null) {
            updatedUrl = pathUrl.replaceAll(SwaggerConstants.TENANT_REPLACEMENT_REGEX, SwaggerConstants.PATH_SEPARATOR);
        }
        return updatedUrl;
    }
}
