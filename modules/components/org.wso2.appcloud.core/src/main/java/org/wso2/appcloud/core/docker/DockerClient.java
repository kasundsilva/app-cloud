/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.appcloud.core.docker;

import com.google.common.base.Strings;
import io.fabric8.docker.client.Config;
import io.fabric8.docker.client.ConfigBuilder;
import io.fabric8.docker.client.DefaultDockerClient;
import io.fabric8.docker.dsl.EventListener;
import io.fabric8.docker.dsl.OutputHandle;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.appcloud.common.AppCloudException;
import org.wso2.appcloud.common.util.AppCloudUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;

/**
 * Java client for docker operations.
 */
public class DockerClient {

    private static Log log = LogFactory.getLog(DockerClient.class);

    io.fabric8.docker.client.DockerClient dockerClient;
    OutputHandle handle;

    final CountDownLatch buildDone = new CountDownLatch(1);
    final CountDownLatch pushDone = new CountDownLatch(1);
    final CountDownLatch pullDone = new CountDownLatch(1);

    final String TAG_LATEST = "latest";

    public DockerClient() {
        Config config = new ConfigBuilder()
                .withDockerUrl(DockerClientConstants.DEFAULT_DOCKER_URL)
                .build();
        dockerClient = new DefaultDockerClient(config);
    }

    public DockerClient(String uri) {
        Config config = new ConfigBuilder()
                .withDockerUrl(uri)
                .withConnectionTimeout(Integer.parseInt(AppCloudUtil.getPropertyValue(DockerClientConstants
                        .DOCKER_CONNECTION_TIMEOUT)))
                .withRequestTimeout(Integer.parseInt(AppCloudUtil.getPropertyValue(DockerClientConstants
                        .DOCKER_REQUEST_TIMEOUT)))
                .withImagePushTimeout(Integer.parseInt(AppCloudUtil.getPropertyValue(DockerClientConstants
                        .DOCKER_PUSH_TIMEOUT)))
                .withImageBuildTimeout(Integer.parseInt(AppCloudUtil.getPropertyValue(DockerClientConstants
                        .DOCKER_BUILLD_TIMEOUT)))
                .withImageSearchTimeout(Integer.parseInt(AppCloudUtil.getPropertyValue(DockerClientConstants
                        .DOCKER_SEARCH_TIMEOUT)))
                .build();
        dockerClient = new DefaultDockerClient(config);
    }

    /**
     * Create a docker file according to given details. This will get docker template file and replace the parameters
     * with the given customized values in the dockerFilePropertyMap
     * @param dockerFilePath
     * @param runtimeId - application runtime id
     * @param dockerTemplateFilePath
     * @param dockerFileCategory - app creation method eg : svn, url, default
     * @param dockerFilePropertyMap
     * @param customDockerFileProperties
     * @throws IOException
     * @throws AppCloudException
     */
    public void createDockerFile(String dockerFilePath, String runtimeId, String dockerTemplateFilePath,
            String dockerFileCategory, Map<String, String> dockerFilePropertyMap,
            Map<String, String> customDockerFileProperties) throws  AppCloudException {

        customDockerFileProperties.keySet().removeAll(dockerFilePropertyMap.keySet());
        dockerFilePropertyMap.putAll(customDockerFileProperties);

        // Get docker template file
        // A sample docker file can be found at
        // https://github.com/wso2/app-cloud/blob/master/modules/resources/dockerfiles/wso2as/default/Dockerfile.wso2as.6.0.0-m1
        String dockerFileTemplatePath = DockerUtil
                .getDockerFileTemplatePath(runtimeId, dockerTemplateFilePath, dockerFileCategory);
        List<String> dockerFileConfigs = new ArrayList<>();


        try {
            for (String line : FileUtils.readLines(new File(dockerFileTemplatePath))) {
                StringTokenizer stringTokenizer = new StringTokenizer(line);
                //Search if line contains keyword to replace with the value
                while (stringTokenizer.hasMoreElements()) {
                    String element = stringTokenizer.nextElement().toString().trim();
                    if (dockerFilePropertyMap.containsKey(element)) {
                        if (log.isDebugEnabled()) {
                            log.debug("Dockerfile placeholder : " + element);
                        }
                        String value = dockerFilePropertyMap.get(element);
                        line = line.replace(element, value);
                    }
                }
                dockerFileConfigs.add(line);
            }
        } catch (IOException e) {
            String msg = "Error occurred while reading docker template file " + dockerFileTemplatePath;
            throw new AppCloudException(msg,e);
        }
        try {
            FileUtils.writeLines(new File(dockerFilePath), dockerFileConfigs);
        } catch (IOException e) {
            String msg = "Error occurred while writing to docker file " + dockerFilePath;
            throw new AppCloudException(msg,e);
        }
    }

    /**
     * Build created docker images
     * @param repoUrl - docker registry url
     * @param imageName - application runtime name
     * @param tag - tag name
     * @param dockerFileUrl - absolute file upload path
     * @throws InterruptedException
     * @throws IOException
     * @throws AppCloudException
     */
    public void buildDockerImage(String repoUrl, String imageName, String tag, String dockerFileUrl)
            throws AppCloudException {

        String dockerImage = repoUrl + "/" + imageName + ":" + tag;
        final boolean[] dockerStatusCheck = new boolean[1];
	    dockerStatusCheck[0] = false; //this is to check docker build status, whether it was successful or failed
        try {
            handle = dockerClient.image().build()
                                 .withRepositoryName(dockerImage)
                                 .withNoCache()
                                 .usingListener(new EventListener() {
                                     @Override
                                     public void onSuccess(String message) {
                                         log.info("Build Success:" + message);
                                         buildDone.countDown();
                                         dockerStatusCheck[0] = true;
                                     }

                                     @Override
                                     public void onError(String message) {
                                         log.error("Build Failure:" + message);
                                         buildDone.countDown();
                                     }

                                     @Override
                                     public void onEvent(String event) {
                                         log.info(event);
                                     }
                                 })
                                 .fromFolder(dockerFileUrl);
            buildDone.await();
        } catch (InterruptedException e) {
            String msg = "Error occurred while building docker image " + imageName + " with tag : " + tag + " of " +
                         "docker file : " + dockerFileUrl;
            throw new AppCloudException(msg, e);
        } finally {
            try {
                handle.close();
            } catch (IOException e) {
                log.warn("Error occurred while closing output handle after building docker image " + imageName +
                         " with tag : " + tag + " of docker file : " + dockerFileUrl);

            }
        }

        if (!dockerStatusCheck[0]) {
            log.error("Docker image building failed: " + imageName + " repo: " + repoUrl + " docker file: "
                    + dockerFileUrl + " tag: " + tag);
            throw new AppCloudException(
                    "Docker image building failed: " + imageName + " repo: " + repoUrl + " docker file: "
                            + dockerFileUrl + " tag: " + tag);
        }
    }

    /**
     * Push docker images
     *
     * @param imageName - application runtime name
     * @param tag       - tag name
     * @throws InterruptedException
     * @throws IOException
     * @throws AppCloudException
     */
    public void pushDockerImage(String imageName, String tag)
            throws AppCloudException {

        final boolean[] dockerStatusCheck = new boolean[1];
        dockerStatusCheck[0] = false;
        try {
            handle = dockerClient.image().withName(imageName).push()
                                 .usingListener(new EventListener() {
                                     @Override
                                     public void onSuccess(String message) {
                                         log.info("Push Success:" + message);
                                         pushDone.countDown();
                                         dockerStatusCheck[0] = true;
                                     }

                                     @Override
                                     public void onError(String message) {
                                         log.error("Push Failure:" + message);
                                         pushDone.countDown();
                                     }

                                     @Override
                                     public void onEvent(String event) {
                                         log.info(event);
                                     }
                                 }).withTag(tag).toRegistry();
            pushDone.await();

        } catch (InterruptedException e) {

            String msg = "Error occurred while pushing docker image " + imageName + " with tag : " + tag + " to " +
                         "docker registry. ";
            log.error(e);
            throw new AppCloudException(msg, e);
        } finally {
            try {
                handle.close();
            } catch (IOException e) {
                log.warn("Error occurred while closing output handle after pushing docker image " + imageName +
                         " with tag : " + tag + " to docker registry. " );
            }
        }

        if (!dockerStatusCheck[0]) {
            log.error("Docker image push failed: " + imageName + " tag: " + tag);
            throw new AppCloudException(
                    "Docker image push failed: " + imageName  + " tag: " + tag);
        }
    }

    public void pullDockerImage(String imageRepoUrl, String imageTag) throws AppCloudException {

        if (log.isDebugEnabled()) {
            log.debug("Docker image pull triggered for repo : " + imageRepoUrl + " with tag : " + imageTag);
        }

        final boolean[] dockerStatusCheck = new boolean[1];
        dockerStatusCheck[0] = false;
        try {
            handle = dockerClient.image().withName(imageRepoUrl).pull()
                                 .usingListener(new EventListener() {
                                     @Override
                                     public void onSuccess(String message) {
                                         log.info("Pull Success:" + message);
                                         pullDone.countDown();
                                         dockerStatusCheck[0] = true;

                                     }

                                     @Override
                                     public void onError(String message) {
                                         log.error("Pull Failure:" + message);
                                         pullDone.countDown();
                                     }

                                     @Override
                                     public void onEvent(String event) {
                                         log.info(event);
                                     }
                                 }).withTag(imageTag).fromRegistry();
            pullDone.await();

        } catch (InterruptedException e) {

            String msg = "Error occurred while pulling docker image " + imageRepoUrl + " with tag : " + imageTag;
            throw new AppCloudException(msg, e);
        } finally {
            try {
                handle.close();
            } catch (IOException e) {
                log.warn("Error occurred while closing output handle after pulling docker image " + imageRepoUrl +
                         " with tag : " + imageTag);
            }
        }

        if (!dockerStatusCheck[0]) {
            log.error("Docker image pull failed : " + imageRepoUrl + " with tag : " + imageTag);
            throw new AppCloudException("Docker image pull failed: " + imageRepoUrl + " with tag : " + imageTag);
        }
    }

    public void tagDockerImage(String oldImage, String oldTag, String newImageName, String newTag)
            throws AppCloudException {

        if (log.isDebugEnabled()) {
            log.debug("Docker image tagging triggered for image : " + oldImage + " with old tag : " + oldTag + " to new " +
                      "tag : " + newImageName);
        }

        boolean dockerStatusCheck = dockerClient.image().withName(oldImage + ":" + oldTag).tag()
                                                .inRepository(newImageName).withTagName(newTag);
        if (!dockerStatusCheck) {
            log.error("Docker custom image tag failed: " + oldImage);
            throw new AppCloudException("Docker custom image tag failed: " + oldImage);
        }
    }


    public void clientClose() throws IOException {
        dockerClient.close();
    }
}
