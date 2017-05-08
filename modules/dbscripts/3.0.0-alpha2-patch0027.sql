--
--  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

-- ---------------------------------------------
-- Ballerina on cloud db changes
-- ---------------------------------------------

-- Adding source location field to version table
ALTER TABLE AC_VERSION ADD COLUMN source_location varchar(60) DEFAULT NULL;

-- Adding ballerin-composer app type

INSERT INTO `AC_APP_TYPE` (`id`, `name`, `description`) VALUES
(10, 'ballerina-composer', 'Allows you to open a Ballerina Composer');

INSERT INTO `AC_CLOUD_APP_TYPE` (`cloud_id`, `app_type_id`) VALUES
('integration_cloud', 10);

INSERT INTO `AC_RUNTIME` (`id`, `name`, `image_name`, `tag`, `description`) VALUES
(21, 'Ballrina Composer - 0.86', 'ballerina-composer', '0.86', 'OS:Alpine, Java Version: Oracle JDK 1.8.0_112');

INSERT INTO `AC_APP_TYPE_RUNTIME` (`app_type_id`, `runtime_id`) VALUES
(10, 21);

INSERT INTO `AC_RUNTIME_CONTAINER_SPECIFICATIONS` (`id`, `CON_SPEC_ID`) VALUES
(21, 1);

INSERT INTO AC_TRANSPORT (`id`, `name`, `port`, `protocol`, `service_prefix`, `description`) VALUES
(11, "http", 9091, "TCP", "bcr", "Ballerina composer - runtime Protocol"),
(12, "http", 8289, "TCP", "bcb", "Ballerina composer - backend protocol"),
(13, "ws", 5056, "TCP", "bcd", "Ballerina composer - debug protocol");

INSERT INTO AC_RUNTIME_TRANSPORT (`transport_id`, `runtime_id`) VALUES
(9, 21),
(10, 21),
(11, 21),
(12, 21);
(13, 21);

