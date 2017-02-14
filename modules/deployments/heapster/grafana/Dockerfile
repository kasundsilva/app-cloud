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

FROM gcr.io/google_containers/heapster_grafana:v2.6.0-2

WORKDIR /
ADD run.sh /run.sh
RUN chmod +x /run.sh

RUN mkdir -p /mnt/grafana/certs
ADD grafana.crt /mnt/grafana/certs/grafana.crt
ADD grafana.key /mnt/grafana/certs/grafana.key


