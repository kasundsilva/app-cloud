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
TOMCAT_HOME_DIR="/home/wso2user/apache-tomcat-8.5.5"

if [ -z ${ADMIN_PASSWORD+x} ]; then 
    echo "ADMIN_PASSWORD is not set.";
    echo "Generating admin password.";
    ADMIN_PASSWORD=${ADMIN_PASS:-$(pwgen -s 12 1)}
    echo "========================================================================="
    echo "Credentials for the instance:"
    echo
    echo "    user name: admin"
    echo "    password : $ADMIN_PASSWORD"
    echo "========================================================================="
else
    echo "ADMIN_PASSWORD set by user.";
fi

cat >$TOMCAT_HOME_DIR/conf/tomcat-users.xml <<EOL
<?xml version="1.0" encoding="utf-8"?>
<tomcat-users>
  <role rolename="admin-gui"/>
  <role rolename="admin-script"/>
  <role rolename="manager-gui"/>
  <role rolename="manager-status"/>
  <role rolename="manager-script"/>
  <user name="admin" password="$ADMIN_PASSWORD"
    roles="admin-gui,admin-script,manager-gui,manager-status,manager-script"/>
</tomcat-users>
EOL

sed -i '/<Service name="Catalina">/a <Connector protocol="org.apache.coyote.http11.Http11NioProtocol" port="8443" maxThreads="150" scheme="https" secure="true" SSLEnabled="true" keystoreFile="/wso2carbon.jks" keystorePass="wso2carbon" clientAuth="false" sslProtocol="TLS"/>' $TOMCAT_HOME_DIR/conf/server.xml

# removing .war part from the APP_NAME
APP_NAME=$(basename $APP_WAR .war)
sed -i "/\/Host/i  \\<Context path=\"""\" docBase=\"$APP_NAME\" debug=\"0\" reloadable=\"true\"></Context>" $TOMCAT_HOME_DIR/conf/server.xml

sed -i '/<Context>/a <JarScanner scanClassPath="false" />' $TOMCAT_HOME_DIR/conf/context.xml

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

if [[ $JFR_FLAG && ${JFR_FLAG-_} && $JFR_FLAG == "true" ]]; then
    export JAVA_OPTS=$JAVA_OPTS" -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=delay=20s,duration=60s,name=jfr_recording,filename=/home/wso2user/jfr_recording.jfr,settings=profile"
fi

if [[ $TAIL_LOG && ${TAIL_LOG-_} && $TAIL_LOG == "true" ]]; then
    $TOMCAT_HOME_DIR/bin/catalina.sh start
    #tail process will run in foreground
    tail -F $TOMCAT_HOME_DIR/logs/catalina.out
else
    $TOMCAT_HOME_DIR/bin/catalina.sh run
fi




