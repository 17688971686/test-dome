(function () {
    'use strict';

    angular.module('app').factory('quartzSvc', quartz);

    quartz.$inject = ['$http'];

    function quartz($http) {
        var url_quartz = rootPath + "/quartz", url_back = '#/quartz';
        var service = {
            grid: grid,
            getQuartzById: getQuartzById,   //根据ID查询定时器
            saveQuartz: saveQuartz,         //保存定时器
            deleteQuartz: deleteQuartz,     //停用定时器
            quartzExecute: quartzExecute,	//执行定时器
            quartzStop: quartzStop	        //停止执行定时器
        };

        return service;

        //begin quartzExecute
        function quartzExecute(id, callBack) {
            var httpOptions = {
                method: "put",
                url: url_quartz + "/quartzExecute",
                params: {quartzId: id}

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
        }//end quartzExecute

        //begin quartzStop
        function quartzStop(id, callBack) {
            var httpOptions = {
                method: "post",
                url: url_quartz + "/quartzStop",
                params: {quartzId: id}

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
        }//end quartzStop


        // begin#deleteQuartz
        function deleteQuartz(id,callBack) {
            var httpOptions = {
                method: 'delete',
                url: url_quartz,
                params: {
                    id:id
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
        }

        // begin#createQuartz
        function saveQuartz(quartz, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/quartz",
                data: quartz,
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

        // begin#getQuartzById
        function getQuartzById(id,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/quartz/html/findById",
                params: {id: id}
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
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/quartz/findByOData"),
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
            var dataBound = function () {
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
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "quartzName",
                    title: "定时器名称",
                    width: 200,
                    filterable: false,
                },
                {
                    field: "className",
                    title: "类名",
                    width: 260,
                    filterable: false,
                },
                {
                    field: "cronExpression",
                    title: "表达式",
                    width: 150,
                    filterable: false,
                },
                {
                    field: "",
                    title: "状态",
                    width: 80,
                    filterable: false,
                    template: function (item) {
                        if (item.curState) {
                            if (item.curState == "9") {
                                return "<span class='label label-success'>正在执行</span>";
                            }
                            if (item.curState == "0") {
                                return "<span class='label label-warning'>已暂停</span>";
                            }
                        } else {
                            return "";
                        }
                    }
                },
                {
                    field: "descInfo",
                    title: "描述",
                    width: 120,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 80,
                    template: function (item) {
                        var canExecute = false;
                        if(item.curState == 9){
                            canExecute = true;
                        }
                        return common.format($('#columnBtns').html(), "vm.edit('" + item.id + "')", "vm.execute('" + item.id + "')", !canExecute, "vm.stop('" + item.id + "')",canExecute);
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
                dataBound: dataBound,
                resizable: true
            };
        }// end fun grid

    }
})();