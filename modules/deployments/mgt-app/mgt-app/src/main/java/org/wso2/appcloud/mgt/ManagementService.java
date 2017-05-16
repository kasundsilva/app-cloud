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
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        log.info("Source location : " + sourceLocation);

        try {
            boolean isFolderCreated = file.mkdirs();
            log.info("isFolderCreated : " + isFolderCreated);
            if (isFolderCreated) {
                log.info("Source location created: " + sourceDir);

                // copy source structure
                File source = new File(System.getenv(Constants.SAMPLE_LOCATION) + "/" + appType + "/" + sample);
                log.info("sample location : " + System.getenv(Constants.SAMPLE_LOCATION) + "/" + appType + "/" + sample);
                File dest = new File(sourceLocation);
                FileUtils.copyDirectory(source, dest);
                log.info("Sample copied for: " + sourceDir);
            }

            //Adding package name for initial sample.
            if(file.exists()) {
                String dirPath =
                        System.getenv(Constants.SOURCE_LOCATION) + "/" + tenantDomain + "/" + appType + "/" + sourceDir;
                String pkgName = "org." + tenantDomain + "." + appType + "." + sourceDir;
                List<String> newLines = new ArrayList<>();
                for (String line : Files
                        .readAllLines(Paths.get(dirPath + "/" + sample +".bal"), StandardCharsets.UTF_8)) {
                    if (line.contains("_pkgName")) {
                        log.info("Adding package to the source file: " + pkgName);
                        newLines.add(line.replace("_pkgName", pkgName));
                    } else {
                        newLines.add(line);
                    }
                }
                Files.write(Paths.get(dirPath + "/" + sample + ".bal"), newLines, StandardCharsets.UTF_8);
            }
        } catch (IOException ex) {
            log.error(ex);
            return false;
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

    @GET
    @Path("/buildService/{tenantDomain}/{appType}/{sourceDir}")
    @Produces("text/plain")
    public boolean buildService(
            @PathParam("tenantDomain") String tenantDomain,
            @PathParam("appType") String appType,
            @PathParam("sourceDir") String sourceDir) {

        String dirPath = System.getenv(Constants.SOURCE_LOCATION) + "/" + tenantDomain + "/" + appType + "/" + sourceDir;

        String cleanCommand = "rm -rf " + dirPath + "/target/";
        String createTargetCommand = "mkdir " + dirPath + "/target";
        String ballerinaRuntime = System.getenv(Constants.BALLERINA_HOME) + "/" + System.getenv(Constants.BALLERINA_RUNTIME) + "/" + "bin/ballerina";

        String ballerinaBuildCommand = "build service " + dirPath +  "/ -o " + dirPath + "/target/" + sourceDir;
        String command = ballerinaRuntime + " " + ballerinaBuildCommand;

        log.info("-------------------------------------");
        log.info("BUILDING: " + sourceDir);
        log.info("-------------------------------------");
        Process p;
        try {
            log.info("Executing: " + cleanCommand);
            p = Runtime.getRuntime().exec(cleanCommand);
            p.waitFor();
            log.info("Executing: " + createTargetCommand);
            p = Runtime.getRuntime().exec(createTargetCommand);
            p.waitFor();
            log.info("Executing: " + command);
            p = Runtime.getRuntime().exec(command);
            p.waitFor();

            File f = new File(dirPath + "/target/" + sourceDir + ".bsz");
            if(f.exists() && !f.isDirectory()) {
                log.info("-------------------------------------");
                log.info("BUILD SUCCESS: " + sourceDir);
                log.info("-------------------------------------");
                return true;
            } else {
                log.info("-------------------------------------");
                log.info("BUILD FAILED: " + sourceDir);
                log.info("-------------------------------------");
                return false;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
