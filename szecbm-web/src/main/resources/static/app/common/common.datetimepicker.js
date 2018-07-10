/**
 * 封装 datetimepicker 的 angular 指令
 * 例子：
 * <div class="input-group date" sn-datetimepicker format="yyyy-mm-dd" style="width: 200px;">
 *    <input class="form-control" size="16" type="text" name="beginDate" ng-model="vm.model.beginDate" readonly>
 *    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
 *    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
 * </div>
 *
 */
(function () {
    'use strict';

    if (typeof angular === 'undefined') {
        return;
    }

    angular.module('sn.common').directive('snDatetimepicker', function () {

        var defaultAttr = {
            language: 'zh-CN',
            autoclose: true,
            minView: 2,
            pickerPosition: "bottom-right",
            format: 'yyyy-mm-dd',
            todayBtn: true,
            todayHighlight: true
        }

        return {
            restrict: 'A',
            require: '?ngModel',
            scope: {
                ngModel: '='
            },
            link: function (scope, element, attr, ngModel) {

                element.datetimepicker($.extend(defaultAttr, attr));

            }
        }
    });

})();