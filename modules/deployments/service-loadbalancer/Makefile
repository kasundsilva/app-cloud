all: push

# 0.0 shouldn't clobber any released builds
TAG = 0.4-0.7.2

PREFIX = registry.182.docker.appfactory.private.wso2.com:5000/haproxy-dev
HAPROXY_IMAGE = haproxy-dev

server: service_loadbalancer.go
	CGO_ENABLED=0 GOOS=linux godep go build -a -installsuffix cgo -ldflags '-w' -o service_loadbalancer ./registry_manager.go ./service_loadbalancer.go ./loadbalancer_log.go

container: server
	docker build -t $(PREFIX):$(TAG) .

push: container
	docker push $(PREFIX):$(TAG)

clean:
	# remove servicelb and contrib-haproxy images
	docker rmi -f $(HAPROXY_IMAGE):$(TAG) || true
	docker rmi -f $(PREFIX):$(TAG) || true
	#rm -f service_loadbalancer

