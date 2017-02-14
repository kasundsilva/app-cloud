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

-- --------------------------------------------------------
-- Manual scaling feature db changes
-- --------------------------------------------------------

ALTER TABLE AC_SUBSCRIPTION_PLANS ADD MAX_REPLICA_COUNT INT(11) NOT NULL;

UPDATE AC_SUBSCRIPTION_PLANS SET MAX_REPLICA_COUNT=2 WHERE PLAN_NAME='free';

UPDATE AC_SUBSCRIPTION_PLANS SET MAX_REPLICA_COUNT=6 WHERE PLAN_NAME='paid';

ALTER TABLE AC_WHITE_LISTED_TENANTS ADD max_replica_count INT(11) NOT NULL DEFAULT 6;

UPDATE AC_WHITE_LISTED_TENANTS SET max_replica_count=6;
