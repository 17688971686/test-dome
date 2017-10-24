(function () {
    'use strict';

    angular.module('app').factory('archivesLibrarySvc', archivesLibrary);

    archivesLibrary.$inject = ['$http', 'bsWin'];

    function archivesLibrary($http, bsWin) {
        var url_archivesLibrary = rootPath + "/archivesLibrary", url_back = '#/archivesLibraryList';
        var service = {
            initArchivesLibrary: initArchivesLibrary,   //初始化档案借阅信息
            startFlow : startFlow,                      //发起流程
            initFlowDeal : initFlowDeal,                //初始化流程信息
            grid: grid,									 //项目借阅审批列表
            createArchivesLibrary: createArchivesLibrary,//保存中心档案借阅
            createCityLibrary: createCityLibrary, 		 //保存市档案借阅
            getArchivesUserName: getArchivesUserName,	 //获取归档员
            deleteArchivesLibrary: deleteArchivesLibrary,
        };

        return service;

        //S_初始化档案借阅信息
        function initArchivesLibrary(id, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/archivesLibrary/findById",
                params:{
                    id : id
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
                success: httpSuccess
            });
        }//E_initArchivesLibrary

        //S_发起流程
        function startFlow(id,callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/archivesLibrary/startFlow",
                params:{
                    id : id
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
                success: httpSuccess
            });
        }//E_startFlow

        //S_初始化流程信息
        function initFlowDeal(vm){
            initArchivesLibrary(vm.businessKey,function(data){
                vm.model = data;
            });
        }//E_initFlowDeal

        //S 获取归档员
        function getArchivesUserName(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/archivesLibrary/findByArchivesUser",
            };
            var httpSuccess = function success(response) {
                vm.userlist = response.data;
                console.log(vm.userlist);
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }// E 获取归档员
        // begin#保存市档案借阅
        function createCityLibrary(model, callBack) {
            var httpOptions = {
                method: 'post',
                url: url_archivesLibrary + "/saveCity",
                data: model
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
        }

        // begin#deleteArchivesLibrary
        function deleteArchivesLibrary(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_archivesLibrary,
                data: id
            };

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: "操作成功",
                            closeDialog: true,
                            fn: function () {
                                vm.isSubmit = false;
                                vm.gridOptions.dataSource.read();
                            }
                        })
                    }
                });
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#保存中心档案借阅
        function createArchivesLibrary(model, callBack) {
            var httpOptions = {
                method: 'post',
                url: url_archivesLibrary + "/savaLibrary",
                data: model
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
        }

        // begin#grid
        function grid(vm) {
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/archivesLibrary/findByOData",$("#searchform")),
                schema: {
                    data: "value",
                    total: function (data) {
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

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>",
                },

                {
                    field: "readCompany",
                    title: "项目单位",
                    width: 140,
                    filterable: false
                },

                {
                    field: "readProjectName",
                    title: "查阅项目",
                    width: 140,
                    filterable: false
                },
                {
                    field: "readUsername",
                    title: "借阅人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "readArchivesCode",
                    title: "归档编号",
                    width: 100,
                    filterable: false
                },
                {
                    field: "readDate",
                    title: "借阅时间",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "",
                    title: "操作",
                    width: 100,
                    template: function (item) {
                        if(item.processInstanceId){
                            return common.format($('#columnBtns').html(),"flowEnd/"+item.id, "FLOW_ARCHIVES", item.processInstanceId);
                        }else{
                            return "";
                        }

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
                dataBound: function () {
                    var rows = this.items();
                    var page = this.pager.page() - 1;
                    var pagesize = this.pager.pageSize();
                    $(rows).each(function () {
                        var index = $(this).index() + 1 + page * pagesize;
                        var rowLabel = $(this).find(".row-number");
                        $(rowLabel).html(index);
                    });
                },
                resizable: true
            };

        }// end fun grid


    }
})();