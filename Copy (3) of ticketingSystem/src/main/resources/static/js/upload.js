 /* global saveAs */
angular
		.module('upload', [ 'angularFileUpload' ])
		.directive(
				'upload',
				function($http,$location, $stateParams, $timeout,
						FileUploader, $compile) {
					function initPopover() {
						$('[data-popover="true"]').popover();
						
					};

					return {
						restrict : 'E',
						// require:'^travelSummary',
						// replace :true,
						templateUrl : '/upload.html',
						// transclude:true,
						scope : {
							referenceNo : '@',
							moduleName : '@',
							tr : '=?',
							displayOnly : '@',
							title : '@',
							multiple : '=?',
							fileTypes : '@',
							files : '=',

						},
						controller : function($http, $scope, $attrs) {
							var requesturl = "user/upload/";

							if ($stateParams.id) {
								requesturl = "user/uploadWithTicket/"

							}

							var uploader = $scope.uploader = new FileUploader({

								url : requesturl,
								
								method : 'POST',
								formData : [ {
									referenceNo : $scope.referenceNo
								} ]

							});

						
						
								
							  $scope.uploadAll = function () {
							 $scope.uploader.queue.length=0;
							 $scope.$emit("uploadAll");
							  }
							 
							$scope.error = {};

							$scope.hoverIn = function() {
								$timeout(initPopover, 200);
							}
							$scope.hoverOut = function() {
								$timeout(initPopover, 200);
							}
							
							uploader.filters.push({

								name : 'maxFileAllowed',
								fn : function(item, options) {
									
									if (this.queue.length > 0) {
										
										var idx = 0;
										var found = false;
										angular.forEach(this.queue, function(v,
												i) {
											if (v.file.name == item.name)
												found = true;
											else if (!found)
												idx++;
										});

										if (found) {
											this.queue.splice(idx, 1);
										}
									}

									
									return this.queue.length < 10;
								}
							}
							
							);
							$scope.removeItem = function(item, index) {
								
								if (item.isUploaded) {
									uploader.queue.splice(index, 1);
									

									if (item.isSuccess)
										$scope
												.removeFileFromServer(item.file.name);
								} else {
									uploader.queue.splice(index, 1);
									
								}
								
							};

							$scope.downloadFile = function(filename) {
								
								$http(
										{
											method : 'GET',
											url : 'file/download?fileName='
													+ filename
													+ '&referenceNo='
													+ $scope.referenceNo,
											responseType : 'arraybuffer',
											headers : {
												'Content-type' : 'application/json'
											}

										})
										.success(
												function(data, status, headers,
														config) {
													
													var filetype = filename
															.substring(
																	filename
																			.lastIndexOf('.') + 1,
																	filename.length);

													
													var blob = new Blob(
															[ data ],
															{
																type : 'attachment/'
																		+ filetype
															});
													saveAs(blob, filename);

												}).error(
												function(data, status, headers,
														config) {
													
												});
							};

							$scope.removeFileFromServer = function(filename) {
								
								$http(
										{
											method : 'POST',
											url : "/user/removeFile/",
											
											data : {

												fileName : filename,
												referenceNo : $scope.referenceNo
											}

										}).success(function() {
									if ($stateParams.id) {
										
										if ($scope.uploader.queue.length > 0)
											$scope.$emit('Event', {
												showContent : true
											});
										else
											$scope.$emit('Event', {
												showContent  : false
											});
									}
									// remove it from the upload also
								}).error(function() {
									// console the log
								})
							}

							$scope.referenceNo = "";

							uploader.onWhenAddingFileFailed = function(item,
									filter, options) {
								console.info('onWhenAddingFileFailed', item,
										filter, options);
							};
							uploader.onAfterAddingFile = function(fileItem) {
								
								
								
								
								if ($scope.referenceNo != "") {

									fileItem.formData[0].referenceNo = $scope.referenceNo;
								} else
									fileItem.formData[0].referenceNo = "";

								if ($stateParams.id) {
									
									fileItem.formData[0].referenceNo = $stateParams.id;

								}
								if (!$stateParams.id&&fileItem.file.size<=10485760) {
									
									fileItem.upload();
								}
								if ($stateParams.id) {

									if ($scope.uploader.queue.length > 0)
										$scope.$emit('Event', {
											showContent : true
										});
									else
										$scope.$emit('Event', {
											showContent : false
										});
								}
								
								if ($stateParams.id) {
									var found = false;
									
								
									
									if($scope.files.length<=0&&fileItem.file.size<=10485760)
										{
										
										fileItem.upload();
										}
									
									
									
									if ($scope.files!=null && $scope.files.length >0) {
										
										angular
												.forEach(
														$scope.files,
														function(v, i) {

															if (v.fileName == fileItem.file.name)
																found = true;

														});
										if (found) {
											
											var id = this.queue
													.indexOf(fileItem);
											this.queue.splice(id, 1);
										} else {
											
											fileItem.upload();

										}

									}
								}

							};

							uploader.onAfterAddingAll = function(addedFileItems) {
								console
										.info('onAfterAddingAll',
												addedFileItems);
								
							};

							uploader.onBeforeUploadItem = function(item) {

								

								
								console.info('onBeforeUploadItem', item);
							};
							uploader.onProgressItem = function(fileItem,
									progress) {
								console.info('onProgressItem', fileItem,
										progress);
							};
							uploader.onProgressAll = function(progress) {
								console.info('onProgressAll', progress);
							};
							uploader.onSuccessItem = function(fileItem,
									response, status, headers) {

								$scope.referenceNo = response.id;
								if (!$stateParams.id)
									$scope.$emit('FilesEvent', {
										fileId : response.id
									});
								

								console.info('onSuccessItem', fileItem,
										response, status, headers);
							};
							uploader.onErrorItem = function(fileItem, response,
									status, headers) {

								fileItem.formData[0].error = response;
								
								console.info('onErrorItem', fileItem, response,
										status, headers);
							};
							uploader.onCancelItem = function(fileItem,
									response, status, headers) {
								console.info('onCancelItem', fileItem,
										response, status, headers);
							};
							uploader.onCompleteItem = function(fileItem,
									response, status, headers) {
								// fileItem.formData[0].progre =
								
							
								$scope.referenceNo = response.id;
								if (!$stateParams.id)
									$scope.$emit('FilesEvent', {
										fileId : response.id
									});

								console.info('onCompleteItem', fileItem,
										response, status, headers);
							};
							uploader.onCompleteAll = function() {
								console.info('onCompleteAll');
							};
						},
						link : function(scope, elem, attrs) {
							$timeout(initPopover, 200);
							

						}
					};
				})
		.directive('parent', function() {
			return {
				restrict : 'E',
				
				transclude : true,
				template : '<div ng-transclude><p>yogesh kumar </p></div>',

				scope : {

				},
				link : function(scope, elem, attrs, ctrl) {

					
				},
				controller : function($scope) {
					this.tr = "hello";
					

					this.get = function() {
						return "return from parent controller";
					}
				}

			}
		})
		.directive('child', function() {
			return {
				restrict : 'E',
				require : '^parent',
				
				scope : {

				},
				template : '<div><p>child bardia </p></div>',
				link : function(scope, el, attr, parent) {
									},
				controller : [ '$scope', function($scope) {
					
				} ]
			}
		})
		.directive(
				'uploadItem',
				function() {

					return {
						restrict : 'E',
						

						template : '<span ng-transclude><a href="" title="{{error.errorMessage}}" data-popover="true" data-toggle="popover" data-placement="bottom" data-content="{{error.errorDetail}}" data-trigger="hover">{{error.errorMessage}}</a><span>',
						controller : function() {
							
						},
						link : function() {
							
						}
					}

				});
;
