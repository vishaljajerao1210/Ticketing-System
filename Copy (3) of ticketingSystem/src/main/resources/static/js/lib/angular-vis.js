angular.module('ngVis', [])


/**
 * Factory for processing data 
 */

    .service('interface', function() {
        var curNode = "";
        var curNodeIndex = "";
        var curEdge = "";
        var curEdgeIndex = "";
        var observerCallbacks = [];
        var isNode ;
        var isEdge ;
        var isNodeValid;

        var ALPHABET = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';

        var ID_LENGTH = 8;

       function guidGenerator() {
            var randLetter = String.fromCharCode(65 + Math.floor(Math.random() * 26));
            var uniqid = randLetter + Date.now();
            return uniqid;
        }

         //call this when you know 'foo' has been changed
        var notifyObservers = function(param){
            angular.forEach(observerCallbacks, function(callback){
                if(param === "isNode")
                    callback(isNode,param,curNode);
                else if(param == "isEdge")
                    callback(isEdge,param,curEdge);
            });
        };        

        var executeCallback = function (param) {
            notifyObservers(param);
        }

        var events = {
            click : function (props) {
            	
                if(props.nodes.length == 0){
                    curNode = null;
                    isNode = false;
                }
                else{

                    isNode = true;
                    console.warn('Event "click" triggered. Setting curNode');
                    curNode = props.nodes[0];
                    
                    executeCallback(curNode);
                }
                
                if(props.edges.length == 0){
                    curEdge = null;
                    isEdge = false;
                }
                else{
                    isEdge = true;
                    console.warn('Event "click" triggered. Setting curEdge');
                    curEdge = props.edges[0];
                }
                
            },
                        
            doubleclick : function (props) {
                console.warn('Event "doubleclick" triggered');
                console.log.apply(console, arguments);
            },
            
            oncontext : function (props) {
                if(props.nodes.length == 0){
                    curNode = null;
                    isNode = false;
                    executeCallback("isNode");
                }
                else{
                    isNode = true;
                    console.warn('Event "click" triggered. Setting curNode');
                    curNode = props.nodes[0];
                    executeCallback("isNode");
                }
                
                if(props.edges.length == 0){
                    curEdge = null;
                    isEdge = false;
                    executeCallback("isEdge");
                }
                else{
                    isEdge = true;
                    console.warn('Event "click" triggered. Setting curEdge');
                    curEdge = props.edges[0];
                    executeCallback("isEdge");
                }
                console.log.apply(console, arguments);
            }
        };
        var options  = {
                            autoResize: true,
                            edges:{
                                arrows: {
                                    to:     {enabled: true, scaleFactor:1, type:'arrow'},
                                    middle: {enabled: false, scaleFactor:0.5, type:'arrow'},
                                    from:   {enabled: false, scaleFactor:1, type:'arrow'}
                                }
                            }
                             
                        };
       

                function _showModal(name){
                    name = '#'+name
                    $(name).modal('show');
                }
                function _showDropDown(){
                    $('.dropdown-toggle').dropdown('toggle')
                }
                function _hideModal(name){
                    name = '#'+name
                    $(name).modal('hide');
                }
        return {
                /** 
                 * This function initiates the Joyride. Always assign the returnValue to valid JOYRIDE OBJECT.
                 *  
                 * @param : config - Joyride Object Configuration
                 * @param : isNextStep - @optional , step() with @eventName : "nxtStep" 
                 * @param : nxtStepName - @optional , initiates step with @eventName : "nxtStep" , @stepName : nxtStepName
                 * @returnValue : config.
                 */
                getEvents: function() {
                        

                       
                    return events
                       
                },
                getOptions: function() {
                    return options;                       
                },
                setEvents: function(param) {
                    events = param;
                    return events
                       
                },
                setOptions: function(param) {
                    options = param;
                    return options;                       
                },
                showModal :function(name){
                    _showModal(name)
                },
                hideModal :function(name){
                    _hideModal(name);
                },
                clearNode:function () {
                    curNode = {};
                },
                registerObserverCallback : function(callback){
                    observerCallbacks.push(callback);
                },
                generateRandomId: function(){
                    return guidGenerator();
                },
                getCurNode : function () {
                    return curNode;
                },
                getCurEdge : function () {
                    return curEdge;
                },
                showContext : function () {
                    if(isEdge || isNode)
                        return true;
                    else    
                        return false;
                },
                setInvalidNode: function (params) {
                    isNodeValid = params;
                },
                setCurNodeIndex : function (param) {
                    curNodeIndex = param;
                },
                setCurEdgeIndex : function (param) {
                    curEdgeIndex = param;
                },
                getCurNodeIndex : function () {
                    return curNodeIndex;
                },
                getCurEdgeIndex : function () {
                    return curEdgeIndex;
                }
            };
    })

    .factory('VisDataSet', function () {
        'use strict';
        return function (data, options) {
            // Create the new dataSets
            return new vis.DataSet(data, options);
        };
    })



/**
 * Directive for network chart.
 */
    .directive('visNetwork', function (interface,$timeout) {
        return {
            restrict: 'EA',
            scope: {
                data: '='
            },  
            transclude: false,
            link: function (scope, element, attr) {
                    
                
                var networkEvents = [
                    'click',
                    'doubleclick',
                    'oncontext',
                    'hold',
                    'release',
                    'selectNode',
                    'selectEdge',
                    'deselectNode',
                    'deselectEdge',
                    'dragStart',
                    'dragging',
                    'dragEnd',
                    'hoverNode',
                    'blurNode',
                    'zoom',
                    'showPopup',
                    'hidePopup',
                    'startStabilizing',
                    'stabilizationProgress',
                    'stabilizationIterationsDone',
                    'stabilized',
                    'resize',
                    'initRedraw',
                    'beforeDrawing',
                    'afterDrawing',
                    'animationFinished'
                ];
                var network = null;
                scope.$watch('data', function (newVal, oldVal) {
                    // Sanity check
                    
                    if (newVal) {
                       
                       $timeout(function(){
                                if (scope.options) {                        
                                    interface.setOptions(scope.options);
                                }else{
                                    scope.options = interface.getOptions();
                                }
                                
                                if (scope.events != null) {                        
                                    interface.setEvents(scope.events);
                                }else{                        
                                    scope.events = interface.getEvents();
                                }

                                // If we've actually changed the data set, then recreate the graph
                                // We can always update the data by adding more data to the existing data set
                                if (network != null) {
                                    network.destroy();
                                }

                                // Create the graph2d object
                                network = new vis.Network(element[0], scope.data, scope.options);
                                network.fit();
                                // Attach an event handler if defined
                                angular.forEach(scope.events, function (callback, event) {
                                    if (networkEvents.indexOf(String(event)) >= 0) {
                                        network.on(event, callback);
                                    }
                                });

                                // onLoad callback
                                if (scope.events != null && scope.events.onload != null &&
                                    angular.isFunction(scope.events.onload)) {
                                    scope.events.onload(graph);
                                }
                       },200)
                        
                    }

                },true);

                scope.$watchCollection('options', function (options) {
                    if (network == null || options == null) {
                        return;
                    }
                    network.setOptions(options);
                });
            }
        };
    })

    .directive('vis', function (interface,$timeout) {
            function initToggle() {
                $('[data-toggle="true"]').toggle();
                $('.dropdown-toggle').dropdown()
            };

            return {
                restrict: 'AE',
                templateUrl: '/vis-interface.html',
                scope: {
                    data : '='    
                },
                controller: function ($scope) {

                    $scope.mode ={};
                    
                $scope.isNode = true;
                $scope.actionsList = {
                    nodes :[],
                    edges :[]
                }

                var watchCurNode = function watchCurNode(flag,data, nodeId){
                    if(data)
                    $scope.$apply(function(){
                    	
                        $scope[data] = flag;
                        if(data == "isNode"){
                            
                            if(interface.getCurNode()){
                                $scope.isNodeValid = true;
                                $scope.isNode = true;
                            }
                            else{
                                $scope.isNode = false; 
                                $scope.isEdge = false;
                                }
                            for(var index =0 ; index<  $scope.data.nodes.length ;index++){
                                if($scope.data.nodes[index].id == nodeId){
                                    interface.setCurNodeIndex(index);
                                    if($scope.data.nodes[index].status !=undefined  &&
                                    ($scope.data.nodes[index].status == "TERMINATE" || $scope.data.nodes[index].status == "BEGIN")){
                                        $scope.isNodeValid = false;
                                        break;
                                    }
                                }
                            }
                        }else if(data == "isEdge"){

                            if(interface.getCurEdge()){
                                $scope.isEdge = true;
                            }
                            else{
                                $scope.isEdge = false; 
                                }
                        }else{
                            $scope.showActionsContext = true;
                        }
                    }) 
                };


                interface.registerObserverCallback(watchCurNode);

                    
                     var node={};
                     var edge = {};

                     $scope.addedActions =[];
                     $scope.addedEdges = [];
                     function resetNode (){
                         node = {
                                            size : 10,
                                            label : "",
                                            status : "",
                                            color : "#ddd",
                                            shape : ""

                         }
                     }

                    function resetCurObj(){
                        $scope.mode = 'add_actions'
                        $scope.curObj = {
                            label : "",
                            status: "",
                            id: null
                        };
                    }

                    function resetCurEdge(){
                        $scope.edge ={
                            to :"",
                            id: null
                        }
                    }

                    function resetEdge(){
                        return edge = {
                            "from": "",
                            "id": "",
                            "to": ""
                        }
                    }
                    resetCurEdge();
                    

                    var tempEdges;
                    $scope.removeModal = function removeModal(name) {
                        interface.hideModal(name);
                        $scope.mode = {};
                    };
                    $scope.removeNode = function removeModal(name, id) {
                        interface.hideModal(name);
                        var index = 0;
                        var len = $scope.data.nodes.length - 1;
                        for(index = 0; index < len; ){
                             if($scope.data.nodes[index].id == id){
                                break
                            }else{
                                index++;
                            }
                        }
                        
                        $scope.data.nodes.splice(index,1);

                    };
                    $scope.removeEdge = function removeModal() {
                        var edgeId = interface.getCurEdge();

                        if(edge){
                            var index = 0;
                            var len = $scope.data.edges.length;
                            for(index = 0; index < len; index++){
                                if($scope.data.edges[index].id == edgeId){
                                    break
                                }
                            }
                            
                            $scope.data.edges.splice(index,1);
                        }
                        interface.hideModal(name);
                        

                    };

                    function valdateEdge(){
                            var flag = false,curEdgeId = interface.getCurEdge();
                            for(var index = 0 ; index< $scope.data.edges.length-1; index++){
                                if($scope.data.edges[index].id == curEdgeId)
                                    flag = true;
                            }
                            return flag
                    }

                    $scope.submitNodes = function(modalName){
                        
                        if(!$scope.isEdit){
                            

                            angular.copy($scope.data.nodes,$scope.actions.nodes);

                            $scope.addedActions.forEach(function(element) {
                                $scope.actions.nodes.push(element);
                            }, this);

                            angular.copy($scope.actions.nodes, $scope.data.nodes);
                            var promise = $scope.actions.edges.forEach(function(element) {      
                                $scope.data.edges.push(element);
                            }, this);
                            $scope.actions.edges = [];
                            $scope.addedActions =[];
                        }else{
                            var temp ={};
                            var index = interface.getCurNodeIndex();
                            
                            $scope.data.nodes[index].label = $scope.curObj.label;
                            $scope.data.nodes[index].status = $scope.curObj.status;
                        }
                        $scope.removeModal(modalName);
                    }

                    $scope.addActions = function(){
                        $scope.isEdit=false;
                        var curNodeId = interface.getCurNode();
                        if(typeof curNodeId === "string"){
                            var node = getNodeObject(curNodeId);
                        }else{
                            $scope.isEdit=true;
                        }
                        resetCurObj();
                        $scope.edge.from= node.id;
                      
                    }

                    $scope.addNode = function addNode(name) {
                        
                        var flag = valdateEdge();
                        if(!flag){ 
                            var edge = null;
                            edge = {};
                            edge.id = interface.generateRandomId();
                            edge.from = $scope.edge.from;
                            edge.to = $scope.curObj.id;
                            $scope.actions.edges.push(edge);
                        }
                        var temp= "";
                        temp = $scope.edge.from;
                        resetCurEdge();
                            $scope.addedActions.push($scope.curObj);
                        resetCurObj();
                        $scope.edge.from = temp;

                        resetCurObj();

                    };

                    function validateIfEdgeExist(edge){
                        var flag = false;
                        $scope.data.edges.forEach(function(elem){
                            if (elem.from == edge.from && elem.to == edge.to){
                                return true;
                            }
                        })
                    }

                     $scope.submitEdges = function(modalName){
                        
                        if(!$scope.isEdit){
                            

                            var edgeList = [];

                            $scope.addedEdges.forEach(function(element) {
                                var edge = {};
                                edge.from = element.from.id;
                                edge.to = element.to.id;
                                edge.id = interface.generateRandomId();
                                if(!validateIfEdgeExist(edge)){
                                    edgeList.push(edge);
                                }
                                edge = null;
                            }, this);

                            edgeList.forEach(function(element) {      
                                $scope.data.edges.push(element);
                            }, this);
                            
                            $scope.addedEdges = [];
                        }else{
                            var temp ={};
                            var index = interface.getCurNodeIndex();
                            
                            $scope.data.nodes[index].label = $scope.curObj.label;
                            $scope.data.nodes[index].status = $scope.curObj.status;
                        }
                        $scope.removeModal(modalName);
                    }


                     $scope.addEdge = function addEdge(name) {
               var temp= {};
                        angular.copy($scope.edge,temp);
                        temp.id = interface.generateRandomId();
                        $scope.addedEdges.push(temp);
                        
                        $scope.edge.to = "";

                    };

                     $scope.setEdge = function (){ 
                        var curNodeId = interface.getCurNode();
                        var curNode = getNodeObject(curNodeId);
                        $scope.edge.from = curNode;
                    }

                    $scope.curObj = {};

                    $scope.edge = {
                            "from": "",
                            "to": ""
                        };

                    $scope.actions ={
                        nodes:[],
                        edges:[]   
                    };
                    angular.copy($scope.data.nodes,$scope.actions.nodes);
                    
                    $scope.$watch('curObj',function(newVal,oldVal){
                        if(newVal && newVal != oldVal && !(newVal.id)){
                            if(newVal.label){
                                var node = {};
                                node = $scope.curObj;
                                node.id = interface.generateRandomId();
                                $scope.actions.nodes.push(node);
                            }

                        }
                    },true);
                    
                    
                    // $('#visNodeModal').on("hidden.bs.modal", function onModalHide() {
                    //     $scope.$applyAsync(function () {
                    //         
                    //         caliberateVisData();
				    //     });
			        // });

                    function getNodeObject(id){
                        var node;
                        $scope.data.nodes.forEach(function(element) {
                            if(element.id == id){
                                node = element;
                                return node;
                            }
                        }, this);

                        return node;
                    }

                    $scope.setNode = function (){ 
                    	
                        $scope.isEdit = true;
                        var curNodeId = interface.getCurNode();
                        var node;
                        if(curNodeId){
                            node = getNodeObject(curNodeId);
                            
                            if(node){
                                $scope.curObj = node;
                                $scope.edge = $scope.setEdge();
                            }
                        }else
                            return
                    }

                    function getEdgeObject(id){
                        var edge;
                        $scope.data.edges.forEach(function(element) {
                            if(element.id == id){
                                edge = element;
                                return edge;
                            }
                        }, this);

                        return edge;
                    }

                    // $scope.$watch('curObj',function(newVal, oldVal){
                    //     if(newVal && newVal != oldVal){
                    //         $timeout(function(){                                
                    //             if(newVal.label && !newVal.id){
                    //                 newVal.id = interface.generateRandomId();
                    //                 $scope.data.nodes.push(newVal);
                    //             }
                    //         })
                    //     }
                    // },true)


                    function checkIfExist(param){
                        var node;
                        $scope.data.nodes.forEach(function(element) {
                            if(element.label === param){
                                node = element;
                            }
                        }, this);
                        return node;
                    }

                    $scope.configureNewNode = function configureNewNode(type){
                        
                        resetNode();
                        node = checkIfExist(type);

                        if(node){
                            var curNodeID = interface.getCurNode();
                            var curNode = getNodeObject(curNodeID);
                            var edge = resetEdge();
                            edge.from =  curNode.id
                            edge.id = interface.generateRandomId();
                            edge.to = node.id;
                            $scope.data.edges.push(edge);
                        }else{
                            resetNode();
                            node.id = interface.generateRandomId();
                            node.size = 10;
                            node.label = type;
                            node.status = type;
                            node.shape = "square";
                            
                            $scope.curObj = node;
                            var edge = {};
                            edge.from = interface.getCurNode();
                            edge.to = node.id;
                            edge.id = interface.generateRandomId();

                            $scope.data.nodes.push(node);
                            $scope.data.edges.push(edge);

                            $timeout(function(){                            
                                addEndNode(node.status, edge);
                            },500);
                        }
                        
                    }

                    $scope.endAction = function(){
                        
                            
                            var node = {};
                            node.label = "End";
                            node.status = "TERMINATE";
                            node.id = interface.generateRandomId();

                            var cuNodeId = interface.getCurNode();
                            var edge = {};
                            edge.from =  cuNodeId;
                            edge.id = interface.generateRandomId();
                            edge.to = node.id;
                            $scope.data.nodes.push(node);
                            $scope.data.edges.push(edge);



                    }
                    
                    $scope.removedAddedAction = function(index){
                        $scope.addedActions.splice(index,1);
                    }
                    $scope.removedAddedEdges = function(index){
                        $scope.addedEdges.splice(index,1);
                    }
                },
                link: function (scope, elem, attrs) {
                    // var $contextMenu = $("#contextMenu");
                    // $("body").on("contextmenu", "vis-network", function(e) {
                    //     $contextMenu.css({
                    //         display: "block",
                    //         left: e.pageX,
                    //         top: e.pageY
                    //     });
                    //     return false;
                    //     });
                    //     $contextMenu.on("click", "a", function() {
                    //     $contextMenu.hide();
                    //     });
                    
                
                var $contextMenu = $("#contextMenu");

                    $("body").on("contextmenu",  function(e) {
                        if(!scope.isNode && !scope.isEdge){
                            return false
                        }else{
                            $contextMenu.css({
                                display: "block",
                                left: e.pageX,
                                top: e.pageY
                            });
                        return false;
                        }
                    });
                    $(document).on("click", "a", function(event) {
                        $contextMenu.hide();
                        event.preventDefault();
                    });

                    $("body").on("click",function(event) {
                        interface.clearNode();
                        $contextMenu.hide();
                        event.preventDefault();
                    });

                }
            };
})
;
