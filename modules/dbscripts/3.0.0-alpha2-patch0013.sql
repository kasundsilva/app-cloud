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

----------------------------------------------------
-- remove repo_url column
----------------------------------------------------
ALTER TABLE `AC_RUNTIME` DROP COLUMN `repo_url`;

----------------------------------------------------
-- update AC_RUNTIME table description
----------------------------------------------------
UPDATE `AC_RUNTIME` SET `description` = 'OS:alpine-java, Oracle JDK:8u102' WHERE `id`= 1;
UPDATE `AC_RUNTIME` SET `description` = 'OS:alpine-java, Oracle JDK:8u102' WHERE `id`= 2;
UPDATE `AC_RUNTIME` SET `description` = 'OS:alpine-java, Oracle JDK:8u102' WHERE `id`= 4;
UPDATE `AC_RUNTIME` SET `description` = 'OS:alpine-java, Oracle JDK:8u102' WHERE `id`= 5;
UPDATE `AC_RUNTIME` SET `description` = 'OS:alpine-java, Oracle JDK:8u102' WHERE `id`= 6;
UPDATE `AC_RUNTIME` SET `description` = 'OS:alpine-java, Oracle JDK:8u102' WHERE `id`= 7;
UPDATE `AC_RUNTIME` SET `description` = 'OS:alpine-java, Oracle JDK:8u102' WHERE `id`= 8;
UPDATE `AC_RUNTIME` SET `description` = 'OS:alpine-java, Oracle JDK:8u102' WHERE `id`= 10;


