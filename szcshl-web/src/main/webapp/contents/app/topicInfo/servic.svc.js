(function () {
    'use strict';

    angular.module('app').factory('topicSvc', topicService);

    topicService.$inject = ['$http','bsWin','sysfileSvc'];

    function topicService($http,bsWin,sysfileSvc) {
        var service = {
            findOrgUser: findOrgUser,                   //查询当前用户所在部门的所有用户信息
            createTopic : createTopic,                  //创建课题研究信息
            updateTopic : updateTopic,                  //更新课题研究信息
            startFlow : startFlow,                      //发起课题研究流程
            initFlowDeal : initFlowDeal,                //初始化课题研究流程信息
            initFlowNode : initFlowNode,                //初始化流程环节信息
            initMyGird : initMyGird,                    //初始化我的课题列表
            initDetail : initDetail,                    //初始化详情信息
            queryGrid : queryGrid ,                     //初始化课题查询列表
            saveContractDetailList : saveContractDetailList, //保存合同信息
            deleteContractConditions: deleteContractConditions  //删除合同信息
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

        //S_更新课题研究信息
        function updateTopic(topicModel,isCommit,callBack){
            isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/topicInfo/updateTopic",
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
        }//

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
                vm.model = {};
                vm.model = response.data;
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
                    width: "25%",
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
                    width: "15%",
                    template: function (item) {
                        //如果已经发起流程，则只能查看
                        var isStartFlow = item.processInstanceId?true:false;
                        return common.format($('#columnBtns').html(), item.id, isStartFlow,"flowEnd/"+item.id+"/"+flowcommon.getFlowDefinedKey().TOPIC_FLOW+"/"+item.processInstanceId,!isStartFlow);
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

        //S_保存合同信息
        function saveContractDetailList(conditions,callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/topicInfo/saveContractDetailList",
                headers:{
                    "contentType":"application/json;charset=utf-8"  //设置请求头信息
                },
                traditional: true,
                dataType : "json",
                data : angular.toJson(conditions)//将Json对象序列化成Json字符串，JSON.stringify()原生态方法
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess
            });
        }

        /**
         * 删除合同信息
         * @param delIds
         * @param isCommit
         * @param callBack
         */
        function deleteContractConditions(delIds,callBack){
            var httpOptions = {
                method : 'delete',
                url: rootPath + "/topicInfo/contractDel",
                params:{
                    ids : delIds
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess,
            });
        }

        //S_初始化流程环节信息
        function initFlowNode(flow,showFlag,vm){
            switch (flow.curNode.activitiId){
                case flowcommon.getTopicFlowNode().TOPIC_BZSH_JH:
                case flowcommon.getTopicFlowNode().TOPIC_FGLD_JH:
                case flowcommon.getTopicFlowNode().TOPIC_ZRSH_JH:
                case flowcommon.getTopicFlowNode().TOPIC_BZSH_FA:
                case flowcommon.getTopicFlowNode().TOPIC_FGLD_FA:
                case flowcommon.getTopicFlowNode().TOPIC_ZRSH_FA:

                case flowcommon.getTopicFlowNode().TOPIC_BZSH_BG:
                case flowcommon.getTopicFlowNode().TOPIC_FGLD_BG:
                case flowcommon.getTopicFlowNode().TOPIC_ZRSH_BG:
                case flowcommon.getTopicFlowNode().TOPIC_BZSH_JT:
                case flowcommon.getTopicFlowNode().TOPIC_FGLD_JT:
                case flowcommon.getTopicFlowNode().TOPIC_ZRSH_JT:
                case flowcommon.getTopicFlowNode().TOPIC_BZSH_GD:
                case flowcommon.getTopicFlowNode().TOPIC_GDY_QR:
                    showFlag.buttBack = true;
                    break;
                //显示附件上传按钮
                case flowcommon.getTopicFlowNode().TOPIC_CGJD:
                case flowcommon.getTopicFlowNode().TOPIC_KTBG:
                case flowcommon.getTopicFlowNode().TOPIC_KTJT:
                    if(flow.businessMap) {  //判断是否第一负责人
                        vm.showFlag.isMainPrinUser = flow.businessMap.MAIN_USER;
                        vm.showFlag.expertEdit = true;          //评审费编辑
                    }
                    break;
                case flowcommon.getTopicFlowNode().TOPIC_LXDW:
                case flowcommon.getTopicFlowNode().TOPIC_QDHT:
                case flowcommon.getTopicFlowNode().TOPIC_YJSS:
                case flowcommon.getTopicFlowNode().TOPIC_NBCS:
                case flowcommon.getTopicFlowNode().TOPIC_YFZL:
                    showFlag.showUploadBT = true;
                    vm.initFileUpload(sysfileSvc.mainTypeValue().TOPIC,sysfileSvc.mainTypeValue().TOPIC,sysfileSvc.mainTypeValue().TOPIC);
                    break;
                //课题计划环节
                case flowcommon.getTopicFlowNode().TOPIC_JHTC:
                    showFlag.businessTr = true;
                    break;
                //工作方案填报环节
                case flowcommon.getTopicFlowNode().TOPIC_GZFA:
                    showFlag.businessTr = true;
                    /*if(flow.businessMap) {  //判断是否第一负责人
                     showFlag.isMainPrinUser = flow.businessMap.MAIN_USER;
                     }*/
                    break;
                //归档环节
                case flowcommon.getTopicFlowNode().TOPIC_ZLGD:
                    showFlag.businessTr = true;
                    vm.showFlag.isMainPrinUser = true;      //可以进行专家评分
                    vm.showFlag.expertEdit = true;          //评审费编辑
                    break;
            }
        }//E_initFlowNode

        //begin queryGrid
        function queryGrid(vm){
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/topicInfo/findByOData", $("#queryTopicForm")),
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
                    field: "rowNumber",
                    title: "序号",
                    width: 50,
                    filterable : false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "",
                    title: "课题名称",
                    width: 200,
                    filterable: false,
                    template : function(item){
                        if(item.processInstanceId){
                            return '<a href="#/topicDetail/'+ item.id
                                + '/'  + item.processInstanceId +'">' + item.topicName + '</a>';
                        }else{
                            return '<a href="#/topicDetail/'+ item.id
                                + '/">' + item.topicName + '</a>';
                        }

                    }
                },
                {
                    field: "cooperator",
                    title: "合作单位",
                    width: 200,
                    filterable: false,
                },
                {
                    field: "createdDate",
                    title: "创建日期",
                    width: 100,
                    filterable: false,
                    format: "{0:yyyy/MM/dd}"
                },
                {
                    field: "endTime",
                    title: "结题日期",
                    width: 100,
                    filterable: false,
                    format: "{0:yyyy/MM/dd}"
                },
                {
                    field: "orgName",
                    title: "申报部门",
                    width: 100,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 100,
                    template: function (item) {
                        return "";
                    }
                }
            ];
            // End:column

            vm.queryTopicOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound:dataBound,
                resizable: true
            };
        }
        //end queryGrid

    }
})();