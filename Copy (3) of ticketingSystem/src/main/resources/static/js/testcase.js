angular.module('testupload', ['angularFileUpload']).directive('upload', function ($http, $cookies, $timeout, FileUploader, $compile) {
	
	 return {
	        restrict: 'E',
	        //require:'^travelSummary',
	        //replace :true,
	        templateUrl: 'templates/upload.html',
	        //transclude:true,
	        scope: {
	            tr: '=?',
	            displayOnly: '@',
	            title: '@',
	            multiple: '=?',
	            fileTypes: '@'
	        },
	
	        controller: function ($http, $scope, $attrs) {

	            var uploader =
	                $scope.uploader = new FileUploader({
	                    url: "fileupload",
	                   
	                });
	            $scope.uploadedFileNames = [];

	            $scope.addFile = function (files) {
	                angular.forEach(files, function (uploadFile, index) {


	                    var file = {
	                        file: {
	                            name: uploadFile.fileName,
	                            canDelete:true
	                        },
	                        isSuccess: true,
	                        isUploaded: true,
	                        progress: 100,
	                        remove: function () {
	                            $scope.uploader.queue.pop(this)
	                        }
	                    }

	                    $scope.uploader.queue.push(file);

	                });


	            }
	            
	            $scope.uploadAll = function () {
	                
	                if ($scope.uploadedFileNames.length <= 0) {
	                    toastr["error"]('No valid file present to upload');
	                    return;
	                }
	                $http({
	                    method: 'POST',
	                    url: "file/upload/all",
	                    headers: {
	                        _csrf: $cookies.get("XSRF-TOKEN")
	                    },
	                    data: {
	                        moduleName: $scope.moduleName,
	                        referenceNo: $scope.referenceNo,
	                        fileNames: $scope.uploadedFileNames
	                    }
	                }).then(function (data) {
	                    
	                    if (data) {
	                        toastr["success"]('Files uploaded successfully');
	                        $scope.uploadedFileNames = [];
	                        uploader.queue = [];
	                    } else
	                        toastr["error"]('File upload failed');

	                });
	            }
	            
	            
	            
	            uploader.filters.push({

	                name: 'maxFileAllowed',
	                fn: function (item, options) {
	                    
	                    return this.queue.length < 10;
	                }
	            });
	            $scope.uploadAll = function () {
	                
	                if ($scope.uploadedFileNames.length <= 0) {
	                    toastr["error"]('No valid file present to upload');
	                    return;
	                }
	                $http({
	                    method: 'POST',
	                    url: "file/upload/all",
	                    headers: {
	                        _csrf: $cookies.get("XSRF-TOKEN")
	                    },
	                    data: {
	                        moduleName: $scope.moduleName,
	                        referenceNo: $scope.referenceNo,
	                        fileNames: $scope.uploadedFileNames
	                    }
	                }).then(function (data) {
	                    
	                    if (data) {
	                        toastr["success"]('Files uploaded successfully');
	                        $scope.uploadedFileNames = [];
	                        uploader.queue = [];
	                    } else
	                        toastr["error"]('File upload failed');

	                });
	            }
	            
	            $scope.error = {};

	            $scope.hoverIn = function () {
	                $timeout(initPopover, 200);
	            }
	            $scope.hoverOut = function () {
	                $timeout(initPopover, 200);
	            }
	            $scope.$on('upload.draft', function (draft) {
	                
	                $scope.tr = draft.targetScope.draftData;
	                //$scope.uploader.queue.push(data.fileUpload.fileItems);
	                if (!angular.isUndefined($scope.tr.fileUpload) && $scope.tr.fileUpload != null)
	                    $scope.addFile($scope.tr.fileUpload.fileItems);
	            });


	            uploader.filters.push({

	                name: 'maxFileAllowed',
	                fn: function (item, options) {
	                    
	                    return this.queue.length < 10;
	                }
	            }
	                /* ,{name:'fileTypeFilter',
	                     fn:function(item,options){
	                         
	                         var filetype =item.file.type
	                     	
	                         if(filetype=='image/jpeg'||filetype=="application/pdf")
	                             return true;
	                         else 
	                             return false;
	                         }
	                     }*/
	            );
	            $scope.removeItem = function (item, index) {

	                if (item.isUploaded) {
	                    uploader.queue.splice(index, 1);
	                    //item.remove();

	                    if (item.isSuccess)
	                        $scope.removeFileFromServer(item.file.name);
	                } else {
	                    uploader.queue.splice(index, 1);
	                    //remove the file from the list
	                }
	            };

	            $scope.downloadFile = function (filename) {
	                $http({
	                    method: 'GET',
	                    url: 'file/download',
	                    responseType: 'arraybuffer',
	                    headers: {
	                        'Content-type': 'application/json'
	                    }

	                }).
	                    success(function (data, status, headers, config) {
	                        
	                        
	                        var filetype = filename.substring(filename.lastIndexOf('.') + 1, filename.length);

	                        /*  var anchor =angular.element("<a/>");
	                        anchor.attr({
	                        href: 'data:attachment/'+filetype+';charset=utf-8,' + encodeURI([data]),
	                        target: '_blank',
	                        download: filename
	                        })[0].click();
	                         */
	                        var blob = new Blob([data], {
	                            type: 'attachment/' + filetype
	                        });
	                        saveAs(blob, filename);

	                    }).
	                    error(function (data, status, headers, config) {
	                        
	                    });
	            };

	            $scope.removeFileFromServer = function (filename) {

	                $http({
	                    method: 'POST',
	                    url: "file/remove?_csrf=" + $cookies.get("XSRF-TOKEN"),
	                    data: {
	                        moduleName: $scope.moduleName,
	                        fileName: filename,
	                        referenceNo: $scope.referenceNo
	                    }

	                }).success(function () {
	                    //remove it from the upload also
	                }).error(function () {
	                    //console the log
	                })
	            }

	            uploader.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/
	                , filter, options) {
	                console.info('onWhenAddingFileFailed', item, filter, options);
	            };
	            uploader.onAfterAddingFile = function (fileItem) {
	                 
	                   /*if (!angular.isUndefined($scope.multiple) && $scope.multiple) {
	                       return;
	                   }*/

	                fileItem.formData[0].referenceNo = $scope.referenceNo;
	                for (var i = 0; i < uploader.queue.length - 1; i++) {
	                    if (uploader.queue[i].file.name == fileItem.file.name) {
	                        fileItem.removeAfterUpload = true;
	                    }
	                }
	            
	                fileItem.url = "file/upload" 
	                fileItem.upload();
	                console.info('onAfterAddingFile', fileItem);


	            };
	            uploader.onAfterAddingAll = function (addedFileItems) {
	                console.info('onAfterAddingAll', addedFileItems);
	            };

	            uploader.onBeforeUploadItem = function (item) {
	                item.formData[0].referenceNo = $scope.referenceNo;
	                console.info('onBeforeUploadItem', item);
	            };
	            uploader.onProgressItem = function (fileItem, progress) {
	                console.info('onProgressItem', fileItem, progress);
	            };
	            uploader.onProgressAll = function (progress) {
	                console.info('onProgressAll', progress);
	            };
	            uploader.onSuccessItem = function (fileItem, response, status, headers) {
	                fileItem.formData[0].error = response;
	                if (!angular.isUndefined(response.errorMessage)) {
	                    fileItem.isUploaded = false;
	                    fileItem.isSuccess = false;
	                } else {
	                    if ($scope.uploadedFileNames.indexOf(fileItem.file.name) < 0)
	                        $scope.uploadedFileNames.push(fileItem.file.name);
	                }
	                //$scope.$apply();
	                console.info('onSuccessItem', fileItem, response, status, headers);
	            };
	            uploader.onErrorItem = function (fileItem, response, status, headers) {

	                fileItem.formData[0].error = response;
	                //$scope.$apply();
	                console.info('onErrorItem', fileItem, response, status, headers);
	            };
	            uploader.onCancelItem = function (fileItem, response, status, headers) {
	                console.info('onCancelItem', fileItem, response, status, headers);
	            };
	            uploader.onCompleteItem = function (fileItem, response, status, headers) {
	                //fileItem.formData[0].progre =
	                console.info('onCompleteItem', fileItem, response, status, headers);
	            };
	            uploader.onCompleteAll = function () {
	                console.info('onCompleteAll');
	            };
	        },
	        link: function (scope, elem, attrs) {
	            $timeout(initPopover, 200);
	            

	        }
	    };
	
	
	 }
}