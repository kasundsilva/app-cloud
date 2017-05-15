sed -i -e 's/api-path-url/http:\/\/'"$API_PATH_URL"'/g' $COMPOSER_HOME/$RUNTIME_NAME/resources/composer/services/workspace-service-config.yaml
sed -i -e 's/launcher-path-url/ws:\/\/'"$LAUNCHER_PATH_URL"'/g' $COMPOSER_HOME/$RUNTIME_NAME/resources/composer/services/workspace-service-config.yaml
sed -i -e 's/root-dir-path/ws:\/\/'"$ROOT_DIR_PATH"'/g' $COMPOSER_HOME/$RUNTIME_NAME/resources/composer/services/workspace-service-config.yaml

sh $COMPOSER_HOME/$RUNTIME_NAME/bin/composer