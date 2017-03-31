/*
* Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.appcloud.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.appcloud.common.AppCloudException;
import org.wso2.appcloud.core.dao.CustomDockerImageDAO;
import org.wso2.appcloud.core.dto.CustomImage;
import org.wso2.carbon.context.CarbonContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Managing Custom Docker Images
 */
public class CustomDockerImageManager {
    private static Log log = LogFactory.getLog(CustomDockerImageManager.class);

    /**
     * Add custom image details to database
     * @param imageId tenant domain-alphaneumeric umage url
     * @param remoteUrl image url
     * @throws AppCloudException
     */
    public static void addCustomDockerImage(String imageId, String remoteUrl) throws AppCloudException{
        Connection dbConnection = DBUtil.getDBConnection();
        int tenantId = CarbonContext.getThreadLocalCarbonContext().getTenantId();
        try {
            Date date = new Date();
            String timeStamp = String.valueOf(new java.sql.Timestamp(date.getTime()));
            if(log.isDebugEnabled()) {
                log.debug("Adding custom docker image with image id : " + imageId + " pulled from remote url : " +
                          remoteUrl + " for tenant id : " + tenantId + " at : " + timeStamp);
            }
            CustomDockerImageDAO.getInstance().
                    addCustomDockerImage(dbConnection, imageId, tenantId, remoteUrl, timeStamp);
            dbConnection.commit();
        } catch (AppCloudException e) {
            String msg = "Error while adding custom image of : " + remoteUrl + " for tenant id : " + tenantId;
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        } catch (SQLException e) {
            String msg = "Error while committing transaction for adding image from : " +
                         remoteUrl + " for tenant id : " + tenantId;
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        } finally {
            DBUtil.closeConnection(dbConnection);
        }
    }

    /**
     * Update image details with test results
     * @param imageId image id
     * @param resultsJsonString json string consists of test results
     *                          sample result json : {"test00":"pass","test01":"fail","test02":"pass","test03":"pass"}
     * @param status
     * @throws AppCloudException
     */
    public static void updateCustomDockerTestResults(String imageId, String resultsJsonString, String status)
            throws AppCloudException {
        Connection dbConnection = DBUtil.getDBConnection();
        try {
            Date date = new Date();
            String timeStamp = String.valueOf(new java.sql.Timestamp(date.getTime()));
            CustomDockerImageDAO.getInstance().
                    updateCustomDockerImageRecord(dbConnection, imageId, resultsJsonString, status, timeStamp);
            dbConnection.commit();
        } catch (AppCloudException e) {
            String msg = "Error while updating custom image  : " + imageId + " with results : " + resultsJsonString +
                         " with status : " + status;
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        } catch (SQLException e) {
            String msg = "Error while committing transaction for updating image : " + imageId;
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        } finally {
            DBUtil.closeConnection(dbConnection);
        }
    }

    /**
     * Check image availability
     * @param remoteUrl remote url
     * @return true if image is available (i.e. already added)
     * @throws AppCloudException
     */
    public static boolean isImageAvailable(String remoteUrl) throws AppCloudException {
        Connection dbConnection = DBUtil.getDBConnection();
        int tenantId = CarbonContext.getThreadLocalCarbonContext().getTenantId();
        try {
            return CustomDockerImageDAO.getInstance().isImageAvailable(dbConnection, remoteUrl, tenantId);
        } catch (AppCloudException e) {
            String msg = "Error while chcking image availability in db for image : " + remoteUrl + " of tenant id : "
                         + tenantId;
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        } finally {
            DBUtil.closeConnection(dbConnection);
        }
    }

    /**
     * Get all custom images
     * @param status null meanns all images, other possible options are pass,failed
     * @return arraylist of CustomImages
     * @throws AppCloudException
     */
    public static CustomImage[] getAllCustomImages(String status) throws AppCloudException { // status null means all
        Connection dbConnection = DBUtil.getDBConnection();
        int tenantId = CarbonContext.getThreadLocalCarbonContext().getTenantId();
        try {
            List<CustomImage> imagesList = CustomDockerImageDAO.getInstance().getCustomImages(dbConnection, tenantId, status);
            if(imagesList.size() > 0) { // to avoid indexOutOfBound exception
                return imagesList.toArray(new CustomImage[imagesList.size()]);
            } else {
                return new CustomImage[0];
            }
        } catch (AppCloudException e) {
            String msg = "Error while retrieving custom images";
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        } finally {
            DBUtil.closeConnection(dbConnection);
        }
    }

    /**
     * Delete image
     * @param imageId image id
     * @return true if successfull
     * @throws AppCloudException
     */
    public static boolean deleteImage(String imageId) throws AppCloudException {
        Connection dbConnection = DBUtil.getDBConnection();
        try {
            return CustomDockerImageDAO.getInstance().deleteImage(dbConnection, imageId);
        } catch (AppCloudException e) {
            String msg = "Error while deleting custom images";
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        } finally {
            DBUtil.closeConnection(dbConnection);
        }
    }

    /**
     * Get image by id
     * @param imageId image id
     * @return CustomImage object
     * @throws AppCloudException
     */
    public static CustomImage getImageById(String imageId) throws AppCloudException {
        Connection dbConnection = DBUtil.getDBConnection();
        try {
            return CustomDockerImageDAO.getInstance().getImageById(dbConnection, imageId);

        } catch (AppCloudException e) {
            String msg = "Error while retrieving custom images";
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        } finally {
            DBUtil.closeConnection(dbConnection);
        }

    }

}
