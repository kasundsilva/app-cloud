/*
 *
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

$(document).ready(function () {

});

function loadCreateApp(appTypeName) {
    fileFrom = $(".option-group input[type='radio']:checked").val();
    if (newVersion == 'true') {
        if (fileFrom == 'local') {
            window.location.href = "application.jag?option=upload-from-file&" + queryString;
        } else if (fileFrom == 'url') {
            window.location.href = "application.jag?option=upload-from-url&" + queryString;
        } else if (fileFrom == 'github') {
            window.location.href = "application.jag?option=github-repo-url&" + queryString;
        }
    } else {
        if (fileFrom == 'local') {
            window.location.href = 'application.jag?appTypeName=' + appTypeName + '&option=upload-from-file';
        } else if (fileFrom == 'url') {
            window.location.href = 'application.jag?appTypeName=' + appTypeName + '&option=upload-from-url';
        } else if (fileFrom == 'github') {
            window.location.href = 'application.jag?appTypeName=' + appTypeName + '&option=github-repo-url';
        }
    }
}

function continueSample() {
    if (newVersion == 'true') {
        window.location = "application.jag?option=deploy-sample&" + queryString;
    } else {
        window.location = "application.jag?appTypeName=" + appTypeName + "&option=deploy-sample";
    }
}

function continueCreateNew() {
    if (newVersion == 'true') {
        window.location = "application.jag?option=start-from-scratch&" + queryString;
    } else {
        window.location = "application.jag?appTypeName=" + appTypeName + "&option=start-from-scratch";
    }
}

function goBack() {
    window.history.back();
}
