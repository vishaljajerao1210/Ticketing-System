/**
 * 
 */
app.controller('testcontroller', function($scope, $rootScope, $http,
		$location, $stateParams,$q) {
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
