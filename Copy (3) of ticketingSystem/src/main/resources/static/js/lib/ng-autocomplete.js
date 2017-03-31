/*
 * angucomplete-alt
 * Autocomplete directive for AngularJS
 * This is a fork of Daryl Rowland's angucomplete with some extra features.
 * By Hidenari Nozaki
 */

/*! Copyright (c) 2014 Hidenari Nozaki and contributors | Licensed under the MIT license */

'use strict';

(function (root, factory) {
    if (typeof module !== 'undefined' && module.exports) {
        // CommonJS
        module.exports = factory(require('angular'));
    } else if (typeof define === 'function' && define.amd) {
        // AMD
        define(['angular'], factory);
    } else {
        // Global Variables
        factory(root.angular);
    }
} (window, function (angular) {

    angular.module('ngAutocomplete', [])
        .directive('ngAutocomplete', ['$q', '$parse', '$http', '$sce', '$timeout', '$templateCache', function ($q, $parse, $http, $sce, $timeout, $templateCache) {
            // keyboard events
            var KEY_DW = 40;
            var KEY_RT = 39;
            var KEY_UP = 38;
            var KEY_LF = 37;
            var KEY_ES = 27;
            var KEY_EN = 13;
            var KEY_BS = 8;
            var KEY_DEL = 46;
            var KEY_TAB = 9;

            var MIN_LENGTH = 1;
            var MAX_LENGTH = 524288;  // the default max length per the html maxlength attribute
            var PAUSE = 500;
            var BLUR_TIMEOUT = 200;

            // string constants
            var REQUIRED_CLASS = 'autocomplete-required';
            var TEXT_SEARCHING = 'Searching...';
            var TEXT_NORESULTS = 'No results found';
            var TEMPLATE_URL = '/angucomplete-alt/index.html';

            // Set the default template for this directive
            $templateCache.put(TEMPLATE_URL,
                '<div class="angucomplete-holder" ng-class="{\'angucomplete-dropdown-visible\': showDropdown}">' +
                '  <input id="{{id}}_value" name={{inputName}} ng-class="{\'angucomplete-input-not-empty\': notEmpty}" ng-model="searchStr" ng-disabled="disableInput" type="{{inputType}}" placeholder="{{placeholder}}" maxlength="{{maxlength}}" ng-focus="onFocusHandler($event)" class="{{inputClass}}" ng-focus="resetHideResults()" ng-blur="hideResults($event)" autocapitalize="off" autocorrect="off" autocomplete="off" ng-change="inputChangeHandler(searchStr)"/>' +
                '  <div id="{{id}}_dropdown" class="angucomplete-dropdown" ng-show="showDropdown">' +
                '    <div class="angucomplete-searching" ng-show="searching" ng-bind="textSearching"></div>' +
                '    <div class="angucomplete-searching" ng-show="!searching && (!results || results.length == 0)" ng-bind="textNoResults"></div>' +
                '    <div class="angucomplete-row" ng-repeat="result in results" ng-click="selectResult(result)" ng-mouseenter="hoverRow($index)" ng-class="{\'angucomplete-selected-row\': $index == currentIndex}">' +
                '      <div ng-if="imageField" class="angucomplete-image-holder">' +
                '        <img ng-if="result.image && result.image != \'\'" ng-src="{{result.image}}" class="angucomplete-image"/>' +
                '        <div ng-if="!result.image && result.image != \'\'" class="angucomplete-image-default"></div>' +
                '      </div>' +
                '      <div class="angucomplete-title" ng-if="matchClass" ng-bind-html="result.title"></div>' +
                '      <div class="angucomplete-title" ng-if="!matchClass">{{ result.title }}</div>' +
                '      <div ng-if="matchClass && result.description && result.description != \'\'" class="angucomplete-description" ng-bind-html="result.description"></div>' +
                '      <div ng-if="!matchClass && result.description && result.description != \'\'" class="angucomplete-description">{{result.description}}</div>' +
                '    </div>' +
                '  </div>' +
                '</div>'
            );

            return {
                restrict: 'EA',
                require: 'ngModel',
                scope: {
                    disableInput: "=",
                    localData: '=',
                    remoteUrlRequestFormatter: '=',
                    remoteUrlRequestWithCredentials: '@',
                    remoteUrlResponseFormatter: '=',
                    remoteUrlErrorCallback: '=',
                    remoteApiHandler: '=',
                    type: '@',
                    placeholder: '@',
                    remoteUrl: '@',
                    remoteUrlDataField: '@',
                    titleField: '@',
                    descriptionField: '@',
                    imageField: '@',
                    inputClass: '@',
                    ignoreCase: '@',
                    pause: '@',
                    name: '@',
                    id: '@',
                    searchFields: '@',
                    length: '@',
                    matchClass: '@',
                    clearSelected: '@',
                    overrideSuggestions: '@',
                    inputChanged: '=',
                    autoMatch: '@',
                    focusOut: '&',
                    focusIn: '&',
                    inputName: '@',
                },
                templateUrl: function (element, attrs) {
                    return attrs.templateUrl || TEMPLATE_URL;
                },
                link: function (scope, elem, attrs, ngModelController) {
                    var inputField = elem.find('input');
                    var minlength = MIN_LENGTH;
                    var searchTimer = null;
                    var hideTimer;
                    var requiredClassName = REQUIRED_CLASS;
                    var responseFormatter;
                    var validState = null;
                    var httpCanceller = null;
                    var dd = elem[0].querySelector('.angucomplete-dropdown');
                    var isScrollOn = false;
                    var mousedownOn = null;
                    var justCameInFocus = false;
                    /**
                     * Stores the last key down event which occured on the input field
                     */
                    var lastKeyDownEvent = null;
                    /**
                     * Stores the last key up event which occured on the input field
                     */
                    var lastKeyUpEvent = null;
                    /**
                     * Stores the last search string with which the query/search was performed
                     */
                    var lastSearchStr = null;

                    elem.on('mousedown', function (event) {
                        if (event.target.id) {
                            mousedownOn = event.target.id;
                        } else {
                            mousedownOn = event.target.className;
                        }
                    });

                    scope.currentIndex = null;
                    scope.searching = false;

                    ngModelController.$render = function () {
                        inputField.val(extractTitle(ngModelController.$viewValue));
                    };

                    $timeout(function () {
                        inputField.val(extractTitle(ngModelController.$viewValue));
                    }, 100);

                    // for IE8 quirkiness about event.which
                    function ie8EventNormalizer(event) {
                        return event.which ? event.which : event.keyCode;
                    }

                    scope.callOrAssign = function callOrAssign(value) {
                        ngModelController.$setViewValue(value != null ? value.originalObject : null);
                        validState = scope.searchStr;
                    };

                    function setInputString(str) {
                        scope.callOrAssign({ originalObject: str });

                        if (scope.clearSelected) {
                            scope.searchStr = null;
                        }
                        scope.clearResults();
                    };

                    function extractTitle(data) {
                        // split title fields and run extractValue for each and join with ' '
                        return scope.titleField.split(',')
                            .map(function (field) {
                                return extractValue(data, field);
                            }).join(' ');
                    };

                    function extractValue(obj, key) {
                        return key != null ? $parse(key)(obj) : obj;
                    };

                    function findMatchString(target, str) {
                        var result, matches, re;
                        // Escape user input to be treated as a literal string within a regular expression
                        re = new RegExp(str.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'), 'i');
                        if (!target) { return; }
                        matches = target.match(re);
                        if (matches) {
                            result = target.replace(re, '<span class="' + scope.matchClass + '">' + matches[0] + '</span>');
                        } else {
                            result = target;
                        }
                        return $sce.trustAsHtml(result);
                    };

                    function keyupHandler(event) {
                        try {
                            var which = ie8EventNormalizer(event);
                            if (which === KEY_LF || which === KEY_RT) {
                                // do nothing
                                return;
                            }

                            if (which === KEY_UP || which === KEY_EN) {
                                event.preventDefault();
                            } else if (which === KEY_DW) {
                                event.preventDefault();
                                if (!scope.showDropdown && scope.searchStr && scope.searchStr.length >= minlength) {
                                    initResults();
                                    scope.searching = true;
                                    searchTimerComplete(scope.searchStr);
                                }
                            } else if (which === KEY_ES) {
                                scope.clearResults();
                                scope.$apply(function () {
                                    inputField.val(scope.searchStr);
                                });
                            } else if (which === KEY_BS) {
                                scope.clearResults();
                                if (ngModelController.$modelValue != null) {
                                    /**
                                     * It should never really come inside this block. 
                                     * Since, the inputChangeHandler will already make the modeValue empty if 
                                     * backspace or delete is pressed
                                     */
                                    scope.searchStr = "";
                                    inputField.val(scope.searchStr);
                                    scope.callOrAssign();
                                }
                            } else {
                                if (minlength === 0 && !scope.searchStr) {
                                    return;
                                }
                                if (ngModelController.$modelValue) {
                                    return;
                                }
                                if (!scope.searchStr || scope.searchStr === '') {
                                    scope.showDropdown = false;
                                } else if (scope.searchStr.length >= minlength) {
                                    initResults();
                                    if (searchTimer) {
                                        $timeout.cancel(searchTimer);
                                    }
                                    scope.searching = true;
                                    searchTimer = $timeout(function () {
                                        searchTimerComplete(scope.searchStr);
                                    }, scope.pause);
                                }

                                if (validState && validState !== scope.searchStr && !scope.clearSelected) {
                                    scope.callOrAssign()
                                }
                            }
                        } finally {
                            lastKeyUpEvent = event;
                        }
                    };

                    function handleOverrideSuggestions(event) {
                        if (scope.overrideSuggestions && !(ngModelController.$modelValue === scope.searchStr)) {
                            if (event) {
                                event.preventDefault();
                            }
                            setInputString(scope.searchStr);
                        }
                    };

                    function dropdownRowOffsetHeight(row) {
                        var css = getComputedStyle(row);
                        return row.offsetHeight
                            + parseInt(css.marginTop, 10) + parseInt(css.marginBottom, 10);
                    };

                    function dropdownHeight() {
                        return dd.getBoundingClientRect().top + parseInt(getComputedStyle(dd).maxHeight, 10);
                    };

                    function dropdownRow() {
                        return elem[0].querySelectorAll('.angucomplete-row')[scope.currentIndex];
                    };

                    function dropdownRowTop() {
                        return dropdownRow().getBoundingClientRect().top - (dd.getBoundingClientRect().top + parseInt(getComputedStyle(dd).paddingTop, 10));
                    };

                    function dropdownScrollTopTo(offset) {
                        dd.scrollTop = dd.scrollTop + offset;
                    };

                    function updateInputField() {
                        var current = scope.results[scope.currentIndex];
                        if (scope.matchClass) {
                            inputField.val(extractTitle(current.originalObject));
                        } else {
                            inputField.val(current.title);
                        }
                    };

                    function keydownHandler(event) {
                        try {
                            var which = ie8EventNormalizer(event);
                            var row = null;
                            var rowTop = null;
                            if (which === KEY_EN && scope.results) {
                                if (scope.currentIndex >= 0 && scope.currentIndex < scope.results.length) {
                                    event.preventDefault();
                                    scope.selectResult(scope.results[scope.currentIndex]);
                                } else {
                                    handleOverrideSuggestions(event);
                                    scope.clearResults();
                                }
                                scope.$apply();
                            } else if (which === KEY_DW && scope.results) {
                                event.preventDefault();
                                if ((scope.currentIndex + 1) < scope.results.length && scope.showDropdown) {
                                    scope.$apply(function () {
                                        scope.currentIndex++;
                                        updateInputField();
                                    });

                                    if (isScrollOn) {
                                        row = dropdownRow();
                                        if (dropdownHeight() < row.getBoundingClientRect().bottom) {
                                            dropdownScrollTopTo(dropdownRowOffsetHeight(row));
                                        }
                                    }
                                }
                            } else if (which === KEY_UP && scope.results) {
                                event.preventDefault();
                                if (scope.currentIndex >= 1) {
                                    scope.$apply(function () {
                                        scope.currentIndex--;
                                        updateInputField();
                                    });

                                    if (isScrollOn) {
                                        rowTop = dropdownRowTop();
                                        if (rowTop < 0) {
                                            dropdownScrollTopTo(rowTop - 1);
                                        }
                                    }
                                }
                                else if (scope.currentIndex === 0) {
                                    scope.$apply(function () {
                                        scope.currentIndex = -1;
                                        inputField.val(scope.searchStr);
                                    });
                                }
                            } else if (which === KEY_TAB) {
                                if (scope.results && scope.results.length > 0 && scope.showDropdown) {
                                    if (scope.currentIndex === -1 && scope.overrideSuggestions) {
                                        // intentionally not sending event so that it does not
                                        // prevent default tab behavior
                                        handleOverrideSuggestions();
                                    }
                                    else {
                                        if (scope.currentIndex === -1) {
                                            scope.currentIndex = 0;
                                        }
                                        /**
                                         * If no results have been highlighted then select the first result when tab is pressed
                                         * Only in the case that there are results available to pick from
                                         */
                                        scope.selectResult(scope.results[scope.currentIndex]);
                                        scope.$digest();
                                    }
                                }
                                else {
                                    // no results
                                    // intentionally not sending event so that it does not
                                    // prevent default tab behavior
                                    if (scope.searchStr && scope.searchStr.length > 0) {
                                        handleOverrideSuggestions();
                                    }
                                }
                            }
                        } finally {
                            lastKeyDownEvent = event;
                        }
                    };

                    function httpSuccessCallbackGen(str) {
                        return function (response) {
                            var responseData = response.data;
                            scope.searching = false;
                            processResults(extractValue(responseFormatter(responseData), scope.remoteUrlDataField), str);
                        };
                    };

                    function httpErrorCallback(response) {
                        if (scope.remoteUrlErrorCallback) {
                            scope.remoteUrlErrorCallback(errorRes, status, headers, config);
                        } else {
                            if (console && console.error) {
                                console.error('http error', response);
                            }
                        }
                    };

                    function cancelHttpRequest() {
                        if (httpCanceller) {
                            httpCanceller.resolve();
                        }
                    };

                    function getRemoteResults(str) {
                        var params = {};
                        var url = scope.remoteUrl + encodeURIComponent(str);
                        if (scope.remoteUrlRequestFormatter) {
                            params = { params: scope.remoteUrlRequestFormatter(str) };
                            url = scope.remoteUrl;
                        }
                        if (!!scope.remoteUrlRequestWithCredentials) {
                            params.withCredentials = true;
                        }
                        cancelHttpRequest();
                        httpCanceller = $q.defer();
                        params.timeout = httpCanceller.promise;
                        //params.cache = true;
                        $http.get(url, params).then(httpSuccessCallbackGen(str), httpErrorCallback);
                    };

                    function getRemoteResultsWithCustomHandler(str) {
                        cancelHttpRequest();

                        httpCanceller = $q.defer();

                        scope.remoteApiHandler(str, httpCanceller.promise).then(httpSuccessCallbackGen(str), httpErrorCallback);
                    };

                    scope.clearResults = function clearResults() {
                        scope.showDropdown = false;
                        scope.results = [];
                        if (dd) {
                            dd.scrollTop = 0;
                        }
                    };

                    function initResults() {
                        scope.showDropdown = true;
                        scope.currentIndex = -1;
                        scope.results = [];
                    };

                    function getLocalResults(str) {
                        var i, match, s, value;
                        var searchFields = scope.searchFields.split(',');
                        var matches = [];

                        for (i = 0; i < scope.localData.length; i++) {
                            match = false;

                            for (s = 0; s < searchFields.length; s++) {
                                value = extractValue(scope.localData[i], searchFields[s]) || '';
                                match = match || (value.toLowerCase().indexOf(str.toLowerCase()) >= 0);
                            }

                            if (match) {
                                matches[matches.length] = scope.localData[i];
                            }
                        }
                        scope.searching = false;
                        processResults(matches, str);
                    };

                    function checkExactMatch(result, obj, str) {
                        if (!str) { return false; }
                        for (var key in obj) {
                            if (attrs.ignoreCase) {
                                if (obj[key].toLowerCase() === str.toLowerCase()) {
                                    scope.selectResult(result);
                                    return true;
                                }
                            } else {
                                if (obj[key] === str) {
                                    scope.selectResult(result);
                                    return true;
                                }
                            }
                        }
                        return false;
                    };

                    function searchTimerComplete(str) {
                        // Begin the search
                        if (!str || str.length < minlength) {
                            return;
                        }

                        if (scope.localData) {
                            scope.$apply(function () {
                                getLocalResults(str);
                            });
                        }
                        else if (scope.remoteApiHandler) {
                            getRemoteResultsWithCustomHandler(str);
                        } else {
                            getRemoteResults(str);
                        }
                    };

                    function processResults(responseData, str) {
                        var i, description, image, text, formattedText, formattedDesc;
                        lastSearchStr = str;
                        if (responseData && responseData.length > 0) {
                            scope.results = [];

                            for (i = 0; i < responseData.length; i++) {
                                if (scope.titleField && scope.titleField !== '') {
                                    text = formattedText = extractTitle(responseData[i]);
                                }

                                description = '';
                                if (scope.descriptionField) {
                                    description = formattedDesc = extractValue(responseData[i], scope.descriptionField);
                                }

                                image = '';
                                if (scope.imageField) {
                                    image = extractValue(responseData[i], scope.imageField);
                                }

                                if (scope.matchClass) {
                                    formattedText = findMatchString(text, str);
                                    formattedDesc = findMatchString(description, str);
                                }
                                scope.results[scope.results.length] = {
                                    title: formattedText,
                                    description: formattedDesc,
                                    image: image,
                                    originalObject: responseData[i]
                                };

                                if (scope.autoMatch) {
                                    checkExactMatch(scope.results[scope.results.length - 1],
                                        { title: text, desc: description || '' }, scope.searchStr);
                                }
                            }

                        } else {
                            scope.results = [];
                        }
                    };

                    function showAll() {

                        if (scope.localData) {
                            processResults(scope.localData, '');
                        } else if (scope.remoteApiHandler) {
                            getRemoteResultsWithCustomHandler('');
                        } else {
                            getRemoteResults('');
                        }
                    };

                    /**
                     * To be called on blur if the input field has text and ng model is empty.
                     * It will call the remote api and try to find a match. It will select a result if it can find a match
                     * Returns a promise which resolves when the auto match check is complete
                     */
                    function tryToAutoMatch(searchStr) {
                        var data = null;
                        var promise = null;
                        if (scope.localData) {
                            data = scope.localData;
                        }
                        else if (scope.remoteApiHandler) {
                            cancelHttpRequest();
                            httpCanceller = $q.defer();
                            promise = scope.remoteApiHandler(str, httpCanceller.promise).then(function (response) {
                                data = response.data;
                            }, httpErrorCallback);
                        }
                        else {
                            cancelHttpRequest();
                            var params = {}
                            var url = scope.remoteUrl + encodeURIComponent(searchStr);
                            if (scope.remoteUrlRequestFormatter) {
                                params = { params: scope.remoteUrlRequestFormatter(searchStr) };
                                url = scope.remoteUrl;
                            }
                            if (!!scope.remoteUrlRequestWithCredentials) {
                                params.withCredentials = true;
                            }
                            httpCanceller = $q.defer();
                            params.timeout = httpCanceller.promise;
                            params.cache = true;
                            promise = $http.get(url, params).then(function (response) {
                                data = extractValue(responseFormatter(response.data), scope.remoteUrlDataField);
                            }, httpErrorCallback);
                        }

                        return $q.when(promise).then(function () {
                            if (data && data.length > 0) {
                                for (var i = 0; i < data.length; i++) {
                                    var text = extractTitle(data[i]);
                                    var description;
                                    if (scope.descriptionField) {
                                        description = extractValue(data[i], scope.descriptionField);
                                    }
                                    if (checkExactMatch({ originalObject: data[i] }, { title: text, desc: description || '' }, searchStr)) {
                                        break;
                                    }
                                }
                            }
                        });
                    }

                    scope.onFocusHandler = function (event) {
                        justCameInFocus = true;
                        if (scope.focusIn) {
                            scope.focusIn();
                        }

                        if (minlength === 0 && (!scope.searchStr || scope.searchStr.length === 0)) {
                            scope.showDropdown = true;
                            showAll();
                        }
                    };

                    scope.hideResults = function (event) {
                        if (mousedownOn && (mousedownOn === scope.id + '_dropdown' || mousedownOn.indexOf('angucomplete') >= 0)) {
                            mousedownOn = null;
                        }
                        else {
                            $timeout.cancel(searchTimer);
                            if (ngModelController.$modelValue == null) {
                                if (scope.autoMatch && scope.searchStr != null && scope.searchStr.length >= minlength && scope.searchStr != lastSearchStr) {
                                    /**
                                     * Trying to auto match with the current search string
                                     */
                                    tryToAutoMatch(scope.searchStr).then(function () {
                                        if (ngModelController.$modelValue == null) {
                                            scope.searchStr = null;
                                        }
                                    });
                                } else {
                                    scope.searchStr = null;
                                }
                            }

                            hideTimer = $timeout(function () {
                                scope.clearResults();
                                scope.$apply(function () {
                                    if (scope.searchStr && scope.searchStr.length > 0) {
                                        inputField.val(scope.searchStr);
                                    }
                                });
                            }, BLUR_TIMEOUT);
                            //cancelHttpRequest();

                            if (scope.focusOut) {
                                scope.focusOut();
                            }

                            if (scope.overrideSuggestions) {
                                if (scope.searchStr && scope.searchStr.length > 0 && scope.currentIndex === -1) {
                                    handleOverrideSuggestions();
                                }
                            }
                        }
                    };

                    scope.resetHideResults = function () {
                        if (hideTimer) {
                            $timeout.cancel(hideTimer);
                        }
                    };

                    scope.hoverRow = function (index) {
                        scope.currentIndex = index;
                    };

                    scope.selectResult = function (result) {
                        lastSearchStr = null;
                        result = angular.copy(result);
                        if (result && result.originalObject) {
                            if (scope.matchClass) {
                                result.title = extractTitle(result.originalObject);
                                result.description = extractValue(result.originalObject, scope.descriptionField);
                            }

                            if (scope.clearSelected) {
                                scope.searchStr = null;
                            } else {
                                scope.searchStr = result.title;
                            }
                        }
                        scope.callOrAssign(result);
                        scope.clearResults();
                    };

                    scope.inputChangeHandler = function (str) {
                        if (str.length < minlength) {
                            scope.clearResults();
                        } else if (str.length === 0 && minlength === 0) {
                            scope.searching = false;
                            showAll();
                        }

                        if (ngModelController.$modelValue != null) {
                            scope.callOrAssign();                            
                            if(ie8EventNormalizer(lastKeyDownEvent) == KEY_BS || ie8EventNormalizer(lastKeyDownEvent) == KEY_DEL) {
                                scope.searchStr = null;
                            } else {
                                scope.searchStr = lastKeyDownEvent.key;
                            }
                        }

                        if (scope.inputChanged) {
                            str = scope.inputChanged(str, oldStr);
                        }
                        return str;
                    };

                    // check min length
                    if (scope.length && scope.length !== '') {
                        minlength = parseInt(scope.length, 10);
                    }

                    // check pause time
                    if (!scope.pause) {
                        scope.pause = PAUSE;
                    }

                    // check clearSelected
                    if (!scope.clearSelected) {
                        scope.clearSelected = false;
                    }

                    // check override suggestions
                    if (!scope.overrideSuggestions) {
                        scope.overrideSuggestions = false;
                    }

                    scope.inputType = attrs.type ? attrs.type : 'text';

                    // set strings for "Searching..." and "No results"
                    scope.textSearching = attrs.textSearching ? attrs.textSearching : TEXT_SEARCHING;
                    scope.textNoResults = attrs.textNoResults ? attrs.textNoResults : TEXT_NORESULTS;

                    // set max length (default to maxlength deault from html
                    scope.maxlength = attrs.maxlength ? attrs.maxlength : MAX_LENGTH;

                    // register events
                    inputField.on('keydown', keydownHandler);
                    inputField.on('keyup', keyupHandler);
                    inputField.on('paste', function(e) {
                        e.preventDefault();
                    });

                    // set response formatter
                    responseFormatter = function (data) {
                        if (angular.isFunction(scope.remoteUrlResponseFormatter)) {
                            return scope.remoteUrlResponseFormatter(data, scope.id);
                        } else {
                            return data;
                        }
                    };

                    // set isScrollOn
                    $timeout(function () {
                        var css = getComputedStyle(dd);
                        isScrollOn = css.maxHeight && css.overflowY === 'auto';
                    });
                }
            };
        }]);

}));