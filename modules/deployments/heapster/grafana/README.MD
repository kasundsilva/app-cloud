REGISTRY=registry.local.wso2.com:5000
IMAGE=wso2_appcloud_heapster_grafana
TAG=v2.6.0-2-wso2v1

docker build -t $REGISTRY/$IMAGE:$TAG .

docker push $REGISTRY/$IMAGE:$TAG

