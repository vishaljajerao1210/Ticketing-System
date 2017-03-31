app.controller('reportController', function($scope, $scope, $http,
		$location, $stateParams,utility,validation,ngToast,message) {
	
	$scope.data=[
	 	                {
		                    "label": "Food",
		                    "value": "285040",
		                    "color":"#008ee4"
		                }, 
		                {
		                    "label": "Apparels",
		                    "value": "146330",
		                    "color": "#6baa01"
		                }, 
		                {
		                    "label": "Electronics",
		                    "value": "105070",
		                    "color": "#e44a00"
		                }
		                
		            ]
	
	

	        $scope.myDataSource= {
	            "chart": {
	                "caption": "Split of revenue by product categories",
	                "subCaption": "Last year",
	                "numberPrefix": "$",
	                "showPercentValues": "1",
	                "showPercentInTooltip": "0",
	                "decimals": "1",
	                //Theme
	                "showCanvasBorder": "0",
	                "showBorder": "0",
	                "bgColor": "#ffffff",
	                "use3DLighting":"1",
	                "startingAngle": "45",
	                "theme": "fint"
	            }
	            
	        }
	   
	    
$scope.myDataSource.data=angular.copy($scope.data);
	
	
	
	
	
	
	
});