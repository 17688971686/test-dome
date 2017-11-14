(function () {
    'use strict';

    angular.module('app').factory('roomCountSvc', roomCount);

    roomCount.$inject = ['$http', '$compile'];
    function roomCount($http, $compile) {
        var url_roomCount = rootPath + "/roomCount";
        var url_room = rootPath + "/room";
        var url_user = rootPath + "/user";
        var url_back = '#/roomCount';
        var url_user = rootPath + '/user';

        var service = {
            grid: grid,
            queryRoomCount: queryRoomCount,//查询
            roomList: roomList,
            cleanValue: cleanValue,
            findAllUsers: findAllUsers
        };
        return service;

        function cleanValue() {
            var tab = $("#roomCountform").find('input,select');
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }

        function queryRoomCount(vm) {
            vm.gridOptions.dataSource.read();
        }

        //S_查询预定人
        function findAllUsers(vm) {
            var httpOptions = {
                method: 'get',
                url: common.format(url_user + "/findAllUsers")
            }
            var httpSuccess = function success(response) {
                vm.userlist = {};
                vm.userlist = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //E_查询预定人

        //S_查询所有会议室名称
        function roomList(vm) {
            var httpOptions = {
                method: 'get',
                url: common.format(url_room + "/meeting")
            }
            var httpSuccess = function success(response) {
                vm.roomlists = {};
                vm.roomlists = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }

        //E_查询所有会议室名称

        //S_giid
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_room + "/fingByOData", $("#roomCountform")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    },

                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                },

            });

            // End:dataSource
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
            // Begin:column
            var columns = [
                {
                    field: "rowNumber",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                }
                ,
                {
                    field: "rbName",
                    title: "会议名称",
                    width: "30%",
                    filterable: false
                },
                {
                    field: "addressName",
                    title: "会议地点",
                    width: "20%",
                    filterable: false
                },
                {
                    field: "rbDay",
                    title: "会议日期",
                    width: "10%",
                    filterable: false
                },
                {
                    field: "beginTimeStr",
                    title: "会议开始时间",
                    width: "10%",
                    filterable: false,
                },
                {
                    field: "endTimeStr",
                    title: "会议结束时间",
                    type: "date",
                    width: "10%",
                    filterable: false,
                },
                {
                    field: "dueToPeople",
                    title: "会议预定人",
                    width: "10%",
                    filterable: false
                },
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