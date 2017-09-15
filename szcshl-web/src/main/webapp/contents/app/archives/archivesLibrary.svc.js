(function () {
    'use strict';

    angular.module('app').factory('archivesLibrarySvc', archivesLibrary);

    archivesLibrary.$inject = ['$http','bsWin'];

    function archivesLibrary($http,bsWin) {
        var url_archivesLibrary = rootPath + "/archivesLibrary", url_back = '#/archivesLibraryList';
        var service = {
            grid: grid,									//项目借阅审批列表
            createArchivesLibrary: createArchivesLibrary,//保存中心档案借阅
            createCityLibrary:createCityLibrary, 		//保存市档案借阅
            updateArchivesLibrary: updateArchivesLibrary,//部长审批借阅档案
            centerLibraryGrid:centerLibraryGrid,		//中心档案查询列表
            cityGridOptions:cityGridOptions,     		//市档案查询列表
            getArchivesLibraryById: getArchivesLibraryById,//根据ID查找
            getArchivesUserName:getArchivesUserName,	//获取归档员
            deleteArchivesLibrary: deleteArchivesLibrary,
        };

        return service;

       //S 获取归档员
        function getArchivesUserName(vm){
        	var httpOptions = {
                    method: 'get',
                    url: rootPath + "/archivesLibrary/findByArchivesUser",
                };
                var httpSuccess = function success(response) {
                    vm.userlist = response.data;
                    console.log(vm.userlist);
                };

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });    
        }
      // E 获取归档员
       // begin#保存市档案借阅
        function createCityLibrary(model,callBack){
        	 var httpOptions = {
                     method: 'post',
                     url: url_archivesLibrary+"/saveCity",
                     data: model
                 };
                 var httpSuccess = function success(response) {
                 	 if (callBack != undefined && typeof callBack == 'function') {
                          callBack(response.data);
                      }
                 };
                 common.http({
                     $http: $http,
                     httpOptions: httpOptions,
                     success: httpSuccess
                 });
        }
        // begin#部长审批借阅档案
        function updateArchivesLibrary(vm) {
        	vm.userlist.forEach(function(su,index){
                     vm.model.archivesUserName = su.displayName;
                
             })
                var httpOptions = {
                    method: 'put',
                    url: url_archivesLibrary+"/updateLibrary",
                    data: vm.model
                }
                var httpSuccess = function success(response) {
        		   bsWin.success("操作成功！")
                }
                common.http({
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
        }

        // begin#deleteArchivesLibrary
        function deleteArchivesLibrary(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_archivesLibrary,
                data: id
            };

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                    	common.alert({
                            vm: vm,
                            msg: "操作成功",
                            closeDialog :true,
                            fn: function () {
                            	vm.isSubmit = false;
                                vm.gridOptions.dataSource.read();
                            }
                        })
                    }
                });
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#保存中心档案借阅
        function createArchivesLibrary(model,callBack) {
                var httpOptions = {
                    method: 'post',
                    url: url_archivesLibrary+"/savaLibrary",
                    data: model
                };
                var httpSuccess = function success(response) {
                	 if (callBack != undefined && typeof callBack == 'function') {
                         callBack(response.data);
                     }
                };
                common.http({
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
        }

        // begin#getArchivesLibraryById
        function getArchivesLibraryById(vm) {
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/archivesLibrary/html/findById",
                params:{id:vm.id}
            };
            var httpSuccess = function success(response) {
                vm.model = response.data;
            };

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
                transport: common.kendoGridConfig().transport(url_archivesLibrary+"/findByProjectList"),
                schema: {
                    data: "value",
                    total: function (data) {
                        if (data['count']) {
                            $("#LIBRARY_COUNT").html(data['count']);
                        } else {
                            $("#LIBRARY_COUNT").html(0);
                        }
                        return data['count'];
                    },
                    model: {
                        id: "id",
                        fields: {
                            createdDate: {
                                type: "date"
                            },
                            modifiedDate: {
                                type: "date"
                            }
                        }
                    }
                },
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

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>",
                },
                
                {
                    field: "readCompany",
                    title: "项目单位",
                    width: 140,
                    filterable: false
                },
               
                {
                    field: "readProjectName",
                    title: "查阅项目",
                    width: 140,
                    filterable: false
                },
                {
                    field: "readUsername",
                    title: "借阅人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "readArchivesCode",
                    title: "归档编号",
                    width: 100,
                    filterable: false
                },
                {
                    field: "readDate",
                    title: "借阅时间",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "",
                    title: "操作",
                    width: 100,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                             item.id);
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
                dataBound: function () {
                    var rows = this.items();
                    var page = this.pager.page() - 1;
                    var pagesize = this.pager.pageSize();
                    $(rows).each(function () {
                        var index = $(this).index() + 1 + page * pagesize;
                        var rowLabel = $(this).find(".row-number");
                        $(rowLabel).html(index);
                    });
                },
                resizable: true
            };

        }// end fun grid

        //S 中心档案查询列表
        function centerLibraryGrid(vm){
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_archivesLibrary+"/findByCenterList"),
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

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>",
                },
                
                {
                    field: "readCompany",
                    title: "项目单位",
                    width: 140,
                    filterable: false
                },
               
                {
                    field: "readProjectName",
                    title: "查阅项目",
                    width: 140,
                    filterable: false
                },
                {
                    field: "readUsername",
                    title: "借阅人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "readArchivesCode",
                    title: "归档编号",
                    width: 100,
                    filterable: false
                },
                {
                    field: "archivesUserName",
                    title: "归档员",
                    width: 100,
                    filterable: false
                },
                
                {
                    field: "readDate",
                    title: "借阅时间",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "",
                    title: "操作",
                    width: 100,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                             item.id);
                    }
                }
            ];
            // End:column

            vm.centerGridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound: function () {
                    var rows = this.items();
                    var page = this.pager.page() - 1;
                    var pagesize = this.pager.pageSize();
                    $(rows).each(function () {
                        var index = $(this).index() + 1 + page * pagesize;
                        var rowLabel = $(this).find(".row-number");
                        $(rowLabel).html(index);
                    });
                },
                resizable: true
            };
        }
       //E 中心档案查询列表
        
       //S 市档案查询列表
       function  cityGridOptions(vm){
           // Begin:dataSource
           var dataSource = new kendo.data.DataSource({
               type: 'odata',
               transport: common.kendoGridConfig().transport(url_archivesLibrary+"/findByCityList"),
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

           // Begin:column
           var columns = [
               {
                   template: function (item) {
                       return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                           item.id)
                   },
                   filterable: false,
                   width: 40,
                   title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
               },
               {
                   field: "",
                   title: "序号",
                   width: 50,
                   filterable: false,
                   template: "<span class='row-number'></span>",
               },
               
               {
                   field: "readCompany",
                   title: "项目单位",
                   width: 140,
                   filterable: false
               },
              
               {
                   field: "readProjectName",
                   title: "查阅项目",
                   width: 140,
                   filterable: false
               },
               {
                   field: "readUsername",
                   title: "借阅人",
                   width: 100,
                   filterable: false
               },
               {
                   field: "readArchivesCode",
                   title: "归档编号",
                   width: 100,
                   filterable: false
               },
               {
                   field: "archivesUserName",
                   title: "归档员",
                   width: 100,
                   filterable: false
               },
               
               {
                   field: "readDate",
                   title: "借阅时间",
                   width: 100,
                   filterable: false,
               },
               {
                   field: "",
                   title: "操作",
                   width: 100,
                   template: function (item) {
                       return common.format($('#columnBtns').html(),
                            item.id);
                   }
               }
           ];
           // End:column

           vm.cityGridOptions = {
               dataSource: common.gridDataSource(dataSource),
               filterable: common.kendoGridConfig().filterable,
               pageable: common.kendoGridConfig().pageable,
               noRecords: common.kendoGridConfig().noRecordMessage,
               columns: columns,
               dataBound: function () {
                   var rows = this.items();
                   var page = this.pager.page() - 1;
                   var pagesize = this.pager.pageSize();
                   $(rows).each(function () {
                       var index = $(this).index() + 1 + page * pagesize;
                       var rowLabel = $(this).find(".row-number");
                       $(rowLabel).html(index);
                   });
               },
               resizable: true
           };
       }
     //E 市档案查询列表
    }
})();