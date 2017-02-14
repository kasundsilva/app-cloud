#!/bin/bash

# ------------------------------------------------------------------------
#
# Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
#
#   WSO2 Inc. licenses this file to you under the Apache License,
#   Version 2.0 (the "License"); you may not use this file except
#   in compliance with the License.
#   You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing,
#   software distributed under the License is distributed on an
#   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
#   KIND, either express or implied.  See the License for the
#   specific language governing permissions and limitations
#   under the License.
#
# ------------------------------------------------------------------------
source custom_domain_details.cfg
KUBERNETES_PATH=""    #Production / Staging

overwrite_svc_annotation_for_http_svc_command="kubectl annotate --overwrite svc htp-$APPHASHID serviceloadbalancer/lb.host='$VERSIONLESSURL' --namespace=$TENANTDOMAIN"
echo $overwrite_svc_annotation_for_http_svc_command
overwrite_svc_annotation_for_http_svc_output="$(eval $KUBERNETES_PATH$overwrite_svc_annotation_for_http_svc_command)"
echo $overwrite_svc_annotation_for_http_svc_output

overwrite_svc_annotation_for_https_svc_command="kubectl annotate --overwrite svc hts-$APPHASHID serviceloadbalancer/lb.host='$VERSIONLESSURL' --namespace=$TENANTDOMAIN"
echo $overwrite_svc_annotation_for_https_svc_command
overwrite_svc_annotation_for_https_svc_output="$(eval $KUBERNETES_PATH$overwrite_svc_annotation_for_https_svc_command)"
echo $overwrite_svc_annotation_for_https_svc_output
