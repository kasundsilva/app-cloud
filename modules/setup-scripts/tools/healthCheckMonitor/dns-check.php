<?php
/*
* Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

$time_start = microtime();

// DNS health check
$ipWso2 = gethostbyname('wso2.com');
$ipIsportal = gethostbyname('isportal.wso2.com');
$ipSupport = gethostbyname('support.wso2.com');
$ipSupporttools = gethostbyname('supporttools.wso2.com');

$time_end = microtime();

$timeInterval = $time_end - $time_start;

if ($ipWso2 == 'wso2.com') {
	http_response_code(500);
    die("DNS check failed for: " . $ipWso2);

} elseif ($ipIsportal == 'isportal.wso2.com') {
	http_response_code(500);
	die("DNS check failed for: " . $ipIsportal);

} elseif ($ipSupport == 'support.wso2.com') {
   	http_response_code(500);
    die("DNS check failed for: " . $ipSupport);

} elseif ($ipSupporttools == 'supporttools.wso2.com') {
   	http_response_code(500);
    die("DNS check failed for: " . $ipIsportal);
}

$array = array(
        "ip-wso2" => $ipWso2,
        "ip-isportal" => $ipIsportal,
        "ip-support" => $ipSupport,
        "ip-supporttools" => $ipSupporttools,
        "time" => $timeInterval . 'ms'
);

$json = json_encode($array);

// set the json header content-type
header('Content-Type: application/json');
echo $json;
?>