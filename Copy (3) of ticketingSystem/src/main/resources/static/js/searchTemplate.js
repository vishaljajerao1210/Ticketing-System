app.directive('searchTemplate',
				function ($http, $location, $state, $timeout, $log, validation) {
		return {
			restrict: 'E',
			templateUrl: '/searchTemplate.html',
			scope: {
				search: "=searchObj",
				getData: "&"
			},
			controller: function ($http, $scope, $attrs) {

				$scope.currentRoute = $state.current.name;
				$scope.filtersShown = false;
				$scope.curDate = new Date().getTime();
				$scope.search = {
					"searchStr": "",
					"isSearched": false
				};
				$scope.formattedDates = {};

				$scope.resetFilters = function () {
					$scope.filterForm.$setPristine();
					$scope.filtersShown = false;
					angular.forEach($scope.search, function (val, key) {
						if (key === "isSearched" || key === "searchStr") {

						} else {
							$scope.search[key] = null;
						}
					})
					$scope.filtersApplied = false;
					$timeout(function () {
						$scope.getSearchResult();
					})
				}

				var setSearched = function () {
					$scope.search.isSearched = false;
					angular.forEach($scope.search, function (val, key) {
						if (key !== "isSearched") {
							if (val !== null && val !== "")
								$scope.search.isSearched = true;
						}
					})
				}
				$scope.getSearchResult = function () {
					$scope.getData();
					setSearched();
				}
				setSearched();

				$scope.applySearch = function () {
					if ($scope.filterForm.$valid) {
						$scope.filtersShown = false;
						$scope.filtersApplied = true;
						$scope.getSearchResult();
					}
				}

				$scope.showDates = function () {
					if ($scope.search.startDate && $scope.search.endDate
						&& $scope.search.startDate !== "" && $scope.search.startDate !== null
						&& $scope.search.endDate !== "" && $scope.search.endDate !== null)
						return true;
					else
						return false;
				}
				$scope.showStatus = function () {
					if ($scope.search.status && $scope.search.status !== "" && $scope.search.status !== null)
						return true;
					else
						return false;
				}


				$scope.longDateFormat = "DD-MM-YYYY HH:mm:ss";

				$scope.showFilters = function () {
					$scope.filtersShown = !$scope.filtersShown;
				}

				$scope.isInvalid = function (ctrl, form) {
					return validation.valid(ctrl, form);
				}

			}
		}
	})