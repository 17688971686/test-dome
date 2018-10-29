(function(){
    'use strict';
    angular.module('app').factory('postdoctoralBaseSvc', postdoctoralBaseSvc);
    postdoctoralBaseSvc.$inject = ['$http', '$state' , 'bsWin' ];
    function postdoctoralBaseSvc($http , $state , bsWin ){
        var service = {
            postdoctoralBaseGrid : postdoctoralBaseGrid ,
            createPostdoctoralBase : createPostdoctoralBase , //保存
            findPostdoctoralBaseById : findPostdoctoralBaseById , //通过ID获取信息
            updatePostdoctoralBase : updatePostdoctoralBase , //更新信息
            deletePostdoctoralBase : deletePostdoctoralBase  , //删除信息

        }

        return service;

        function deletePostdoctoralBase(id , callBack){
            var httpOptions = {
                method : "delete",
                url : rootPath + "/postdoctoralBase/deletePostdoctoralBase",
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

        function updatePostdoctoralBase(vm , callBack){
            var httpOptions = {
                method : "post",
                url : rootPath + "/postdoctoralBase/updatePostdoctoralBase",
                data : vm.postdoctoralBase
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

        function findPostdoctoralBaseById(id , callBack){
            var httpOptions = {
                method : "post",
                url : rootPath + "/postdoctoralBase/findById",
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

        function createPostdoctoralBase(vm , callBack){
            var httpOptions = {
                method : "post",
                url : rootPath + "/postdoctoralBase/createPostdoctoralBase",
                data : vm.postdoctoralBase
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

        function postdoctoralBaseGrid(vm){
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/postdoctoralBase/findByOData" ),
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
                    width: 40,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "baseName",
                    title: "基地名称",
                    width: 100,
                    filterable: false,
                    template: function (item) {
                        return '<a  href="#/postdoctoralBaseDetail/' + item.id + '">'+item.baseName+'</a>'
                    }
                },
                {
                    field: "foundingTime",
                    title: "成立时间",
                    width: 160,
                    filterable: false,
                },
                {
                    field: "principalBase",
                    title: "基地负责人",
                    width: 160,
                    filterable: false,
                },
                {
                    field: "dailyMananger",
                    title: "日常管理人员",
                    width: 160,
                    filterable: false,
                },
                {
                    field: "",
                    title: "操作",
                    width: 120,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),   item.id , "vm.deletePostdoctoralBase('" + item.id + "')");
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