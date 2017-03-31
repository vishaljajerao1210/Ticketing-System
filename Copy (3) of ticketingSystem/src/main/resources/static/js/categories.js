app.controller('categoriescontroller', function ($scope, $http,
	$location, $stateParams, utility, validation) {

	utility.tooltip();
	$scope.showform = false;
	$scope.search = {};
	$scope.search.searchStr = "";
	$scope.category = {};
	$scope.viewOnly = false;
	$scope.editMode = false;
	$scope.currentPage = 1;
	$scope.pageSize = 10;
	$scope.maxSize = 5;
	$scope.pageSizeOptions = ['1', '5', '10', '15', '20'];

	var getCategories = function () {
		var page = $scope.currentPage - 1;
		$http.get("public/categories/?PI=" + page + "&PG=" + $scope.pageSize + "&searchParam=" + $scope.search.searchStr).then(function (response) {
			$scope.categories = response.data.content;
			$scope.allTaskCount = response.data.totalElements;
			$scope.totalItems = response.data.totalElements;
			$scope.showPagination = response.data.totalPages > 1;
		}, function (response) {


		});
	}
	$scope.clearSearch = function () {
		$scope.search.searchStr = "";
	}

	$scope.setPage = function () {
		getCategories();
		$scope.showform = false;
	}

	$scope.searchResult = function () {
		getCategories();
		$scope.showform = false;
	}

	$scope.setSize = function (currentSize) {
		$scope.pageSize = currentSize;
		getCategories();
	}

	$scope.viewCategory = function (category) {
		$scope.id = $scope.categories.indexOf(category);
		$scope.category = $scope.categories[$scope.id];
		$scope.invalid = false;
		$scope.showform = true;
		$scope.viewOnly = true;
	}

	$scope.editable = function (id, event) {
		$scope.invalid = false;
		$scope.viewOnly = false;
		$scope.editCategory(id, event);
	}

	$scope.editModeCategory = function (data) {
		$scope.invalid = false;
		$scope.category = data;
		$scope.showform = true;
		$scope.viewOnly = true;
	}

	$scope.invalid = false;
	$scope.checkvalid = function (categoryname) {
		$http.get("admin/checkCategory/?category=" + categoryname).success(
			function (response) {
				$scope.invalid = response.status;
				if ($scope.invalid) {
					$scope.categoryform.category.$invalid = true;

				}
			}).error(function () {
				$scope.invalid = false;
			});


	}


	getCategories();

	$scope.add = function () {
		$scope.showform = true;
		$scope.viewOnly = false;
		$scope.editMode = false;
		$scope.category = {};
		$scope.invalid = false;
		$scope.categoryform.$submitted = false;

	}

	$scope.addCategory = function () {
		$scope.showform = true;
		if (!$scope.isFormInvalid() && !$scope.invalid) {
			$http({
				method: 'POST',
				url: '/admin/addCategory',

				data: $scope.category
			}).then(function (response) {
				$scope.showform = false;
				getCategories();
				Toasty.info("", "Category saved successfully!");
			}, function (response) {
				Toasty.error("", "Error occured while saving category");
			});
		}
	}
	var index = 0;

	$scope.editCategory = function (index, event) {

		$scope.showform = true;
		$scope.viewOnly = false;
		event.stopPropagation();

		$http.get("public/getCategoryById/" + index).then(function (response) {
			$scope.category = response.data;
			$scope.editMode = true;
			Toasty.info("", "Category saved successfully!");
		}, function (response) {
			Toasty.error("", "Error occured while saving category");
		})
	}

	$scope.cancel = function () {
		$scope.showform = false;
		$scope.viewOnly = false;
	}


	$scope.isInvalid = function (ctrl, form) {
		return validation.valid(ctrl, form);
	}

	$scope.isFormInvalid = function (form) {
		return $scope.categoryform.$invalid;
	}

})