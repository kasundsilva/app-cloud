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

function loadDashboards() {
    var dashboards = dashboardProperties.dashboards;
    var dashboard_list = '';

    for (var i=0; i<dashboards.length; i++) {
        var showDashboard = false;
        if (dashboards[i].runtimes[0] == "all") {
            showDashboard = true;
        } else {
            for (var j=0; j<dashboards[i].runtimes.length; j++) {
                if (dashboards[i].runtimes[j] == selectedApplicationRevision.runtimeId) {
                    showDashboard = true;
                    break;
                }
            }
        }

        var isAvailable = (dashboards[i].isAvailable == 'true');
        var url = dataAnalyticsServerUrl + dashboards[i].dashboardContext + eval(dashboards[i].dashboardTypeUtil).getQueryString();
        if (showDashboard) {
            var dashboard = '' +
                '<div class="col-xs-12 col-md-12 col-lg-12" data-toggle="tooltip" title="' + dashboards[i].title + '">' +
                '<a class="block-anch" href="' + url + ' " onclick="return ' + isAvailable +'" target="_blank">' +
                '<div class="block-monitoring wrapper">';
            if (!isAvailable) {
                dashboard += '<div class="ribbon-wrapper"><div class="ribbon">Available Soon</div></div>';
            }
            dashboard += '<h3 class="ellipsis"><i class="fw fw-dashboard fw-lg icon"></i>' + dashboards[i].title + '</h3>' +
                '</div>' +
                '</a>' +
                '</div>';
            dashboard_list += dashboard;
        }
    }

    $("#dashboards").html(dashboard_list);
}

// DashboardTypeUtil interface
var DashboardTypeUtil = {
    getQueryString: function () {}
};

// define classes
var OperationalDashboardTypeUtil = function () {};
var HttpMonitoringDashboardTypeUtil = function () {};
var ESBAnalyticsDashboardTypeUtil = function () {};

// extend the DashboardTypeUtil interface
OperationalDashboardTypeUtil.prototype = Object.create(DashboardTypeUtil);
HttpMonitoringDashboardTypeUtil.prototype = Object.create(DashboardTypeUtil);
ESBAnalyticsDashboardTypeUtil.prototype = Object.create(DashboardTypeUtil);

// actual implementation goes here
OperationalDashboardTypeUtil.prototype.getQueryString = function () {
    return "/t/" + tenantDomain + "/dashboards/operational-dashboard/?shared=true&id=" + applicationName + "_" + selectedRevision + "_" + selectedApplicationRevision.hashId;
};

HttpMonitoringDashboardTypeUtil.prototype.getQueryString = function () {
    return "?";
};

ESBAnalyticsDashboardTypeUtil.prototype.getQueryString = function () {
    var currentTime = new Date().getTime();
    var prevTime = currentTime - 3600000;
    return "/t/" + tenantDomain + "/dashboards/esb-analytics/?shared=true&timeFrom=" + prevTime + "&timeTo=" + currentTime;
};