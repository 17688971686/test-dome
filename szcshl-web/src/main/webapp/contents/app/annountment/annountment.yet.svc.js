(function () {
    'use strict';

    angular.module('app').factory('annountmentYetSvc', annountmentYet);

    annountmentYet.$inject = ['$http'];

    function annountmentYet($http) {
    	
    	var service={
    		grid : grid
    	};
    	
    	return service;
    	
    	 // begin#grid
        function grid(vm) {
            // Begin:dataSource
            var dataSource = common.kendoGridDataSource(rootPath + "/annountment/findByIssue",$("#annountmentYetform"),vm.queryParams.page,vm.queryParams.pageSize,vm.gridParams);
            // End:dataSource

            // Begin:column
            var columns = [
                /*{
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.anId)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },*/
                {
                    field: "unitSort",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "",
                    title: "标题",
                    width: 300,
                    filterable: false,
                    template:function(item){
                        if(item.isStick == 9){
                            return '<span class="label label-primary">置顶</span>'+item.anTitle;
                        }else{
                            return item.anTitle;
                        }
                    }
                },
                {
                    field: "issueDate",
                    title: "发布时间",
                    format: "{0:yyyy-MM-dd hh24:mm:ss}",
                    width: 160,
                    filterable: false
                },
                {
                    field: "issueUser",
                    title: "发布人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 150,
                    template: function (item) {
                    	
                        return common.format($('#columnBtns').html(),"vm.detail('" + item.anId + "')");
                    	
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                pageable : common.kendoGridConfig(vm.queryParams).pageable,
                dataBound:common.kendoGridConfig(vm.queryParams).dataBound,
                columns: columns,
                resizable: true
            };

        }// end fun grid
    	
    	
    }
})();
