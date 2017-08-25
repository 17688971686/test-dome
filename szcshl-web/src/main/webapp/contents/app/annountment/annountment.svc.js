(function () {
    'use strict';

    angular.module('app').factory('annountmentSvc', annountment);

    annountment.$inject = ['$http', 'bsWin','sysfileSvc'];

    function annountment($http, bsWin,sysfileSvc) {

        var url_annountment = rootPath + "/annountment";
        var url_back = "#/annountment";
        var service = {
            grid: grid,		                            //初始化列表
            createAnnountment: createAnnountment,	    //新增通知公告
            initAnOrg: initAnOrg,		                //初始化发布单位
            findAnnountmentById: findAnnountmentById,	//获取通知公告信息
            updateIssueState: updateIssueState,         //更改通知公告的发布状态
            updateAnnountment: updateAnnountment,	    //更新通知公告
            deleteAnnountment: deleteAnnountment,	    //删除通知公告
            findDetailById: findDetailById,	            //通过id获取通过公告
            postArticle: postArticle,	                //访问上一篇文章
            nextArticle: nextArticle,	                //访问下一篇文章
        };

        return service;

        //begin initAnOrg
        function initAnOrg(vm) {
            var httpOptions = {
                method: "get",
                url: url_annountment + "/initAnOrg"
            }

            var httpSuccess = function success(response) {
                vm.annountment.anOrg = "";
                vm.annountment.anOrg = response.data.substring(1, response.data.length - 1);
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }

        //end initAnOrg

        //begin findAnnountmentById
        function findAnnountmentById(vm) {
            var httpOptions = {
                method: "post",
                url: url_annountment + "/findAnnountmentById",
                params: {
                    anId: vm.annountment.anId
                }
            }

            var httpSuccess = function success(response) {
                vm.annountment = response.data;
                $("#froalaEditor").froalaEditor('html.set', vm.annountment.anContent);
                vm.initFileUpload();
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//end findAnnountmentById

        //begin createAnnountment
        function createAnnountment(vm) {
        	vm.annountment.anContent=$("#froalaEditor").val();
            common.initJqValidation();
            var isValid = $('#form').valid();
            if (isValid) {
                vm.isSubmit = true;
                var httpOptions = {
                    method: "post",
                    url: url_annountment,
                    data: vm.annountment
                }
                var httpSuccess = function success(response) {
                    vm.isSubmit = false;
                    vm.annountment.anId = response.data.anId;
                    bsWin.alert("保存成功！");
                }
                common.http({
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess,
                    onError:function(){
                        vm.isSubmit = false;
                    }
                });
            }
        }//end createAnnountment

        //begin updateAnnountment
        function updateAnnountment(vm) {
            vm.isSubmit = true;
        	vm.annountment.anContent=$("#froalaEditor").val();
            var httpOptions = {
                method: "put",
                url: url_annountment,
                data: vm.annountment
            }

            var httpSuccess = function success(response) {
                vm.isSubmit = false;
                bsWin.alert("操作成功！");
            }

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError:function(){
                    vm.isSubmit = false;
                }
            });

        }//end updateAnnountment


        //begin deleteAnnountment
        function deleteAnnountment(vm,anId) {
            var httpOptions = {
                method: "delete",
                url: url_annountment,
                data: anId
            }
            var httpSuccess = function success(response) {
                vm.gridOptions.dataSource.read();
                bsWin.alert("删除成功！");
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }

        //end deleteAnnountment

        // begin#grid
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/annountment/fingByCurUser",$("#annountmentform")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        },
                        modifiedDate: {
                            type: "date"
                        }

                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: [
                {
                    field: "issue",
                    dir: "asc"
                }
                ]
                	

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
            };
            //S_序号

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.anId)
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
                    field: "anTitle",
                    title: "标题",
                    width: 300,
                    filterable: false
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
                    title: "发布状态",
                    width: 100,
                    template: function (item) {
                        if (item.issue && item.issue == '9') {
                            return "已发布";
                        } else {
                            return "未发布";
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 150,
                    template: function (item) {
                    	
                        return common.format($('#columnBtns').html(),
                            "vm.detail('" + item.anId + "')", item.anId, "vm.del('" + item.anId + "')");
                    	
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                dataBound: dataBound,
                columns: columns,
                resizable: true
            };
        }// end fun grid

        //S_updateIssueState
        function updateIssueState(vm, state) {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: "请选择数据"
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/annountment/updateIssueState",
                    params: {
                        ids: ids.join(','),
                        issueState: state
                    }
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            vm.isSubmit = false;
                            vm.gridOptions.dataSource.read();
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                closeDialog: true
                            })
                        }
                    })
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }

        }//E_updateIssueState

        function findDetailById(vm,id) {
            var httpOptions = {
                method: "post",
                url: url_annountment + "/findAnnountmentById",
                params: {
                    anId: id
                }
            }
            var httpSuccess = function success(response) {
                vm.annountment = response.data;
                sysfileSvc.findByBusinessId(id,function(data){
                    vm.sysFilelists = data;
                });
                postArticle(vm, id);
                nextArticle(vm, id);
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//end findAnnountmentById

        //begin postArticle
        function postArticle(vm, id) {
            var httpOptions = {
                method: "get",
                url: rootPath + "/annountment/postArticle",
                params: {
                    anId: id
                }
            }

            var httpSuccess = function success(response) {
                vm.annountmentPost = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//end postArticle


        //begin nextArticle
        function nextArticle(vm, id) {
            var httpOptions = {
                method: "get",
                url: rootPath + "/annountment/nextArticle",
                params: {
                    anId: id
                }
            }
            var httpSuccess = function success(response) {
                vm.annountmentNext = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//end nextArticle


    }
})();