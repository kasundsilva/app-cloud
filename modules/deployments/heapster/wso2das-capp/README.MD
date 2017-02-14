    create car file and deploy in DAS
    zip -r org.wso2.appcloud.operational.dashboard-1.0.0.car *


    add the following json to <DAS_HOME>/repository/deployment/server/jaggeryapps/portal/configs/designer.json with correct values

     "operational_dashboard": {
        "influxdb_url": "http://influxdb.wso2localapps.com",
        "influxdb_db": "k8s"
     }


