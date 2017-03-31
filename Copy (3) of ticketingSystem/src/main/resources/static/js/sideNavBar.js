app.directive('sideBar',
				function($http,$location, $stateParams, $timeout,$log,$state,
						FileUploader, $compile,getApprovalHistory,getFieldsForTicket,getGlyphicon,searchOptions,getsubdomainsservice) {
	
	return {
		restrict: 'E',
		templateUrl : '/navBarOptions.html',
		scope : {
			getOptions:'=getOptions'
		},
		controller : function($http, $scope, $attrs) {
			
			
			$scope.isActive = function(viewLocation) {
				
				var active2=false;
				
				if(viewLocation=="master.listmasters")
					active2=(viewLocation ==$state.current.name||$state.current.url=="/listvalues/:listId")
				else
				active2 = (viewLocation ==$state.current.name);
				return active2;
			}
			
		}
		}
	
	
});
