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

package org.wso2.appcloud.tierapi.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Service class defines operations related to Container Specifications related services.
 */
public interface ContainerSpecService {

    /**
     * Get all Container Specifications.
     * @return {@link Response}
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getContainerSpecifications();

    /**
     * Get Plan using Container Specifications ID.
     * @param containerSpecId            Container Specifications ID of the Container Specification
     * @return {@link Response}
     */
    @GET
    @Path("/{containerSpecId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getContainerSpecification(@PathParam("containerSpecId") int containerSpecId);

    /**
     * Get Container Specifications using Runtime ID.
     * @param runtimeId         runTimeId ID
     * @return {@link Response}
     */
    @GET
    @Path("allowedruntime/{runTimeId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getContainerSpecificationbyRuntimeId(@PathParam("runTimeId") int runtimeId);

}
