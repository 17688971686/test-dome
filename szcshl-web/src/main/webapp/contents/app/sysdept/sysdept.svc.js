(function () {
    'use strict';

    angular.module('app').factory('sysdeptSvc', sysdept);

    sysdept.$inject = ['$http'];

    function sysdept($http) {
        var service = {
            listGrid : listGrid,			        //初始化表格
            findById : findById,                    //根据Idc初始化表格

        };
        return service;

        //S_初始化表格
        function listGrid(gridOption) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/sysdept/fingByOData", $("#sysDeptForm")),
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
            var  dataBound=function () {
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
                    filterable : false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "name",
                    title: "小组名称",
                    width: 160,
                    filterable: false
                },
                {
                    field : "",
                    title : "操作",
                    width : 200,
                    template:function(item){
                        return common.format($('#columnBtns').html(),"vm.del('"+item.id+"')",item.id);
                    }
                }

            ];

            gridOption = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound:dataBound,
                resizable: true
            };
        }//E_listGrid

        //S_根据ID初始化表格
        function findById(id,callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath+"/sysdept/getSysDeptById",
                params : {
                    id:id
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http:$http,
                httpOptions:httpOptions,
                success:httpSuccess
            });
        }


    }//E_sysConfig

})();