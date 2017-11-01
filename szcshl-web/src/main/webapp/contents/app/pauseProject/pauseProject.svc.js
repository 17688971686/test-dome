(function () {
    'use strict';

    angular.module('app').factory('pauseProjectSvc', pauseProject);

    pauseProject.$inject = ['$http', '$state' , 'bsWin'];

    function pauseProject($http, $state , bsWin) {
        var service = {
            initProject : initProject,                  //初始化项目信息
            pauseProject: pauseProject,                 //保存暂停项目
            initFlowDeal : initFlowDeal,                //初始化流程信息
            grid : grid,
            getProjectStopByStopId : getProjectStopByStopId,    //通过id获取暂停项目
            updateProjectStop : updateProjectStop,  //更新暂停项目审批信息
            findPausingProject : findPausingProject, //查找正在申请暂停的项目
            getListInfo : getListInfo , //获取审批结果通过的项目列表
        };
        return service;

        //begin getListInfo
        function getListInfo(signId , callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/projectStop/getListInfo",
                params : {signId : signId}
            }
            var httpSuccess = function success(response){
                if(callBack !=undefined && typeof  callBack == "function"){
                    callBack(response.data);
                }
            }

            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });

        }
        //end getListInfo

        //beign findPausingProject
        function findPausingProject(vm,signId){
            var httpOptions={
                method : "get",
                url : rootPath + "/projectStop/findPausingProject",
                params :{signId : signId}
            }

            var httpSuccess=function success(response){
                if(response.data !=""){
                    bsWin.success("该项目暂停申请正在处理");

                }else{
                    $state.go("projectStopForm" , {signId : signId , stopId : ''} );

                }
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //end findPausingProject

        //begin initProject
        function initProject(signId,callBack){
            var httpOptions={
                method : "post",
                url : rootPath + "/projectStop/initProjectBySignId",
                params :{
                    signId : signId
                }
            }

            var httpSuccess=function success(response){
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }//end initProject

        //S_保存项目暂停表信息
        function pauseProject(projectStop,callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/projectStop/savePauseProject",
                data: projectStop
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
        } //end_pauseProject

        //S_初始化项目暂停信息
        function initFlowDeal(vm){
            getProjectStopByStopId(vm.businessKey,function(data){
                vm.model = data;
                vm.sign = vm.model.signDispaWork;
            });
        }//E_initFlowDeal

        //begin getProjectStopByStopId
        function getProjectStopByStopId(stopId , callBack){
            var httpOptions={
                method : "post",
                url : rootPath + "/projectStop/getProjectStopByStopId",
                params : {
                    stopId : stopId
                }
            }

            var httpSuccess=function success(response){
                if( callBack != undefined && typeof  callBack == 'function'){
                    return callBack(response.data);
                }
            }

            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end getProjectStopByStopId

        //begin updataProjectStop
        function updateProjectStop(vm){
            common.initJqValidation();
            var isValid = $('#form').valid();
            if (isValid) {
                var httpOptions = {
                    method: "post",
                    url: rootPath + "/projectStop/updateProjectStop",
                    data: vm.projectStop
                }
                var httpSuccess = function success(response) {
                    bsWin.success("操作成功");
                    $state.go('pauseProject');

                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }
        //end updataProjectStop


        function grid(vm){
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/projectStop/findByOData"),
                schema: {
                    data: "value",
                    total: function (data) {
                        if (data['count']) {
                            $("#PAUSE_COUNT").html(data['count']);
                        } else {
                            $("#PAUSE_COUNT").html(0);
                        }
                        return data['count'];
                    },
                    model: {
                        id: "id",
                        fields: {
                            createdDate: {
                                type: "date"
                            },
                            modifiedDate: {
                                type: "date"
                            }
                        }
                    }
                },
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

            var columns = [
                {
                    field: "",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>",
                },
                {
                    field: "",
                    title: "项目名称",
                    width: 200,
                    filterable: false,
                    template: function (item) {
                        return item.sign.projectname;
                    }
                },
                {
                    field: "",
                    title : "评审阶段",
                    width : 100,
                    filterable : false,
                    template : function(item){
                        return item.sign.reviewstage;
                    }
                },
                {
                    field : "createdDate",
                    title : "申请日期",
                    width : 100,
                    format : "{0:yyyy-MM-dd}",
                    filterable : false
                },
                {
                    field : "",
                    title : "操作",
                    width : 100,
                    filterable : false,
                    template : function(item){
                        return common.format($("#columnBtns").html(),item.sign.signid , item.stopid );
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                dataBound: function () {
                    var rows = this.items();
                    var page = this.pager.page() - 1;
                    var pagesize = this.pager.pageSize();
                    $(rows).each(function () {
                        var index = $(this).index() + 1 + page * pagesize;
                        var rowLabel = $(this).find(".row-number");
                        $(rowLabel).html(index);
                    });
                }
            };


        }


    }
})();