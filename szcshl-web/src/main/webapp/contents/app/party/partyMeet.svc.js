(function(){
    'use strict';
    angular.module('app').factory('partyMeetSvc', partyMeet);
    partyMeet.$inject = ['$http', '$state' , 'bsWin' ];

    function partyMeet($http , $state , bsWin ){
        var service = {
            partyMeetGrid : partyMeetGrid ,
            createPartyMeet : createPartyMeet , //保存党务会议
            findMeetById : findMeetById , //通过ID获取会议信息
            updatePartyMeet : updatePartyMeet , //更新会议信息
            deletePartyMeet : deletePartyMeet  , //删除会议信息

        }

        return service;

        function deletePartyMeet(id , callBack){
            var httpOptions = {
                method : "delete",
                url : rootPath + "/partyMeet/deletePartyMeet",
                params : {"mId" : id}
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

        function updatePartyMeet(vm , callBack){
            var httpOptions = {
                method : "put",
                url : rootPath + "/partyMeet/updatePartyMeet",
                data : vm.partyMeet
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

        function findMeetById(id , callBack){
            var httpOptions = {
                method : "post",
                url : rootPath + "/partyMeet/findMeetById",
                params : {"mId" : id}
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

        function createPartyMeet(vm , callBack){
            var httpOptions = {
                method : "post",
                url : rootPath + "/partyMeet/createPartyMeet",
                data : vm.partyMeet
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

        function partyMeetGrid(vm){
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/partyMeet/findByDataList" ),
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
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />", item.mId)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox' />"
                },
                {
                    field: "rowNumber",
                    title: "序号",
                    width: 20,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "",
                    title: "会议标题",
                    width: 100,
                    filterable: false,
                    template: function (item) {
                        return '<a  href="#/partyMeetDetail/' + item.mId + '">'+item.mTitle+'</a>'
                    }
                },
                {
                    field: "mDate",
                    title: "会议时间",
                    width: 50,
                    filterable: false,
                },
                {
                    field: "mAddress",
                    title: "会议地点",
                    width: 160,
                    filterable: false,
                },
                {
                    field: "",
                    title: "操作",
                    width: 120,
                    template: function (item) {
                        var canExecute = false;
                        if(item.curState == 9){
                            canExecute = true;
                        }
                        return common.format($('#columnBtns').html(),   item.mId , "vm.deletePartyMeet('" + item.mId + "')");
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