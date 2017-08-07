(function () {
    'use strict';

    angular.module('app').factory('pauseProjectSvc', pauseProject);

    pauseProject.$inject = ['$http', '$state'];

    function pauseProject($http, $state) {
        var service = {
            pauseProjectWindow : pauseProjectWindow,    //项目暂停弹出框
            pauseProject: pauseProject,//保存暂停项目
            initProject : initProject, //初始化项目信息
            countUsedWorkday : countUsedWorkday,    //初始化已用工作日
            grid : grid,
            getProjectStopByStopId : getProjectStopByStopId,    //通过id获取暂停项目
            updateProjectStop : updateProjectStop,  //更新暂停项目审批信息
            findPausingProject : findPausingProject //查找正在申请暂停的项目
        };
        return service;

        function pauseProjectWindow(vm,signid,stopid){
            vm.sign={};
            vm.projectStop = {};
            vm.projectStop.signid = signid;
            $("#spwindow").kendoWindow({
                width: "1000px",
                height: "600px",
                title: "暂停项目审批处理",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
            initProject(vm,signid);
            countUsedWorkday(vm,signid);
            if(stopid !=""){
                vm.showIdea=true;
                getProjectStopByStopId(vm,stopid);
            }

        }

        //beign findPausingProject
        function findPausingProject(vm,signId,stopid){
            var httpOptions={
                method : "get",
                url : rootPath + "/projectStop/findPausingProject",
                params :{signId : signId}
            }

            var httpSuccess=function success(response){
                if(response.data !=""){
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "该项目暂停申请正在处理",
                                fn: function () {
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                }
                            })
                        }

                    });
                }else{
                    pauseProjectWindow(vm,signId,stopid);
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
        function initProject(vm,signId){

            var httpOptions={
                method : "get",
                url : rootPath + "/projectStop/initProjectBySignId",
                params :{signId : signId}
            }

            var httpSuccess=function success(response){
                vm.sign=response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }//end initProject

        //begin countUsedWorkday
        function countUsedWorkday(vm,signid){
            var httpOptions={
                method: "get",
                url : rootPath +"/projectStop/countUsedWorkday",
                params : {signId : signid}
            }

            var httpSuccess=function success(response){
                vm.sign.countUsedWorkday=response.data;
            }
            common.http({
                vm : vm,
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess
            });
        }
        //end countUsedWorkday

        //start_pauseProject
        function pauseProject(vm){
            common.initJqValidation();
            var isValid = $('#form').valid();
            if (isValid) {
                var materials = [];
                materials.push($('#file1').val());
                materials.push($('#file2').val());
                var materialStr = materials.join(",");
                vm.projectStop.material = materialStr;
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/projectStop/savePauseProject",
                    data: vm.projectStop
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            window.parent.$("#spwindow").data("kendoWindow").close();
                            vm.gridOptions.dataSource.read();
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                }
                            })
                        }

                    });
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        } //end_pauseProject

        //begin getProjectStopByStopId
        function getProjectStopByStopId(vm,stopId){
            var httpOptions={
                method : "get",
                url : rootPath + "/projectStop/getProjectStopByStopId",
                params : {stopId : stopId}
            }

            var httpSuccess=function success(response){
                vm.projectStop = response.data;
                if( vm.projectStop.directorIdeaContent == undefined){
                    vm.projectStop.directorIdeaContent="";
                }
                if(vm.projectStop.leaderIdeaContent == undefined){
                    vm.projectStop.leaderIdeaContent="";
                }
            }

            common.http({
                vm : vm,
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
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            window.parent.$("#spwindow").data("kendoWindow").close();
                            vm.gridOptions.dataSource.read();
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                }
                            })
                        }

                    });
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
                            $("#pauseCount").html(data['count']);
                        } else {
                            $("#pauseCount").html(0);
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
                            return common.format($("#columnBtns").html(),"vm.pauseProjectWindow('"+item.sign.signid+"','"+item.stopid+"')");
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