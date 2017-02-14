/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.appcloud.esb.fileupdater;


import org.apache.axis2.engine.ListenerManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @scr.component name="org.wso2.appcloud.esb.fileupdater.FileUpdaterServiceComponent"
 * immediate="true"
 * @scr.reference name="listener.manager"
 * interface="org.apache.axis2.engine.ListenerManager"
 * cardinality="1..1"
 * policy="dynamic"
 * bind="setListenerManager"
 * unbind="unsetListenerManager"
 */
public class FileUpdaterServiceComponent {
    private static final Log log = LogFactory.getLog(FileUpdaterServiceComponent.class);

    private ListenerManager listenerManager;

    protected void activate(ComponentContext ctx) {
        String carbonHome = System.getenv("CARBON_HOME_PATH");

        if (carbonHome != null) {
            String folderPath = carbonHome + "/repository/deployment/server/eventpublishers/";
            String configPublisher = "MessageFlowConfigurationPublisher.xml";
            String statsPublisher = "MessageFlowStatisticsPublisher.xml";

            File configurationPublisher = new File(folderPath + configPublisher);
            try {
                Files.deleteIfExists(configurationPublisher.toPath());
                log.info("MessageFlowConfigurationPublisher.xml successfully removed");
            } catch (IOException e) {
                log.error("Error occurred while removing MessageFlowConfigurationPublisher.xml", e);
            }
            File statisticsPublisher = new File(folderPath + statsPublisher);
            try {
                Files.deleteIfExists(statisticsPublisher.toPath());
                log.info("MessageFlowStatisticsPublisher.xml successfully removed");
            } catch (IOException e) {
                log.error("Error occurred while removing MessageFlowStatisticsPublisher.xml", e);
            }
        } else {
            log.warn("CARBON_HOME_PATH environment variable is not set. Therefore, not updating files");
        }
    }

    protected void setListenerManager(ListenerManager listenerManager) {
        this.listenerManager = listenerManager;
    }

    protected void unsetListenerManager(ListenerManager listenerManager) {
        setListenerManager(null);
    }
}
