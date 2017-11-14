(function () {
    'use strict';

    angular.module('app').factory('meetingSvc', meeting);

    meeting.$inject = ['$http'];

    function meeting($http) {
        var url_meeting = rootPath + "/meeting";
        var url_back = '#/meeting';
        var service = {
            grid: grid,
            getMeetingById: getMeetingById,
            createMeeting: createMeeting,               //创建会议室
            deleteMeeting: deleteMeeting,               //删除会议室
            updateMeeting: updateMeeting,               //更新会议室
            roomUseState: roomUseState,                 //更改会议室状态
            findAllMeeting:findAllMeeting,              //查询所有的会议室
        };
        return service;


        // begin#updateUser
        function updateMeeting(model,isSubmit,callBack) {
            isSubmit = true;
            var httpOptions = {
                method: 'put',
                url: url_meeting,
                data: model
            }

            var httpSuccess = function success(response) {
                isSubmit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#deleteUser
        function deleteMeeting(id,isSubmit,callBack) {
            isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_meeting,
                params: {id: id}
            }
            var httpSuccess = function success(response) {
                isSubmit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        function roomUseState(model,callBack) {
            var httpOptions = {
                method: 'put',
                url: url_meeting + "/roomUseState",
                data: model
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
        }

        // begin#createUser
        function createMeeting(model,iscommit,callBack) {
            iscommit = true;
            var httpOptions = {
                method: 'post',
                url: url_meeting,
                data: model
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
                onError: function (response) {
                    iscommit = false;
                }
            });
        }

        // begin#getUserById
        function getMeetingById(vm) {
            var httpOptions = {
                method: 'get',
                url: url_meeting + "/html/findByIdMeeting",
                params: {id: vm.id}
            }
            var httpSuccess = function success(response) {
                vm.model = response.data;
            }

            common.http({
                vm: vm,
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
                transport: common.kendoGridConfig().transport(url_meeting + "/fingByOData", $("#meetingFrom")),
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

            // End:dataSourc

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
                    filterable: false,
                    template: "<span class='row-number'></span>"
                }
                ,
                {
                    field: "num",
                    title: "编号",
                    width: "8%",
                    filterable: false
                },
                {
                    field: "mrName",
                    title: "会议室名称",
                    width: "25%",
                    filterable: false
                },
                {
                    field: "mrType",
                    title: "类型",
                    width: "10%",
                    filterable: false
                },
                {
                    field: "addr",
                    title: "会议室地点",
                    width: "25%",
                    filterable: false
                },
                {
                    field: "capacity",
                    title: "容量",
                    width: "10%",
                    filterable: false
                },
                /*  {
                 field: "userName",
                 title: "负责人",
                 width: 70,
                 filterable: false
                 },
                 {
                 field: "userPhone",
                 title: "负责人电话",
                 width: 120,
                 filterable: false
                 },*/
                {
                    field: "",
                    title: "状态",
                    width: "8%",
                    template: function (item) {
                        if (item.mrStatus == "2") {
                            return "停用";
                        } else {
                            return "正常";
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: "14%",
                    template: function (item) {
                        var isUse = false;
                        if (item.mrStatus == "2") {
                            isUse = true;//会议室可用
                        } else {
                            isUse = false;//会议室不可用
                        }
                        //return common.format($('#columnBtns').html(),"vm.stoped('" + item.id + "')",isUse,"vm.used('" + item.id + "')",isUse, item.id,"vm.del('" + item.id + "')");
                        return common.format($('#columnBtns').html(), "vm.stoped('" + item.id + "')", isUse, "vm.used('" + item.id + "')", isUse, item.id);
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


        //S_查询所有的会议室
        function findAllMeeting(callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/meeting/findAll",
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