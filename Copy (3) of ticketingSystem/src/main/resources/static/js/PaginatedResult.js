/**
 * 
 */

app.directive('paginatedUrl',
		function($http,$location, $stateParams, $timeout,$log,$state,
				FileUploader, $compile,getApprovalHistory,getFieldsForTicket,getGlyphicon,searchOptions,getsubdomainsservice) {
	return {
		restrict: 'E',
		templateUrl : '/pageTemplate.html',
		scope:{
			url:"@",
			data:"=",
			paramsId:"&",
			dynamicQueryObject:"="
		},
		controller : function($http, $scope, $attrs) {
			var page="";


			$scope.currentPage=1;
			$scope.pageSize=1;
			$scope.maxSize=5;
			var requestUrl="";







			$scope.$on("triggerRequest",function(event){


				$scope.getResult();
			})



			$scope.getResult=function(){
				page=$scope.currentPage-1;

				var requestUrl=$scope.url+"?PI="+page+'&PG='+$scope.pageSize;


				if($location.search().group)
					requestUrl=$scope.url+"?PG="+$scope.pageSize+'&PI='+page+"&paramsId="+$location.search().group;


				$http.get(requestUrl)
				.success(function(data){

					$scope.allTaskCount=data.totalElements;


					$scope.data=data.content;

					
					$scope.totalItems = data.totalElements;

				})

			}
			$scope.getResult();


			$scope.setPage = function() { 

				$scope.getResult();
			}


			$scope.pageSizeOptions=['1','5','10','15','20'];


			$scope.setSize=function(currentSize) { 
				$scope.pageSize=currentSize;
				$scope.getResult();
			}





		}


	}		
});