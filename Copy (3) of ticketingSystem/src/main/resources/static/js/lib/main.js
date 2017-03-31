var app = angular.module('app', ['ngVis']);

app.controller('MainCtrl', ['$scope',function($scope) {

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
  }
]);