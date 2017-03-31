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

package org.wso2.appcloud.tierapi.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.appcloud.tierapi.SQLQueryConstants;
import org.wso2.appcloud.tierapi.bean.ContainerSpecifications;
import org.wso2.appcloud.tierapi.dao.ContainerSpecsDao;
import org.wso2.appcloud.tierapi.util.DBUtil;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class ContainerSpecDaoImpl implements ContainerSpecsDao {
	private static final Log log = LogFactory.getLog(ContainerSpecDaoImpl.class);

	@Override
	public List<ContainerSpecifications> getAllContainerSpecs() throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<ContainerSpecifications> containerSpecsList = new ArrayList<ContainerSpecifications>();
		try {
			dbConnection = DBUtil.getConnection();
			preparedStatement = dbConnection.prepareStatement(SQLQueryConstants.GET_ALL_CONTAINER_SPECIFICATIONS);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ContainerSpecifications containerSpec = getContainerSpecifications(rs);
				containerSpecsList.add(containerSpec);
			}
		} catch (SQLException e) {
			String msg = "Error while getting details of Container Specifications";
			log.error(msg, e);
			throw e;
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(preparedStatement);
			DBUtil.closeDatabaseConnection(dbConnection);
		}
		return containerSpecsList;
	}

	@Override
	public List<ContainerSpecifications> getContainerSpecByRuntimeID(int runtimeId) throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<ContainerSpecifications> containerSpecsList = new ArrayList<ContainerSpecifications>();
		try {
			dbConnection = DBUtil.getConnection();
			preparedStatement = dbConnection.prepareStatement(
					SQLQueryConstants.GET_CONTAINER_SPECIFICATIONS_BY_RUNTIME_ID);
			preparedStatement.setInt(1, runtimeId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ContainerSpecifications containerSpec = getContainerSpecifications(rs);
				containerSpecsList.add(containerSpec);
			}
		} catch (SQLException e) {
			String msg = "Error while getting details of Container Specifications for Runtime ID " + runtimeId;
			log.error(msg, e);
			throw e;
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(preparedStatement);
			DBUtil.closeDatabaseConnection(dbConnection);
		}
		return containerSpecsList;
	}

	@Override
	public ContainerSpecifications getContainerSpecById(int containerSpecId) throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		ContainerSpecifications containerSpec = new ContainerSpecifications();
		try {
			dbConnection = DBUtil.getConnection();
			preparedStatement = dbConnection.prepareStatement(SQLQueryConstants.GET_CONTAINER_SPECIFICATION_BY_ID);
			preparedStatement.setInt(1, containerSpecId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				containerSpec = getContainerSpecifications(rs);
			}
		} catch (SQLException e) {
			String msg =
					"Error while getting details of Container Specification with the ID" + containerSpecId;
			log.error(msg, e);
			throw e;
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(preparedStatement);
			DBUtil.closeDatabaseConnection(dbConnection);
		}
		return containerSpec;
	}

	@Override
	public ContainerSpecifications defineContainerSpec(ContainerSpecifications containerSpec) throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			dbConnection = DBUtil.getConnection();
			preparedStatement = dbConnection.prepareStatement(SQLQueryConstants.ADD_CONTAINER_SPECIFICATION);
			preparedStatement.setString(1, containerSpec.getConSpecName());
			preparedStatement.setInt(2, containerSpec.getCpu());
			preparedStatement.setInt(3, containerSpec.getMemory());
			preparedStatement.setInt(4, containerSpec.getCostPerHour());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			preparedStatement = dbConnection.prepareStatement(SQLQueryConstants.GET_CONTAINER_SPECIFICATION_BY_NAME);
			preparedStatement.setString(1, containerSpec.getConSpecName());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				containerSpec = getContainerSpecifications(rs);
			}
		} catch (SQLException e) {
			String msg = "Error while defining the Container Specifications";
			log.error(msg, e);
			throw e;
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(preparedStatement);
			DBUtil.closeDatabaseConnection(dbConnection);
		}
		return containerSpec;
	}

	@Override
	public boolean deleteContainerSpecById(int containerSpecId) throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		boolean isDeleted;
		try {
			dbConnection = DBUtil.getConnection();
			preparedStatement = dbConnection.prepareStatement(SQLQueryConstants.DELETE_CONTAINER_SPECIFICATION);
			preparedStatement.setInt(1, containerSpecId);
			isDeleted = preparedStatement.executeUpdate() == 1 ? true : false;
		} catch (SQLException e) {
			String msg =
					"Error while deleting the Container Specifications with ID " + containerSpecId;
			log.error(msg, e);
			throw e;
		} finally {
			DBUtil.closePreparedStatement(preparedStatement);
			DBUtil.closeDatabaseConnection(dbConnection);
		}
		return isDeleted;
	}

	@Override
	public ContainerSpecifications updateContainerSpecById(int containerSpecId, ContainerSpecifications containerSpec) throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			dbConnection = DBUtil.getConnection();
			preparedStatement = dbConnection.prepareStatement(SQLQueryConstants.UPDATE_CONTAINER_SPECIFICATION);
			preparedStatement.setString(1, containerSpec.getConSpecName());
			preparedStatement.setInt(2, containerSpec.getCpu());
			preparedStatement.setInt(3, containerSpec.getMemory());
			preparedStatement.setInt(4, containerSpec.getCostPerHour());
			preparedStatement.setInt(5, containerSpecId);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			preparedStatement = dbConnection.prepareStatement(SQLQueryConstants.GET_CONTAINER_SPECIFICATION_BY_ID);
			preparedStatement.setInt(1, containerSpecId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				containerSpec = getContainerSpecifications(rs);
			}
		} catch (SQLException e) {
			String msg =
					"Error while Updating the Container Specifications with ID " + containerSpecId;
			log.error(msg, e);
			throw e;
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(preparedStatement);
			DBUtil.closeDatabaseConnection(dbConnection);
		}
		return containerSpec;
	}

	/**
	 * Method to get container specification given the result set.
	 *
	 * @param rs result set
	 * @return container specification
	 * @throws SQLException
	 */
	private ContainerSpecifications getContainerSpecifications(ResultSet rs) throws SQLException {
		ContainerSpecifications containerSpec = new ContainerSpecifications();
		containerSpec.setId(rs.getInt(SQLQueryConstants.CON_SPEC_ID));
		containerSpec.setConSpecName(rs.getString(SQLQueryConstants.CON_SPEC_NAME));
		containerSpec.setCpu(rs.getInt(SQLQueryConstants.CPU));
		containerSpec.setMemory(rs.getInt(SQLQueryConstants.MEMORY));
		containerSpec.setCostPerHour(rs.getInt(SQLQueryConstants.COST_PER_HOUR));
		return containerSpec;
	}
}
