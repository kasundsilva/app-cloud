    create namespace
    kubectl create -f kube-heapster/kube-heapster.yaml

    Before create images

    create grafana rc
    set correct image name, GRAFANA_PASSWD, GF_SECURITY_ADMIN_PASSWORD
    kubectl create -f grafana-controller.yaml

    create grafana service
    set correct serviceloadbalancer/lb.host
    kubectl create -f grafana-service.yaml

    create heapster rc and service
    kubectl create -f heapster-controller.yaml
    kubectl create -f heapster-service.yaml

    create influxdb rc
    kubectl create -f influxdb-controller.yaml

    create influxdb service
    set correct serviceloadbalancer/lb.host
    kubectl create -f influxdb-service.yaml
