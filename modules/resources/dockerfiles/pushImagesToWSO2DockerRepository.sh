#!/bin/sh
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


function docker_push() {
    dockerPushLog=dockerPush.log
    echo "Pushing docker image:$1" >> $dockerPushLog
    docker images $1 | awk '{if(NR>1)print}' >> $dockerPushLog
    docker push $1 >> $dockerPushLog
    echo "Completed docker push for image:$1" >> $dockerPushLog
    echo "-----------------" >> $dockerPushLog
}

read -p "You are going to push docker images to docker.wso2.com. This will update existing docker images with your new tags. Are you sure? " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]
then
    #push docker images to docker.wso2.com and do not push deprecated runtimes. You need to login first as a valid OT user.
    docker login docker.wso2.com

    #tomcat base images.
    docker_push docker.wso2.com/wso2-cloud/tomcat:8.5.5-alpine3.4-open-jdk1.8.0
    docker_push docker.wso2.com/wso2-cloud/tomcat:8.5.5-alpine3.4-oracle-jdk1.8.0
    docker_push docker.wso2.com/wso2-cloud/tomcat:8.5.5-ubuntu16.04-open-jdk1.8.0
    docker_push docker.wso2.com/wso2-cloud/tomcat:8.5.5-ubuntu16.04-oracle-jdk1.8.0

    #msf4j base image
    docker_push docker.wso2.com/wso2-cloud/msf4j:2.0.0-alpine3.4-oracle-jdk1.8.0

    #php base image
    docker_push docker.wso2.com/wso2-cloud/php:7.0.4-debian8-apache2.4.10

    #jaggery base image
    docker_push docker.wso2.com/wso2-cloud/jaggery:0.11.0-alpine3.4-oracle-jdk1.8.0

    #datas service base image
    docker_push docker.wso2.com/wso2-cloud/wso2dss:3.5.0-alpine3.4-oracle-jdk1.8.0
    docker_push docker.wso2.com/wso2-cloud/wso2dss:3.5.1-alpine3.4-oracle-jdk1.8.0

    #esb base image
    docker_push docker.wso2.com/wso2-cloud/wso2esb:5.0.0-alpine3.4-oracle-jdk1.8.0

fi
