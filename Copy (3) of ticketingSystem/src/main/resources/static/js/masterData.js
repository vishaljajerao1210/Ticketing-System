/**
 * 
 */
app.controller(
				'mastercontroller',
				function($scope, $rootScope, $http, $location, $stateParams,$state,
						getdomainsservice, utility, validation, $parse,getGroupMasters,getSubDomainService,getstatusmasterservice) {
	
					$scope.menuOptions=[{
						sref:"master.categories",
						name:"Categories"
					},
					{
						sref:"master.domains",
						name:"Domains"
					},
					{
						sref:"master.subdomains",
						name:"Sub-Domains"
						
					},
					{
						
						sref:"master.domainfields",
						name:"Domain Fields"
					},
					{
						sref:"master.subdomainfields",
						name:"Sub-Domain Fields"
					},
					{
						sref:"master.listmasters",
						name:"Lists"
					},
				
					{
						sref:"master.configureWorkflow",
						name:"Configure Workflow"
					}
					]
					

					if($state.current.url=="/master"||$state.current.url=="/categories")
					$state.go("master.categories");
					
					
				});
				