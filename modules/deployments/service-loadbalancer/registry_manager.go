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
)

const (
	registryPath             = "customurl/"
	cloudType                = "app-cloud/"
	securityCertificates     = "/securityCertificates/"
	maxRetryCount            = 3
	pemFileExtension         = ".pem"
	keyFileExtension         = ".key"
	pubFileExtension         = ".pub"
	ivFileExtension          = ".iv"
	authorizationHeader      = "Authorization"
	authorizationHeaderType  = "Basic "
	hypenSeparator           = "-"
	getHTTPMethod            = "GET"
	forwardSlashSeparator    = "/"
	applicationLaunchBaseUrl = ".wso2apps.com"
)

func getResource(resource string, retryCount int, authorizationHeaderValue string) *http.Response {
	tr := &http.Transport{
		TLSClientConfig: &tls.Config{InsecureSkipVerify: true},
	}
	client := &http.Client{Transport: tr}
	request, _ := http.NewRequest(getHTTPMethod, resource, nil)
	request.Header.Set(authorizationHeader, authorizationHeaderType+authorizationHeaderValue)
	response, err := client.Do(request)
	if err != nil {
		glog.Info("ERROR: ")
		glog.Errorln(err)
	} else {
		if response.StatusCode != 200 && retryCount < maxRetryCount {
			retryCount++
			response = getResource(resource, retryCount, authorizationHeaderValue)
			if retryCount == maxRetryCount {
				glog.Infof("INFO: Unable to get resource " + resource)
				return nil
			}
		}
	}
	return response
}

func getResourceContent(resourcePath string, fileName string, retryCount int, authorizationHeaderValue string) string {
	response := getResource(resourcePath+fileName, retryCount, authorizationHeaderValue)
	if response != nil {
		defer response.Body.Close()
		byteResponse, err := ioutil.ReadAll(response.Body)
		if err != nil {
			glog.Info("ERROR: ")
			glog.Errorln(err)
		} else {
			stringResponse := string(byteResponse)
			return stringResponse
		}
	}
	return ""
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

func addSecurityCertificate(resourcePath string, appName string, certificatesDir string,
	authorizationHeaderValue string, encodedKey string) {
	key, err := b64.StdEncoding.DecodeString(encodedKey)
	if err != nil {
		glog.Warningf("Error while decoding private key: %v", err)
	} else {
		retryCount := 0
		certFile := *appTenantDomain + hypenSeparator + appName + pemFileExtension
		keyFile := *appTenantDomain + hypenSeparator + appName + keyFileExtension
		chainFile := *appTenantDomain + hypenSeparator + appName + pubFileExtension
		ivFile := *appTenantDomain + hypenSeparator + appName + ivFileExtension

		ivString := getResourceContent(resourcePath, ivFile, retryCount, authorizationHeaderValue)
		iv, err := b64.StdEncoding.DecodeString(ivString)
		if err != nil {
			glog.Warningf("Error while decoding initialization vector: %v", err)
		} else {
			//Create new AES Cipher
			block, err := aes.NewCipher(key)
			if err != nil {
				glog.Warningf("Error while creating new AES cipher: %v", err)
			} else {
				//Decrypt certificate content
				encryptedCertString := getResourceContent(resourcePath, certFile, retryCount, authorizationHeaderValue)
				certString := decryptResourceContent(encryptedCertString, block, iv)

				//Decrypt key content
				encryptedKeyString := getResourceContent(resourcePath, keyFile, retryCount, authorizationHeaderValue)
				keyString := decryptResourceContent(encryptedKeyString, block, iv)

				//Decrypt chain content
				encryptedChainString := getResourceContent(resourcePath, chainFile, retryCount, authorizationHeaderValue)
				chainString := decryptResourceContent(encryptedChainString, block, iv)

				if certString != "" && keyString != "" && chainString != "" {
					filePath := certificatesDir + *appTenantDomain + hypenSeparator + appName + pemFileExtension
					createSSLPemFile(certString, keyString, chainString, filePath)
				}
			}
		}
	}
}

func decryptResourceContent(content string, block cipher.Block, iv []byte) string {
	if content != "" {
		//Decode resource content
		cipherText, err := b64.StdEncoding.DecodeString(content)
		if err != nil {
			glog.Warningf("Error while decoding resource content: %v", err)
			return ""
		} else {
			//Create new CBC Decrypter
			decrypter := cipher.NewCBCDecrypter(block, iv)

			if len(cipherText) < aes.BlockSize {
				panic("ciphertext too short")
			}
			if len(cipherText)%aes.BlockSize != 0 {
				panic("ciphertext is not a multiple of the block size")
			}

			//Decrypt Content
			decrypter.CryptBlocks(cipherText, cipherText)
			return string(PKCS5UnPadding(cipherText))
		}
	} else {
		return ""
	}
}

func PKCS5UnPadding(src []byte) []byte {
	length := len(src)
	unpadding := int(src[length-1])
	return src[:(length - unpadding)]
}
