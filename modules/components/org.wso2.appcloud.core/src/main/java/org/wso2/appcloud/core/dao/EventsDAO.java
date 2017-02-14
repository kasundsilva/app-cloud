/*
* Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.appcloud.core.dao;

import org.wso2.appcloud.common.AppCloudException;
import org.wso2.appcloud.core.DBUtil;
import org.wso2.appcloud.core.SQLQueryConstants;
import org.wso2.appcloud.core.dto.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for persisting or retrieving application creation related events.
 */

public class EventsDAO {

    private static final EventsDAO eventsDAO = new EventsDAO();

    /**
     * Constructor.
     */
    private EventsDAO() {

    }

    /**
     * Method for getting current instance.
     *
     * @return EventsDAO
     */
    public static EventsDAO getInstance() {
        return eventsDAO;
    }

    /**
     * Method for adding application creation events to database.
     *
     * @param dbConnection  database connection
     * @param versionHashId version hash id
     * @param event         application creation event
     * @param tenantId      id of tenant
     * @throws AppCloudException
     */
    public void addAppCreationEvent(Connection dbConnection, String versionHashId, Event event, int tenantId)
            throws AppCloudException {

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = dbConnection.prepareStatement(SQLQueryConstants.ADD_APP_CREATION_EVENT);
            preparedStatement.setString(1, event.getEventName());
            preparedStatement.setString(2, event.getEventStatus());
            preparedStatement.setString(3, versionHashId);
            preparedStatement.setTimestamp(4, event.getTimestamp());
            preparedStatement.setString(5, event.getEventDescription());
            preparedStatement.setInt(6, tenantId);

            preparedStatement.execute();
        } catch (SQLException e) {
            String msg = "Error while adding app creation event : " + event.getEventName() + ", status : "
                    + event.getEventStatus() + ", timestamp : " + event.getTimestamp() + " for version: " +
                    versionHashId + " in tenant : " + tenantId;
            throw new AppCloudException(msg, e);
        } finally {
            DBUtil.closePreparedStatement(preparedStatement);
        }
    }

    /**
     * Delete all the events related to a particular app version.
     *
     * @param dbConnection  database connection
     * @param versionHashId hash id of version
     * @param tenantId      id of tenant
     * @throws AppCloudException
     */
    public void deleteAppVersionEvents(Connection dbConnection, String versionHashId, int tenantId)
            throws AppCloudException {

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = dbConnection.prepareStatement(SQLQueryConstants.DELETE_ALL_APP_VERSION_EVENTS);
            preparedStatement.setString(1, versionHashId);
            preparedStatement.setInt(2, tenantId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            String msg = "Error occurred while deleting all the events for application version hash id " +
                    versionHashId + " in tenant " + tenantId;
            throw new AppCloudException(msg, e);

        } finally {
            DBUtil.closePreparedStatement(preparedStatement);
        }
    }


    /**
     * Method to get event stream of an application.
     *
     * @param dbConnection  database connection
     * @param versionHashId hash id of version
     * @param tenantId      id of tenant
     * @return list of events
     * @throws AppCloudException
     */
    public List<Event> getEventsOfApplication(Connection dbConnection, String versionHashId, int tenantId)
            throws AppCloudException {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Event> eventList = new ArrayList<>();

        try {
            preparedStatement = dbConnection.prepareStatement(SQLQueryConstants.GET_ALL_EVENTS_OF_APPLICATION);
            preparedStatement.setString(1, versionHashId);
            preparedStatement.setInt(2, tenantId);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Event event = new Event();
                event.setEventName(resultSet.getString(SQLQueryConstants.NAME));
                event.setEventStatus(resultSet.getString(SQLQueryConstants.STATUS));
                event.setTimestamp(resultSet.getTimestamp(SQLQueryConstants.EVENT_TIMESTAMP));
                event.setEventDescription(resultSet.getString(SQLQueryConstants.DESCRIPTION));

                eventList.add(event);
            }

        } catch (SQLException e) {
            String msg = "Error while retrieving Application creation event stream for application with hash id : "
                    + versionHashId + " in tenant : " + tenantId;
            throw new AppCloudException(msg, e);
        } finally {
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(preparedStatement);
        }
        return eventList;
    }
}
