--
--  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
--
--    WSO2 Inc. licenses this file to you under the Apache License,
--    Version 2.0 (the "License"); you may not use this file except
--    in compliance with the License.
--    You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
--    Unless required by applicable law or agreed to in writing,
--    software distributed under the License is distributed on an
--    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
--    KIND, either express or implied.  See the License for the
--    specific language governing permissions and limitations
--    under the License.
--

-- -----------------
-- creating custom docker images table
-- ------------------------------------
CREATE TABLE IF NOT EXISTS AC_CUSTOM_DOCKER_IMAGES (
  `image_id` VARCHAR(255) NOT NULL,
  `tenant_id` INT NOT NULL,
  `remote_url` VARCHAR(255) NULL,
  `test_results_json` VARCHAR(500) NULL,
  `status` VARCHAR(10) NULL,
  `last_updated` DATETIME NULL,
  PRIMARY KEY (`image_id`))
ENGINE = InnoDB;


-- -----------------------------
-- changing transports
-- -----------------------------


-- changing existing runtime name
UPDATE AC_RUNTIME set name='Custom Docker http-9443 https-9763' where id=11;
-- add new runtime
INSERT INTO AC_RUNTIME (name,image_name,tag,description) VALUES ('Custom Docker http-8080 https-8443','custom','customtag','OS:Custom, JAVA Version:custom');

-- link apptype with new runtime
INSERT INTO AC_APP_TYPE_RUNTIME VALUES(7,18);

-- link runtimes and transports
DELETE FROM AC_RUNTIME_TRANSPORT WHERE runtime_id=11;
INSERT INTO AC_RUNTIME_TRANSPORT VALUES (5,11),(6,11),(3,18),(4,18);


-- setting runtime container spec new runtime
INSERT INTO AC_RUNTIME_CONTAINER_SPECIFICATIONS VALUES (18,4);




