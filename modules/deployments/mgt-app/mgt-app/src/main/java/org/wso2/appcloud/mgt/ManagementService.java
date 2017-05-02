/*
 * Copyright (c) 2017, WSO2 Inc. (http://wso2.com) All Rights Reserved.
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

package org.wso2.appcloud.mgt;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

@Path("/")
public class ManagementService {
    private static Log log = LogFactory.getLog(ManagementService.class);

    @GET
    @Path("/createSource/{tenantDomain}/{appType}/{sourceDir}/{sample}")
    public boolean createSource(
            @PathParam("tenantDomain") String tenantDomain,
            @PathParam("appType") String appType,
            @PathParam("sourceDir") String sourceDir,
            @PathParam("sample") String sample) throws IOException {

        String sourceLocation = System.getenv(Constants.SOURCE_LOCATION) + "/" + tenantDomain + "/" + appType + "/" + sourceDir;
        File file = new File(sourceLocation);

        if (file.mkdirs()) {
            log.info("source location created : " + sourceDir);

            // copy source structure
            File source = new File(System.getenv(Constants.SAMPLE_LOCATION) + "/" + appType + "/" + sample);
            File dest = new File(sourceLocation);
            FileUtils.copyDirectory(source, dest);
            log.info("sample copied");
        }

        return true;
    }

    @GET
    @Path("/downloadArtifact/{tenantDomain}/{appType}/{sourceDir}/{fileType}")
    @Produces("text/plain")
    public Response getArtifact(
            @PathParam("tenantDomain") String tenantDomain,
            @PathParam("appType") String appType,
            @PathParam("sourceDir") String sourceDir,
            @PathParam("fileType") String fileType) {

        String dirPath = System.getenv(Constants.SOURCE_LOCATION) + "/" + tenantDomain + "/" + appType + "/" + sourceDir;
        File dir = new File(dirPath);

        File[] fileList = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(fileType);
            }
        });

        for(File file : fileList) {
            Response.ResponseBuilder response = Response.ok((Object) file);
            response.header("Content-Disposition","attachment; filename=\"" +file.getName()+ "\"");
            return response.build();
        }

        return null;
    }

    @GET
    @Path("/listSourceDirs/{tenantDomain}/{appType}")
    public String[] getBuiltArtifact(
            @PathParam("tenantDomain") String tenantDomain,
            @PathParam("appType") String appType) {

        File file = new File(System.getenv(Constants.SOURCE_LOCATION) + "/" + tenantDomain + "/" + appType);
        return file.list();
    }

}
