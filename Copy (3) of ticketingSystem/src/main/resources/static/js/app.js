var app = angular.module('app', ['ui.router', 'upload', 'angular-loading-bar', 'ui.bootstrap', 'ngVis',
	'ui.bootstrap.datetimepicker'])

	.config(function ($stateProvider, $httpProvider, $urlRouterProvider) {

		$urlRouterProvider.otherwise('/');
		$stateProvider
			.state('/', {
				url: '/',
				templateUrl: 'home.html'
			})
			.state('/home', {
				url: '/home',
				templateUrl: 'home.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			})
			.state('/tickets', {
				url: '/tickets',
				templateUrl: 'tickets.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('/raisenewticket', {
				url: '/raisenewticket',
				templateUrl: 'raiseNewTicket.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('/mytickets', {
				url: '/mytickets',
				templateUrl: 'myTicket.html',
				data: {
					url: 'public/getTickets'
				},
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('/mytickets/:id', {
				parentName: 'mytickets',
				url: '/mytickets/:id',
				templateUrl: 'myTicket.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('/myactionables', {
				url: '/myactionables',
				templateUrl: 'myActionables.html',
				data: { id: "" },
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}
			}).state('/myactionables/:id', {
				parentName: 'myactionables',
				url: '/myactionables/:id',
				data: { id: "" },
				templateUrl: 'myActionables.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('master', {
				url: '/master',
				templateUrl: 'masterData.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			})
			.state('master.categories', {
				parentName: 'master',
				url: '/categories',
				templateUrl: 'categories.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('master.expertDispositioning', {
				parentName: 'master',
				url: '/expertDispositioning',
				templateUrl: 'expertDispositioningMaster.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('master.configureWorkflow', {
				parentName: 'master',
				url: '/configureWorkflow',
				templateUrl: 'configureWorkflow.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('master.domains', {
				url: '/domains',
				templateUrl: 'domains.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('master.subdomains', {
				parentName: 'master',
				url: '/subdomains',
				templateUrl: 'subDomains.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('master.domainfields', {
				parentName: 'master',
				url: '/domainfields',
				templateUrl: 'domainFields.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('master.subdomainfields', {
				parentName: 'master',
				url: '/subdomainfields',
				templateUrl: 'subDomainFields.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('/reports', {
				url: '/reports',
				templateUrl: 'reports.html',

				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			})
			.state('master.listmasters', {
				parentName: 'master',
				url: '/listmasters',
				templateUrl: 'listMasters.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('master.listvalues', {
				parentName: 'master',
				url: '/listvalues/:listId',
				templateUrl: 'listValues.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('/groups', {
				url: '/groups',
				templateUrl: 'groups.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('usergroups', {
				url: '/usergroups',
				templateUrl: 'userGroups.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('/ticketdesk', {
				url: '/ticketdesk',
				templateUrl: 'ticketDesk.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('/personaltasks', {
				url: '/personaltasks',
				data: { id: "" },
				templateUrl: 'personalTask.html',
				data: {
					url: '/user/getPersonalTasks'
				},

				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('/ticketdesk/:id', {
				url: '/ticketdesk/:id',
				templateUrl: 'ticketDesk.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			}).state('/personaltasks/:id', {
				parentName: 'personaltasks',
				url: '/personaltasks/:id',
				data: { id: "" },
				templateUrl: 'personalTask.html',
				resolve: {
					access: ['UserStatus', function (UserStatus) {
						return UserStatus.isAuthenticated();
					}],
				}

			})
		$httpProvider.interceptors.push('authInterceptor');
		$httpProvider.defaults.headers.common['X-Requested-With'] =
			'XMLHttpRequest';
	})


app.controller('loginController', function ($rootScope, $http, $location, $state, $scope) {

	$scope.auth = {};
	$rootScope.authenticated = false;
	$scope.isActive = function (viewLocation) {
		return (viewLocation == $state.current.name) || (viewLocation == "/" + $state.current.parentName);
	}

	$scope.login = function () {

		var authData = $scope.auth.userName + ':' + $scope.auth.password;
		var encodedAuthData = btoa(authData);

		$http({
			method: 'GET',
			url: '/public/login',
			headers: {
				'Authorization': 'Basic ' + encodedAuthData
			}
		}).then(function (response) {
			$rootScope.response = response.data;
			$scope.errormessage = false;

			if ($rootScope.path)
				$location.path($rootScope.path);
			else
				$location.url("/home");

			$rootScope.path = null;

			$rootScope.authenticated = true;
			$rootScope.authenticationChecked = true;

			$scope.auth = {};
			if ($rootScope.response) {

				$rootScope.user = true;
				$rootScope.admin = false;
			} else {
				$rootScope.admin = true;
				$rootScope.user = false;

			}

		}, function (response) {
			$rootScope.authenticated = false;
			$scope.errormessage = true;
		});

	}

	$rootScope.hasRole = function (role) {
		var flag = false;
		if ($rootScope.response.userRoles.length > 0 && $rootScope.authenticated) {
			angular.forEach($rootScope.response.userRoles, function (v, i) {
				if (v.roleType == role) {
					flag = true;
				}

			})
		}
		return flag;
	}

	$rootScope.logOut = function () {

		$http({
			method: 'POST',
			url: 'logout',
		}).then(function (response) {

		});
		$rootScope.authenticated = false;
		$rootScope.admin = false;
		$rootScope.user = false;
		$scope.errormessage = false;
		$location.search({});
		$location.path("/")
		$rootScope.path = null;
	}

})

app.run(function ($rootScope, $http, $location, $state) {

	$rootScope.path = null;
	$rootScope.shortDateFormat = "DD-MM-YYYY";
	$rootScope.longDateFormat = " DD-MM-YYYY HH:mm:ss";

	$rootScope.$on("$stateChangeError", function (event, current, previous,
		rejection) {
		$rootScope.path = $location.url();
		$location.path("/home");
	})
})


app.service('authInterceptor', function ($q, $rootScope, $location) {
	var service = this;
	service.request = function (config) {
		return config;
	};
	service.responseError = function (response) {
		if (response.status == 401) {
			$rootScope.authenticated = false;
			$location.path("/");
		}
		return $q.reject(response);
	};
})

app.factory('UserStatus', ['$q', '$rootScope', '$http',
	function ($q, $rootScope, $http) {

		return {
			isAuthenticated: function () {
				var deferred = $q.defer();
				if ($rootScope.authenticationChecked) {
					if ($rootScope.authenticated) {
						deferred.resolve('OK');
					} else {
						$location.url("/");
						deferred.reject('Unauthorized');
					}
				} else {
					$http.get('/public/login').success(function (response) {
						deferred.resolve('OK');
						$rootScope.authenticationChecked = true;
						$rootScope.authenticated = true;
						$rootScope.response = response;
					}).error(function () {
						deferred.reject('Unauthorized');
					})
				}
				return deferred.promise;
			}
		}
	}]);