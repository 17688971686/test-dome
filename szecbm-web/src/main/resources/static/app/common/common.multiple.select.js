/**
 * 封装multipleSelect的angular指令
 */
(function () {
    'use strict';

    if (typeof angular === 'undefined') {
        return;
    }

    var config = {
        placeholder: "请选择单位",
        filter: true,
        noMatchesFound: '未查询到单位',
        single: true
    }

    angular.module('sn.common').directive('snMultipleSelect', function ($timeout) {

        return {
            restrict: 'EA',
            require: '?ngModel',
            scope: {
                snMultipleSelect: '=',
                ngModel: '='
            },
            link: function (scope, element, attrs, ngModel) {
                element.multipleSelect($.extend({}, config, attrs));

                var isLoad = false, isNewValue = false;
                scope.$watch('snMultipleSelect', function (value) {
                    if (!value) {
                        return;
                    }

                    var tags = [];
                    $.each(value, function (index, v) {
                        tags.push(util.format('<option value="{0}">{1}</option>', v.organId, v.organName));
                    });
                    element.html(tags.join(""));

                    $timeout(function () {
                        element.multipleSelect("refresh", true);
                        setElValue();
                        isLoad = true;
                    }, 100)
                });

                scope.$watch(function(){
                    return ngModel.$modelValue;
                }, function(newValue){
                    isNewValue = true;
                    if (isLoad) {
                        $timeout(setElValue);
                    }
                })

                function setElValue() {
                    if (isNewValue) {
                        element.multipleSelect("setSelects", [ngModel.$modelValue]);
                        isNewValue = false;
                    }
                }
            }
        }
    });

})();