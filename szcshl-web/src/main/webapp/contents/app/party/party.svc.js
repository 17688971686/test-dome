(function () {
    'use strict';

    angular.module('app').factory('partySvc', party);

    party.$inject = ['$http', '$state' , 'bsWin'];

    function party($http, $state , bsWin) {

        var service = {
            createParty : createParty,  //保存党员信息
            partyGrid : partyGrid, //查询党员列表
            findById : findById , //通过id查询
            
        }
        
        return service;

        //begin createParty
        function createParty(vm , cellBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + '/partyManager/createParty',
                data : vm.party
            }

            var httpSuccess = function success(response){
                if(cellBack != undefined && typeof  cellBack == 'function' ){
                    cellBack(response.data);
                }
            }

            common.http({
                $http : $http ,
                httpOptions : httpOptions,
                success : httpSuccess
            });
        }
        //end createParty

        //begin findById
        function findById(pmId , cellback){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/partyManager/findById",
                params : {pmId : pmId}
            }

            var httpSuccess = function(response){
                if(cellback != undefined && typeof  cellback == 'function'){
                    cellback(response.data);
                }
            }
            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });

        }
        //end findById
        //begin partyGrid
        function partyGrid(vm){
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/partyManager/findByOData" , $("#partyform")),
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
                    field: "",
                    title: "名称",
                    width: 100,
                    filterable: false,
                    template: function (item) {
                        return '<a  ng-click="vm.partyDetail(\''+item.pmId+'\')">'+item.pmName+'</a>'
                    }
                },
                {
                    field: "pmSex",
                    title: "性别",
                    width: 50,
                    filterable: false,
                },
                {
                    field: "pmIDCard",
                    title: "身份证号",
                    width: 200,
                    filterable: false,
                },
                {
                    field: "pmNation",
                    title: "民族",
                    width: 50,
                    filterable: false,
                },
                {
                    field: "pmEducation",
                    title: "学历",
                    width: 100,
                    filterable: false
                },
                {
                    field: "pmPhone",
                    title: "手机号",
                    width: 100,
                    filterable: false
                },
                {
                    field: "pmTel",
                    title: "固定电话",
                    width: 80,
                    filterable: false
                },
                {
                    field: "",
                    title: "人员类别",
                    width: 80,
                    filterable: false,
                    template : function(item){
                        if(item.pmCategory == '1'){
                            return "正式党员";
                        }else if(item.pmCategory == '2'){
                            return "预备党员";
                        }else{
                            return "";
                        }
                    }
                },
                {
                    field: "pmJoinPartyDate",
                    title: "入党日期",
                    width: 80,
                    filterable: false
                },
                {
                    field: "pmTurnToPatryDate",
                    title: "转正日期",
                    width: 80,
                    filterable: false
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
                        return common.format($('#columnBtns').html(),  "vm.partyDetail('" + item.pmId + "')");
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
        //end partyGrid

    }
})();