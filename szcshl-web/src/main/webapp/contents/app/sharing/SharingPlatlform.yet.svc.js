(function () {
    'use strict';
    angular.module('app').factory('sharingPlatlformYetSvc', sharingPlatlformYet);
    sharingPlatlformYet.$inject = ['$http'];

    function sharingPlatlformYet($http) {
        var service = {
            grid: grid,
        };

        return service;

        // begin#grid
        function grid(vm) {
            // Begin:dataSource
            var dataSource = common.kendoGridDataSource(rootPath + "/sharingPlatlform/findByReception",$("#formSharingPub"),vm.queryParams.page,vm.queryParams.pageSize,vm.gridParams);
            // End:dataSource

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.sharId)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "unitSort",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                
                {
                    field: "theme",
                    title: "共享主题",
                    width: 180,
                    filterable: true
                },
                {
                    field: "publishUsername",
                    title: "发布人",
                    width: 100,
                    filterable: true
                },
                {
                    field: "publishDate",
                    title: "发布时间",
                    width: 100,
                    filterable: true
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),item.sharId);
                    	
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                pageable : common.kendoGridConfig(vm.queryParams).pageable,
                dataBound:common.kendoGridConfig(vm.queryParams).dataBound,
                resizable: true
            };

        }// end fun grid

    }
})();