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

-- -------------------------------------------------
-- Alter AC_CONTAINER table to increase version field
-----------------------------------------------------------
ALTER TABLE AC_CONTAINER MODIFY version VARCHAR(100);

-- --------------------------------------------------------
-- Update and Insert to the AC_RUNTIMES
-- --------------------------------------------------------
UPDATE `AC_RUNTIME` SET `name` = 'Apache Tomcat 8.5.5 (Alpine 3.4/Oracle JDK 1.8.0_112)', `tag` = '8.5.5-alpine3.4-oracle-jdk1.8.0', `description` = 'OS:Alpine 3.4, Oracle JDK 1.8.0_112' WHERE `id`= 13;
INSERT INTO AC_RUNTIME (name, image_name , tag, description ) VALUES ('Apache Tomcat 8.5.5 (Ubuntu 16.04/Oracle JDK 1.8.0_112)', 'tomcat', '8.5.5-ubuntu16.04-oracle-jdk1.8.0', 'OS:Ubuntu 16.04, Oracle JDK 1.8.0_112');
INSERT INTO AC_RUNTIME (name, image_name , tag, description ) VALUES ('Apache Tomcat 8.5.5 (Alpine 3.4/Open JDK 1.8.0_92)', 'tomcat', '8.5.5-alpine3.4-open-jdk1.8.0', 'OS:Alpine 3.4, Open JDK 1.8.0_92');
INSERT INTO AC_RUNTIME (name, image_name , tag, description ) VALUES ('Apache Tomcat 8.5.5 (Ubuntu 16.04/Open JDK 1.8.0_91)', 'tomcat', '8.5.5-ubuntu16.04-open-jdk1.8.0', 'OS:Ubuntu 16.04, Open JDK 1.8.0_91');

UPDATE `AC_RUNTIME` SET `name` = 'Apache Tomcat 8.0.28 / WSO2 Application Server 6.0.0-M1 - Deprecated' WHERE `id`= 1;
UPDATE `AC_RUNTIME` SET `name` = 'OracleJDK 8 + WSO2 MSF4J 1.0.0 - Deprecated' WHERE `id`= 2;
UPDATE `AC_RUNTIME` SET `name` = 'Apache Tomcat 8.0.28 / WSO2 Application Server 6.0.0-M2 - Deprecated' WHERE `id`= 6;
UPDATE `AC_RUNTIME` SET `name` = 'Apache Tomcat 8.0.36 / WSO2 Application Server 6.0.0-M3 - Deprecated' WHERE `id`= 10;


-- -----------------------------------------------------------
-- Updates to the AC_APP_TYPE_RUNTIME
-- -----------------------------------------------------------
INSERT INTO AC_APP_TYPE_RUNTIME VALUES (1, 14);
INSERT INTO AC_APP_TYPE_RUNTIME VALUES (1, 15);
INSERT INTO AC_APP_TYPE_RUNTIME VALUES (1, 16);


-- -----------------------------------------------------------
-- Updates to the AC_RUNTIME_CONTAINER_SPECIFICATIONS
-- -----------------------------------------------------------
INSERT INTO AC_RUNTIME_CONTAINER_SPECIFICATIONS VALUES (14, 3);
INSERT INTO AC_RUNTIME_CONTAINER_SPECIFICATIONS VALUES (14, 4);
INSERT INTO AC_RUNTIME_CONTAINER_SPECIFICATIONS VALUES (15, 3);
INSERT INTO AC_RUNTIME_CONTAINER_SPECIFICATIONS VALUES (15, 4);
INSERT INTO AC_RUNTIME_CONTAINER_SPECIFICATIONS VALUES (16, 3);
INSERT INTO AC_RUNTIME_CONTAINER_SPECIFICATIONS VALUES (16, 4);


-- -----------------------------------------------------------
-- Updates to the AC_RUNTIME_TRANSPORT
-- -----------------------------------------------------------
INSERT INTO AC_RUNTIME_TRANSPORT VALUES (3,14);
INSERT INTO AC_RUNTIME_TRANSPORT VALUES (4,14);
INSERT INTO AC_RUNTIME_TRANSPORT VALUES (3,15);
INSERT INTO AC_RUNTIME_TRANSPORT VALUES (3,16);
INSERT INTO AC_RUNTIME_TRANSPORT VALUES (4,15);
INSERT INTO AC_RUNTIME_TRANSPORT VALUES (4,16);

-- -----------------------------------------------------------
-- Updates to the AC_APP_TYPE
-- -----------------------------------------------------------
UPDATE `AC_APP_TYPE` SET `description` = 'Allows you to deploy an ESB configuration that is supported by WSO2 Enterprise Service Bus' WHERE `id` = 6;
