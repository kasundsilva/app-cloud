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

package org.wso2.appcloud.tierapi.server.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.appcloud.tierapi.bean.ContainerSpecifications;
import org.wso2.appcloud.tierapi.dao.impl.ContainerSpecDaoImpl;
import org.wso2.appcloud.tierapi.delegate.DAOdelegate;
import org.wso2.appcloud.tierapi.server.ContainerSpecService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

/**
 * Implementation of {@link ContainerSpecService}.
 */

@Path("/containerSpecs")
public class ContainerSpecServiceImpl {

    private ContainerSpecDaoImpl ContainerSpecInstance = (ContainerSpecDaoImpl) DAOdelegate.getContainerSpecInstance();
    private static final Log log = LogFactory.getLog(ContainerSpecServiceImpl.class);

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getContainerSpecifications() {

        try {
            List<ContainerSpecifications> containerSpecificationsList = ContainerSpecInstance.getAllContainerSpecs();
            GenericEntity<List<ContainerSpecifications>> entity = new
                    GenericEntity<List<ContainerSpecifications>>(containerSpecificationsList) {};
            return Response.ok().entity(entity).type(MediaType.APPLICATION_JSON_TYPE).build();
        } catch (SQLException e) {
            String msg = "Error while getting container specifications list";
            log.error(msg, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).
                    type(MediaType.APPLICATION_JSON_TYPE).build();
        }

    }

    @GET
    @Path("/{containerSpecId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getContainerSpecification(@PathParam("containerSpecId") int containerSpecId) throws SQLException {

        try {
            ContainerSpecifications containerSpecifications = ContainerSpecInstance.
                    getContainerSpecById(containerSpecId);
            return Response.ok().entity(containerSpecifications).type(MediaType.APPLICATION_JSON_TYPE).build();
        } catch (SQLException e) {
            String msg = "Error while getting conatiner specifications for specification ID: " + containerSpecId;
            log.error(msg, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).
                    type(MediaType.APPLICATION_JSON_TYPE).build();
        }
    }

    @GET
    @Path("allowedruntime/{runTimeId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getContainerSpecificationbyRuntimeId(@PathParam("runTimeId") int runtimeId) throws SQLException {

        try {
            List<ContainerSpecifications> containerSpecificationsList = ContainerSpecInstance.
                    getContainerSpecByRuntimeID(runtimeId);
            GenericEntity<List<ContainerSpecifications>> entity = new
                    GenericEntity<List<ContainerSpecifications>>(containerSpecificationsList) {};
            return Response.ok().entity(entity).type(MediaType.APPLICATION_JSON_TYPE).build();
        } catch (SQLException e) {
            String msg = "Error while getting container specifications list for runtime ID: " +runtimeId;
            log.error(msg, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).
                    type(MediaType.APPLICATION_JSON_TYPE).build();
        }
    }

}
