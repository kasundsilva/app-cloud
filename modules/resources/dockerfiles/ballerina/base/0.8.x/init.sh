#!/bin/bash
# ------------------------------------------------------------------------
#
# Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

#check whether JAVA_OPTS env variable is defined and not empty
if [[ $JAVA_OPTS && ${JAVA_OPTS-_} ]]; then
       export JAVA_OPTS=$JAVA_OPTS
else
    #Calculate max heap size and the perm size for Java Opts
    #Check whether TOTAL_MEMORY env variable defined or and not empty
        if [[ $TOTAL_MEMORY && ${TOTAL_MEMORY-_} ]]; then
            let MAX_HEAP_SIZE=$TOTAL_MEMORY*128/256
            JAVA_OPTS="-Xms64m -Xmx"$MAX_HEAP_SIZE"m -XX:MaxMetaspaceSize=64m"
            echo "Default JAVA_OPTS = "$JAVA_OPTS
            export JAVA_OPTS=$JAVA_OPTS
        fi
fi

$APP_HOME/$RUNTIME_NAME/bin/ballerina run service $APP_HOME/$APP_FILE


