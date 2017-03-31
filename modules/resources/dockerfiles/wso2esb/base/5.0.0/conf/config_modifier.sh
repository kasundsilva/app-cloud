#!/bin/bash


# Get configurations
allConfigs=($(printenv | awk '$1 ~ /CONFIG_PROPERTIES_/ {print $1}'));

# Constant
fileType_Properties="CONFIG_PROPERTIES_";
properties_ext=".properties";

# Do Propertie file modifications
function do_properties_file_modify {

 IFS='=' read -r -a tmp <<<$1;
 # Removing special characters
 local pattern=$(echo "$(echo "${tmp[0]}" | sed -e 's/\//\\\//g')" | sed -e 's/\$/\\$/');
 local replace=$(echo "$(echo "${tmp[1]}" | sed -e 's/\//\\\//g')" | sed -e 's/\$/\\$/');

 sed -i "/$pattern=/c$pattern=$replace" $2;
}

# TODO
function do_xml_file_modify {

 # Separate the  pattern and replacing value
 IFS='*' read -r -a tmp_xml <<<$1;

 # Replace / and $
 local pattern=$(echo "$(echo "${tmp_xml[0]}" | sed -e 's/\//\\\//g')" | sed -e 's/\$/\\$/');
 local replace=$(echo "$(echo "${tmp_xml[1]}" | sed -e 's/\//\\\//g')" | sed -e 's/\$/\\$/');

 IFS='%' read -r -a tmp_pattern <<< $pattern;
 IFS='%' read -r -a tmp_replace <<< $replace;

 local startLine=$(sed -n -e "/${tmp_pattern[0]}/ =" "$file.$fileType");
 local endLine=$(($startLine+${#tmp_pattern[@]}i-1));

 local replace_index=0;
 for var in "${tmp_pattern[@]}"
  do
     local replace_word=${tmp_replace[$replace_index]};
     sed -i -e "$startLine,$endLine s/$var/$replace_word/" "$file.$fileType";
     replace_index=$((replace_index+1));
  done

}

function do_file_modify {
 # Read the argument in to an array
 IFS='=' read -r -a config <<<$1;

 # Get the ENV name
 envName=${config[0]};

 local fileType=${envName:0:18};
 local file=${envName:18};

 changes=$( printenv $envName);


 # decode user input
 allModificationsNotSeparated=$(echo -n $changes | base64 -d);
 # Get each modifications
 IFS='|' read -r -a allModificationsSeparated <<<$allModificationsNotSeparated;

 for var in "${allModificationsSeparated[@]}"
   do

    if [ "${fileType}" = "$fileType_Properties" ]; then
	do_properties_file_modify $var "$file$properties_ext";
   fi
   done
}

# Iterate through each Env
for var in "${allConfigs[@]}"
 do
  do_file_modify $var;
 done


