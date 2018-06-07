(function () {
    'use strict';

    angular.module('app').factory('annountmentSvc', annountment);

    annountment.$inject = ['$http', 'bsWin', 'sysfileSvc'];

    function annountment($http, bsWin, sysfileSvc) {

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
            startFlow: startFlow,                           //启动流程
            initFlowDeal: initFlowDeal                      //初始化流程数据
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
        function findAnnountmentById(vm, callBack) {
            var httpOptions = {
                method: "post",
                url: url_annountment + "/findAnnountmentById",
                params: {
                    anId: vm.annountment.anId
                }
            }

            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//end findAnnountmentById

        //begin createAnnountment
        function createAnnountment(vm, callBack) {
            vm.annountment.anContent = vm.editor.getContent();
            ;
            common.initJqValidation();
            var isValid = $('#form').valid();
            if (isValid) {
                vm.isSubmit = true;
                var httpOptions = {
                    method: "post",
                    url: rootPath + "/annountment",
                    data: vm.annountment
                }
                var httpSuccess = function success(response) {
                    vm.isSubmit = false;
                    if (callBack != undefined && typeof callBack == 'function') {
                        callBack(response.data);
                    }
                }
                common.http({
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess,
                    onError: function () {
                        vm.isSubmit = false;
                    }
                });
            }
        }//end createAnnountment

        //begin updateAnnountment
        function updateAnnountment(vm, callBack) {
            vm.isSubmit = true;
            vm.annountment.anContent = vm.editor.getContent();
            var httpOptions = {
                method: "put",
                url: url_annountment,
                data: vm.annountment
            }

            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    vm.isSubmit = false;
                    callBack(response.data);
                }
            }

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function () {
                    vm.isSubmit = false;
                }
            });

        }//end updateAnnountment


        //begin deleteAnnountment
        function deleteAnnountment(anId, callBack) {
            var httpOptions = {
                method: "delete",
                url: url_annountment,
                params: {
                    anId: anId
                }
            }
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

        //end deleteAnnountment

        // begin#grid
        function grid(vm) {
            // Begin:dataSource
            var dataSource = common.kendoGridDataSource(rootPath + "/annountment/fingByCurUser", $("#annountmentform"), vm.queryParams.page, vm.queryParams.pageSize, vm.gridParams);
            // End:dataSource
            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />", item.anId)
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
                    field: "",
                    title: "标题",
                    width: 300,
                    filterable: false,
                    template: function (item) {
                        //如果是审批流程，则可以查看审批流程的详情信息
                        if (item.processInstanceId) {
                            return '<a href="#/flowDetail/' + item.anId + '/ANNOUNT_MENT_FLOW//' + item.processInstanceId+'" >' + item.anTitle + '</a>';
                        } else {
                            return item.anTitle;
                        }
                    }
                },
                {
                    field: "",
                    title: "是否置顶",
                    width: 100,
                    filterable: false,
                    template: function (item) {
                        if (item.isStick && item.isStick == 9) {
                            return "是";
                        } else {
                            return "否";
                        }
                    }
                },
                {
                    field: "",
                    title: "是否审批",
                    width: 100,
                    filterable: false,
                    template: function (item) {
                        if (item.processInstanceId) {
                            return "是";
                        } else {
                            return "否";
                        }
                    }
                },
                {
                    field: "",
                    title: "审批结果",
                    width: 100,
                    filterable: false,
                    template: function (item) {
                        if (item.appoveStatus) {
                            if (item.appoveStatus != '9') {
                                return "审批中";
                            } else if (item.appoveStatus == '9' && item.issue == '9') {
                                return "<span style='color: green'>审批通过</span>";
                            } else if (item.appoveStatus == '9' && item.issue != '9') {
                                return "<span style='color: red'>审批不通过</span>";
                            }
                        } else {
                            return "";
                        }
                    }
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
                    field: "issueDate",
                    title: "发布时间",
                    format: "{0:yyyy-MM-dd hh24:mm:ss}",
                    width: 160,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 150,
                    template: function (item) {
                        var isCanDel = true;
                        var isCanEdit = true;
                        //已发布或者走流程的不能删除
                        if (item.issue == '9' || item.processInstanceId) {
                            if (item.issue == '0' && item.appoveStatus == '9') {
                                isCanDel = true;
                            } else {
                                isCanDel = false;
                            }

                        }
                        if (item.processInstanceId) {
                            if (item.issue == '0' && item.appoveStatus == '9') {
                                isCanEdit = true;
                            } else {
                                isCanEdit = false;
                            }

                        }
                        return common.format(
                            $('#columnBtns').html(),
                            "vm.detail('" + item.anId + "')",
                            item.anId,
                            isCanEdit,
                            "vm.del('" + item.anId + "')",
                            isCanDel);

                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                pageable: common.kendoGridConfig(vm.queryParams).pageable,
                dataBound: common.kendoGridConfig(vm.queryParams).dataBound,
                columns: columns,
                resizable: true
            };
        }// end fun grid

        //S_updateIssueState
        function updateIssueState(vm, state) {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                bsWin.alert("请选择要修改的数据");
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
                    vm.isSubmit = false;
                    if (response.data.flag || response.data.reCode == 'ok') {
                        bsWin.alert("操作成功！", function () {
                            vm.gridOptions.dataSource.read();
                        });
                    } else {
                        bsWin.alert(response.data.reMsg);
                    }
                }
                common.http({
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }//E_updateIssueState

        function findDetailById(vm, id) {
            var httpOptions = {
                method: "post",
                url: url_annountment + "/findAnnountmentById",
                params: {
                    anId: id
                }
            }
            var httpSuccess = function success(response) {
                vm.annountment = response.data;
                sysfileSvc.findByBusinessId(id, function (data) {
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


        //S_startFlow
        function startFlow(id, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/annountment/startFlow",
                params: {
                    id: id
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_startFlow

        //S_初始化流程数据
        function initFlowDeal(vm) {
            vm.annountment = {};
            vm.annountment.anId = vm.businessKey;
            findAnnountmentById(vm, function (data) {
                vm.annountment = data;
            })
        }//E_initFlowDeal


    }
})();