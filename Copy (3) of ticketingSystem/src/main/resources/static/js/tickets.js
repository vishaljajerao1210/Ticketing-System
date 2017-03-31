app.controller('ticketcontroller', function ($scope, $http, $state, requestCanceller,
	$location, $stateParams, utility, getSubDomainService) {

	$scope.subdomainfields = {};
	var selectedItems = {};
	utility.tooltip();
	$scope.disableSubmit = false;
	$scope.show = false;
	$scope.fileId = "";
	$scope.raiseticket = {};
	$scope.viewDetails = false;
	$scope.search = {};
	$scope.currentPage = 1;
	$scope.pageSize = 10;
	$scope.maxSize = 5;
	$scope.tickets = [];
	var flag = false;
	$scope.pageSizeOptions = ['10', '15', '20'];

	if ($stateParams.id) {
		$scope.viewDetails = true;
	}

	$scope.fetchDataForAutoComplete = {

		getCategories: function (val) {
			return $http.get("public/categories/?PI=0&PG=10&searchParam=" + val).then(function (response) {
				return response.data.content;
			})
		},
		getDomainsByCategory: function (domainName) {
			return $http.get("public/getDomainByCategoryId/" + selectedItems.categoryId + "?domainName=" + domainName).then(
				function (response) {
					return response.data;
				})
		},
		getSubDomainsByCategory: function (subDomainName) {
			return $http.get("public/getSubDomainsByDomainId/" + selectedItems.domainId + "?subDomainName=" + subDomainName).then(
				function (response) {
					return response.data;
				})
		}
	}

	$scope.selectItems = {
		selectCategory: function ($item) {
			selectedItems.categoryId = $item.id;
			if ($scope.domainname && $scope.subdomainname) {
				$scope.domainname = "";
				$scope.subdomainname = "";
				$scope.show = false;
			}
		},
		selectDomain: function ($item) {
			selectedItems.domainId = $item.id;
			if ($scope.subdomainname) {
				$scope.subdomainname = "";
				$scope.show = false;
			}
		},
		selectSubDomain: function ($item) {
			selectedItems.subDomainId = $item.id;
			getFields();
		},

	}

	$scope.getFlag = function () {
		if ($scope.categoryname == undefined || $scope.categoryname == null) {
			$scope.domainname = null;
			$scope.subdomainname = null;
		}
		if ($scope.domainname == undefined || $scope.domainname == undefined)
			$scope.subdomainname = null;

		return ($scope.categoryname != undefined || $scope.categoryname != null) &&
			($scope.domainname != undefined || $scope.domainname != null) && ($scope.subdomainname != undefined || $scope.subdomainname != null);
	}


	if ($scope.show == false) {
		$location.search({});
	}

	var chosenSubdomain = function () {
		$location.search("domainid", selectedItems.domainId);
		$location.search("subdomainid", selectedItems.subDomainId);
	}


	$scope.cancel = function () {
		$scope.show = false;
		$location.search({});
		$scope.categoryname = "";
		$scope.domainname = "";
		$scope.subdomainname = "";
		$scope.domains = null;
		$scope.subdomains = null;
	}

	var getFields = function () {

		if (selectedItems.domainId != null || selectedItems.subDomainId != null) {

			$http.get(
				"public/returnFieldsForTicket/?domainid="
				+ selectedItems.domainId + "&subdomainid="
				+ selectedItems.subDomainId).success(
				function (response) {
					$scope.fields = response;
					$scope.domainfields = $scope.fields.domainfields;
					$scope.subdomainfields = $scope.fields.subdomainfields;
					$scope.show = true;
					$scope.dynamicForm.$submitted = false;

					if ($scope.domainfields.length == 0
						&& $scope.subdomainfields.length == 0)
						$scope.showbutton = false;
					else
						$scope.showbutton = true;

				}).error(function (response) {
					Toasty.error("", "Something went wrong. Please try again.");
				})


		}
	}


	$scope.$on("FilesEvent", function (event, args) {
		$scope.fileId = args.fileId;
	})

	$scope.addTicket = function () {
		/*if($scope.fileId!=undefined)
		$scope.raiseticket.fileId=angular.copy($scope.fileId);*/

		$scope.disableSubmit = true;
		if (!$scope.isFormInvalid()) {

			$http(
				{
					method: 'POST',
					url: "/public/raiseTicket/?domainid="
					+ selectedItems.domainId + "&subdomainid="
					+ selectedItems.subDomainId + "&fileId=" + $scope.fileId,

					data: $scope.raiseticket
				}).then(function (response) {
					$scope.disableSubmit = false;
					$scope.raiseticket = {};
					$scope.raiseticket.multipleValues = [];
					$location.search({});
					Toasty.info("", "Ticket #" + response.data + " rasied successfully.");
					$state.go("/mytickets");

				}, function (response) {
					$scope.disableSubmit = false;
					Toasty.error("", "Something went wrong. Please try again.");
				});
		}
	}


	$scope.raiseticket.multipleValues = [];
	$scope.searchableSubdomains = [];

	$scope.getSubDomains = function () {
		getSubDomainService.getSubDomains.success(function (response) {
			$scope.searchableSubdomains = response;
		}).error(function (response) {
		})
	}


	$scope.toggleSelected = function (id, data, type) {

		var flag = false;
		var index = 0;
		for (var i = 0; i < $scope.raiseticket.multipleValues.length; i++) {

			if ($scope.raiseticket.multipleValues[i].id == id
				&& $scope.raiseticket.multipleValues[i].type == type) {
				index = i;
				flag = true;
				break;

			}
		}

		if (flag) {
			var dataindex = $scope.raiseticket.multipleValues[index].data
				.indexOf(data);
			if (dataindex == -1)
				$scope.raiseticket.multipleValues[index].data.push(data);
			else
				$scope.raiseticket.multipleValues[index].data.splice(dataindex,
					1);
		} else
			$scope.raiseticket.multipleValues.push({
				id: id,
				type: type,
				data: [data]
			});
	}


	$scope.listTickets = function () {
		var config = requestCanceller.cancelPromise().config;
		var page = $scope.currentPage - 1;
		$http.get("public/getTickets?PI=" + page + "&PG=" + $scope.pageSize + "&searchParams=" + JSON.stringify($scope.search), config).success(function (response) {
			requestCanceller.deferred = null;
			$scope.tickets = response.content;
			$scope.allTaskCount = response.totalElements;
			$scope.totalItems = response.totalElements;
			$scope.showPagination = response.totalPages > 1;
		}).error(function (response) {
			
		})
	}

	$scope.listTickets();

	$scope.setPage = function () {
		$scope.listTickets();
	}


	$scope.label = function (flag) {
		return (flag) ? 'control-label' : 'normal-label';
	}

	$scope.setSize = function (currentSize) {
		$scope.pageSize = currentSize;
		$scope.listTickets();
	}

	$scope.isInvalid = function (ctrl, flag) {
		if (flag) {
			if ($scope.dynamicForm && $scope.dynamicForm.$submitted) {
				var elementError = $scope.dynamicForm[ctrl] && $scope.dynamicForm[ctrl].$invalid;
				var autoCompleteError = $scope.dynamicForm.$error[ctrl] ? true : false;

				return (elementError || autoCompleteError) ? 'has-error' : '';
			}
			return '';
		}
	}

	$scope.isFormInvalid = function () {
		return $scope.dynamicForm.$invalid;
	}

})