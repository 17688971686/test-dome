/**
 *定配置angular应用指令
 * Created by Administrator on 2017/9/7 0007.
 */
(function(){
    'use strict';
    var appModule = angular.module('app');
    //返回
    appModule.directive('goBack', function() {
        return {
            restrict : 'AE',
            template : '<a class="btn btn-sm btn-primary" href="javascript:void(0);" ng-click="back();"><span class="glyphicon glyphicon-chevron-left"></span>返回</a>'
        };
    });
    //返回项目签收流程
    appModule.directive('goBackSignflow', function() {
        return {
            restrict : 'AE',
            template : '<button class="btn btn-sm btn-danger" ng-click="backtoflow();"><span class="glyphicon glyphicon-chevron-left"></span>返回流程</button>'
        };
    });
    //流程上传和查看附件按钮
    appModule.directive('flowFileButton', function() {
        return {
            restrict : 'AE',
            template : '<button class="btn btn-sm btn-primary" ng-click="vm.clickUploadBt();" id="upload_file_bt">上传附件</button> <button class="btn btn-sm btn-primary" ng-click="vm.clickDetailBt();" id="detail_file_bt">查看附件</button>'
        };
    });
})();
