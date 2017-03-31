app.directive('commonTemplate', function ($http, $location, $stateParams, $timeout, $state, getApprovalHistory, 
getFieldsForTicket, action) {
		return {
			restrict: 'E',
			templateUrl: '/ticketDetail.html',
			scope: {
				previousPageLink: "@"
			},
			controller: function ($http, $scope, $attrs, $rootScope) {
				$scope.fields = [];
				$scope.files = [];
				$scope.expertOptions = [];
				$scope.statusCode = "";
				$scope.comments = "";
				$scope.approvalHistory = {};

				var initPage = function () {
					if ($stateParams.id) {
						$scope.viewDetails = true;
						getTicketDetails();
						getComments();
						getExpertOptions();
						fetchApprovalHistory();
						getFiles();
						$scope.currentTab = $location.search().currentTab;
				}
				}

				var getTicketDetails = function () {
					getFieldsForTicket.getInfo($stateParams.id).success(function (response) {
						$scope.fields = response;
						$scope.ticketDetails = $scope.fields[0].ticketmaster;
						getExpertOptions();
					}).error(function (response) {
						Toasty.error("", "Something went wrong. Please reload the page.");
					})
				}

				var getExpertOptions = function () {
					
					if ($scope.ticketDetails && $scope.ticketDetails.isExpertTask) {
						$http.get("user/getExpertOptions/?id=" + $stateParams.id).success(function (response) {
							$scope.expertOptions = response;
							
						}).error(function (response) {
							Toasty.error("", "Something went wrong. Please reload the page.");
						})
					}

				}
				var getComments = function () {
					$http.get("user/getTaskComments/?ticketId=" + $stateParams.id).success(function (response) {
						$scope.taskComments = response;
					})
				}

				var fetchApprovalHistory = function () {
					getApprovalHistory.approvalHistory($stateParams.id).success(function (response) {
						$scope.approvalHistory = response;				
					}).error(function (response) {
						Toasty.error("", "Something went wrong. Please reload the page.");
					})
				}

				var getFiles = function () {
					if ($stateParams.id) {
						$http.get("public/getFilesForTicket/" + $stateParams.id).success(function (response) {
							$scope.files = response;
						}).error(function (response) {
							Toasty.error("", "Something went wrong. Please reload the page.");
						})
					}
				}

				initPage();
			
				$scope.isCurrentUrl=function(url)
				{	
						return $state.current.url==url;
				}
				
				$scope.getIconForActivity = function (statusCode, isExpertOption) {
					if (statusCode == 'STS01' || statusCode == 'STS04' || statusCode == 'STS13' || statusCode == 'STS14')
						return "check_circle";
					if (statusCode == 'STS02' || statusCode == 'STS11')
						return "cancel";
					if (statusCode == 'STS03')
						return "info";
					if (statusCode == 'STS12')
						return "replay";
					else{
						return isExpertOption ?  "" : "fiber_manual_record";
					}
				}

				$scope.getIconStyle = function (statusCode) {
					if (statusCode == 'STS01' || statusCode == 'STS04' || statusCode == 'STS13' || statusCode == 'STS14')
						return { "color": "#00897b", "font-size": "13px" };
					if (statusCode == 'STS02' || statusCode == 'STS11')
						return { "color": "#D32F2F", "font-size": "13px" };
					if (statusCode == 'STS03')
						return { "color": "#3949AB", "font-size": "13px" };
					if (statusCode == 'STS12')
						return { "color": "#333", "font-size": "13px" };
					else
					{
						return { "color": "#9B9B9B", "font-size": "11px",  "right" : "5px" };
					}
				}

				var claimTask = function () {
					$http.post("user/claimTask/?id=" + $stateParams.id + "&status=" + $scope.statusCode + "&comments=" + $scope.comments)
					.success(function (response) {
						$("#commentModal").modal('hide');
						Toasty.info("", "Task claimed, please perfom the appropriate action.")
						$location.url("/personaltasks/"+$stateParams.id);
					})
					.error(function (response) {
						Toasty.error("", "Could not claim the task");
					})
				}

				var revokeTask = function (status) {
					$http.post("user/revokeTask/?id=" + $stateParams.id + "&status=" + $scope.statusCode + "&comments=" + $scope.comments).success(function (response) {
						$("#commentModal").modal('hide');
						$location.search("group",$scope.ticketDetails.currentGroupMaster.id)
						$location.path("/myactionables");
						Toasty.info("", "Task revoked back to group "+$scope.ticketDetails.currentGroupMaster.groupName)
					}).error(function (response) {
						Toasty.error("", "Could not revoke the task");
					})
				}

				$scope.setStatus = function (statusCode) {
					$scope.statusCode = statusCode;
				}

				$scope.submitRequest = function () {
					if ($scope.statusCode == 'STS08') {
						claimTask();
					}
					if ($scope.statusCode == 'STS12') {
						revokeTask();
					}
					else if (!($scope.statusCode == 'STS08' || $scope.statusCode == 'STS12')) {
						action.performAction($stateParams.id, $scope.statusCode, $scope.comments).success(function (response) {
							$("#commentModal").modal('hide');
							$location.url("/"+$scope.previousPageLink);
							Toasty.info("", "Action performed successfully!");
						}).error(function (response) {
							Toasty.error("", "Could not perform the action");
						})
					}
				}

				$scope.showExpertOptions = function () {
					if ($scope.ticketDetails) {
						return $scope.ticketDetails.isExpertTask;
					}
				}

				$scope.showContent = false;
				$scope.$on("Event", function (event, args) {
					$scope.showContent = args.showContent;
				})
				$scope.$on("uploadAll", function () {
					getFiles();
				})

				$scope.$on("FileOperation", function (event) {
					getFiles();
				})

				$scope.downloadfile = function (filename, referenceNo) {
					$http({
						method: 'GET',
						url: 'file/download?fileName=' + filename + '&referenceNo=' + referenceNo,
						responseType: 'arraybuffer',
						headers: {
							'Content-type': 'application/json'
						}
					}).
						success(function (data, status, headers, config) {
							var filetype = filename.substring(filename.lastIndexOf('.') + 1, filename.length);
							var blob = new Blob([data], {
								type: 'attachment/' + filetype
							});
							saveAs(blob, filename);
						}).
						error(function (data, status, headers, config) {
					Toasty.error("", "Something went wrong while downloading the file");		
						});
				}	
	}
		}
		
})