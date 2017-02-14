This README contains instructions to get wso2das based logging solution for kubernetes.

Steps
1.  Build docker image and push the image to internal docker registry which is configured with Kubernetes.
    Prerequisites: 
        Copy certificate files required to communicate with Kubernetes API server into 'wso2das-and-fluentd/certs' directory
        Replace below placeholders with actual values in fluent.conf file.
            <K8S_API_SERVER_URL>
            <WSO2DAS_SERVER_IP_AND_PORT>
            <WSO2DAS_DATA_PUBLISHER_USER>
            <WSO2DAS_DATA_PUBLISHER_USER_PASSWORD>
         
    docker build --no-cache -t registry.cloudstaging.wso2.com:5000/wso2das-fluentd:1.0.0 .
    docker push registry.cloudstaging.wso2.com:5000/wso2das-fluentd:1.0.0

2.  Create a daemon set for above built image
    Prerequisites:
        Create a namespace called 'appcloud-extensions' if not exists already. (./kubectl create ns appcloud-extensions)
        Update image name in wso2das-fluentd.yaml file if image name is different.
    Run command './kubectl create -f wso2das-fluentd.yaml'
    
3.  Zip capp content as 'app_cloud_container_logs_capp.car' and deploy that file into wso2das.
    