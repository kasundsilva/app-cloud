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

#KUBERNETES_PATH="/home/appcloud/scripts/"   #162
KUBERNETES_PATH=""    #Production / Staging

exposure_level="public"     #public or private

kub_command="kubectl get namespaces"
getnamespaces_command=$KUBERNETES_PATH$kub_command

namespaces_output="$(eval $getnamespaces_command)"

row_number=0

while read -r line
do
        if [ "$row_number" -ne "0" ];then
                #echo "$line"
                tenant_name="$(cut -d ' ' -f1 <<< $line)"
                echo $tenant_name

               # set_exposure_level_command="kubectl label --overwrite svc --all exposure-level="$exposure_level" --namespace="$tenant_name
                set_exposure_level_command="kubectl label svc --all exposure-level="$exposure_level" --namespace="$tenant_name
                echo $set_exposure_level_command

                set_exposure_level_output="$(eval $KUBERNETES_PATH$set_exposure_level_command)"
                echo $set_exposure_level_output

        fi

        row_number=$((row_number+1))

done <<<"$namespaces_output"
