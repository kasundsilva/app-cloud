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

add_custom_domain_label_for_http_svc_command="kubectl label svc htp-$APPHASHID customDomain="$CUSTOMDOMAIN" --namespace=$TENANTDOMAIN"
echo $add_custom_domain_label_for_http_svc_command
add_custom_domain_label_for_http_svc_output="$(eval $KUBERNETES_PATH$add_custom_domain_label_for_http_svc_command)"
echo $add_custom_domain_label_for_http_svc_output

add_custom_domain_label_for_https_svc_command="kubectl label svc hts-$APPHASHID customDomain="$CUSTOMDOMAIN" --namespace=$TENANTDOMAIN"
echo $add_custom_domain_label_for_https_svc_command
add_custom_domain_label_for_https_svc_output="$(eval $KUBERNETES_PATH$add_custom_domain_label_for_https_svc_command)"
echo $add_custom_domain_label_for_https_svc_output
