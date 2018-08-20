(function(){
    'ust strict';
    angular.module('app').factory('policySvc' , policy);
    policy.$inject = ['$http'];
    function policy($http){
        var service = {
            initFileFolder : initFileFolder , //初始化政策指标库所有的文件夹
            createPolicy : createPolicy, //创建政策指标库
            findFileByIdGrid : findFileByIdGrid , // 通过id查询文件
        }

        return service;

        //begin initFileFolder
        function initFileFolder($scope,callBack){

            var httpOptions = {
                method: 'post',
                url: rootPath + "/policy/initFileFolder",
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
        //end initFileFolder


        //S_创建政策指标库
        function createPolicy(topicModel,callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/policy",
                data : topicModel
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
        //end createPolicy

        //begin findFileByIdGrid
        function findFileByIdGrid( vm) {

            // begin#grid
            function grid(vm) {
                // Begin:dataSource
                var dataSource = new kendo.data.DataSource({
                    type: 'odata',
                    transport: common.kendoGridConfig().transport(rootPath + "//policy/findFileById", $("#usersform"),{filter: "id eq '" + vm.standardId + "'"}),
                    schema: common.kendoGridConfig().schema({
                        id: "id"
                    }),
                    serverPaging: false,
                    serverSorting: false,
                    serverFiltering: false,
                    pageSize: 10
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
                    }
                    ,
                    {
                        field: "standardNo",
                        title: "标准号",
                        width: 100,
                        filterable: false
                    },
                    {
                        field: "standardName",
                        title: "标准名称",
                        width: 100,
                        filterable: false
                    },
                    {
                        field: "publishDate",
                        title: "发布日期",
                        width: 100,
                        filterable: false
                    },
                    {
                        field: "implementDate",
                        title: "实施日期",
                        width: 160,
                        filterable: false
                    },
                    {
                        field: "state",
                        title: "状态",
                        width: 160,
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
        //end findFileByIdGrid

    }
})();