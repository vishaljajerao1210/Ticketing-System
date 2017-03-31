app.controller('actionablescontroller', function ($scope, $rootScope, $http, $state, requestCanceller,
	$location, $stateParams) {

	$scope.groups = {};
	$scope.currentPage = 1;
	$scope.pageSize = 10;
	$scope.maxSize = 5;
	$scope.search = {};
	$scope.pageSizeOptions = ['10', '15', '20'];
	$scope.groupTasks = [];
	$scope.viewDetails = false;

	if ($stateParams.id) {
		$scope.viewDetails = true;
	}

	$scope.getGroups = function () {
		$http.get("user/getGroups").success(function (response) {
			$scope.groups = response;
			if (!$location.search().group)
				$location.search("group", $scope.groups[0].id);
			$scope.getGroupTasksByGroup();
		})
	}
	$scope.getGroups();

	$scope.selected = function (code) {
		$location.search("group", code);
		$scope.viewDetails = false;
		$scope.search = {};
		$scope.search.searchStr="";
		$state.current.data.id = $location.search().group;
		$scope.getGroupTasksByGroup();
	}

	if ($location.search().group) {
		$state.current.data.id = $location.search().group;
		$scope.currentGroup = $location.search().group;
	}

	$scope.getGroupTasksByGroup = function () {
		var config = requestCanceller.cancelPromise().config;
		var page = $scope.currentPage - 1;
		if ($location.search().group) {
			$http.get("/user/userGroupTasks/?paramsId=" + $location.search().group + "&PI=" + page + "&PG=" + $scope.pageSize + "&searchParams=" + JSON.stringify($scope.search), config).success(function (response) {
				$scope.groupTasks = response.content;
				$scope.allTaskCount = response.totalElements;
				$scope.totalItems = response.totalElements;
				$scope.showPagination = response.totalPages > 1;
				requestCanceller.deferred = null;
			}).error(function (response) {

			})

		}
	}

	$scope.setActive = function (param) {
		return $location.search().group == param;
	}

	$scope.setPage = function () {
		$scope.getGroupTasksByGroup();
	}

	$scope.getCurrentGroup = function () {
		return $scope.currentGroup;
	}


	$rootScope.$on('$stateChangeStart',
		function (event, toState, toParams, fromState, fromParams) {
			if (fromState.url == "/myactionables/:id" || fromState.url == "/myactionables") {
				if (fromState.data)
					$location.search("group", fromState.data.id);
			}
		})



	var getTaskCount = function () {
		$http.get("/user/getTaskCountForUser").success(function (response) {
			$scope.taskCount = response;
		}).error(function (response) {
			Toasty.error("", "Something went wrong. Please reload the page.");
		})
	}
	getTaskCount();
	$scope.showCount = function (groupName) {
		if ($scope.taskCount)
			return $scope.taskCount[groupName];
		else return null;
	}

})