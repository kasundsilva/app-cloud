#!/bin/bash

data=`cat k8s_migrate`
source admin_service.cfg
LOGFILE=k8s_migrate.log

echo "----------Login to admin service----------" >> $LOGFILE
curl -c cookies -v -X POST -k $SERVICE_URL/appmgt/site/blocks/user/login/ajax/login.jag -d "action=login&userName=$ADMIN_USERNAME&password=$ADMIN_PASSWORD" >> $LOGFILE 2>&1
echo -e "\n" >> $LOGFILE

(echo $data | jq '.[][] | .appName + "," + .version + "," + .tenantDomain + "," + .versionHashId + "," + .appType') > tmp_out

cat tmp_out | while read appx
do
	app=$(echo $appx | sed 's/"//g')
        name=$(echo $app | cut -d "," -f 1)
        version=$(echo $app | cut -d "," -f 2)
        tenant=$(echo $app | cut -d "," -f 3)
	hashId=$(echo $app | cut -d "," -f 4)
	apptype=$(echo $app | cut -d "," -f 5)

 	echo "----------Redeploying application : " $tenant $name $version $hashId $apptype >> $LOGFILE	
	curl -b cookies -v -X POST -k $SERVICE_URL/appmgt/site/blocks/admin/admin.jag -d "action=redeployApplicationVersion&tenantDomain=$tenant&applicationName=$name&applicationVersionHashId=$hashId&applicationVersion=$version&applicationType=$apptype" >> $LOGFILE 2>&1
	echo -e "\n" >> $LOGFILE
        echo "done"
done
