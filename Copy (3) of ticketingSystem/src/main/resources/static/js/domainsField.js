app.controller('domainfieldscontroller', function ($scope, $http,
	$location, $stateParams, getdomainsservice, getlistmasters, utility, validation, getFieldOptions) {

	utility.tooltip();

	$scope.id = "";
	$scope.listId = "";
	$scope.domainfield = {};
	$scope.search = {};
	$scope.search.searchStr = "";
	$scope.currentPage = 1;
	$scope.pageSize = 10;
	$scope.maxSize = 5;

	$scope.fetchAutoCompleteResults = {

		getDomains: function (val) {
			getdomainsservice.domains(val).success(function (response) {
				$scope.response = response.content;
			});
			return $scope.response;
		},
		getListMasters: function (val) {
			getlistmasters.listmasters(val).success(function (response) {
				$scope.response = response.content;
			})
			return $scope.response;
		}
	}


	$scope.selectItems = {

		domainId: function ($item) {
			$scope.id = $item.id;
		},
		listMasterId: function ($item) {
			$scope.listId = $item.id;
		}
	}


	$scope.add = function () {
		$scope.showform = true;
		$scope.viewOnly = false;
		$scope.editMode = false;
		$scope.domainfield = {};
		$scope.domainname = "";
		$scope.show = false;
		$scope.domainfieldform.$submitted = false;
	}


	$scope.fieldTypes = getFieldOptions.getOptions();

	$scope.listmastername = "";
	$scope.checkIfList = function (list) {
		if (list == "checkbox" || list == "radio" || list == "dropdown")
			return true;
		else {
			$scope.listmastername = "";
			return false;
		}
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
		$http.get("public/getFieldsOfAllDomains/?PI=" + page + "&PG=" + $scope.pageSize + "&searchParam=" + $scope.search.searchStr).then(function (response) {
			$scope.domainfields = response.data.content;
			$scope.allTaskCount = response.data.totalElements;
			$scope.totalItems = response.data.totalElements;
			$scope.showPagination = response.data.totalPages > 1;
		}, function (response) {
			Toasty.error("", "Error occured while saving domain");
		});
	}

	$scope.getFields();

	$scope.editDomainField = function (id, event) {
		$scope.showform = true;
		$scope.viewOnly = false;
		event.stopPropagation();
		$http.get("/public/getFieldOfDomain/" + id).then(function (response) {
			$scope.domainfield = response.data;
			$scope.domainname = $scope.domainfield.domain.domainDescription;
			if ($scope.domainfield.isList) {
				$scope.show = true;
				$scope.listmastername = $scope.domainfield.listmaster.listMasterValue;
				$scope.listId = $scope.domainfield.listmaster.id;
			}
			$scope.editMode = true;
		}, function (response) {

		});

	}


	$scope.editModeDomainField = function (data) {
		$scope.domainfield = data;
		$scope.domainname = $scope.domainfield.domain.domainDescription;
		$scope.showform = true;
		$scope.viewOnly = true;
		if ($scope.domainfield.isList) {
			$scope.show = true;
			$scope.listmastername = $scope.domainfield.listmaster.listMasterValue;
			$scope.listId = $scope.domainfield.listmaster.id;
		}
		else {
			$scope.show = false;
			$scope.listmastername = "";
		}

	}

	$scope.editable = function (id, event) {
		$scope.viewOnly = false;
		$scope.editDomainField(id, event);
	}


	$scope.view = function (index) {
		$scope.showform = true;
		$scope.viewOnly = true;
		$scope.domainfield = $scope.domainfields[index];
		$scope.domainname = $scope.domainfield.domain.domainDescription;
		if ($scope.domainfield.isList) {
			$scope.show = true;
			$scope.listmastername = $scope.domainfield.listmaster.listMasterValue;
			$scope.listId = $scope.domainfield.listmaster.id;
		}
		else {
			$scope.show = false;
			$scope.listmastername = "";
		}
	}


	$scope.addDomainField = function () {
		if (!$scope.isFormInvalid()) {

			$http({
				method: 'POST',
				url: '/admin/addDomainFields/?id=' + $scope.id + "&listId=" + $scope.listId,
				data: $scope.domainfield
			}).then(function (response) {
				Toasty.info("", "Field saved successfully!");
				$scope.getFields();
				$scope.domainfield = {};
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
		$http.get("admin/toggleDomainFieldStatus/?id=" + $scope.fieldId).success(function (response) {
			$("#activate").modal('hide');
			$scope.getFields();
		}).error(function (response) {
			Toasty.error("", "Error occured while saving domain");
		})
	}


	$scope.isInvalid = function (ctrl, form) {
		return validation.valid(ctrl, form);
	}

	$scope.isFormInvalid = function () {
		return $scope.domainfieldform.$invalid;
	}

})