/**
 * 
 */

app.controller(
				'expertDispositionController',
				function($scope, $rootScope, $http, $location, $stateParams,
						getdomainsservice, utility, validation, $parse,getGroupMasters,getSubDomainService,getstatusmasterservice) {
					utility.tooltip();
					$scope.showform = false;
					
					
					getSubDomainService.getSubDomains().success(function(response){
						$scope.subdomains=response;
						
					})
					
					
					getGroupMasters.groupMasters().success(function(response){
						$scope.groups=response;
					})
					
					
					getstatusmasterservice.getStatuses().success(function(response){
						$scope.statuses=response;
						
					})
					
					
					
					$scope.subdomainsexpertmaster=[];
					
					$scope.getDispositionings=function()
					{
						
						$http.get("public/getDispositionings").success(function(response){
							$scope.subdomainsexpertmaster=response;
							
						}).error(function(response){
							Toasty.error("", "Error occured while saving");
						})
						
						
					}
					
					$scope.ActorType=[{name:"Expert",currentActorflag:false,nextActorflag:false},{name:"Expert Group",currentActorflag:false,nextActorflag:false}]
					
					
					
					
					$scope.expertMaster={};
					
					
					
					
					$scope.nextSelected=function(val)
					{
						
						if(val==$scope.ActorType[1].name)
							{
						$scope.ActorType[1].nextActorflag=true;
						$scope.ActorType[0].nextActorflag=false;
						
							}
						else
							{
							$scope.ActorType[0].nextActorflag=true;
							$scope.ActorType[1].nextActorflag=false;
					}
					}
					
					
					
					
					
					
					
					$scope.currentSelected=function(val)
					{
						if(val==$scope.ActorType[1].name)
						{
					$scope.ActorType[1].currentActorflag=true;
					$scope.ActorType[0].currentActorflag=false;
					
						}
					else
						{
						$scope.ActorType[0].currentActorflag=true;
						$scope.ActorType[1].currentActorflag=false;
				}
					}
					
					
					$scope.getDispositionings();
					
					
					$scope.addDisposition=function()
					{
						
						
						$http.post("admin/saveDispositioning/?subdomain="+$scope.subdomain+"&nextGroup="+$scope.nextGroup+"&status="+$scope.status+"&currentGroup="+$scope.currentGroup,$scope.expertMaster).success(function(response){
							Toasty.info("", "Domain saved successfully!");
							$scope.subdomain="";
							$scope.nextGroup="";
							$scope.currentGroup="";
							$scope.status="";
							$scope.expertmaster={};
							$scope.showform=false;
							$scope.getDispositionings();
						}).error(function(response){
							Toasty.error("", "Error occured while saving");
						})
						
						
					}
					
					
					$scope.editExpertDispositioning = function(id) {
						
						$http
								.get("admin/getExpertDispositioningById/"+id)
								.then(
										function(response) {

											$scope.data = response.data;
											$scope.subdomain = $scope.data.subdomainDispositioning.subDomainDescription;
											$scope.status=$scope.data.currentStatus.statusName;
											$scope.expertMaster.assignedActor=$scope.data.assignedActor;
											$scope.expertMaster.nextActor=$scope.data.nextActor;
											$scope.expertMaster.id=$scope.data.id;
										
											if($scope.data.isNextGroup==true)
												{

												$scope.ActorType[1].nextActorflag=true;
												$scope.ActorType[0].nextActorflag=false;
												$scope.nextGroup=$scope.data.nextUserGroup.groupName;
												}
											else
												{
												
												$scope.ActorType[1].nextActorflag=false;
												$scope.ActorType[0].nextActorflag=true;
												}
											
											if($scope.data.isCurrentGroup==true)
												{
												$scope.ActorType[1].currentActorflag=true;
												$scope.ActorType[0].currentActorflag=false;
												$scope.currentGroup=$scope.data.currentAssignedGroup.groupName;
												
												}
											else
												{
												$scope.ActorType[1].currentActorflag=false;
												$scope.ActorType[0].currentActorflag=true;
												}
												
											$scope.showform = true;
											
											


											
										}, function(response) {
											alert("failed")
										})
					}
					
					
					
					
					
					
					$scope.add = function() {

						$scope.showform = true;
						$scope.viewOnly = false;
						$scope.editMode=false;
						$scope.subdomain="";
						$scope.nextGroup="";
						$scope.currentGroup="";
						$scope.status="";
						
						
						 $scope.ActorType[1].nextActorflag=false;
						 $scope.ActorType[0].nextActorflag=false;
						 $scope.ActorType[1].currentActorflag=false;
						 $scope.ActorType[0].currentActorflag=false;
					}
					
					$scope.cancel=function()
					{
						$scope.showform = false;
					}
					
					$scope.data = {
	                        "nodes": [{
	                            "id": "0",
	                            "label": "Start",
	                            "size": 10,
	                            "status":"BEGIN",
	                            "color": "#ddd",
	                            "shape": "circle",
	                            "shadow": true
	                        },{
	                            "id": "1",
	                            "label": "New",
	                            "size": 10,
	                            "color": "#hih",
	                            "shape": "square",
	                            "shadow": true
	                        }],
	                        "edges": [{"from" : "0", "to" : "1", "id": "asdads"} ]
	                    };
					
				});