app.factory("utility", function () {
	return {
		tooltip: function () {
			$('[data-toggle="tooltip"]').tooltip({
				delay: {
					hide: 500
				}
			})
		}
	}

})


app.factory('getcategoriesservice', function ($http) {
	return {
		categories: function (val) {
			return $http.get('public/categories/?PI=0&PG=10&searchParam=' + val);

		}
	}
});

app.factory('getdomainsservice', function ($http) {
	return {
		domains: function (val) {
			return $http.get('public/domains/?PI=0&PG=10&searchParam=' + val);
		}
	}
});

app.factory('getlistmasters', function ($http) {
	return {
		listmasters: function (val) {
			return $http.get('public/getAllListMasters/?PI=0&PG=10&searchParam=' + val);
		}
	}
});

app.factory('getsubdomainsservice', function ($http) {
	return {
		subdomains: function (val) {
			return $http.get('public/subdomains/?PI=0&PG=10&searchParam=' + val);

		}
	}
});



app.service('getFieldOptions', function () {
	this.getOptions = function () {


		var fieldTypes = [{
			fieldtype: "text"
		}, {
			fieldtype: "textarea"
		}, {
			fieldtype: "date"
		},

		{
			fieldtype: "checkbox"
		},

		{
			fieldtype: "dropdown"
		}]

		return fieldTypes;


	}
})




app.factory('getstatusmasterservice', function ($http) {
	return {
		getStatuses: function () {
			return $http.get('public/getStatusMaster');
		}
	}
});

app.service('getUsersService', function ($http) {
	return {
		getUsers: function (userName) {
			return $http.get('admin/getManagers/?q=' + userName);
		}
	}
});


app.filter('momentDate', function () {
	return function (dateString, format) {

		if (dateString == null) {
			return null;
		} else {
			return moment(dateString).format(format);
		}
	};
});
app.filter('shortDate', ['$rootScope', '$filter', function ($rootScope, $filter) {
	return function (date) {

		var shortDateFormat = $filter("momentDate");
		return shortDateFormat(date, $rootScope.shortDateFormat);

	};
}]);


app.filter('longDate', ['$rootScope', '$filter', function ($rootScope, $filter) {
	return function (date) {

		var longDateFormat = $filter("momentDate");
		return longDateFormat(date, $rootScope.shortDateFormat);

	};
}]);

app.factory('validation', function () {

	return {

		valid: function (ctrl, form) {

			if (form && form.$submitted) {
				var elementError = form[ctrl] && form[ctrl].$invalid;
				var autoCompleteError = form.$error[ctrl] ? true : false;

				return (elementError || autoCompleteError) ? 'has-error' : '';
			}
			return '';

		}
	}
});


app.factory('getApprovalHistory', function ($http) {

	return {
		approvalHistory: function (id) {
			return $http.get('/public/getApprovalHistory/?id=' + id);
		}
	}
});



app.factory('action', function ($http) {
	return {

		performAction: function (id, status, comments) {
			return $http.post("/public/CompleteTask/?id=" + id + "&statusCode=" + status + "&comments=" + comments)
		}
	}

})

app.factory('searchOptions', function ($http) {
	return {
		getResult: function (beforeDate, afterDate, subdomain) {

			return $http.get("public/getSearchResults/?startDate=" + beforeDate + "&endDate=" + afterDate + "&subdomain=" + subdomain)
		}
	}


})

app.factory('getSubDomainService', function ($http) {
	return {
		getSubDomains: function () {
			return $http.get("public/subdomains");
		}
	}
})






app.factory('getGroupMasters', function ($http) {

	return {

		groupMasters: function (val) {

			return $http.get("public/getGroups/?searchParam=" + val);
		}
	}
});

app.factory('getFieldsForTicket', function ($http) {

	return {
		getInfo: function (id) {
			return $http.get("public/getFieldsForTicket/" + id)
		}
	}

})


app.service('getUser', function ($http, $q) {
	var deferred = $q.defer();
	this.getLoggedInUser = function () {
		$http({
			method: 'GET',
			url: '/public/login',
		}).then(function (response) {
			deferred.resolve(response);

		}, function (response) {
			deferred.reject('Unauthorised');

		});
		return deferred.promise;
	}
})















app.factory('getSymbol', function () {

	return {
		getGlyphiconSymbol: function (code) {
			switch (code) {
			case "STS01":
				return "glyphicon glyphicon-thumbs-up";
			case "STS04":
			case "STS08":
			case "STSO7":
			case "STSO9":
			case "STS10":
				return "glyphicon glyphicon-ok";
			case "STS03":
			case "STS05":
			case "STS06":
			case "STS11":
				return "glyphicon glyphicon-time";
			case "STS02":
				return "glyphicon glyphicon-thumbs-down";
			}


		}


	}


})














app.factory('getGlyphicon', function () {
	return {
		getSymbol: function (code) {
			switch (code) {
			case 'STS01':
				return 'text-Success';
			case 'STS02':
			case 'STS11':
				return 'text-danger';
			case 'STS03':
			case 'STS05':
			case 'STS06':
			case 'STS07':
			case 'STS09':
				return 'text-info';
			case 'STS04':
			case 'STS08':
			case 'STS10':
				return 'text-success';
			default:
				return '';


			}


		}

	}
})

app.service('getAlert', function () {

	this.getObject = function () {

		var alert = {
				success: "Operation sucessful !",
				failure: "Operation failed, try again."
		}

		return alert;

	}

})




/*app.service('fileErrorMessage', function (ngToast) {
	this.promptMessage = function () {
		ngToast.create({
			className: 'danger',
			positionClass: 'toast-top-right',
			content: 'Failed to upload, file size exceeded',
			timeout: 3000
		});
	}
})*/









/*app.service('message', function (getAlert, ngToast) {



	this.printSuccessMessage = function () {

		ngToast.create({
			className: 'success',
			positionClass: 'toast-top-right',
			content: '<i class="material-icons md-18 padding-right-sm text-strong" style="top:4px">done</i>' + getAlert.getObject().success,
			//dismissOnTimeout : false,

		});

	}

	this.printErrorMessage = function () {

		ngToast.create({
			className: 'danger',
			positionClass: 'toast-top-right',
			content: '<i class="material-icons md-18 padding-right-sm text-strong" style="top:4px">priority_high</i>' + getAlert.getObject().failure,

		});

	}




})*/


app.service("requestCanceller", function ($q) {
	var cancellerObj = {
			deferred: null,
			config: { "timeout": null },
			cancelPromise: function () {
				if (this.deferred != null) {
					this.deferred.resolve();
				}
				this.deferred = $q.defer();
				this.config.timeout = this.deferred.promise;
				return cancellerObj;
			}
	}
	return cancellerObj;
})