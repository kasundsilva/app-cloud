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

package org.wso2.appcloud.tierapi.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {

    private static final Log log = LogFactory.getLog(DBUtil.class);

    public static Connection getConnection() throws SQLException {
        try {
            return org.wso2.appcloud.core.DBUtil.getDBConnection();
        } catch (Exception e) {
            String msg = "Error while connecting to Data Base ";
            log.error(msg, e);
            throw new SQLException(msg, e);
        }
    }

    /**
     * This method is used to close the database result set.
     * The intended use of this method is within a finally block.
     * This method will log any exceptions that is occurred while closing the database result set.
     *
     * @param resultSet The database result set that needs to be closed.
     */
    public static void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            // This method is called within a finally block. Hence we do not throw an error from here
            String msg = "Could not close resultSet";
            log.error(msg, e);
        }
    }

    /**
     * This method is used to close Prepared Statements.
     * The intended use of this method is within a finally block.
     * This method will log any exceptions that is occurred while closing the prepared statement.
     *
     * @param preparedStatement The prepared statement that needs to be closed.
     */
    public static void closePreparedStatement(PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            // This method is called within a finally block. Hence we do not throw an error from here
            String msg = "Could not close preparedStatement";
            log.error(msg, e);
        }
    }

    /**
     * This method is used to close the database connection.
     * The intended use of this method is within a finally block.
     * This method will log any exceptions that is occurred while closing the database connection.
     *
     * @param dbConnection The database connection that needs to be closed.
     */
    public static void closeDatabaseConnection(Connection dbConnection) {
        try {
            if (dbConnection != null) {
                dbConnection.close();
            }
        } catch (SQLException e) {
            // This method is called within a finally block. Hence we do not throw an error from here
            String msg = "Could not close database connection";
            log.error(msg, e);
        }
    }

}