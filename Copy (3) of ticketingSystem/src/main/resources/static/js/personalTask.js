app.controller('personalTaskController', function ($scope, $rootScope, $http, $state, requestCanceller,
	$location, $stateParams, $q) {

	var requestCanceller = requestCanceller;
	$scope.tasks = [];
	$scope.task = {};
	$scope.currentPage = 1;
	$scope.pageSize = 1;
	$scope.maxSize = 5;
	$scope.completedTasks = [];
	var cancellingPromise = null;
	
	$scope.pendingTaskspage = {
		pSize: 10,
		currentPage: 1,
		pageSizeOptions: ['10', '15', '20']
	}
	
	$scope.completedTaskspage = {
		pSize: 10,
		currentPage: 1,
		pageSizeOptions: ['10', '15', '20']
	}
	
	$scope.searchPendingTasks = {};
	$scope.searchCompletedTasks = {};


	if ($stateParams.id) {
		$scope.viewDetails = true;
	}

	$scope.getTasks = function () {
		var config = requestCanceller.cancelPromise().config;
		var page = $scope.pendingTaskspage.currentPage - 1;
		$http.get("/user/getPersonalTasks?PI=" + page + "&PG=" + $scope.pendingTaskspage.pSize + "&searchParams=" + JSON.stringify($scope.searchPendingTasks), config).success(function (response) {
			requestCanceller.deferred = null;
			$scope.tasks = response.content;
			$scope.pendingTaskspage.allTaskCount = response.totalElements;
			$scope.pendingTaskspage.totalItems = response.totalElements;
			$scope.pendingTaskspage.showPagination = response.totalPages > 1;
		}).error(function (response) {
		})
	}

	$scope.getTasks();

	$scope.setCurrentPage = function () {
		$scope.getTasks();
	}


	$scope.getCompletedTasks = function () {
		if (cancellingPromise != null) {
			cancellingPromise.resolve();
		}
		cancellingPromise = $q.defer();
		var config = {
			"timeout": cancellingPromise.promise,
		}
		page = $scope.completedTaskspage.currentPage - 1;
		var promise = $http.get("/user/getCompletedTasks?PI=" + page + "&PG=" + $scope.completedTaskspage.pSize + "&searchParams=" + JSON.stringify($scope.searchCompletedTasks), config)

		promise.success(function (response) {
			cancellingPromise = null;
			$scope.completedTasks = response.content;
			$scope.completedTaskspage.allTaskCount = response.totalElements;
			$scope.completedTaskspage.totalItems = response.totalElements;
			$scope.completedTaskspage.showPagination = response.totalPages > 1;
		}).error(function (response) {
			cancellingPromise = null;
		})

	}

	$rootScope.$on('$stateChangeStart',
		function (event, toState, toParams, fromState, fromParams) {
			if (fromState.url == "/personaltasks" || fromState.url == "/personaltasks/:id") {
				if (fromState.data)
					$location.search("currentTab", fromState.data.id);
			}
		})
	$scope.getCompletedTasks();


	$scope.setCompletedPage = function () {
		$scope.getCompletedTasks();
	}

	var getTaskCount = function () {
		if ($rootScope.authenticated) {
			$http.get("/public/dashboardStats").success(function (response) {
				$scope.taskStats = response;
			}).error(function (response) {
			})
		}
	}
	getTaskCount();


	$scope.setParam = function (param) {
		$location.search("currentTab", param);
		$scope.searchCompletedTasks={};
		$scope.searchCompletedTasks.searchStr="";
		$scope.searchPendingTasks={};
		$scope.searchPendingTasks.searchStr="";
		$state.current.data.id = $location.search().currentTab;
		
		if($location.search().currentTab=="completedTasks")
			$scope.getCompletedTasks();
		else
			$scope.getTasks();
	}

	if ($location.search().currentTab) {
		$state.current.data.id = $location.search().currentTab;
	}


	if (!$location.search().currentTab) {
		$scope.setParam('pendingTasks');
	}
	$scope.setActive = function (param) {
		return $location.search().currentTab == param;
	}

})