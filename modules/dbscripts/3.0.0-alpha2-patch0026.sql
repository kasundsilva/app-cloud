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

-- added 2GB container spec for DSS
insert into AC_RUNTIME_CONTAINER_SPECIFICATIONS values(12 , 5);

-- update the incorrect runtime name
update AC_RUNTIME set name = 'Custom Docker http-9763 https-9443' where id =11;

-- changes needed to support the nodejs runtime
INSERT INTO AC_APP_TYPE ( id ,  name ,  description ) VALUES (9, 'nodejs', 'Allows you to deploy Node.Js applications');

INSERT INTO  AC_RUNTIME  ( id ,  name ,  image_name ,  tag ,  description ) VALUES (20, 'Node.JS 7.7.1 (Alpine 3.4/Node.JS 7.7.1)', 'nodejs', '7.7.1', 'OS:Alpine 3.4, Node.JS 7.7.1');

INSERT INTO  AC_APP_TYPE_RUNTIME  ( app_type_id ,  runtime_id ) VALUES (9, 20);

INSERT INTO  AC_RUNTIME_TRANSPORT  ( transport_id ,  runtime_id ) VALUES (3, 20);

INSERT INTO  AC_RUNTIME_TRANSPORT  ( transport_id ,  runtime_id ) VALUES (4, 20);

INSERT INTO  AC_RUNTIME_CONTAINER_SPECIFICATIONS  ( id ,  CON_SPEC_ID ) VALUES (20, 3);

INSERT INTO  AC_CLOUD_APP_TYPE  ( cloud_id ,  app_type_id ) VALUES ('integration_cloud', 9);

--other db changes identified

INSERT INTO  AC_RUNTIME_CONTAINER_SPECIFICATIONS  ( id ,  CON_SPEC_ID ) VALUES (19, 5);

INSERT INTO  AC_RUNTIME_CONTAINER_SPECIFICATIONS  ( id ,  CON_SPEC_ID ) VALUES (19, 7);


