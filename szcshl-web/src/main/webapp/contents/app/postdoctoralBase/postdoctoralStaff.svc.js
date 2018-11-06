(function(){
    'use strict';
    angular.module('app').factory('postdoctoralStaffSvc', postdoctoralStaffSvc);
    postdoctoralStaffSvc.$inject = ['$http', '$state' , 'bsWin' ];
    function postdoctoralStaffSvc($http , $state , bsWin ){
        var service = {
            postdoctoralStaffGrid : postdoctoralStaffGrid ,
            postdoctoralPopStaffGrid : postdoctoralPopStaffGrid ,
            createPostdoctoralStaff : createPostdoctoralStaff , //保存
            findPostdoctoralStaffById : findPostdoctoralStaffById , //通过ID获取信息
            updatePostdoctoralStaff : updatePostdoctoralStaff , //更新信息
            deletePostdoctoralStaff : deletePostdoctoralStaff  , //删除信息
            approvePostdoctoralStaff : approvePostdoctoralStaff,
            backPostdoctoralStaff : backPostdoctoralStaff

        }

        return service;

        function deletePostdoctoralStaff(id , callBack){
            var httpOptions = {
                method : "delete",
                url : rootPath + "/postdoctoralStaff/deletePostdoctoralStaff",
                params : {"id" : id}
            }

            var httpSuccess = function success(response){
                if(callBack != undefined && typeof  callBack == 'function'){
                    callBack(response.data);
                }
            }

            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }

        function updatePostdoctoralStaff(vm , callBack){
            var httpOptions = {
                method : "post",
                url : rootPath + "/postdoctoralStaff/updatePostdoctoralStaff",
                data : vm.postdoctoralStaff
            }

            var httpSuccess = function success(response){
                if(callBack != undefined && typeof  callBack == 'function'){
                    callBack(response.data);
                }
            }

            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }

        function findPostdoctoralStaffById(id , callBack){
            var httpOptions = {
                method : "post",
                url : rootPath + "/postdoctoralStaff/findById",
                params : {"id" : id}
            }

            var httpSuccess = function success(response){
                if(callBack != undefined && typeof callBack =="function"){
                    callBack(response.data);
                }
            }

            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }

        function createPostdoctoralStaff(vm , callBack){
            var httpOptions = {
                method : "post",
                url : rootPath + "/postdoctoralStaff/createPostdoctoralStaff",
                data : vm.postdoctoralStaff
            }

            var httpSuccess = function success(response){
                if(callBack != undefined && typeof  callBack == 'function'){
                    callBack(response.data);
                }
            }

            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }

        function approvePostdoctoralStaff(id ,status, callBack){
            var httpOptions = {
                method : "post",
                url : rootPath + "/postdoctoralStaff/approvePostdoctoralStaff",
                params : {"id" : id,"status":status}
            }

            var httpSuccess = function success(response){
                if(callBack != undefined && typeof  callBack == 'function'){
                    callBack(response.data);
                }
            }

            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }

        function backPostdoctoralStaff(id ,status, callBack){
            var httpOptions = {
                method : "post",
                url : rootPath + "/postdoctoralStaff/backPostdoctoralStaff",
                params : {"id" : id,"status":status}
            }

            var httpSuccess = function success(response){
                if(callBack != undefined && typeof  callBack == 'function'){
                    callBack(response.data);
                }
            }

            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }

        function postdoctoralStaffGrid(vm){
            // Begin:dataSource

            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/postdoctoralStaff/findByOData", $("#postdoctoralStaffForm"),{$filter:"status ne '4'"},true),
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
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />", item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox' />"
                },
                {
                    field: "rowNumber",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "name",
                    title: "姓名",
                    width: 100,
                    filterable: false,
                    template: function (item) {
                        return common.format($('#columnNames').html(),item.id ,item.name);
                       // return '<a  href="#/postdoctoralStaffDetail/' + item.id + '">'+item.name+'</a>'
                    }
                },
                {
                    field: "sex",
                    title: "性别",
                    width: 50,
                    filterable: false,
                },
                {
                    field: "enterBaseDate",
                    title: "进入基地时间",
                    width: 160,
                    filterable: false,
                    format: "{0:yyyy-MM-dd}"
                },
                {
                    field: "enterStackApproveDate",
                    title: "进站批准日期",
                    width: 160,
                    filterable: false,
                    format: "{0:yyyy-MM-dd}"
                },
                {
                    field: "",
                    title: "操作",
                    width: 120,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),   item.id , "vm.approvePostdoctoralStaff('" + item.id + "','"+item.status+"')","vm.deletePostdoctoralStaff('" + item.id + "')",item.name,item.status,"vm.backPostdoctoralStaff('" + item.id + "','"+item.status+"')",item.createdBy);
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
        }

        function postdoctoralPopStaffGrid(vm){
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/postdoctoralStaff/findByOData", $("#postdoctoralPopStaffForm"), {$filter: "status eq '4'"},true),
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
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />", item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox' />"
                },
                {
                    field: "rowNumber",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "name",
                    title: "姓名",
                    width: 100,
                    filterable: false,
                    template: function (item) {
                        return common.format($('#columnNames').html(),item.id ,item.name);
                        // return '<a  href="#/postdoctoralStaffDetail/' + item.id + '">'+item.name+'</a>'
                    }
                },
                {
                    field: "sex",
                    title: "性别",
                    width: 50,
                    filterable: false,
                },
                {
                    field: "enterStackApproveDate",
                    title: "进站时间",
                    width: 160,
                    filterable: false,
                    format: "{0:yyyy-MM-dd}"
                },
                {
                    field: "pooStackDate",
                    title: "出站时间",
                    width: 160,
                    filterable: false,
                    format: "{0:yyyy-MM-dd}"
                },
                {
                    field: "",
                    title: "操作",
                    width: 120,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),   item.id,"vm.deletePostdoctoralStaff('" + item.id + "')",item.name,item.status,"vm.backPostdoctoralStaff('" + item.id + "','"+item.status+"')");
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
        }

    }
})();