app.controller('subdomainfieldscontroller', function ($scope, $http,
	$location, $stateParams, getsubdomainsservice, getlistmasters, utility, validation, getFieldOptions) {

	utility.tooltip();

	$scope.id = "";
	$scope.listId = "";
	$scope.subdomainfield = {};
	$scope.maxSize = 5;
	$scope.search = {};
	$scope.listmastername = "";
	$scope.search.searchStr = "";
	$scope.currentPage = 1;
	$scope.pageSize = 10;
	$scope.fetchAutoCompleteResults = {

		getSubDomains: function (val) {
			getsubdomainsservice.subdomains(val).success(function (response) {
				$scope.response = response.content;
			});
			return $scope.response;
		},
		getListMasters: function (val) {
			getlistmasters.listmasters(val).success(function (response) {
				$scope.response = response.content;
			});
			return $scope.response;
		}
	}



	$scope.selectItems = {
		subdomainId: function ($item) {
			$scope.id = $item.id;
		},
		listMasterId: function ($item) {
			$scope.listId = $item.id;
		}
	}

	$scope.fieldTypes = getFieldOptions.getOptions();


	$scope.add = function () {
		$scope.showform = true;
		$scope.viewOnly = false;
		$scope.editMode = false;
		$scope.subdomainfield = {};
		$scope.subdomainname = "";
		$scope.listmastername = "";
		$scope.subdomainfieldform.$submitted = false;
	}


	$scope.searchResult = function () {
		$scope.getFields();
		$scope.showform = false;
	}

	$scope.setPage = function () {
		$scope.getFields();
		$scope.showform = false;
		$scope.listmastername = "";
		$scope.show = false;
	}

	$scope.getFields = function () {
		var page = $scope.currentPage - 1;
		$http.get("public/getFieldsOfAllSubDomains/?PI=" + page + "&PG=" + $scope.pageSize + "&searchParam=" + $scope.search.searchStr).then(function (response) {
			$scope.subdomainfields = response.data.content;
			$scope.allTaskCount = response.data.totalElements;
			$scope.totalItems = response.data.totalElements;
			$scope.showPagination = response.data.totalPages > 1;
		}, function (response) {
			Toasty.error("", "Something went wrong. Please reload the page.");
		});
	}
	$scope.getFields();

	$scope.editSubDomainField = function (id, event) {
		$scope.showform = true;
		$scope.viewOnly = false;
		event.stopPropagation();
		$http.get("/public/getFieldOfSubDomain/" + id).then(function (response) {
			$scope.subdomainfield = response.data;
			$scope.subdomainname = $scope.subdomainfield.subdomains.subDomainDescription;
			if ($scope.subdomainfield.isList) {
				$scope.show = true;
				$scope.listmastername = $scope.subdomainfield.listmaster.listMasterValue;
				$scope.listId = $scope.domainfield.listmaster.id;
			}
			$scope.editMode = true;
		}, function (response) {
			Toasty.error("", "Error occured while saving domain");
		});
	}

	$scope.editable = function (id, event) {
		$scope.viewOnly = false;
		$scope.editSubDomainField(id, event);
	}

	$scope.checkIfList = function (list) {
		if (list == "checkbox" || list == "radio" || list == "dropdown")
			return true;
		else {
			$scope.listmastername = "";
			return false;
		}
	}

	$scope.editModeSubDomain = function (data) {
		$scope.subdomainfield = data;
		$scope.subdomainname = $scope.subdomainfield.subdomains.subDomainDescription;
		$scope.showform = true;
		$scope.viewOnly = true;
		if ($scope.subdomainfield.isList) {
			$scope.show = true;
			$scope.listmastername = $scope.subdomainfield.listmaster.listMasterValue;
			$scope.listId = $scope.domainfield.listmaster.id;
		}
		else {
			$scope.show = false;
			$scope.listmastername = "";
		}
	}

	$scope.view = function (index) {
		$scope.showform = true;
		$scope.viewOnly = true;
		$scope.subdomainfield = $scope.subdomainfields[index];
		$scope.subdomainname = $scope.subdomainfield.subdomains.subDomainDescription;

		if ($scope.subdomainfield.isList) {
			$scope.show = true;
			$scope.listmastername = $scope.subdomainfield.listmaster.listMasterValue;
			$scope.listId = $scope.subdomainfield.listmaster.id;
		}
		else {
			$scope.show = false;
			$scope.listmastername = "";
		}
	}

	$scope.addSubDomainField = function () {
		if (!$scope.isFormInvalid()) {

			$http({
				method: 'POST',
				url: '/admin/addSubDomainFields/?id=' + $scope.id + "&listId=" + $scope.listId,
				data: $scope.subdomainfield
			}).then(function (response) {
				Toasty.info("", "Field saved successfully!");
				$scope.getFields();
				$scope.subdomainfield = {};
				$scope.showform = false;
			}, function (response) {
				Toasty.error("", "Error occured while saving");
			});
		}
	}


	$scope.cancel = function () {
		$scope.showform = false;
		$scope.viewOnly = false;
	}

	$scope.saveFieldId = function (id) {

		$scope.fieldId = id;
	}

	$scope.toggleStatus = function () {
		$http.get("admin/toggleSubDomainFieldStatus/?id=" + $scope.fieldId).success(function (response) {
			$("#activate").modal('hide');
			$scope.getFields();
		}).error(function (response) {

		})
	}

	$scope.isInvalid = function (ctrl, form) {
		return validation.valid(ctrl, form);

	}

	$scope.isFormInvalid = function () {
		return $scope.subdomainfieldform.$invalid;
	}


})