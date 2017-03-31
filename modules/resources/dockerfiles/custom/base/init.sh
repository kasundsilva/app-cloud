#!/bin/bash
# ------------------------------------------------------------------------
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


# If a pidfile is still around (for example after a container restart),
# delete it so that docker can start.
rm -rf /var/run/docker.pid

dockerd $DOCKER_DAEMON_ARGS &
(( timeout = 60 + SECONDS ))
until docker info >/dev/null 2>&1
        do
                if (( SECONDS >= timeout )); then
                        echo 'Timed out trying to connect to internal docker host.' >&2
                        break
                fi
                sleep 1
        done

# pulling custom docker images and starting the container
docker run -d $CUSTOM_DOCKER_IMAGE_URL

# calling tests
sh /usr/local/bin/tests.sh



exec bash --login
