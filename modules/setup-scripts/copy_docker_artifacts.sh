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

# This is a script to copy artifacts which is required to build docker base images

if [[ $# -ne 2 ]] ; then
    echo 'Usage: ./copy_docker_artifacts.sh /srv/app_cloud /srv/wso2-app-cloud'
    exit 1
fi

APPCLOUD_HOME=$1
SVN_ARTIFACT_HOME=$2
echo "app-cloud source : $APPCLOUD_HOME"
echo "wso2-app-cloud source : $SVN_ARTIFACT_HOME"
echo "Copy app-cloud/modules/resources/dockerfiles to appcloud/"
rm -r $APPCLOUD_HOME/dockerfiles
cp -r $APPCLOUD_HOME/modules/resources/dockerfiles $APPCLOUD_HOME

PACK_DIR=$APPCLOUD_HOME/packs
DOCKER_DIR=$APPCLOUD_HOME/dockerfiles
ARTIFACT_DIR=$SVN_ARTIFACT_HOME/modules/resources/dockerfiles
EXTENSION_DIR=$APPCLOUD_HOME/modules/extensions

JAGGERY_VERSION=wso2as-5.3.0
WSO2AS600M1_VERSION=wso2as-6.0.0-m1
WSO2AS600M2_VERSION=wso2as-6.0.0-m2
WSO2AS600M3_VERSION=wso2as-6.0.0-m3
WSO2DSS350_VERSION=wso2dss-3.5.0
WSO2DSS351_VERSION=wso2dss-3.5.1
WSO2ESB500_VERSION=wso2esb-5.0.0
TOMCAT855_VERSION=apache-tomcat-8.5.5

echo "Build extentions"
cd $EXTENSION_DIR
mvn clean install -Dmaven.test.skip=true -f $APPCLOUD_HOME/modules/extensions/pom.xml

function copy_to_jaggery() {
    cp $PACK_DIR/$JAGGERY_VERSION.zip $DOCKER_DIR/jaggery/base/0.11.0/

    cp $ARTIFACT_DIR/jaggery/base/0.11.0/lib/mysql-connector-java-5.1.35-bin.jar $DOCKER_DIR/jaggery/base/0.11.0/lib/
    cp -r $ARTIFACT_DIR/jaggery/base/0.11.0/modules/* $DOCKER_DIR/jaggery/base/0.11.0/modules/
    cp -r $ARTIFACT_DIR/jaggery/base/0.11.0/patches/* $DOCKER_DIR/jaggery/base/0.11.0/patches/

    echo "Copied to jaggery"
}

function copy_to_wso2as() {
    cp $PACK_DIR/$WSO2AS600M1_VERSION.zip $DOCKER_DIR/wso2as/base/6.0.0-m1/
    cp $PACK_DIR/$WSO2AS600M2_VERSION.zip $DOCKER_DIR/wso2as/base/6.0.0-m2/
    cp $PACK_DIR/$WSO2AS600M3_VERSION.zip $DOCKER_DIR/wso2as/base/6.0.0-m3/

    cp $ARTIFACT_DIR/wso2as/base/6.0.0-m2/lib/mysql-connector-java-5.1.35-bin.jar $DOCKER_DIR/wso2as/base/6.0.0-m2/lib/
    cp $ARTIFACT_DIR/wso2as/base/6.0.0-m3/lib/mysql-connector-java-5.1.35-bin.jar $DOCKER_DIR/wso2as/base/6.0.0-m3/lib/

    echo "Copied to wso2as"
}

function copy_to_wso2dss() {
    cp $PACK_DIR/$WSO2DSS350_VERSION.zip $DOCKER_DIR/wso2dataservice/base/3.5.0/
    cp $PACK_DIR/$WSO2DSS351_VERSION.zip $DOCKER_DIR/wso2dataservice/base/3.5.1/

    cp $ARTIFACT_DIR/wso2dataservice/base/3.5.0/lib/mysql-connector-java-5.1.35-bin.jar $DOCKER_DIR/wso2dataservice/base/3.5.0/lib/
    cp $ARTIFACT_DIR/wso2dataservice/base/3.5.1/lib/mysql-connector-java-5.1.35-bin.jar $DOCKER_DIR/wso2dataservice/base/3.5.1/lib/

    cp $EXTENSION_DIR/org.wso2.appcloud.dss.integration/target/org.wso2.appcloud.dss.integration-3.0.0-SNAPSHOT.aar $DOCKER_DIR/wso2dataservice/base/3.5.0/
    cp $EXTENSION_DIR/org.wso2.appcloud.dss.integration/target/org.wso2.appcloud.dss.integration-3.0.0-SNAPSHOT.aar $DOCKER_DIR/wso2dataservice/base/3.5.1/

    echo "Copied to wso2dss"
}

function copy_to_wso2esb() {
    cp -r $ARTIFACT_DIR/wso2esb/base/5.0.0/lib/ $DOCKER_DIR/wso2esb/base/5.0.0/
    cp -r $ARTIFACT_DIR/wso2esb/base/5.0.0/patches/ $DOCKER_DIR/wso2esb/base/5.0.0/
    cp $PACK_DIR/$WSO2ESB500_VERSION.zip $DOCKER_DIR/wso2esb/base/5.0.0/

    cp $APPCLOUD_HOME/modules/extensions/org.wso2.appcloud.esb.integration/org.wso2.appcloud.esb.endpointsapp/ContainerAPICompositeApplication/target/ContainerAPICompositeApplication_3.0.0-SNAPSHOT.car $DOCKER_DIR/wso2esb/base/5.0.0/carbonapps/
    cp $APPCLOUD_HOME/modules/extensions/org.wso2.appcloud.tenant.initializer/target/org.wso2.appcloud.tenant.initializer-3.0.0-SNAPSHOT.jar $DOCKER_DIR/wso2esb/base/5.0.0/dropins/
    cp $APPCLOUD_HOME/modules/extensions/org.wso2.appcloud.httpgetprocessor.extensions/target/org.wso2.appcloud.httpgetprocessor.extensions-3.0.0-SNAPSHOT.jar $DOCKER_DIR/wso2esb/base/5.0.0/lib/
    cp $APPCLOUD_HOME/modules/extensions/org.wso2.appcloud.esb.integration/org.wso2.appcloud.esb.fileupdater/target/org.wso2.appcloud.esb.fileupdater-3.0.0-SNAPSHOT.jar $DOCKER_DIR/wso2esb/base/5.0.0/dropins/

    echo "Copied to wso2esb"
}

function copy_to_tomcat() {
    cp $PACK_DIR/$TOMCAT855_VERSION.zip $DOCKER_DIR/tomcat/base/8.5.5/

    echo "Copied to tomcat"
}

copy_to_jaggery;
copy_to_wso2as;
copy_to_wso2dss;
copy_to_wso2esb;
copy_to_tomcat;

echo "Done"
echo "Now go to the $DOCKER_DIR and run the docker_images.sh"
