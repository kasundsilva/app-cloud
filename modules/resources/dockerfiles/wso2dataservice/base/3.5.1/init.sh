#!/usr/bin/env bash
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

CARBON_HOME_PATH=/home/wso2user/wso2dss-3.5.1
#remove default java opts
sed -i '/-Xms256m/d' $CARBON_HOME_PATH/bin/wso2server.sh

sed -i "/port=\"9763\"/a   proxyPort=\"80\"" $CARBON_HOME_PATH/repository/conf/tomcat/catalina-server.xml
sed -i "/port=\"9443\"/a   proxyPort=\"443\"" $CARBON_HOME_PATH/repository/conf/tomcat/catalina-server.xml

#Changing admin password
if [ -z ${ADMIN_PASSWORD+x} ]; then
    echo "ADMIN_PASSWORD is not set.";
    echo "Generating admin password.";
    ADMIN_PASSWORD=${ADMIN_PASS:-$(pwgen -s 12 1)}
    export ADMIN_PASSWORD=$ADMIN_PASSWORD
    echo "========================================================================="
    echo "Credentials for the instance:"
    echo
    echo "    user name: admin"
    echo "    password : $ADMIN_PASSWORD"
    echo "========================================================================="
    sed -i "s/.*<Password>admin<\/Password>.*/<Password>$ADMIN_PASSWORD<\/Password>/" $CARBON_HOME_PATH/repository/conf/user-mgt.xml
else
    echo "ADMIN_PASSWORD set by user.";
fi

#Check whether JAVA_OPTS env variable is defined and not empty
if [[ $JAVA_OPTS && ${JAVA_OPTS-_} ]]; then
	export JAVA_OPTS=$JAVA_OPTS
else
    #Calculate max heap size and the perm size for Java Opts
    #Check whether TOTAL_MEMORY env variable defined or and not empty
	if [[ $TOTAL_MEMORY && ${TOTAL_MEMORY-_} ]]; then
	    let MAX_HEAP_SIZE=$TOTAL_MEMORY/256*128
	    let MAX_META_SPACE_SIZE=$TOTAL_MEMORY/256*128
	    JAVA_OPTS="-Xms128m -Xmx"$MAX_HEAP_SIZE"m -XX:MaxMetaspaceSize="$MAX_META_SPACE_SIZE"m"
	    export JAVA_OPTS=$JAVA_OPTS
	fi
fi

$CARBON_HOME_PATH/bin/wso2server.sh
