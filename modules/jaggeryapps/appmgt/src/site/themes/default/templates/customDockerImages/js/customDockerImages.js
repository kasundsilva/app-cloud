/*
 *
 *   Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 * /
 */

$(document).ready(function () {
    /**
     * On focustout keyup blur and click events of #imageUrl
     */
    $('#imageUrl').on('focusout keyup blur click', function () { // fires on every keyup & blur
        if ($('#imageUrl').val()) {
            $("#addImage").prop("disabled", false);
        } else {
            $("#addImage").prop("disabled", true);
        }
    });
    $('#first_imageUrl').on('focusout keyup blur click', function () { // fires on every keyup & blur
        if ($('#first_imageUrl').val()) {
            $("#addFirstImage").prop("disabled", false);
        } else {
            $("#addFirstImage").prop("disabled", true);
        }
    });

    // Initial draw of images list table with the page load.
    fillImagesListTable();
    /**
     *  onClick event of a delete icon in image list table
     *  the image id is stored as data-uid parameter in each table row
     */
    $(document).on('click', '.deleteImage', function (e) {
        e.preventDefault(); // this is to prevent the default behaviour of an anchor tag
        var imageId = $(this).closest('li').data('uid');
        jagg.popMessage({
            type: 'confirm',
            modalStatus: true,
            title: 'Delete Image',
            content: 'Are you sure you want to delete this image ?',
            yesCallback: function () {
                deleteImage(imageId);
            },
            noCallback: function () {
            }
        });
    });

    /**
     *  onClick event of a update icon in image list table
     *  the image id is stored as data-uid parameter in each table row
     */
    $(document).on('click', '.updateImage', function (e) {
        e.preventDefault();
        var imageId = $(this).closest('li').data('uid');
        jagg.popMessage({
            type: 'confirm',
            modalStatus: true,
            title: 'Update Image',
            content: 'Are you sure you want to update this image ?',
            yesCallback: function () {
                updateImage(imageId);
            },
            noCallback: function () {
            }
        });
    });

    /**
     * This is for changing plus/ minus icons in view result modal's accordion.
     * @param e
     */
    function toggleIcon(e) {
        $(e.target)
            .prev('.panel-heading')
            .find(".more-less")
            .toggleClass('fw-up');
    }

    // On expansion event of test results accordion
    $(document).on('hidden.bs.collapse', function (e) {
        toggleIcon(e);
    });
    // On shrink event of test results accordion
    $(document).on('shown.bs.collapse', function (e) {
        toggleIcon(e);
    });

    /**
     *  onClick event of a view result icon in image list table
     *  the image id is stored as data-uid parameter in each table row
     */
    $(document).on('click', '.viewResult', function (e) {
        e.preventDefault(); // this is to prevent the default behaviour of an anchor tag
        var resultJson = $(this).data('uid');
        resultJson = decodeURI(resultJson);
        resultJson = JSON.parse(resultJson);

        // Constructing viewResult Modal and Accordion
        var modalBody = '<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">';
        for (i = 0; i < testsJson.length; i++) {
            var testId = testsJson[i].testId;
            var panelColorClass;
            var successIconClass;
            if (resultJson[testId] == "pass") {
                panelColorClass = "panel-success";
                successIconClass = "fw-success";
            } else if (resultJson[testId] == "fail") {
                panelColorClass = "panel-danger";
                successIconClass = "fw-error";
            }
            modalBody += '<div class="panel ' + panelColorClass + '">' +
                         '<div class="panel-heading" role="tab" id="heading' + i + '">' + '<h4 class="panel-title">' +
                         '<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse' + i + '" aria-expanded="true" aria-controls="collapse' + i + '">' +
                         '<i class="docker-test-icon fw ' + successIconClass + '"></i>' +
                         '<i class="more-less fw-down"></i>' +
                         testsJson[i].title + ' : ' + resultJson[testId] + '</a></h4></div>' +
                         '<div id="collapse' + i + '" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading' + i + '">' +
                         '<div class="panel-body result-panel">' +
                         '<p>Test :</p>' +
                         '<p class="result-description">' + testsJson[i].description + '</p>' +
                         /*'<p>Docker Benchmark Reference : </p>' +
                         '<p class="result-description">' + testsJson[i].dockerBenchReference + '</p>' +*/
                         '<p>Remedy : </p>' +
                         '<p class="result-description">' + testsJson[i].remedy + '</p>' +

                         '</div></div></div></div>';
        }
        $('#viewResultModal .modal-body').html(modalBody);
    });

});


/**
 *  Adding new image - This will tiggerred by onClick event of #addImage button
 */
function addNewImage(isfirstImage) {
    var imageUrl;
    if (isfirstImage) {
        $('#first_imageUrl').prop("disabled", true);
        imageUrl = $('#first_imageUrl').val().trim();
        $("#addFirstImage").html("<span class=\"fw-stack fw-lg btn-action-ico\"><i class=\"fw fw-loader2 fw-spin fw-2x\"></i></span>");
    } else {
        $('#imageUrl').prop("disabled", true);
        $("#addImage").html("<span class=\"fw-stack fw-lg btn-action-ico\"><i class=\"fw fw-loader2 fw-spin fw-2x\"></i></span>");
        imageUrl = $('#imageUrl').val().trim();
    }

    jagg.post("../blocks/customDockerImages/ajax/customDockerImages.jag", {
        action: "isImageAvailable",
        imageUrl: imageUrl
    }, function (result) {
        result = $.trim(result);
        if (result == "false") { // isImageAvailable=false means image is not added currently.
            jagg.post("../blocks/customDockerImages/ajax/customDockerImages.jag", {
                action: "addImageAndCheckSecurity",
                imageUrl: imageUrl
            }, function (result) {
                fillImagesListTable();
                jagg.message({
                    content: 'New Image added and queued for security check. It will take some time!',
                    type: 'info',
                    id: 'addnewcustomdockerimage'
                });

                if (isfirstImage) {
                    $("#addFirstImage").loadingButton({action: 'hide'}).prop("disabled", true);
                    $('#first_imageUrl').prop("disabled", false).val("");
                    location.reload();
                } else {
                    $("#addImage").loadingButton({action: 'hide'}).prop("disabled", true);
                    $("#addImage").html("Add");
                    $('#imageUrl').prop("disabled", false).val("");
                }

            }, function (jqXHR, textStatus, errorThrown) {
                jagg.message({
                    content: jqXHR.responseText,
                    type: 'error',
                    id: 'addnewcustomdockerimage',
                    timeout: 8000
                });
            });
        } else { // This means image is already added
            jagg.message({
                content: "Cant add image. Image has been already added.",
                type: 'error',
                id: 'addnewcustomdockerimage',
                timeout: 8000
            });
        }
    }, function (jqXHR, textStatus, errorThrown) {
        jagg.message({
            content: jqXHR.responseText,
            type: 'error',
            id: 'isImageAlreadyAdded',
            timeout: 8000
        });
    });
}

function fillImagesListTable() {

    var validImageCount = 0; // images count passed all security tests
    var pendingImagesAvailable = false; // images that are not completed the security test
    jagg.post("../blocks/customDockerImages/ajax/customDockerImages.jag", {
        action: "getAllImages"
    }, function (result) {
        var ulHtml = '';
        var imagesJsonObject = JSON.parse(result);
        for (i = 0; i < imagesJsonObject.length; i++) {
            var statusIcon='', statusBg="", notActiveForCreateApplication="" , notActiveForModifyImage = "";
            if (imagesJsonObject[i].status == "passed") {
                statusIcon = '<i class="fw fw-success"></i>';
                statusBg = 'bg-success';
                validImageCount += 1;
            } else if (imagesJsonObject[i].status == "failed") {
                statusIcon = '<i class="fw fw-error"></i>';
                notActiveForCreateApplication = "not-active";
                statusBg = 'bg-danger';
            } else { // results pending
                statusIcon = '<i class="fw fw-loader2 fw-spin"></i>';
                notActiveForCreateApplication = "not-active";
                notActiveForModifyImage = "not-active";
                pendingImagesAvailable = true;
                statusBg = 'bg-muted';
            }

            ulHtml += '<li class="row no-gutters" data-uid= "' + imagesJsonObject[i].imageId + '">' +
                          '<div class="clearfix col-md-8">' +
                              '<div class="custom-list-icon pull-left ' + statusBg +'">' +
                                statusIcon +
                              '</div>' +
                              '<div class="pull-left custom-content">' +
                                  '<div class="primary">' + imagesJsonObject[i].remoteUrl + '</div>' +
                                  '<div class="text-muted secondary">Last Updated: ' + imagesJsonObject[i].lastUpdated.split(".")[0] + '</div>' +
                              '</div>' +
                          '</div>'+
                          '<div class="clearfix col-md-4">' +
                              '<div class="pull-right custom-actions">' +
                                  '<a href="application.jag?appTypeName=custom&selectedImageId=bla" class="' + notActiveForCreateApplication + '"><i class="fw fw-application"></i> Create App</a>' +
                                  '<a href="#" class="' + notActiveForModifyImage + ' updateImage"><i class="fw fw-sync"></i> Update</a>' +
                                  '<a href="#viewResultModal" data-uid="' + encodeURI(imagesJsonObject[i].results) + '" data-toggle="modal" class="' + notActiveForModifyImage + ' viewResult" title="View test report"><i class="fw fw-view"></i> View Details</a>' +
                                  '<a href="#" class="deleteImage"><i class="fw fw-delete"></i> Remove</a>' +
                              '</div>' +
                          '</div>' +
                      '</li>';
        }

        var warning = '<div class="message message-warning">' +
            '<h4><i class="icon fw fw-info"></i>Warning</h4>' +
            '<p>Currently you don\'t have any valid docker images added. Please make sure your docker ' +
            'image is valid before you create applications.</p>' +
            '</div>';

        if (validImageCount == 0) {
            $('#warning-block').html(warning);
        } else {
            $('#warning-block').html("");
        }

        $('#imageList').html(ulHtml);
        if (pendingImagesAvailable) {
            setTimeout(fillImagesListTable, 5000); // this will poll while pending images are available
        }
    }, function (jqXHR, textStatus, errorThrown) {
    });
}

/**
 * Deleteing an image
 * @param imageId - image id
 */
function deleteImage(imageId) {

    jagg.post("../blocks/customDockerImages/ajax/customDockerImages.jag", {
        action: "deleteImage",
        imageId: imageId
    }, function (result) {
        if (result == "true") {
            jagg.message({
                 content: imageId + ' deleted successfully',
                 type: 'success',
                 id: 'deleteimage'
             });
        }
        fillImagesListTable();
    }, function (jqXHR, textStatus, errorThrown) {
    });
}

function updateImage(imageId) {
    jagg.post("../blocks/customDockerImages/ajax/customDockerImages.jag", {
        action: "updateImage",
        imageId: imageId
    }, function (result) {
        if (result == "true") {
            jagg.message({
                 content: imageId + ' updated successfully',
                 type: 'success',
                 id: 'updateimage'
             });
        }
        fillImagesListTable();
    }, function (jqXHR, textStatus, errorThrown) {

    });
}


