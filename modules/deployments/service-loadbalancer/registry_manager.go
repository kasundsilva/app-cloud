/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 */
package main

import (
	"bytes"
	"crypto/aes"
	"crypto/cipher"
	"crypto/tls"
	b64 "encoding/base64"
	"github.com/golang/glog"
	"io/ioutil"
	"net/http"
	"os"
	"io"
	"unicode/utf8"
	"errors"
	"time"
)

const (
	registryPath             = "resource/1.0.0/artifact/_system/governance/customurl/"
	cloudType                = "app-cloud/"
	securityCertificates     = "/securityCertificates/"
	pemFileExtension         = ".pem"
	keyFileExtension         = ".key"
	pubFileExtension         = ".pub"
	ivFileExtension          = ".iv"
	authorizationHeader      = "Authorization"
	authorizationHeaderType  = "Basic "
	hypenSeparator           = "-"
	getHTTPMethod            = "GET"
	postHTTPMethod           = "POST"
	forwardSlashSeparator    = "/"
	applicationLaunchBaseUrl = ".wso2apps.com"
	defaultPemFile		 = "ssl.pem"
	errorFileName		 = "error"
	cloudmgtAuthenticateEP	 = "cloudmgt/site/blocks/user/authenticate/ajax/login.jag"
	cloudmgtSendMailEP	 = "cloudmgt/site/blocks/tenant/users/add/ajax/add.jag"
	parameterActionKey	 = "action"
	loginAction		 = "login"
	sendEmailAction		 = "sendEmailWithCustomMessage"
	parameterUsernameKey	 = "userName"
	parameterPasswordKey	 = "password"
	parameterToKey		 = "to"
	parameterSubjectKey	 = "subject"
	parameterMessageKey	 = "message"
	headerCookieKey		 = "Cookie"
	headerSetCookieKey	 = "Set-Cookie"
	parameterSubjectValue	 = "HAProxy Error"

)

/*
 Method to perform HTTP request
 */
func doHTTPRequest(httpMethod, url string, headersMap, paramsMap map[string]string) (*http.Response, error) {
	tr := &http.Transport{
		TLSClientConfig: &tls.Config{InsecureSkipVerify: true},
	}
	client := &http.Client{
		Transport: tr,
		Timeout: time.Duration(*cloudmgtRestAPIRequestTimeout)*time.Second,
	}
	request, _ := http.NewRequest(httpMethod, url, nil)
	//Set Params
	query := request.URL.Query()
	for key, value := range paramsMap {
		query.Add(key, value)
	}
	request.URL.RawQuery = query.Encode()
	//Set Headers
	for key, value := range headersMap {
		request.Header.Add(key, value)
	}
	//Send request
	return client.Do(request)
}

/*
 Method to get resource content from registry
 */
func getResourceContent(resourcePath, fileName string) (string, error) {
	authorizationHeaderValue := b64.StdEncoding.EncodeToString([]byte(*cloudmgtRestAPIUsername + ":" +
		*cloudmgtRestAPIPassword))
	headersMap := make(map[string]string)
	headersMap[authorizationHeader] =  authorizationHeaderType+authorizationHeaderValue
	paramsMap := make(map[string]string)
	response, err := doHTTPRequest(getHTTPMethod, resourcePath+fileName, headersMap, paramsMap)
	if err != nil {
		return "", err
	} else {
		defer response.Body.Close()
		byteResponse, err := ioutil.ReadAll(response.Body)
		if err != nil {
			return "", err
		} else {
			stringResponse := string(byteResponse)
			return stringResponse, nil
		}
	}
}

func createFile(content string, fileName string) {
	out, err := os.Create(fileName)
	if err != nil {
		glog.Errorln(err)
	} else {
		out.WriteString(content)
		out.Sync()
	}
}

func createSSLPemFile(certString string, keyString string, chainString string, filePath string) {
	var buffer bytes.Buffer
	if keyString != "" && certString != "" && chainString != "" {
		buffer.WriteString(keyString)
		buffer.WriteString(certString)
		buffer.WriteString(chainString)
	}
	createFile(buffer.String(), filePath)
}

/*
 Method to add security certificate to certificates directory in haproxy
 */
func addSecurityCertificate(resourcePath, appName, certificatesDir, encodedKey string) {
	rootPath := certificatesDir + *appTenantDomain + hypenSeparator + appName
	destinationErrorFile := rootPath + hypenSeparator + errorFileName + pemFileExtension
	errorMessage := ""
	key, err := b64.StdEncoding.DecodeString(encodedKey)
	isError := true
	if err != nil {
		errorMessage = "Error while decoding private key: " + err.Error()
		glog.Warningf(errorMessage)
	} else {
		iv, err, message := getIVString(resourcePath, appName)
		if err != nil {
			errorMessage = message
		} else {
			//Create new AES Cipher
			block, err := aes.NewCipher(key)
			if err != nil {
				errorMessage = "Error while creating new AES cipher: " + err.Error()
				glog.Warningf(errorMessage)
			} else {
				certString, err, message := getDecryptedString(pemFileExtension, "certificate",
					appName, resourcePath, block, iv)
				if err != nil {
					errorMessage = message
				} else {
					if utf8.ValidString(certString) {
						keyString, err, message := getDecryptedString(keyFileExtension, "key",
							appName, resourcePath, block, iv)
						if err != nil {
							errorMessage = message
						} else {
							if utf8.ValidString(keyString) {
								chainString, err, message := getDecryptedString(
									pubFileExtension, "chain", appName,
									resourcePath, block, iv)
								if err != nil {
									errorMessage = message
								} else {
									if utf8.ValidString(chainString) {
										filePath := rootPath + pemFileExtension
										createSSLPemFile(certString, keyString,
											chainString, filePath)
										isError = false
									} else {
										errorMessage =
										"Invalid utf8 content for decoded chain"
										glog.Errorf(errorMessage)
									}
								}
							} else {
								errorMessage = "Invalid utf8 content for decoded key"
								glog.Errorf(errorMessage)
							}
						}
					} else {
						errorMessage = "Invalid utf8 content for decoded certificate"
						glog.Errorf(errorMessage)
					}
				}
			}
		}
	}
	if (isError) {
		errorMessage = errorMessage + " for tenant: " + *appTenantDomain + " for applicaton name: " + appName
		createErrorPemFile(certificatesDir, destinationErrorFile)
		cookie := logintoCloudmgt()
		if cookie != ""  {
			_, err := sendMail(cookie, errorMessage)
			if err != nil {
				glog.Infof("Error while sending email to: " + *sendEmailTo)
			}
		}
	}
}

/*
 Method to get decrypted string for resource
 */
func getDecryptedString(fileExtension, fileName, appName, resourcePath string, block cipher.Block, iv []byte) (string, error, string) {
	file := *appTenantDomain + hypenSeparator + appName + fileExtension
	//Decrypt certificate content
	encryptedString, err := getResourceContent(resourcePath, file)
	errorMessage := ""
	if err != nil {
		errorMessage = "Error while getting resource content for " + fileName + ": " + err.Error()
		glog.Errorf(errorMessage)
		return encryptedString, err, errorMessage
	} else {
		decryptedString, err := decryptResourceContent(encryptedString, block, iv)
		if err != nil {
			errorMessage = "Error while decrypting resource content for " + fileName + ": " + err.Error()
			glog.Errorf(errorMessage)
		}
		return decryptedString, err, errorMessage
	}
}

/*
 Method to get IV string from registry
 */
func getIVString(resourcePath, appName string) ([]byte, error, string) {
	ivFile := *appTenantDomain + hypenSeparator + appName + ivFileExtension
	ivString, err := getResourceContent(resourcePath, ivFile)
	errorMessage := ""
	if err != nil {
		errorMessage = "Error while getting resource content for iv: " + err.Error()
		glog.Errorf(errorMessage)
		return []byte(ivString), err, errorMessage
	} else {
		iv, err := b64.StdEncoding.DecodeString(ivString)
		if err != nil {
			errorMessage = "Error while decoding initialization vector: " + err.Error()
			glog.Warningf(errorMessage)
			return iv, err, errorMessage
		} else {
			return iv, nil, errorMessage
		}
	}
}

/*
 Method to decrypt registry resource content
 */
func decryptResourceContent(content string, block cipher.Block, iv []byte) (string,error) {
	if content != "" {
		//Decode resource content
		cipherText, err := b64.StdEncoding.DecodeString(content)
		if err != nil {
			glog.Warningf("Error while decoding resource content: %v", err)
			return "", err
		} else {
			//Create new CBC Decrypter
			decrypter := cipher.NewCBCDecrypter(block, iv)
			if len(cipherText) < aes.BlockSize {
				return "", errors.New("Ciphertext too short")
			}
			if len(cipherText)%aes.BlockSize != 0 {
				return "", errors.New("Ciphertext is not a multiple of the block size")
			}
			//Decrypt Content
			decrypter.CryptBlocks(cipherText, cipherText)
			return string(PKCS5UnPadding(cipherText)), nil
		}
	} else {
		return "", errors.New("Empty resource content")
	}
}

func PKCS5UnPadding(src []byte) []byte {
	length := len(src)
	unpadding := int(src[length-1])
	return src[:(length - unpadding)]
}

/*
 Method to login to cloudmgt rest api
 */
func logintoCloudmgt() string {
	url := *serverUrl + cloudmgtAuthenticateEP
	paramsMap := make(map[string]string)
	paramsMap[parameterUsernameKey] = *cloudmgtRestAPIUsername
	paramsMap[parameterPasswordKey] = *cloudmgtRestAPIPassword
	paramsMap[parameterActionKey] = loginAction
	headersMap := make(map[string]string)
	response, err := doHTTPRequest(postHTTPMethod, url, headersMap, paramsMap)
	if err != nil {
		return ""
	} else {
		return response.Header.Get(headerSetCookieKey)
	}
}

/*
 Method to invoke cloudmgt rest api to send emails
 */
func sendMail(cookie, error string) (*http.Response, error) {
	url := *serverUrl + cloudmgtSendMailEP
	paramsMap := make(map[string]string)
	paramsMap[parameterActionKey] = sendEmailAction
	paramsMap[parameterToKey] = "[" + *sendEmailTo + "]"
	paramsMap[parameterSubjectKey] = parameterSubjectValue
	paramsMap[parameterMessageKey] = error
	headersMap := make(map[string]string)
	headersMap[headerCookieKey] = cookie
	return doHTTPRequest(postHTTPMethod, url, headersMap, paramsMap)
}

/*
 Method to create error pem file
 */
func createErrorPemFile(certificatesDir, destinationErrorPemFile string) {
	sourcePemFile := certificatesDir + defaultPemFile
	err := copyFileContents(sourcePemFile, destinationErrorPemFile)
	if err != nil {
		glog.Warningf("Failed to copy ssl.pem file to %q due to %q\n", destinationErrorPemFile, err)
	}
}

/*
  Method to copy contents of file src to a file named dest. The file will be created if it does not already exist.
  If the destination file exists, all it's contents will be replaced by the contents of the source file.
 */
func copyFileContents(src, dest string) (err error) {
	in, err := os.Open(src)
	if err != nil {
		return err
	}
	defer in.Close()
	out, err := os.Create(dest)
	if err != nil {
		return err
	}
	defer func() {
		cerr := out.Close()
		if err == nil {
			err = cerr
		}
	}()
	if _, err = io.Copy(out, in); err != nil {
		return err
	}
	err = out.Sync()
	return err
}
