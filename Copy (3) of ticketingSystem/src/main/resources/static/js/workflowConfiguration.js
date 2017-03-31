app.controller("workflowController", function ($scope, $http, getstatusmasterservice, getUsersService, getGroupMasters, validation) {

    $scope.pageRequest = {
        currentPage: 1,
        pageSize: 10,
        maxSize: 5,
        setPage : function() { 
	 $scope.fetchDispositions.setDispositions();
	     }
    }
    $scope.dispositionItem = {};

    $scope.fetchDataForAutoComplete = {
        getSubDomainsByCategory: function (subDomainName) {
            return $http.get("public/subdomains?PI=0&PG=5&searchParam=" + subDomainName).then(
                function (response) {
                    return response.data.content;
                })
        }
    }

    $scope.fetchDispositions = {
        requestServer: function () {
            var page = $scope.pageRequest.currentPage - 1;
            return $http.get("public/getDispositionings?PI=" + page + "&PG=" + $scope.pageRequest.pageSize +
                "&subDomainId=" + $scope.selectedSubDomain.id)
        },
        setDispositions: function () {
            $scope.fetchDispositions.requestServer().success(function (response) {
                $scope.dispositions = response.content;
                $scope.totalItems = response.totalElements;
                $scope.showPagination = response.totalPages > 1;
            }).error(function (response) {
                
            })
        }
    }

    var fetchStatuses = function () {
        getstatusmasterservice.getStatuses().then(function (response) {
            $scope.statuses = response.data;
        })
    }
    fetchStatuses();

    $scope.fetchUserInformation = {
        getUsers: function (userName) {
            return getUsersService.getUsers(userName).then(function (response) {
                return response.data;
            })
        },
        getGroups: function (groupName) {
            return getGroupMasters.groupMasters(groupName).then(function (response) {
                return response.data;
            })
        }
    }

    $scope.editData = function (diposition, isView) {

        $scope.isView = isView;
        $scope.dispositionItem = angular.copy(diposition);

    }
    $scope.updatedRecord = {};

    $scope.toggleStatus = function (dispositioning) {
        $scope.updatedRecord = angular.copy(dispositioning);
    }

    $scope.getStatus = function () {
        return $scope.updatedRecord.isShowToUser;
    }

    $scope.updateRecord = function (isView) {
        $scope.isView = isView;
        $scope.dispositionItem = angular.copy($scope.updatedRecord);
        $scope.saveDisposition();
    }


    $scope.options = {
        isTerminal: [
            { value: true, label: 'Yes' },
            { value: false, label: 'No' },
        ],
        isGroup: [
            { value: true, label: 'Group' },
            { value: false, label: 'User' },
        ]
    };

    $scope.saveDisposition = function () {
        $scope.dispositionForm.$setSubmitted();
        if($scope.dispositionForm.$valid){
        $scope.dispositionItem.subdomainDispositioning = $scope.selectedSubDomain;
        $http.post("/admin/saveDisposition", $scope.dispositionItem).then(
            function (response) {
                $scope.dispositionItem = {};
                $('#addDisposition').modal('hide');
                $("#activate").modal('hide');
                $scope.updatedRecord = {};
                $scope.fetchDispositions.setDispositions();
                Toasty.info("", "Disposition save successfully!");
            }, function(response){
                Toasty.error("", "Error occured while saving");
            }
        )
        }
    }

    $scope.addDisposition = function () {

        $scope.isView = false;
        $scope.isFirst = false;
        if ($scope.dispositions.length == 0) {
            $scope.isFirst = true;
            if ($scope.selectedSubDomain.actorType === 'Expert') {
                $scope.dispositionItem.isCurrentGroup = false;
                $scope.dispositionItem.currentAssignedUser = $scope.selectedSubDomain.defaultExpert;
            }
            else if ($scope.selectedSubDomain.actorType === 'Expert Group') {
                $scope.dispositionItem.isCurrentGroup = true;
                $scope.dispositionItem.currentAssignedGroup = $scope.selectedSubDomain.defaultUserGroup;
            }
        }
    }

    $('#addDisposition').on('hidden.bs.modal', function (e) {
        $scope.dispositionItem = {};
        $scope.dispositionForm.$submitted = false;
    })

    $scope.isInvalid = function(ctrl,form){
         return validation.valid(ctrl,form);
    } 

})