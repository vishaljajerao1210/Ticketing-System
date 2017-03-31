app.controller('homeController', function ($scope, $rootScope, $http) {

	$scope.groupTasks = {};
	$scope.personalTasks = {};
	$scope.taskCount = {};

	var getGroupTasks = function () {
		if ($rootScope.authenticated) {
			$http.get("/user/getAllGroupTasks").success(function (response) {
				$scope.groupTasks = response.content;
			}).error(function (response) {
				Toasty.error("", "Something went wrong. Please try again.");
			})
		}

	}

	var getPersonalTasks = function () {
		if ($rootScope.authenticated) {
			$http.get("/user/getPersonalTasks/?PI=0&PG=4&searchParams=" + JSON.stringify({})).success(function (response) {
				$scope.personalTasks = response.content;
			}).error(function (response) {
				Toasty.error("", "Something went wrong. Please try again.");
			})

		}
	}

	var getTaskCount = function () {
		if ($rootScope.authenticated) {
			$http.get("/public/dashboardStats").success(function (response) {
				$scope.dashboardStats = response;

			}).error(function (response) {
				Toasty.error("", "Something went wrong. Please try again.");
			})
		}

	}

	getGroupTasks();
	getPersonalTasks();
	getTaskCount();

});