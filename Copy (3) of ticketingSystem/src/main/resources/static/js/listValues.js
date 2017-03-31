app.controller('listvaluecontroller', function ($scope, $http,
	$location, $stateParams, getlistmasters, utility, validation) {

	utility.tooltip();
	$scope.id = "";
	$scope.search = {};
	$scope.search.searchStr = "";
	$scope.maxSize = 5;
	$scope.currentPage = 1;
	$scope.pageSize = 1;
	$scope.listvalue = {};

	$scope.fetchAutoCompleteResults = {
		getListMasters: function (val) {
			getlistmasters.listmasters(val).success(function (response) {
				$scope.response = response.content;
			});
			return $scope.response;
		}
	}

	$scope.selectItems = {
		listMasterId: function ($item) {
			$scope.id = $item.id;
		}
	}

	$scope.searchResult = function () {
		$scope.getListValues();
		$scope.showform = false;
	}

	$scope.setPage = function () {
		$scope.getListValues();
		$scope.showform = false;
	}

	var getListMasters = function(){
		$http.get("public/getListMasterById/" + $stateParams.listId).success(function (response) {
			$scope.listMaster = response;
			$scope.editMode = true;
		}).error(function (response) {
		})
	}
	getListMasters();

	$scope.getListValues = function () {
		var page = $scope.currentPage - 1;
		$http.get("public/getListValuesByListId/?listId=" + $stateParams.listId + "&searchParam=" + $scope.search.searchStr).success(function (response) {
			$scope.listvalues = response;
		}).error(function (response) {

		})
	}
	$scope.getListValues();

	$scope.add = function () {
		$scope.showform = true;
		$scope.viewOnly = false;
		$scope.editMode = false;
		$scope.listvalue = {};
		$scope.listmastervalue = "";
		$scope.listvalueform.$submitted = false;
	}

	$scope.view = function (index) {
		$scope.showform = true;
		$scope.viewOnly = true;
		$scope.listvalue = $scope.listvalues[index];
		$scope.listmastervalue = $scope.listvalue.listmaster.listMasterValue;
	}

	$scope.editable = function (id, event) {
		$scope.viewOnly = false;
		$scope.editListValue(id, event);
	}

	$scope.editListValue = function (id, event) {

		$scope.showform = true;
		$scope.viewOnly = false;
		event.stopPropagation();

		$http.get("public/getListValueById/" + id).success(function (response) {
			$scope.listvalue = response;
			$scope.listmastervalue = $scope.listvalue.listmaster.listMasterValue;
			$scope.editMode = true;
			Toasty.info("", "Value saved successfully!");
		}).error(function (response) {
			Toasty.error("", "Error occured while saving.");
		})
	}

	$scope.editModeListValue = function (data) {
		$scope.listvalue = data;
		$scope.listmastervalue = $scope.listvalue.listmaster.listMasterValue;
		$scope.showform = true;
		$scope.viewOnly = true;
	}


	$scope.addListValue = function () {
		$scope.listvalue.listmaster = { "id": $stateParams.listId };
		$scope.showform = true;

		if (!$scope.isFormInvalid()) {
			$http({
				method: 'POST',
				url: '/admin/addListValues/?id=' + $scope.id,

				data: $scope.listvalue
			}).then(function (response) {
				Toasty.info("", "Value saved successfully!");
				$scope.showform = false;
				$scope.getListValues();
			}, function (response) {
				Toasty.error("", "Error occured while saving.");
			});
		}
	}

	$scope.cancel = function () {
		$scope.showform = false;
		$scope.viewOnly = false;
	}

	$scope.isInvalid = function (ctrl, form) {
		return validation.valid(ctrl, form)
	}

	$scope.isFormInvalid = function () {
		return $scope.listvalueform.$invalid;
	}


})