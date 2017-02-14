/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 */

function loadEndpointView() {
    clearInterval(timerId);
    if (selectedApplicationRevision.status == APPLICATION_RUNNING && application.applicationType != custom) { // endpoints will not be loaded and displayed for custom docker image applications
        // This is not implemented for mss 1.0.0 runtimes.
        if (application.applicationType == "mss" && selectedApplicationRevision.runtimeId == 2) {
            // if mss 1.0.0 do not show endpoints section
        } else {
            if (application.applicationType == "wso2dataservice") {
                displayEndpointNotloadingMessage();
            }
            showLoadingEndpointView();
            var deploymentURL = generateDefaultLaunchUrl();
            loadEndpoints(deploymentURL, applicationType, selectedApplicationRevision.versionId);
            timerId = setInterval(function () {
                loadEndpoints(deploymentURL, applicationType, selectedApplicationRevision.versionId);
            }, 3000);
        }
    } else {
        $("#app-type-data").html('');
    }
}

function displayEndpointNotloadingMessage() {
    jagg.message({
        modalStatus: true,
        type: 'warning',
        timeout: 15000,
        content: "The endpoints of your application might not be available if you created it before <b>2016/8/25</b>." +
            " Please recreate the application or create a new version of it to see the endpoints."
    });
}

function showLoadingEndpointView() {
    $("#app-type-data").html('<div class="block-endpoints "><h5>' +
        '<span><i class="fw fw-loader2 fw-spin fw-2x"></i></span>' +
        ' &nbsp; Endpoints of ' + selectedApplicationRevision.runtimeName + ' runtime is loading ...</h5></div>');
}

function loadEndpoints(deploymentURL, applicationType, versionId) {
    jagg.post("../blocks/application/application.jag", {
        action: "loadEndpoints",
        appType: applicationType,
        deploymentURL: deploymentURL,
        versionId: versionId
    }, function(result) {
        var endpoints = JSON.parse(result);
        if (endpoints == null) {
            showLoadingEndpointView();
        } else {
            // Generate SOAP Services Section
            var soap_html = "";
            if (endpoints.data.soapEndpoints != null) {
                soap_html += '<h4><i class="fw fw-soap fw-2x"></i> &nbsp; SOAP Services</h4>' +
                    '<table class="table table-responsive">' +
                    '<thead class="thead">' +
                    '<tr>' +
                    '<th width="30%">Name</th>' +
                    '<th>WSDL</th>' +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>';
                for (var i = 0; i < endpoints.data.soapEndpoints.length; i++) {
                    var proxy = endpoints.data.soapEndpoints[i];

                    soap_html += '<tr>' +
                        '<td>' + proxy.name + '</td>' +
                        '<td><a href="' + proxy.wsdl + '" target="_blank">' + proxy.wsdl + '</a></td>' +
                        '</tr>';
                }
                soap_html += '</tbody>' +
                    '</table>';
            }

            // Generate REST APIs Section
            var rest_html = "";
            if (endpoints.data.restEndpoints != null) {
                rest_html += '<h4><i class="fw fw-rest-api fw-2x"></i> &nbsp; REST APIs</h4>' +
                    '<table class="table table-responsive">' +
                    '<thead class="thead">' +
                    '<tr>' +
                    '<th width="30%">Name</th>' +
                    '<th>URL</th>' +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>';

                for (var j = 0; j < endpoints.data.restEndpoints.length; j++) {
                    var api = endpoints.data.restEndpoints[j];
                    rest_html += '<tr>' +
                        '<td>' + api.name + '</td>' +
                        '<td><a href="' + api.url + '" target="_blank">' + api.url + '</a></td>' +
                        '</tr>';
                }
                rest_html += '</tbody></table>';

            }

            // Generate Web URLs Section
            var web_html = "";
            if (endpoints.data.webEndpoints != null) {
                web_html += '<h4><i class="fw fw-endpoint fw-2x"></i> &nbsp; Web URLs</h4>' +
                    '<table class="table table-responsive">' +
                    '<thead class="thead">' +
                    '<tr>' +
                    '<th width="30%">Context</th>' +
                    '<th>URL</th>' +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>';

                for (var k = 0; k < endpoints.data.webEndpoints.length; k++) {
                    var web = endpoints.data.webEndpoints[k];
                    web_html += '<tr>' +
                        '<td>' + web.context + '</td>' +
                        '<td><a href="' + web.url + '" target="_blank">' + web.url + '</a></td>' +
                        '</tr>';
                }
                web_html += '</tbody></table>';

            }

            // Generate Swagger URL Section
            var swagger_html = "";
            if (endpoints.data.swaggerEndpoints != null) {
                swagger_html += '<h4><i class="fw fw-swagger fw-2x"></i> &nbsp; Swagger</h4>' +
                    '<table class="table table-responsive">' +
                    '<thead class="thead">' +
                    '<tr>' +
                    '<th width="30%">Context</th>' +
                    '<th>URL</th>' +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>';

                for (var j = 0; j < endpoints.data.swaggerEndpoints.length; j++) {
                    var swagger = endpoints.data.swaggerEndpoints[j];
                    swagger_html += '<tr>' +
                        '<td>' + swagger.context + '</td>' +
                        '<td><a href="' + swagger.url + '" target="_blank">' + swagger.url + '</a></td>' +
                        '</tr>';
                }
                swagger_html += '</tbody></table>';

            }
            $("#app-type-data").html('<div class="block-endpoints"><h3>Endpoints</h3>' + rest_html + soap_html + web_html + swagger_html + '</div>');
            clearInterval(timerId);
        }

    }, function(jqXHR, data, errorThrown) {

    });
}