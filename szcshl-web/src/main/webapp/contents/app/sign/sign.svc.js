(function () {
    'use strict';

    angular.module('app').factory('signSvc', sign);

    sign.$inject = ['$http', '$state', 'flowSvc'];

    function sign($http, $state, flowSvc) {
        var service = {
            grid: grid,						//初始化项目列表
            querySign: querySign,			//查询
            createSign: createSign,			//新增
            initFillData: initFillData,		//初始化表单填写页面（可编辑）
            initDetailData: initDetailData,	//初始化详情页（不可编辑）
            updateFillin: updateFillin,		//申报编辑
            deleteSign: deleteSign,			//删除收文
            findOfficeUsersByDeptName: findOfficeUsersByDeptName,//根据协办部门名称查询用户
            initFlowPageData: initFlowPageData, //初始化流程收文信息
            removeWP: removeWP,             //删除工作方案
            associateGrid: associateGrid,//项目关联列表
            saveAssociateSign: saveAssociateSign,//保存项目关联
            initAssociateSigns: initAssociateSigns,//初始化项目关联信息
            showAssociateSign:showAssociateSign     //项目关联弹窗

        };
        return service;

        //S_初始化grid(过滤掉已经签收的项目)
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/sign/fingByOData", $("#searchform"),{filter:"issign eq '9'"}),
                schema: common.kendoGridConfig().schema({
                    id: "signid",
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
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />", item.signid)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"

                },
                {
                    field: "projectname",
                    title: "项目名称",
                    width: 160,
                    filterable: false
                },
                {
                    field: "projectcode",
                    title: "收文编号",
                    width: 160,
                    filterable: false,
                },
                {
                    field: "designcompanyName",
                    title: "项目单位",
                    width: 200,
                    filterable: false,
                },
                {
                    field: "reviewstage",
                    title: "项目阶段",
                    width: 160,
                    filterable: false,
                },
                {
                    field: "projectcode",
                    title: "项目代码",
                    width: 160,
                    filterable: false,
                },
                {
                    field: "receivedate",
                    title: "收文时间",
                    width: 160,
                    filterable: false,
                    format: "{0:yyyy/MM/dd HH:mm:ss}"

                },
                {
                    field: "",
                    title: "流程状态",
                    width: 160,
                    filterable: false,
                    template: function (item) {
                        if (item.folwState) {
                            if (item.folwState == 1) {
                                return '<span style="color:green;">进行中</span>';
                            } else if (item.folwState == 2) {
                                return '<span style="color:orange;">已暂停</span>';
                            } else if (item.folwState == 8) {
                                return '<span style="color:red;">强制结束</span>';
                            } else if (item.folwState == 9) {
                                return '<span style="color:blue;">已完成</span>';
                            }
                        } else {
                            return "未发起"
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 180,
                    template: function (item) {
                        //如果已经发起流程，则只能查看
                        var isFlowStart = false, hideStopButton = true, hideRestartButton = true,
                            isAlreadyAssociate = false;
                        if (item.folwState && item.folwState > 0) {
                            isFlowStart = true;
                            if (item.folwState == 1) {
                                hideStopButton = false;
                            }
                            if (item.folwState == 2) {
                                hideRestartButton = false;
                            }
                        }
                        return common.format($('#columnBtns').html(), item.signid, item.folwState,
                            item.signid + "/" + item.processInstanceId, "vm.del('" + item.signid + "')", isFlowStart,
                            "vm.startNewFlow('" + item.signid + "')", isFlowStart,
                            "vm.stopFlow('" + item.signid + "')", hideStopButton,
                            "vm.restartFlow('" + item.signid + "')", hideRestartButton);
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
                resizable: true
            };
        }//E_初始化grid

        //S_查询grid
        function querySign(vm) {
            vm.gridOptions.dataSource.read();
        }//E_查询grid

        //S_创建收文
        function createSign(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/sign",
                    data: vm.model
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功,请继续填写报审登记表！",
                                closeDialog: true,
                                fn: function () {
                                    //跳转并刷新页面
                                    $state.go('fillSign', {signid: response.data.signid}, {reload: true});
                                }
                            })
                        }
                    });
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }//E_创建收文

        //start  根据协办部门查询用户
        function findOfficeUsersByDeptName(vm, status) {
            var param = {};
            if ("main" == status) {
                param.maindeptName = vm.model.maindeptName;
            } else {
                param.assistdeptName = vm.model.assistdeptName;
            }
            var httpOptions = {
                method: 'post',
                url: rootPath + "/officeUser/findOfficeUsersByDeptName",
                data: param
            };

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        if ("main" == status) {
                            vm.mainOfficeList = {};
                            vm.mainOfficeList = response.data;
                        } else {
                            vm.assistOfficeList = {};
                            vm.assistOfficeList = response.data;
                        }
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

        //end  根据协办部门查询用户

        //Start 申报登记编辑
        function updateFillin(vm) {
            common.initJqValidation($('#sign_fill_form'));
            var isValid = $('#sign_fill_form').valid();
            if (isValid) {
                var httpOptions = {
                    method: 'put',
                    url: rootPath + "/sign",
                    data: vm.model,
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                }
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
        }

        //End 申报登记编辑

        //Start 删除收文
        function deleteSign(vm, signid) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/sign",
                data: signid
            }

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.isSubmit = false;
                        vm.gridOptions.dataSource.read();
                    }

                });

            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //End 删除收文

        //S_初始化填报页面数据
        function initFillData(vm) {
        	/*vm.ProjectAdvice=true;
        	vm.importDeviceList=true;
        	vm.exportDeviceList=true;*/
        	
            var httpOptions = {
                method: 'get',
                url: rootPath + "/sign/html/initFillPageData",
                params: {signid: vm.model.signid}
            }

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.model = response.data.sign;
                        vm.deptlist = response.data.deptlist

                        if (response.data.mainOfficeList) {
                            vm.mainOfficeList = response.data.mainOfficeList;
                        }
                        if (response.data.assistOfficeList) {
                            vm.assistOfficeList = response.data.assistOfficeList;
                        }
                        //建设单位
                        vm.builtcomlist = response.data.builtcomlist;
                        //编制单位
                        vm.designcomlist = response.data.designcomlist;

                    }
                })
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_初始化填报页面数据

        //S_初始化详情数据
        function initDetailData(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/sign/html/initDetailPageData",
                params: {signid: vm.model.signid}
            }

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.model = response.data;
                    }
                })
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_初始化详情数据

        //S_初始化流程页面
        function initFlowPageData(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/sign/html/initFlowPageData",
                params: {signid: vm.model.signid, queryAll: true}
            }

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.model = response.data;
                        //工作方案
                        if (vm.model.workProgramDtoList && vm.model.workProgramDtoList.length > 0) {
                            vm.show_workprogram = true;
                            vm.model.workProgramDtoList.forEach(function (w, index) {
                                if (w.isMain == 9) {
                                    vm.showMainwork = true;
                                    vm.mainwork = {};
                                    vm.mainwork = w;
                                } else if (w.isMain == 0) {
                                    vm.showAssistwork = true;
                                    vm.assistwork = {};
                                    vm.assistwork = w;
                                }
                            });
                        }
                        //发文
                        if (vm.model.dispatchDocDto) {
                            vm.show_dispatch = true;
                            vm.dispatchDoc = vm.model.dispatchDocDto;
                        }
                        //归档
                        if (vm.model.fileRecordDto) {
                            vm.show_filerecord = true;
                            vm.fileRecord = vm.model.fileRecordDto;
                        }
                        //抽取专家
                        if (vm.model.expertSelectedDtoList && vm.model.expertSelectedDtoList.length > 0) {
                            vm.show_expert = true;
                        }
                        //先加载完业务数据，再加载流程业务数据
                        if (vm.dealFlow) {
                            flowSvc.getFlowInfo(vm);
                        }
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_初始化流程页面

        //S_removeWP
        function removeWP(vm) {
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/workprogram/deleteBySignId",
                params: {
                    signId: vm.model.signid
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: "操作成功",
                            fn: function () {
                                vm.isSubmit = false;
                                vm.model.isNeedWrokPrograml = '0'
                                $('.alertDialog').modal('hide');
                                initFlowPageData(vm);
                            }
                        })
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_removeWP

        //associateGrid
        function associateGrid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/sign/fingByOData", $("#searchform")),
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
                    field: "projectname",
                    title: "项目名称",
                    width: 160,
                    filterable: false
                },
                {
                    field: "projectcode",
                    title: "收文编号",
                    width: 140,
                    filterable: false,
                },
                {
                    field: "designcompanyName",
                    title: "项目单位",
                    width: 200,
                    filterable: false,
                },
                {
                    field: "reviewstage",
                    title: "项目阶段",
                    width: 160,
                    filterable: false,
                },
                {
                    field: "projectcode",
                    title: "项目代码",
                    width: 140,
                    filterable: false,
                }/*,
                    {
                        field : "",
                        title : "流程状态",
                        width : 160,
                        filterable : false,
                        template : function(item) {
                            if(item.folwState){
                                if(item.folwState == 1){
                                    return '<span style="color:green;">进行中</span>';
                                }else if(item.folwState == 2){
                                    return '<span style="color:orange;">已暂停</span>';
                                }else if(item.folwState == 8){
                                    return '<span style="color:red;">强制结束</span>';
                                }else if(item.folwState == 9){
                                    return '<span style="color:blue;">已完成</span>';
                                }
                            }else{
                                return "未发起"
                            }
                        }
                    }*/,
                {
                    field: "",
                    title: "操作",
                    width: 180,
                    template: function (item) {
                        return common.format($('#associateColumnBtns').html(), "vm.saveAssociateSign('" + item.signid + "')");
                    }
                }
            ];
            // End:column

            vm.associateGridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true
            };
            vm.associateGridOptions.dataSource.read();
        }//E_初始化associateGrid

        //start saveAssociateSign
        //如果associateSignId为空，解除关联
        function saveAssociateSign(vm, signId, associateSignId,callBack) {

            associateSignId = associateSignId == 'undefined'?null:associateSignId;
            var httpOptions = {
                method: 'post',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                url: rootPath + "/sign/associate",
                data: $.param({signId: signId, associateId: associateSignId}, true),

            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: associateSignId != undefined ? "项目关联成功" : "项目解除关联成功",
                            closeDialog: true,
                            fn: function () {
                                //关闭项目关联窗口
                                //vm.gridOptions.dataSource.read();
                                if(callBack != undefined&&typeof callBack == 'function'){
                                    callBack();
                                }
                            }
                        });
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //end saveAssociateSign

        //显示关联信息
        //start initAssociateSigns
        function initAssociateSigns(vm, singid) {

            var httpOptions = {
                method: 'get',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                url: rootPath + "/sign/associate?signId=" + singid,

            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                         if(response.data != undefined){
                            vm.associateSign = response.data;
                            var signs = response.data;
                           // console.log(signs);
                            var steps = [];
                            var html_ = '';
                            for(var i = (signs.length-1);i>=0;i--){
                                var s = signs[i];
                                var signdate = s.signdate||'';
                                html_ += '<div class="intro-list">'+
                                            '<div class="intro-list-left">'+
                                                '项目阶段：'+s.reviewstage+"<br/>签收时间："+signdate+
                                            '</div>'+
                                            '<div class="intro-list-right">'+
                                                '<span></span>'+
                                                '<div class="intro-list-content">'+
                                                    '名称：<span style="color:red;">'+s.projectname+'</span><br/>'+
                                                    '送件人：<span style="color:red;">'+s.sendusersign+'</span><br/>'+
                                                '</div>'+
                                            '</div>'+
                                         '</div>';

                            }
                            $('#introFlow').html(html_);
                            var step= $("#myStep").step({
                                    animate:true,
                                    initStep:1,
                                    speed:1000
                            });

                        }
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //end initAssociateSigns



         //start 项目关联弹框
         function showAssociateSign(){
            //选中要关联的项目
            var signAssociateWindow=$("#associateWindow");
            signAssociateWindow.kendoWindow({
                width:"50%",
                height:"80%",
                title:"项目关联",
                visible:false,
                modal:true,
                closable:true,
                actions:["Pin","Minimize","Maximize","close"]
            }).data("kendoWindow").center().open();

            //初始化associateGrid

         }
         //end 项目关联
    }
})();