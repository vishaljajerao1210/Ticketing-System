app.controller('subdomainscontroller',
				function ($scope, $http, $location, $stateParams,
		getdomainsservice, utility, validation, $parse, getGroupMasters) {
		utility.tooltip();
		$scope.showform = false;
		$scope.groupname = "";
		$scope.domainname = "";
		$scope.groupId = "";
		$scope.id = "";
		$scope.search = {};
		$scope.search.searchStr = "";
		$scope.maxSize = 5;
		$scope.currentPage = 1;
		$scope.pageSize = 10;
		$scope.invalid = false;
		$scope.subdomain = {};

		$scope.fetchAutoCompleteResults = {
			getGroups: function (val) {
				getGroupMasters.groupMasters(val).success(function (response) {
					$scope.response = response;
				}).error(function (response) {
					Toasty.error("", "Error occured while saving domain");
				})

				return $scope.response;
			},
			getDomains: function (val) {
				getdomainsservice.domains(val).success(function (response) {
					$scope.response = response.content;
				});
				return $scope.response;
			},
			getUsers: function (val) {
				return $http.get("admin/getManagers/?q=" + val).then(
					function (response) {
						return response.data;
					})
			}
		}

		$scope.selectItems = {
			domainId: function ($item) {
				$scope.id = $item.id;
				if ($scope.subdomain.subDomainDescription)
					$scope.checkValid($scope.subdomain.subDomainDescription);
			},
			groupId: function ($item) {
				$scope.groupId = $item.id;
			},
			user: function ($item) {
				$scope.subdomain.defaultExpert = $item;
			}
		}


		$scope.selectionType = function ($item) {
			if ($scope.defaultActorType[0].flag)
				$scope.selectItems.user($item);
			else if ($scope.defaultActorType[1].flag)
				$scope.selectItems.groupId($item);
		}

		$scope.autoCompleteDecision = function (val) {
			if ($scope.defaultActorType[0].flag)
				return $scope.fetchAutoCompleteResults.getUsers(val);
			else if ($scope.defaultActorType[1].flag)
				return $scope.fetchAutoCompleteResults.getGroups(val);
		}

		$scope.subdomain = {};

		$scope.defaultActorType = [{ name: "Expert", flag: false }, { name: "Expert Group", flag: false }]
		$scope.subdomain.isManagerApproval = false;
		$scope.subdomain.isSpecialManagerApproval = false;

		$scope.toggleManagerSelected = function () {
			if ($scope.subdomain.isManagerApproval)
				$scope.subdomain.isManagerApproval = false;
			else
				$scope.subdomain.isManagerApproval = true;
		}

		$scope.toggleSpecialManagerSelected = function () {
			if ($scope.subdomain.isSpecialManagerApproval)
				$scope.subdomain.isSpecialManagerApproval = false;
			else
				$scope.subdomain.isSpecialManagerApproval = true;


		}

		$scope.selected = function (val) {
			$scope.subdomainform.$submitted = false;

			if (val == "Expert") {
				$scope.defaultActorType[0].flag = true;
				$scope.defaultActorType[1].flag = false;
			}
			else {
				$scope.defaultActorType[0].flag = false;
				$scope.defaultActorType[1].flag = true;
			}
		}


		$scope.searchResult = function () {
			$scope.getSubDomains();
			$scope.showform = false;
		}

		$scope.setPage = function () {

			$scope.getSubDomains();
			$scope.showform = false;
		}

		$scope.getSubDomains = function () {
			var page = $scope.currentPage - 1;
			$http.get("public/subdomains/?PI=" + page + "&PG=" + $scope.pageSize + "&searchParam=" + $scope.search.searchStr).then(function (response) {
				$scope.subdomains = response.data.content;
				$scope.allTaskCount = response.data.totalElements;
				$scope.totalItems = response.data.totalElements;
				$scope.showPagination = response.data.totalPages > 1;
			}, function (response) {

			});

		}

		$scope.viewSubDomain = function (index) {

			$scope.subdomain = $scope.subdomains[index];

			if ($scope.subdomain.defaultUserGroup != null && $scope.subdomain.isGroup) {
				$scope.groupname = $scope.subdomain.defaultUserGroup.groupName;
				$scope.actorType = $scope.defaultActorType[1].name;
				$scope.defaultActorType[0].flag = false;
				$scope.defaultActorType[1].flag = true;
			}
			else {
				$scope.groupname = "";
				$scope.actorType = $scope.defaultActorType[0].name;
				$scope.defaultActorType[0].flag = true;
				$scope.defaultActorType[1].flag = false;
			}

			if ($scope.subdomain.domains != null)
				$scope.domainname = $scope.subdomain.domains.domainDescription;
			else
				$scope.domainname = "";
			$scope.showform = true;
			$scope.viewOnly = true;
			$scope.invalid = false;
		}

		$scope.editable = function (id, event) {
			$scope.invalid = false;
			$scope.viewOnly = false;
			$scope.editSubDomain(id, event)

		}


		$scope.getSubDomains();

		$scope.add = function () {

			$scope.showform = true;
			$scope.viewOnly = false;
			$scope.editMode = false;
			$scope.subdomain = {};
			$scope.domainname = "";
			$scope.groupname = "";
			$scope.actorType = "";
			$scope.invalid = false;
			$scope.id = "";
			$scope.subdomainform.$submitted = false;
			$scope.defaultActorType[0].flag = false;
			$scope.defaultActorType[1].flag = false;
		}

		$scope.editModeSubDomain = function (data) {

			$scope.subdomain = data;
			$scope.invalid = false;
			$scope.domainname = $scope.subdomain.domains.domainDescription;
			if ($scope.subdomain.defaultUserGroup != null && $scope.subdomain.isGroup) {
				$scope.groupname = $scope.subdomain.defaultUserGroup.groupName;
				$scope.defaultActorType[0].flag = false;
				$scope.defaultActorType[1].flag = true;
				$scope.actorType = $scope.defaultActorType[1].name;

			}
			else {
				$scope.groupname = "";
				$scope.defaultActorType[0].flag = true;
				$scope.actorType = $scope.defaultActorType[0].name;
				$scope.defaultActorType[1].flag = false;
			}
			$scope.showform = true;
			$scope.viewOnly = true;

		}
		$scope.expertObj = {};

		$scope.addSubDomain = function () {
			if (!$scope.isFormInvalid() && !$scope.invalid) {
				$http(
					{
						method: 'POST',
						url: '/admin/addSubDomain/?id='
						+ $scope.id + '&groupId=' + $scope.groupId + "&actorType=" + $scope.actorType,
						data: $scope.subdomain
					}).then(function (response) {
						Toasty.info("", "Sub-domain saved successfully!");
						$scope.showform = false;
						$scope.subdomain = {};
						$scope.getSubDomains();
					}, function (response) {
						Toasty.error("", "Error occured while saving sub-domain");
					});
			}
		}

		var index = 0;
		$scope.editSubDomain = function (index, event) {
			$scope.showform = true;
			$scope.viewOnly = false;
			event.stopPropagation();
			$http
				.get("public/getSubDomainById/" + index)
				.then(
				function (response) {

					$scope.subdomain = response.data;
					$scope.domainname = $scope.subdomain.domains.domainDescription;

					if ($scope.subdomain.defaultUserGroup != null && $scope.subdomain.isGroup) {
						$scope.groupname = $scope.subdomain.defaultUserGroup.groupName;
						$scope.defaultActorType[0].flag = false;
						$scope.actorType = $scope.defaultActorType[1].name;
						$scope.defaultActorType[1].flag = true;
					}
					else {
						$scope.groupname = "";
						$scope.defaultActorType[0].flag = true;
						$scope.actorType = $scope.defaultActorType[0].name;
						$scope.defaultActorType[1].flag = false;
					}

					$scope.editMode = true;
				}, function (response) {
					Toasty.error("", "Error occured while saving domain");
				})
		}

		$scope.cancel = function () {
			$scope.showform = false;
			$scope.viewOnly = false;
			$scope.id = "";
		}

		$scope.isInvalid = function (ctrl, form) {
			return validation.valid(ctrl, form);
		}

		$scope.isFormInvalid = function () {
			return $scope.subdomainform.$invalid;
		}

		$scope.checkValid = function (subdomain) {
			$http.get("admin/checkSubDomain/?subdomain=" + subdomain + "&id=" + $scope.id).success(
				function (response) {
					$scope.invalid = response.status;
					if ($scope.invalid)
						$scope.subdomainform.subdomain.$invalid = true;
					else
						$scope.subdomainform.subdomain.$invalid = false;
				}).error(function () {
					$scope.invalid = false;
				});
		}


				})