(function () {
    'use strict';

    angular.module('app').factory('topicSvc', topicService);

    topicService.$inject = ['$http','bsWin'];

    function topicService($http,bsWin) {
        var service = {
            findOrgUser: findOrgUser,                   //查询当前用户所在部门的所有用户信息
            createTopic : createTopic,                  //创建课题研究信息
            startFlow : startFlow,                      //发起课题研究流程
            initFlowDeal : initFlowDeal,                //初始化课题研究流程信息
            initMyGird : initMyGird,                    //初始化我的课题列表
            initDetail : initDetail,                    //初始化详情信息
        };

        return service;

        //S_查询当前用户所在部门的所有用户信息
        function findOrgUser(callBack){
            var httpOptions = {
                method: 'get',
                url: rootPath + "/user/findChargeUsers",
            };
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
        }//E_findOrgUser

        //S_创建课题研究信息
        function createTopic(topicModel,isCommit,callBack){
            isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/topicInfo",
                data : topicModel
            };
            var httpSuccess = function success(response) {
                isCommit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError : function(){isCommit = false;}
            });
        }//E_createTopic

        //S_启动流程
        function startFlow(topicModel,isCommit,callBack){
            isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/topicInfo/startFlow",
                data : topicModel
            };
            var httpSuccess = function success(response) {
                isCommit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError : function(){isCommit = false;}
            });
        }//E_startFlow

        //S_初始化课题研究流程信息
        function initFlowDeal(vm){
            //vm.businessKey,vm.taskId,vm.instanceId
            var httpOptions = {
                method: 'post',
                url: rootPath + "/topicInfo/findById",
                params : {
                    id:vm.businessKey
                }
            };
            var httpSuccess = function success(response) {
                vm.topic = response.data;
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError : function(){}
            });
        }//E_initFlowDeal

        //S_初始化我的课题列表
        function initMyGird(vm){
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/topicInfo/findByOData", $("#myTopicForm"),{filter: "createdBy eq ${CURRENT_USER.id}"}),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            // End:dataSource
            //S_序号
            var  dataBound=function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            }
            //S_序号
            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />", item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"

                },
                {
                    field: "rowNumber",
                    title: "序号",
                    width: 50,
                    filterable : false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "topicName",
                    title: "课题名称",
                    width: "25%",
                    filterable: false
                },
                {
                    field: "cooperator",
                    title: "合作单位",
                    width: "20%",
                    filterable: false,
                },
                {
                    field: "createdDate",
                    title: "创建日期",
                    width: "15%",
                    filterable: false,
                    format: "{0:yyyy/MM/dd HH:mm:ss}"
                },
                {
                    field: "",
                    title: "已发起流程",
                    width: "15%",
                    filterable: false,
                    template: function (item) {
                        if(item.processInstanceId){
                            return "是";
                        }else{
                            return "否";
                        }
                    }
                },

                {
                    field: "",
                    title: "操作",
                    width: 180,
                    template: function (item) {
                        var isStartFlow = item.processInstanceId?true:false;
                        return common.format($('#columnBtns').html(), item.id, isStartFlow);
                    }
                }
            ];
            // End:column

            vm.myTopicOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound:dataBound,
                resizable: true
            };
        }//E_initMyGird

        //S_初始化详情信息
        function initDetail(topicId,callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/topicInfo/findDetailById",
                params : {
                    id:topicId
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
                onError : function(){}
            });
        }
    }
})();