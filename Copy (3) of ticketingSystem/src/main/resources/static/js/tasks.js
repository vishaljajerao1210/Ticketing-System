app.controller('taskcontroller', function($scope, $rootScope, $http,$state,
		$location, $stateParams,$q, getcategoriesservice ,utility,validation,getApprovalHistory,action,getFieldsForTicket,getGlyphicon) {
	
	
	$scope.tasks={};
	
	$scope.getTasks=function()
	{
	
	$http.get("/user/userGroupTasks").success(function(response){
		
		$scope.tasks=response;
	
	}).error(function(response){
		Toasty.error("", "Error occured while saving domain");
	})
	
	}
	
	
	$scope.getTasks();
	
	

$scope.comments="";

if($stateParams.id)
	{
	$scope.viewDetails=true;
	}







	
})