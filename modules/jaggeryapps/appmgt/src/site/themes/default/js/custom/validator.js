//Regex patterns
var ALPHA_NUMERIC_PLUS_UNDERSCORE_REGEX = "^[A-Za-z0-9_]+$";
var ALPHA_NUMERIC_PLUS_UNDERSCORE_REGEX_NOT_STARTING_WITH_NUMBER = "^[A-Za-z_][A-Za-z0-9_]+$";
var VERSION_REGEX = "^[A-Za-z0-9_.-]+$";

//Environment key validation
function validateEnvKey(envKey){
    var envKeyRegex = new RegExp(ALPHA_NUMERIC_PLUS_UNDERSCORE_REGEX_NOT_STARTING_WITH_NUMBER);
    var validator;
    if (!envKeyRegex.test(envKey)) {
        validator = {
            status: false,
            msg: "Only alphanumeric characters and underscore are allowed for Environmental Variable key. And cannot start with a number."
        }
    } else {
        validator = {
            status: true,
            msg: "Environment variable key is successfully added."
        }
    }
    return validator;
}

function validateDbName(databaseName) {
    var databaseNameRegex = new RegExp(ALPHA_NUMERIC_PLUS_UNDERSCORE_REGEX);
    var validator;
    if (!databaseNameRegex.test(databaseName)) {
        validator = {
            status: false,
            msg: "Only alphanumeric characters and underscore are allowed."
        }
    } else {
        validator = {
            status: true,
            msg: "Database name is successfully added."
        }
    }
    return validator;
}

//Validated the application version provided by user
function validateApplicationVersion(version){
    var versionRegex = new RegExp(VERSION_REGEX);
    var validator;
    if (!versionRegex.test(version)) {
        validator = {
            status: false,
            msg: "Invalid characters found for application version. Valid char set [a-z, A-Z, 0-9, -, _, .]"
        }
    } else {
        validator = {
            status: true,
            msg: "Version validation is successful."
        }
    }
    return validator;
}