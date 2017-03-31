app.controller('listmastercontroller', function ($scope, $http,
	$location, $stateParams, getdomainsservice, utility, validation) {

	utility.tooltip();
	$scope.search = {};
	$scope.search.searchStr = "";
	$scope.maxSize = 5;
	$scope.currentPage = 1;
	$scope.pageSize = 1;
	$scope.listmaster = {};

	$scope.add = function () {
		$scope.showform = true;
		$scope.viewOnly = false;
		$scope.editMode = false;
		$scope.listmaster = {};
		$scope.listmasterform.$submitted = false;
	}


	$scope.searchResult = function () {
		$scope.getListMasters();
		$scope.showform = false;
	}

	$scope.setPage = function () {
		$scope.showform = false;
		$scope.getListMasters();
	}

	$scope.getListMasters = function () {
		var page = $scope.currentPage - 1
		$http.get("public/getAllListMasters/?PI=" + page + "&PG=" + $scope.pageSize + "&searchParam=" + $scope.search.searchStr).success(function (response) {
			$scope.listmasters = response.content;
			$scope.allTaskCount = response.totalElements;
			$scope.totalItems = response.totalElements;
			$scope.showPagination = response.totalPages > 1;
		}).error(function (response) {
		})
	}

	$scope.getListMasters();


	$scope.addListMaster = function () {
		if (!$scope.isFormInvalid()) {

			$scope.showform = true;
			$http({
				method: 'POST',
				url: '/admin/addListMasters',
				data: $scope.listmaster
			}).then(function (response) {

				Toasty.info("", "List saved successfully!");
				$scope.showform = false;
				$scope.getListMasters();
			}, function (response) {
				Toasty.error("", "Error occured while saving");
			});
		}
	}

	$scope.editModeListMaster = function (data) {
		$scope.listmaster = data;
		$scope.showform = true;
		$scope.viewOnly = true;
	}

	$scope.cancel = function () {
		$scope.showform = false;
		$scope.viewOnly = false;
	}

	$scope.editListMaster = function (id, event) {
		$scope.showform = true;
		$scope.viewOnly = false;
		event.stopPropagation();
		$http.get("public/getListMasterById/" + id).success(function (response) {
			$scope.listmaster = response;
			$scope.editMode = true;
		}).error(function (response) {
		})


	}
	$scope.view = function (index) {
		$scope.listmaster = $scope.listmasters[index];
		$scope.showform = true;
		$scope.viewOnly = true;
	}

	$scope.editable = function (id, event) {
		$scope.viewOnly = false;
		$scope.editListMaster(id, event);
	}


	$scope.isInvalid = function (ctrl, form) {
		return validation.valid(ctrl, form);

	}

	$scope.isFormInvalid = function () {
		return $scope.listmasterform.$invalid;
	}

})