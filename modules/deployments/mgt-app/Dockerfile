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

FROM docker.wso2.com/wso2-cloud/msf4j:2.0.0-alpine3.4-oracle-jdk1.8.0

ENV BALLERINA_HOME /home/wso2user/ballerina
ENV BALLERINA_VERSION 0.85
ENV BALLERINA_RUNTIME_DISTRIBUTION ballerina-$BALLERINA_VERSION.zip
ENV BALLERINA_RUNTIME ballerina-$BALLERINA_VERSION

RUN mkdir -p "$BALLERINA_HOME" && \
    chown wso2user:wso2 "$BALLERINA_HOME"

ADD $BALLERINA_RUNTIME_DISTRIBUTION $BALLERINA_HOME/

RUN unzip $BALLERINA_HOME/$BALLERINA_RUNTIME_DISTRIBUTION -d "$BALLERINA_HOME/" && \
    rm $BALLERINA_HOME/$BALLERINA_RUNTIME_DISTRIBUTION && \
    chmod +x $BALLERINA_HOME/$BALLERINA_RUNTIME/bin/ballerina

USER wso2user

ENV MSF4J_JAR management-Service-3.0.0-SNAPSHOT.jar
COPY mgt-app/target/management-Service-3.0.0-SNAPSHOT.jar "$MSF4J_HOME/"
COPY sample "$MSF4J_HOME/sample"

CMD ["bash", "-c", "source /init.sh"]