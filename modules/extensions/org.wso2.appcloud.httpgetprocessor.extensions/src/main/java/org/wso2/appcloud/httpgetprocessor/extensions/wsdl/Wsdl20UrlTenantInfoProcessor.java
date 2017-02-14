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
package org.wso2.appcloud.httpgetprocessor.extensions.wsdl;

import org.apache.axiom.util.blob.OverflowBlob;
import org.apache.axis2.context.ConfigurationContext;
import org.wso2.carbon.core.transports.CarbonHttpRequest;
import org.wso2.carbon.core.transports.CarbonHttpResponse;
import org.wso2.carbon.core.transports.util.Wsdl20Processor;

/**
 * Wsdl generator for wsdl 2.0 requests which overwrites axis2 wsdl 2.0 processor result. This class overwrites address
 * urls by removing tenant information
 */
public class Wsdl20UrlTenantInfoProcessor extends Wsdl20Processor {

    /**
     * Process wsdl 2.0 request, generate wsdl from axis2 and update tenant information
     *
     * @param request              CarbonHttpRequest with request information
     * @param response             CarbonHttpResponse with generated wsdl stream
     * @param configurationContext Axis2 Configuration Context related to the request
     * @throws Exception
     */
    public void process(final CarbonHttpRequest request, final CarbonHttpResponse response, final ConfigurationContext configurationContext) throws Exception {
        OverflowBlob temporaryData = new OverflowBlob(256, 4048, WsdlUtils.tempPrefixWsdl20, WsdlUtils.tempSuffix);
        CarbonHttpResponse updatedResponse = new CarbonHttpResponse(temporaryData.getOutputStream());
        try {
            //Generate wsdl from axis2 wsdl 2.0 processor
            super.process(request, updatedResponse, configurationContext);
            //Overwrite endpoint urls
            WsdlUtils.updateResponse(response, updatedResponse);
        } finally {
            temporaryData.release();
        }
    }
}
