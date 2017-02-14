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

import java.util.regex.Pattern;

/**
 * Constants used in Swagger definition generation
 */
public class SwaggerConstants {

    //swagger definition constants
    public static final String SWAGGER = "swagger";
    public static final String SWAGGER_VERSION = "2.0";
    public static final String DEFAULT_API_VERSION = "1.0.0";
    public static final String HOST = "host";
    public static final String BASE_PATH = "basePath";
    public static final String INFO = "info";
    public static final String DESCRIPTION = "description";
    public static final String VERSION = "version";
    public static final String TITLE = "title";
    public static final String PATHS = "paths";
    public static final String PARAMETERS = "parameters";
    public static final String RESPONSES = "responses";
    public static final String DEFAULT_VALUE = "default";
    public static final String SCHEMES = "schemes";
    public static final String API_DESC_PREFIX = "API Definition of ";
    public static final String DEFAULT_RESPONSE = "Default Response";

    //parameter related
    public static final String PARAMETER_DESCRIPTION = "description";
    public static final String PARAMETER_IN = "in";
    public static final String PARAMETER_NAME = "name";
    public static final String PARAMETER_REQUIRED = "required";
    public static final String PARAMETER_TYPE = "type";

    //parameter types
    public static final String PARAMETER_TYPE_STRING = "string";

    //parameter in types
    public static final String PARAMETER_IN_PATH = "path";
    public static final String PARAMETER_IN_QUERY = "query";

    //schemes
    public static final int PROTOCOL_HTTP_ONLY = 1;
    public static final int PROTOCOL_HTTPS_ONLY = 2;

    //protocols
    public static final String PROTOCOL_HTTP = "http";
    public static final String PROTOCOL_HTTPS = "https";

    //content types
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_YAML = "application/x-yaml";

    //http status codes
    public static final int HTTP_OK = 200;

    public static final int DEFAULT_INDENT_FACTOR = 4;

    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final String TENANT_REPLACEMENT_REGEX="\\/t\\/.*?\\/";
    public static final String HOST_NAME = System.getenv("APP_HOST");
    public static final String PATH_SEPARATOR = "/";
    public static final Pattern PATH_PARAMETER_PATTERN = Pattern.compile("\\{(.*?)\\}");
}
