app.controller('domainscontroller', function ($scope, $http,
	$location, $stateParams, getcategoriesservice, utility, validation) {

	utility.tooltip();
	$scope.showform = false;


	$scope.id = "";
	$scope.search = {};
	$scope.categories = [];
	$scope.search.searchStr = "";
	$scope.domain = {};
	$scope.currentPage = 1;
	$scope.pageSize = 10;
	$scope.maxSize = 5;

	$scope.searchResult = function () {
		$scope.getDomains();
		$scope.showform = false;
	}

	$scope.selectedItem = function ($item) {
		$scope.id = $item.id;
	}

	$scope.setPage = function () {

		$scope.getDomains();
		$scope.showform = false;
	}


	$scope.getDomains = function () {
		var page = $scope.currentPage - 1;
		$http.get("public/domains/?PI=" + page + "&PG=" + $scope.pageSize + "&searchParam=" + $scope.search.searchStr).then(function (response) {
			$scope.domains = response.data.content;
			$scope.allTaskCount = response.data.totalElements;
			$scope.totalItems = response.data.totalElements;
			$scope.showPagination = response.data.totalPages > 1;
		}, function (response) {

		});
	}

	$scope.getCategories = function (val) {

		getcategoriesservice.categories(val).success(function (response) {
			$scope.response = response.content;
		});
		return $scope.response;

	}

	$scope.getDomains();

	$scope.add = function () {
		$scope.showform = true;
		$scope.viewOnly = false;
		$scope.editMode = false;
		$scope.domain = {};
		$scope.categoryname = "";
		$scope.domainform.$submitted = false;
		$scope.domainform.domain.$error.duplicateValue = false;
	}
	$scope.addDomain = function () {

		if (!$scope.isFormInvalid()) {
			$http({
				method: 'POST',
				url: '/admin/addDomain/?id=' + $scope.id,

				data: $scope.domain
			}).then(function (response) {

				Toasty.info("", "Domain saved successfully!");
				$scope.showform = false;
				$scope.domain = {};
				$scope.getDomains();
				Toasty.info("", "Domain saved successfully!");
			}, function (response) {
				Toasty.error("", "Error occured while saving domain");
			});
		}
	}

	$scope.editable = function (id, event) {
		$scope.viewOnly = false;
		$scope.editDomain(id, event);

	}

	$scope.checkvalid = function (domainname) {
		$http.get("admin/checkDomain/?domain=" + domainname + "&categoryId=" + $scope.id).success(
			function (response) {
				if (response) {
					$scope.domainform.domain.$setValidity("duplicateValue", true);
				} else {
					$scope.domainform.domain.$setValidity("duplicateValue", false);
				}
			}).error(function () {
				$scope.invalid = false;
			});

	}


	$scope.viewDomain = function (index) {
		$scope.domain = $scope.domains[index];
		$scope.categoryname = $scope.domain.categories.categoryDescription;
		$scope.domainform.domain.$error.duplicateValue = false;
		$scope.showform = true;
		$scope.viewOnly = true;
	}

	$scope.editModeDomain = function (data) {
		$scope.domain = data;
		$scope.categoryname = $scope.domain.categories.categoryDescription;
		$scope.showform = true;
		$scope.viewOnly = true;
	}

	var index = 0;
	$scope.editDomain = function (index, event) {
		$scope.showform = true;
		$scope.viewOnly = false;
		event.stopPropagation();
		$scope.domainform.domain.$error.duplicateValue = false;
		$http.get("public/getDomainById/" + index).then(function (response) {
			$scope.domain = response.data;
			$scope.categoryname = $scope.domain.categories.categoryDescription;
			$scope.editMode = true;
			Toasty.info("", "Domain saved successfully!");
		}, function (response) {
			Toasty.error("", "Error occured while saving domain");
		})

	}
	$scope.cancel = function () {
		$scope.showform = false;
		$scope.viewOnly = false;
	}

	$scope.isInvalid = function (ctrl, form) {
		return validation.valid(ctrl, form);
	}

	$scope.isFormInvalid = function () {
		return $scope.domainform.$invalid;
	}

})