(function () {
    'use strict';

    angular.module('app').factory('achievementSvc', achievement);

    achievement.$inject = ['$http'];

    function achievement($http) {
        var service = {
            achievementSum: achievementSum, //业绩汇总
            findAchievementDetail: findAchievementDetail, //部门业绩明细
            saveTopicDetailList: saveTopicDetailList,     // 保存课题明细
            deleteTopicMaintainDel: deleteTopicMaintainDel,//删除课题明细
            findTopicDetail: findTopicDetail,              //初始化课题明细
            exportAchievementDetail: exportAchievementDetail,  //员工业绩汇总导出
            exportDeptAchievementDetail: exportDeptAchievementDetail,  //部门业绩汇总导出（）
            exportTopicMaintainInfo: exportTopicMaintainInfo, //课题维护导出
            exportProReview: exportProReview //主办协办项目导出
        };
        return service;

        //S_业绩汇总
        function achievementSum(vm, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/getAchievementSum",
                data: vm.model
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_

        function findAchievementDetail(model, id, level, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/findAchievementDetail",
                params: {
                    yearName: model.year,
                    quarter: model.quarter,
                    id: id,
                    level: level
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_


        //S_保存课题业务
        function saveTopicDetailList(conditions, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/topicInfo/saveTopicDetailList",
                headers: {
                    "contentType": "application/json;charset=utf-8"  //设置请求头信息
                },
                traditional: true,
                dataType: "json",
                data: angular.toJson(conditions)//将Json对象序列化成Json字符串，JSON.stringify()原生态方法
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        /**
         * 删除课题信息
         * @param delIds
         * @param isCommit
         * @param callBack
         */
        function deleteTopicMaintainDel(delIds, callBack) {
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/topicInfo/topicMaintainDel",
                params: {
                    ids: delIds
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
            });
        }


        //S_初始化课题详情信息
        function findTopicDetail(vm, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/topicInfo/findTopicDetail",
                params: {
                    userId: vm.userId,
                    yearName:vm.model.year,
                    quarter:vm.model.quarter
                }
            };
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function () {
                }
            });
        }

        //员工业绩汇总导出
        function exportAchievementDetail(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/exportUserAchievement",
                params: {
                    yearName:vm.model.year,
                    quarter:vm.model.quarter
                }
            }
            var httpSuccess = function success(response) {
                var fileName = "员工业绩统计表.doc";
                var fileType = "msword";
                common.downloadReport(response.data, fileName, fileType);
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //部门业绩汇总导出
        function exportDeptAchievementDetail(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/exportDeptAchievementSum",
                data: vm.model
            }
            var httpSuccess = function success(response) {
                var fileName = "部门业绩统计表.doc";
                var fileType = "msword";
                common.downloadReport(response.data, fileName, fileType);
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //部门业绩汇总导出
        function exportDeptAchievementDetail(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/exportDeptAchievementSum",
                data: vm.model
            }
            var httpSuccess = function success(response) {
                var fileName = "部门业绩统计表.doc";
                var fileType = "msword";
                common.downloadReport(response.data, fileName, fileType);
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //课题维护导出
        function exportTopicMaintainInfo(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/exportTopicMaintainInfo",
                params: {userId: vm.userId}
            };
            var httpSuccess = function success(response) {
                var fileName = "课题研究一览表.doc";
                var fileType = "msword";
                common.downloadReport(response.data, fileName, fileType);
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //主/协办项目一览表
        function exportProReview(vm,isMain) {
            var fileName1 = "";
            if (isMain == '9') {
                fileName1 = "主办项目一览表.doc";
            } else {
                fileName1 = "协办项目一览表.doc";
            }
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/exportProReview",
                params: {
                    yearName:vm.model.year,
                    quarter:vm.model.quarter,
                    level:vm.level,
                    isMainUser:isMain
                }
            }
            var httpSuccess = function success(response) {
                var fileName = fileName1;
                var fileType = "msword";
                common.downloadReport(response.data, fileName, fileType);
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

    }
})();