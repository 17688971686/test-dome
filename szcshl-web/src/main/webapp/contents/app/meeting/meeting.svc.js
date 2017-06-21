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
            createMeeting: createMeeting,
            deleteMeeting: deleteMeeting,
            updateMeeting: updateMeeting,
            queryMeeting: queryMeeting,		//会议室查询
            roomUseState: roomUseState
        };

        return service;

        //会议室查询
        function queryMeeting(vm) {
            vm.gridOptions.dataSource.read();
        }

        // begin#updateUser
        function updateMeeting(vm) {

            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id
                var httpOptions = {
                    method: 'put',
                    url: url_meeting,
                    data: vm.model
                }

                var httpSuccess = function success(response) {

                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                }
                            })
                        }

                    })
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            } else {
                // common.alert({
                // vm:vm,
                // msg:"您填写的信息不正确,请核对后提交!"
                // })
            }

        }

        // begin#deleteUser
        function deleteMeeting(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_meeting,
                data: id

            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.isSubmit = false;
                        vm.gridOptions.dataSource.read();
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

        function roomUseState(vm) {
            var httpOptions = {
                method: 'put',
                url: url_meeting + "/roomUseState",
                data: vm.model
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.gridOptions.dataSource.read();
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

        // begin#createUser
        function createMeeting(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                var httpOptions = {
                    method: 'post',
                    url: url_meeting,
                    data: vm.model
                }

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                    location.href = url_back;
                                }
                            })
                        }
                    });

                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess,
                    onError: function (response) {
                        vm.iscommit = false;
                    }
                });

            }
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
                    width: 30,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "rowNumber",
                    title: "序号",
                    width: 40,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                }
                ,
                {
                    field: "num",
                    title: "会议室编号",
                    width: 100,
                    filterable: false
                },
                {
                    field: "mrName",
                    title: "会议室名称",
                    width: 180,
                    filterable: false
                },
                {
                    field: "mrType",
                    title: "会议室类型",
                    width: 180,
                    filterable: false
                },
                {
                    field: "addr",
                    title: "会议室地点",
                    width: 200,
                    filterable: false
                },
                {
                    field: "capacity",
                    title: "会议室容量",
                    width: 100,
                    filterable: false
                },
                {
                    field: "userName",
                    title: "会议室负责人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "userPhone",
                    title: "负责人电话",
                    width: 150,
                    filterable: false
                },
                {
                    field : "",
                    title : "状态",
                    width : 100,
                    template: function (item) {
                       if(item.mrStatus == "2"){
                            return "停用";
                       }else{
                           return "正常";
                       }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 180,
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


    }
})();