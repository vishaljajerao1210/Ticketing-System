/*globals define, jQuery, module, require */
/*jslint vars:true */

/**
 * @license angular-bootstrap-datetimepicker  version: 0.3.13
 * Copyright 2013-2015 Knight Rider Consulting, Inc. http://www.knightrider.com
 * License: MIT
 */

/**
 *    Original Work
 *    @author        Dale "Ducky" Lotts
 *    @since        2013-Jul-8
 *
 *    Modified Work
 *    @author        Pavan Andhukuri
 *    @since         2015-Jul-4
 */

(function (factory) {
    'use strict';
    /* istanbul ignore if */
    if (typeof define === 'function' && /* istanbul ignore next */ define.amd) {
        define(['angular', 'moment'], factory); // AMD
        /* istanbul ignore next */
    } else if (typeof exports === 'object') {
        module.exports = factory(require('angular'), require('moment')); // CommonJS
    } else {
        factory(window.angular, window.moment); // Browser global
    }
} (function (angular, moment) {
    'use strict';
    angular.module('ui.bootstrap.datetimepicker', [])
        .constant('dateTimePickerConfig', {
            dropdownSelector: null,
            minuteStep: 5,
            minView: 'minute',
            startView: 'day'
        })
        .directive('datetimepicker', ['$log', 'dateTimePickerConfig', '$parse', function datetimepickerDirective($log, defaultConfig, $parse) {
            var tempDate = new Date();
            var localOffset = tempDate.getTimezoneOffset() * 60000;
            var settingTime = false;
            function DateObject() {
                this.utcDateValue = tempDate.getTime();

                this.localDateValue = function () {
                    return this.utcDateValue + localOffset;
                };

                this.selectable = true;

                var validProperties = ['utcDateValue', 'localDateValue', 'display', 'active', 'selectable', 'past', 'future'];

                for (var prop in arguments[0]) {
                    /* istanbul ignore else */
                    //noinspection JSUnfilteredForInLoop
                    if (validProperties.indexOf(prop) >= 0) {
                        //noinspection JSUnfilteredForInLoop
                        this[prop] = arguments[0][prop];
                    }
                }
            }

            var validateConfiguration = function validateConfiguration(configuration) {
                //Manual change. Added monitorModel and scope as additional valid options
                var validOptions = ['startView', 'minView', 'minuteStep', 'dropdownSelector', 'minDateModel', 'maxDateModel', 'scope', 'disableDaysBefore', 'displayModel', 'displayFormat'];

                for (var prop in configuration) {
                    //noinspection JSUnfilteredForInLoop
                    if (validOptions.indexOf(prop) < 0) {
                        throw ('invalid option: ' + prop);
                    }
                }

                // Order of the elements in the validViews array is significant.
                var validViews = ['minute', 'hour', 'day', 'month', 'year'];

                if (validViews.indexOf(configuration.startView) < 0) {
                    throw ('invalid startView value: ' + configuration.startView);
                }

                if (validViews.indexOf(configuration.minView) < 0) {
                    throw ('invalid minView value: ' + configuration.minView);
                }

                if (validViews.indexOf(configuration.minView) > validViews.indexOf(configuration.startView)) {
                    throw ('startView must be greater than minView');
                }

                if (!angular.isNumber(configuration.minuteStep)) {
                    throw ('minuteStep must be numeric');
                }
                if (configuration.minuteStep <= 0 || configuration.minuteStep >= 60) {
                    throw ('minuteStep must be greater than zero and less than 60');
                }
                if (configuration.dropdownSelector !== null && !angular.isString(configuration.dropdownSelector)) {
                    throw ('dropdownSelector must be a string');
                }
                if (configuration.minDateModel != null && !angular.isString(configuration.minDateModel)) {
                    throw ('minDateModel must be a string');
                }
                if (configuration.maxDateModel != null && !angular.isString(configuration.maxDateModel)) {
                    throw ('maxDateModel must be a string');
                }
                /*if ((configuration.minDateModel != null || configuration.maxDateModel != null) && configuration.scope == null) {
                    throw ('scope must be provided as scope=this in configuration options');
                }*/
                if (configuration.disableDaysBefore && !angular.isNumber(configuration.disableDaysBefore)) {
                    throw ('disableDaysBefore must be numeric');
                }
                if (configuration.displayModel != null && !angular.isString(configuration.displayModel)) {
                    throw ('displayModel must be a string');
                }
                if (configuration.displayModel != null && configuration.displayFormat == null) {
                    throw ('displayFormat needs to be passed');
                }


                /* istanbul ignore next */
                if (configuration.dropdownSelector !== null && ((typeof jQuery === 'undefined') || (typeof jQuery().dropdown !== 'function'))) {
                    $log.error('Please DO NOT specify the dropdownSelector option unless you are using jQuery AND Bootstrap.js. ' +
                        'Please include jQuery AND Bootstrap.js, or write code to close the dropdown in the on-set-time callback. \n\n' +
                        'The dropdownSelector configuration option is being removed because it will not function properly.');
                    delete configuration.dropdownSelector;
                }
            };

            return {
                restrict: 'E',
                require: 'ngModel',
                template: '<div class="datetimepicker table-responsive">' +
                '<table class="table table-striped  {{ data.currentView }}-view">' +
                '   <thead>' +
                '       <tr>' +
                '           <th class="left" data-ng-click="changeView(data.currentView, data.leftDate, $event)"><i class="glyphicon glyphicon-arrow-left" data-ng-show="data.leftDate.selectable"/></th>' +
                '           <th class="switch" colspan="5" data-ng-show="data.previousViewDate.selectable" data-ng-click="changeView(data.previousView, data.previousViewDate, $event)">{{ data.previousViewDate.display }}</th>' +
                '           <th class="right" data-ng-click="changeView(data.currentView, data.rightDate, $event)"><i class="glyphicon glyphicon-arrow-right"  data-ng-show="data.rightDate.selectable"/></th>' +
                '       </tr>' +
                '       <tr>' +
                '           <th class="dow" data-ng-repeat="day in data.dayNames" >{{ day }}</th>' +
                '       </tr>' +
                '   </thead>' +
                '   <tbody>' +
                '       <tr data-ng-if="data.currentView !== \'day\'" >' +
                '           <td colspan="7" >' +
                '              <span    class="{{ data.currentView }}" ' +
                '                       data-ng-repeat="dateObject in data.dates"  ' +
                '                       data-ng-class="{active: dateObject.active, past: dateObject.past, future: dateObject.future, disabled: !dateObject.selectable}" ' +
                '                       data-ng-click="changeView(data.nextView, dateObject, $event)">{{ dateObject.display }}</span> ' +
                '           </td>' +
                '       </tr>' +
                '       <tr data-ng-if="data.currentView === \'day\'" data-ng-repeat="week in data.weeks">' +
                '           <td data-ng-repeat="dateObject in week.dates" ' +
                '               data-ng-click="changeView(data.nextView, dateObject, $event)"' +
                '               class="day" ' +
                '               data-ng-class="{active: dateObject.active, past: dateObject.past, future: dateObject.future, disabled: !dateObject.selectable}" >{{ dateObject.display }}</td>' +
                '       </tr>' +
                '   </tbody>' +
                '</table></div>',
                scope: {
                    onSetTime: '&',
                    beforeRender: '&'
                },
                replace: true,
                link: function link(scope, element, attrs, ngModelController) {

                    var directiveConfig = {};
                    var localObj = {};

                    if (attrs.datetimepickerConfig) {
                        directiveConfig = scope.$parent.$eval(attrs.datetimepickerConfig);
                    }
					
					
					
                    var configuration = {};

                    angular.extend(configuration, defaultConfig, directiveConfig);

                    validateConfiguration(configuration);
					
					if(!configuration.scope){
						configuration.scope = scope.$parent;
					}

                    var startOfDecade = function startOfDecade(unixDate) {
                        var startYear = (parseInt(moment.utc(unixDate).year() / 10, 10) * 10);
                        return moment.utc(unixDate).year(startYear).startOf('year');
                    };

                    //Min model and max Model changes
                    if (configuration.disableDaysBefore != null) {
                        localObj.minDate = moment().add(configuration.disableDaysBefore, 'day').startOf('day').valueOf();
                    }
                    else
                        localObj.minDate = 0;

                    if (configuration.minDateModel) {
                    	
                        configuration.scope.$watch(configuration.minDateModel, function (newValue, oldValue) {
                            if (configuration.disableDaysBefore != null) {
                                localObj.minDate = moment().add(configuration.disableDaysBefore, 'day').startOf('day').valueOf();
                            } else {
                                localObj.minDate = 0;
                            }

                            if (newValue) {
                                var utcMinDate = newValue.valueOf();
                                localObj.minDate = configuration.minDate > utcMinDate ? localObj.minDate : utcMinDate;                                
                            } else {
                                localObj.minDate = null;
                            }

                            scope.changeView(configuration.startView, new DateObject({ utcDateValue: getUTCTime(ngModelController.$viewValue) }));                            
                        });
                       
                    }
                    if (configuration.maxDateModel) {
                    	
                        configuration.scope.$watch(configuration.maxDateModel, function (newValue, oldValue) {
                            if (newValue) {
                                localObj.maxDate = newValue.valueOf();
                            } else {
                                localObj.maxDate = null;
                            }
                            
                            scope.changeView(configuration.startView, new DateObject({ utcDateValue: getUTCTime(ngModelController.$viewValue) }));
                        });
                       
                    }

                    if (configuration.displayModel) {
                    
                        configuration.scope.$watch(configuration.displayModel, function (newVal, oldVal) {
                            if (settingTime) {
                                settingTime = false;
                                return;
                            }
                            if (newVal) {
                                var dateSel = moment(newVal, configuration.displayFormat, true);
                                if (dateSel.isValid() && (dateSel.valueOf() >= localObj.minDate) && (localObj.maxDate ? (dateSel.valueOf() <= localObj.maxDate) : true)) {
                                    dataFactory.setTime(dateSel - localOffset);
                                }
                                else {
                                    settingTime = true;
                                    var parseFun = $parse(configuration.displayModel);
                                    parseFun.assign(configuration.scope, oldVal);
                                }
                            } else {
                                ngModelController.$setViewValue(null);
                                ngModelController.$render();
                            }

                        });
                    }

                    $(configuration.dropdownSelector).on('show.bs.dropdown', function () {
                        alert('dropdown being shown');
                    });

                    var dataFactory = {
                        year: function year(unixDate) {
                            var selectedDate = moment.utc(unixDate).startOf('year');
                            // View starts one year before the decade starts and ends one year after the decade ends
                            // i.e. passing in a date of 1/1/2013 will give a range of 2009 to 2020
                            // Truncate the last digit from the current year and subtract 1 to get the start of the decade
                            var startDecade = (parseInt(selectedDate.year() / 10, 10) * 10);
                            var startDate = moment.utc(startOfDecade(unixDate)).subtract(1, 'year').startOf('year');

                            var activeYear = ngModelController.$modelValue ? moment(ngModelController.$modelValue).year() : 0;

                            var result = {
                                'currentView': 'year',
                                'nextView': configuration.minView === 'year' ? 'setTime' : 'month',
                                'previousViewDate': new DateObject({
                                    utcDateValue: null,
                                    display: startDecade + '-' + (startDecade + 9)
                                }),
                                'leftDate': new DateObject({ utcDateValue: moment.utc(startDate).subtract(9, 'year').valueOf() }),
                                'rightDate': new DateObject({ utcDateValue: moment.utc(startDate).add(11, 'year').valueOf() }),
                                'dates': []
                            };

                            for (var i = 0; i < 12; i += 1) {
                                var yearMoment = moment.utc(startDate).add(i, 'years');
                                var dateValue = {
                                    'utcDateValue': yearMoment.valueOf(),
                                    'display': yearMoment.format('YYYY'),
                                    'past': yearMoment.year() < startDecade,
                                    'future': yearMoment.year() > startDecade + 9,
                                    'active': yearMoment.year() === activeYear,
                                    'selectable': (yearMoment.endOf('year').valueOf() + localOffset) >= localObj.minDate && (localObj.maxDate ? (yearMoment.startOf('year').valueOf() + localOffset) <= localObj.maxDate : true)
                                };

                                result.dates.push(new DateObject(dateValue));
                            }

                            return result;
                        },

                        month: function month(unixDate) {

                            var startDate = moment.utc(unixDate).startOf('year');
                            var previousViewDate = startOfDecade(unixDate);
                            var activeDate = ngModelController.$modelValue ? moment(ngModelController.$modelValue).format('YYYY-MMM') : 0;

                            var result = {
                                'previousView': 'year',
                                'currentView': 'month',
                                'nextView': configuration.minView === 'month' ? 'setTime' : 'day',
                                'previousViewDate': new DateObject({
                                    utcDateValue: previousViewDate.valueOf(),
                                    display: startDate.format('YYYY')
                                }),
                                'leftDate': new DateObject({ utcDateValue: moment.utc(startDate).subtract(1, 'year').valueOf() }),
                                'rightDate': new DateObject({ utcDateValue: moment.utc(startDate).add(1, 'year').valueOf() }),
                                'dates': []
                            };

                            for (var i = 0; i < 12; i += 1) {
                                var monthMoment = moment.utc(startDate).add(i, 'months');
                                var dateValue = {
                                    'utcDateValue': monthMoment.valueOf(),
                                    'display': monthMoment.format('MMM'),
                                    'active': monthMoment.format('YYYY-MMM') === activeDate,
                                    'selectable': (monthMoment.endOf('month').valueOf() + localOffset) >= localObj.minDate && (localObj.maxDate ? (monthMoment.startOf('month').valueOf() + localOffset) <= localObj.maxDate : true)
                                };

                                result.dates.push(new DateObject(dateValue));
                            }

                            return result;
                        },

                        day: function day(unixDate) {
                            var selectedDate = moment.utc(unixDate);
                            var startOfMonth = moment.utc(selectedDate).startOf('month');
                            var previousViewDate = moment.utc(selectedDate).startOf('year');
                            var endOfMonth = moment.utc(selectedDate).endOf('month');

                            var startDate = moment.utc(startOfMonth).subtract(Math.abs(startOfMonth.weekday()), 'days');

                            var activeDate = ngModelController.$modelValue ? moment(ngModelController.$modelValue).format('YYYY-MMM-DD') : '';

                            var result = {
                                'previousView': 'month',
                                'currentView': 'day',
                                'nextView': configuration.minView === 'day' ? 'setTime' : 'hour',
                                'previousViewDate': new DateObject({
                                    utcDateValue: previousViewDate.valueOf(),
                                    display: startOfMonth.format('YYYY-MMM'),
                                    overrideSelectable: true
                                }),
                                'leftDate': new DateObject({ utcDateValue: moment.utc(startOfMonth).subtract(1, 'months').valueOf() }),
                                'rightDate': new DateObject({ utcDateValue: moment.utc(startOfMonth).add(1, 'months').valueOf() }),
                                'dayNames': [],
                                'weeks': []
                            };


                            for (var dayNumber = 0; dayNumber < 7; dayNumber += 1) {
                                result.dayNames.push(moment.utc().weekday(dayNumber).format('dd'));
                            }

                            for (var i = 0; i < 6; i += 1) {
                                var week = { dates: [] };
                                for (var j = 0; j < 7; j += 1) {
                                    var monthMoment = moment.utc(startDate).add((i * 7) + j, 'days');
                                    var dateValue = {
                                        'utcDateValue': monthMoment.valueOf(),
                                        'display': monthMoment.format('D'),
                                        'active': monthMoment.format('YYYY-MMM-DD') === activeDate,
                                        'past': monthMoment.isBefore(startOfMonth),
                                        'future': monthMoment.isAfter(endOfMonth),
                                        'selectable': (monthMoment.endOf('day').valueOf() + localOffset) >= localObj.minDate && (localObj.maxDate ? (monthMoment.startOf('day').valueOf() + localOffset) <= localObj.maxDate : true)
                                    };
                                    week.dates.push(new DateObject(dateValue));
                                }
                                result.weeks.push(week);
                            }

                            return result;
                        },

                        hour: function hour(unixDate) {
                            var selectedDate = moment.utc(unixDate).startOf('day');
                            var previousViewDate = moment.utc(selectedDate).startOf('month');

                            var activeFormat = ngModelController.$modelValue ? moment(ngModelController.$modelValue).format('YYYY-MM-DD H') : '';

                            var result = {
                                'previousView': 'day',
                                'currentView': 'hour',
                                'nextView': configuration.minView === 'hour' ? 'setTime' : 'minute',
                                'previousViewDate': new DateObject({
                                    utcDateValue: previousViewDate.valueOf(),
                                    display: selectedDate.format('ll')
                                }),
                                'leftDate': new DateObject({ utcDateValue: moment.utc(selectedDate).subtract(1, 'days').valueOf() }),
                                'rightDate': new DateObject({ utcDateValue: moment.utc(selectedDate).add(1, 'days').valueOf() }),
                                'dates': []
                            };

                            for (var i = 0; i < 24; i += 1) {
                                var hourMoment = moment.utc(selectedDate).add(i, 'hours');
                                var dateValue = {
                                    'utcDateValue': hourMoment.valueOf(),
                                    'display': hourMoment.format('LT'),
                                    'active': hourMoment.format('YYYY-MM-DD H') === activeFormat,
                                    'selectable': (hourMoment.endOf('hour').valueOf() + localOffset) >= localObj.minDate && (localObj.maxDate ? (hourMoment.startOf('hour').valueOf() + localOffset) <= localObj.maxDate : true)
                                };

                                result.dates.push(new DateObject(dateValue));
                            }

                            return result;
                        },

                        minute: function minute(unixDate) {
                            var selectedDate = moment.utc(unixDate).startOf('hour');
                            var previousViewDate = moment.utc(selectedDate).startOf('day');
                            var activeFormat = ngModelController.$modelValue ? moment(ngModelController.$modelValue).format('YYYY-MM-DD H:mm') : '';

                            var result = {
                                'previousView': 'hour',
                                'currentView': 'minute',
                                'nextView': 'setTime',
                                'previousViewDate': new DateObject({
                                    utcDateValue: previousViewDate.valueOf(),
                                    display: selectedDate.format('lll')
                                }),
                                'leftDate': new DateObject({ utcDateValue: moment.utc(selectedDate).subtract(1, 'hours').valueOf() }),
                                'rightDate': new DateObject({ utcDateValue: moment.utc(selectedDate).add(1, 'hours').valueOf() }),
                                'dates': []
                            };

                            var limit = 60 / configuration.minuteStep;

                            for (var i = 0; i < limit; i += 1) {
                                var hourMoment = moment.utc(selectedDate).add(i * configuration.minuteStep, 'minute');
                                var dateValue = {
                                    'utcDateValue': hourMoment.valueOf(),
                                    'display': hourMoment.format('LT'),
                                    'active': hourMoment.format('YYYY-MM-DD H:mm') === activeFormat,
                                    'selectable': (hourMoment.endOf('minute').valueOf() + localOffset) >= localObj.minDate && (localObj.maxDate ? (hourMoment.startOf('minute').valueOf() + localOffset) <= localObj.maxDate : true)
                                };

                                result.dates.push(new DateObject(dateValue));
                            }

                            return result;
                        },

                        setTime: function setTime(unixDate) {
                            var tempDate = new Date(unixDate);
                            var newDate = new Date(tempDate.getTime() + (tempDate.getTimezoneOffset() * 60000));

                            var oldDate = ngModelController.$modelValue;
                            ngModelController.$setViewValue(moment(newDate).valueOf());
                            ngModelController.$render();

                            settingTime = true;
                            $parse(configuration.displayModel).assign(configuration.scope, moment(newDate).format(configuration.displayFormat));

                            if (configuration.dropdownSelector && jQuery(configuration.dropdownSelector).attr('aria-expanded') == 'true') {
                                jQuery(configuration.dropdownSelector).dropdown('toggle');
                            }

                            scope.onSetTime({ newDate: newDate, oldDate: oldDate });

                            return dataFactory[configuration.startView](unixDate);
                        }
                    };

                    var getUTCTime = function getUTCTime(modelValue) {
                        var tempDate = (modelValue ? moment(modelValue).toDate() : new Date());
                        return tempDate.getTime() - (tempDate.getTimezoneOffset() * 60000);
                    };

                    scope.changeView = function changeView(viewName, dateObject, event) {
                        if (event) {
                            event.stopPropagation();
                            event.preventDefault();
                        }

                        if (viewName && (dateObject.utcDateValue > -Infinity) && dateObject.selectable && dataFactory[viewName]) {
                            var result = dataFactory[viewName](dateObject.utcDateValue);

                            var weekDates = [];
                            if (result.weeks) {
                                for (var i = 0; i < result.weeks.length; i += 1) {
                                    var week = result.weeks[i];
                                    for (var j = 0; j < week.dates.length; j += 1) {
                                        var weekDate = week.dates[j];
                                        weekDates.push(weekDate);
                                    }
                                }
                            }

                            scope.beforeRender({
                                $view: result.currentView,
                                $dates: result.dates || weekDates,
                                $leftDate: result.leftDate,
                                $upDate: result.previousViewDate,
                                $rightDate: result.rightDate
                            });

                            scope.data = result;
                        }
                    };

                    ngModelController.$render = function $render() {
                    	
                        var viewValue = ngModelController.$viewValue;
                        var parseFun = $parse(configuration.displayModel);
                        if (viewValue) {
                            if (viewValue < localObj.minDate || (localObj.maxDate ? (viewValue > localObj.maxDate) : false)) {
                                ngModelController.$setViewValue(null);
                                ngModelController.$render();
                                if(parseFun){
                                    parseFun.assign(configuration.scope, null);                                    
                                }
                            } else {
                                settingTime = true;
                                if(parseFun){
                                    parseFun.assign(configuration.scope, moment(ngModelController.$viewValue).format(configuration.displayFormat));
                                }
                            }
                        } else {
                        	
                            if(parseFun){
                                parseFun.assign(configuration.scope, null);                                    
                            }
                        }
                        scope.changeView(configuration.startView, new DateObject({ utcDateValue: getUTCTime(ngModelController.$viewValue) }));
                    };
                }
            };
        }]);
}));
