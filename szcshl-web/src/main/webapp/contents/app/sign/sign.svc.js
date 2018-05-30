(function () {
    'use strict';

    angular.module('app').factory('signSvc', sign);

    sign.$inject = ['$http', '$state', 'bsWin', 'sysfileSvc', 'templatePrintSvc'];

    function sign($http, $state, bsWin, sysfileSvc, templatePrintSvc) {
        var service = {
            signGrid: signGrid,				//初始化项目列表
            createSign: createSign,			//新增
            initFillData: initFillData,		//初始化表单填写页面（可编辑）
            initDetailData: initDetailData,	//初始化详情页（不可编辑）
            updateFillin: updateFillin,		//申报编辑
            deleteSign: deleteSign,			//删除收文
            findOfficeUsersByDeptName: findOfficeUsersByDeptName,//根据协办部门名称查询用户
            initFlowPageData: initFlowPageData, //初始化流程收文信息
            removeWP: removeWP,             //删除工作方案
            associateGrid: associateGrid,   //项目关联列表
            getAssociateSign: getAssociateSign, //获取项目关联阶段信息
            getAssociateSignGrid: getAssociateSignGrid,//获取项目关联阶段信息的grid表格
            saveAssociateSign: saveAssociateSign,   //保存项目关联
            initAssociateSigns: initAssociateSigns, //初始化项目关联信息
            meetingDoc: meetingDoc,                 //生成会前准备材
            createDispatchFileNum: createDispatchFileNum,       //生成发文字号
            realSign: realSign,                                 //正式签收
            createDispatchTemplate: createDispatchTemplate,     //生成发文模板
            signGetBackGrid: signGetBackGrid,                   //项目取回列表
            getBack: getBack,                           //项目取回
            editTemplatePrint: editTemplatePrint,       //编辑模板打印
            /*workProgramPrint:workProgramPrint,        //工作方案模板打印*/
            signDeletGrid: signDeletGrid,               //作废项目
            editSignState: editSignState,               //恢复项目
            sumExistDays: sumExistDays,                 //统计项目接受到现在所存在的天数（办结的，按办结日期，未办结的，按现在时间）
            MaintenanProjectGrid: MaintenanProjectGrid, //维护项目
            excelExport: excelExport,                   //项目查询统计导出
            excelExport2: excelExport2,                 //项目查询统计导出
            findExpertReview: findExpertReview,         //查询项目在办的专家抽取方案信息
            getSignInfo: getSignInfo ,                  //通过收文编号获取委里信息
            findSignUnitScore : findSignUnitScore,      //获取评分单位信息
            findAVGDayById : findAVGDayById ,                  //获取平均评审天数和工作日
            addAOrg : addAOrg ,                                //  添加评审部门（项目维护）
            deleteAOrg : deleteAOrg ,                           //移除评审部门（项目维护）
            addSecondUser : addSecondUser ,                     //保存添加负责人（项目维护）
            deleteSecondUser : deleteSecondUser ,               //删除添加的负责人（项目维护）
            saveMoreExpert : saveMoreExpert                     //保存是否能多个自选专家
        };
        return service;

        function findAVGDayById(isgnIds , callBack){
            var httpOptions = {
                method: "post",
                url: rootPath + "/sign/findAVGDayId",
                params: {signIds: isgnIds}
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
        }

        function findSignUnitScore(signId, callBack){
            var httpOptions = {
                method: "post",
                url: rootPath + "/sign/findSignUnitScore",
                params: {signId: signId}
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
        }

        //S_查询项目在办的专家抽取方案信息
        function findExpertReview(signId, callBack) {
            var httpOptions = {
                method: "post",
                url: rootPath + "/signwork/fingSignWorkById",
                params: {signId: signId}
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
        }//E_findExpertReview

        //s_项目查询统计导出
        function excelExport(signIds) {
            var downForm = $("#countSignDayForm");
            downForm.attr("target", "");
            downForm.attr("method", "post");
            downForm.attr("action", rootPath + "/signView/excelExport");
            downForm.find("input[name='signIds']").val(signIds);
            downForm.submit();//表单提交
        }
        //s_项目查询统计导出
        function excelExport2(signIds) {
            var downForm = $("#countSignDayForm");
            downForm.attr("target", "");
            downForm.attr("method", "post");
            downForm.attr("action", rootPath + "/signView/excelExport2");
            downForm.find("input[name='signIds']").val(signIds);
            downForm.submit();//表单提交
        }

        //e_项目查询统计导出

        //S_统计项目接受到现在所存在的天数
        function sumExistDays(signIds, callBack) {
            var httpOptions = {
                method: "post",
                url: rootPath + "/sign/sumExistDays",
                params: {signIds: signIds}
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
        }//E_sumExistDays

        //negin createDispatchTemplate
        function createDispatchTemplate(vm, callBack) {
            var httpOptions = {
                method: "post",
                url: rootPath + "/dispatch/createDispatchTemplate",
                params: {signId: vm.model.signid}
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
        }

        //end createDispatchTemplate

        //E 上传附件列表

        function getSignInfo(fileCode, signType, callBack) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/intfc/getPreSign",
                params: {
                    fileCode: fileCode,
                    signType: signType
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
        }

        //S_初始化grid(过滤已签收和已经完成的项目)
        function signGrid(vm) {
            // Begin:dataSource
            /*      var dataSource = new kendo.data.DataSource({
             type: 'odata',
             transport: common.kendoGridConfig().transport(rootPath + "/sign/findBySignUser", $("#searchform")),
             schema: {
             data: "value",
             total: function (data) {
             if (data['count']) {
             $('#GET_SIGN_COUNT').html(data['count']);
             } else {
             $('#GET_SIGN_COUNT').html(0);
             }
             return data['count'];
             },
             model: {
             id: "taskId"
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
             });*/
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/sign/findBySignUser", $("#searchform"), vm.gridParams),
                schema: {
                    data: "value",
                    total: function (data) {
                        if (data['count']) {
                            $('#GET_SIGN_COUNT').html(data['count']);
                        } else {
                            $('#GET_SIGN_COUNT').html(0);
                        }
                        return data['count'];
                    },
                    model: {
                        id: "signid"
                    }
                },
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: vm.queryParams.pageSize || 10,
                page: vm.queryParams.page || 1,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            // End:dataSource
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
                    title: "项目名称",
                    width: 280,
                    filterable: false,
                    template: function (item) {
                        return '<a ng-click="vm.saveView()"  href="#/fillSign/' + item.signid + '/" >' + item.projectname + '</a>';
                    }
                },
                {
                    field: "filecode",
                    title: "收文编号",
                    width: 120,
                    filterable: false,
                },
                {
                    field: "designcompanyName",
                    title: "项目单位",
                    width: 260,
                    filterable: false,
                },
                {
                    field: "reviewstage",
                    title: "评审阶段",
                    width: 130,
                    filterable: false,
                },
                {
                    field: "projectcode",
                    title: "项目代码",
                    width: 160,
                    filterable: false,
                },
                {
                    field: "signdate",
                    title: "签收日期",
                    width: 100,
                    filterable: false,
                    format: "{0:yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "",
                    title: "流程状态",
                    width: 80,
                    filterable: false,
                    template: function (item) {
                        if (item.signState) {
                            if (item.signState == 1) {
                                return '<span style="color:green;">进行中</span>';
                            } else if (item.signState == 2) {
                                return '<span style="color:orange;">已暂停</span>';
                            } else if (item.signState == 8) {
                                return '<span style="color:red;">强制结束</span>';
                            } else if (item.signState == 9) {
                                return '<span style="color:blue;">已完成</span>';
                            } else if (item.signState == 5) {
                                return '未发起';
                            } else {
                                return "";
                            }
                        } else {
                            return "未发起"
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 320,
                    template: function (item) {
                        var isStartFlow = false;
                        if (item.processInstanceId) {
                            isStartFlow = true;
                        }
                        var isRealSign = false;
                        if (item.issign && (item.issign == 9 || item.issign == '9' )) {
                            isRealSign = true;
                        }
                        //如果已经发起流程，则只能查看
                        return common.format($('#columnBtns').html(),
                            item.signid, isStartFlow,
                            item.signid + "/" + item.processInstanceId,
                            "vm.del('" + item.signid + "')", isStartFlow,
                            "vm.startNewFlow('" + item.signid + "')", isStartFlow,
                            "vm.realSign('" + item.signid + "')", isRealSign);
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                pageable: common.kendoGridConfig(vm.queryParams).pageable,
                dataBound: common.kendoGridConfig(vm.queryParams).dataBound,
                resizable: true
            };
        }//E_初始化grid


        //S_创建收文
        function createSign(model, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign",
                data: model
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
        }//E_创建收文

        //start  根据协办部门查询用户
        function findOfficeUsersByDeptName(param, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/officeUser/findOfficeUsersByDeptName",
                data: param
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

        //end  根据协办部门查询用户

        //Start 申报登记编辑
        function updateFillin(signObj, callBack) {
            var httpOptions = {
                method: 'put',
                url: rootPath + "/sign",
                data: signObj,
            }
            var httpSuccess = function success(response) {
                //关闭项目关联窗口
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //End 申报登记编辑

        //Start 删除收文
        function deleteSign(signid, callBack) {
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/sign",
                params: {
                    signid: signid
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

        //End 删除收文

        //S_初始化填报页面数据
        function initFillData(signid, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/html/initFillPageData",
                params: {
                    signid: signid
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
        }//E_初始化填报页面数据

        //S_初始化详情数据
        function initDetailData(signid, callBack) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/sign/html/initDetailPageData",
                params: {
                    signid: signid
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
        }//E_初始化详情数据

        //S_初始化流程页面
        function initFlowPageData(signId, callBack) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/sign/initFlowPageData",
                params: {
                    signid: signId,
                    queryAll: true
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
                vm.isSubmit = false;
                if (response.data.flag || response.data.reCode == "ok") {
                    //更改状态
                    vm.businessFlag.isFinishWP = false;
                    bsWin.success("操作成功！");
                } else {
                    bsWin.error(response.data.reMsg);
                }
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_removeWP

        //associateGrid（停用，2017-08-27，改用List的方式）
        function associateGrid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/sign/fingByOData", $("#searchAssociateform"), {filter: "isAssociate eq 0"}),
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
        }//E_初始化associateGrid


        //getAssociateSignGrid
        function getAssociateSignGrid(vm, callBack) {
            /* var httpOptions = {
             method: 'post',
             url: rootPath + "/sign/findAssociateSignList",
             params: {
             signid: vm.price.signid,
             reviewstage:vm.price.reviewstage,
             projectname:vm.price.projectname,
             skip:vm.price.skip,
             size:vm.price.size,

             },
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
             });*/
            $http({
                method: 'post',
                url: rootPath + "/sign/findAssociateSignList",
                params: {
                    signid: vm.price.signid,
                    reviewstage: vm.price.reviewstage,
                    projectname: vm.price.projectname,
                    mUserName : vm.price.mUserName,
                    skip: vm.price.skip,
                    size: vm.price.size,
                },
            }).then(function (r) {
                if (typeof callBack == 'function') {
                    callBack(r.data);
                }
            });
        }//E_getAssociateSignGrid


        //S_获取关联项目
        function getAssociateSign(signModel, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/findAssociateSign",
                data: signModel
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
        }//E_getAssociateSign

        //start saveAssociateSign
        //如果associateSignId为空，解除关联
        function saveAssociateSign(signId, associateSignId, callBack) {
            associateSignId = associateSignId == 'undefined' ? null : associateSignId;
            var httpOptions = {
                method: 'post',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                url: rootPath + "/sign/associate",
                data: $.param({signId: signId, associateId: associateSignId}, true),

            }
            var httpSuccess = function success(response) {
                //关闭项目关联窗口
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
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
                        if (response.data != undefined) {
                            vm.associateSign = response.data;
                            var signs = response.data;

                            var html_ = '';
                            for (var i = (signs.length - 1); i >= 0; i--) {
                                var s = signs[i];
                                var signdate = s.signdate || '';
                                html_ += '<div class="intro-list">' +
                                    '<div class="intro-list-left">' +
                                    '项目阶段：' + s.reviewstage + "<br/>签收时间：" + signdate +
                                    '</div>' +
                                    '<div class="intro-list-right">' +
                                    '<span></span>' +
                                    '<div class="intro-list-content">' +
                                    '名称：<span style="color:red;">' + s.projectname + '</span><br/>' +
                                    '送件人：<span style="color:red;">' + s.sendusersign + '</span><br/>' +
                                    '</div>' +
                                    '</div>' +
                                    '</div>';

                            }
                            $('#introFlow').html(html_);
                            var step = $("#myStep").step({
                                animate: true,
                                initStep: 1,
                                speed: 1000
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

        //S_meetingDoc
        function meetingDoc(vm, callBack) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/workprogram/createMeetingDoc",
                params: {
                    signId: vm.model.signid,
                    // workprogramId: wpId
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof  callBack == "function") {
                    callBack(response.data);
                }
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_meetingDoc


        //S_createDispatchFileNum
        function createDispatchFileNum(signId, dispatchId, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/dispatch/createFileNum",
                params: {
                    signId: signId,
                    dispatchId: dispatchId
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
        }//E_createDispatchFileNum

        //S_项目正式签收
        function realSign(signid, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/realSign",
                params: {
                    signid: signid
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
                success: httpSuccess,
            });
        }//E_realSign


        //signGetBackGrid
        function signGetBackGrid(vm) {
            // Begin:dataSource
            /* var dataSource = new kendo.data.DataSource({
             type: 'odata',
             transport: common.kendoGridConfig().transport(rootPath + "/sign/fingByGetBack", $("#signBackform")),
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
             });*/

            var dataSource = common.kendoGridDataSource(rootPath + "/sign/fingByGetBack", $("#signBackform"), vm.queryParams.page, vm.queryParams.pageSize, vm.gridParams);

            // End:dataSource

            // Begin:column
            var columns = [
                {
                    field: "",
                    title: "序号",
                    template: "<span class='row-number'></span>",
                    width: 50
                },
                {
                    field: "projectName",
                    title: "项目名称",
                    width: "25%",
                    filterable: false
                },
                {
                    field: "nodeNameValue",
                    title: "当前环节",
                    width: "10",
                    filterable: false,
                },
                {
                    field: "displayName",
                    title: "处理人",
                    width: "10",
                    filterable: false,
                },
                {
                    field: "",
                    title: "合并评审",
                    width: "12%",
                    filterable: false,
                    template: function (item) {
                        if (item.reviewType) {
                            if (item.reviewType == 9 || item.reviewType == '9') {
                                return "合并评审[主项目]";
                            } else {
                                return "合并评审[次项目]";
                            }
                        } else {
                            return "否";
                        }
                    }
                },
                {
                    field: "reviewStage",
                    title: "项目阶段",
                    width: "15%",
                    filterable: false,
                },
                {
                    field: "signDate",
                    title: "签收时间",
                    width: "10%",
                    filterable: false,
                },
                {
                    field: "",
                    title: "操作",
                    width: "12%",
                    template: function (item) {
                        return common.format($('#columnBtns').html(), "signFlowDetail", item.businessKey, item.taskId, item.processInstanceId, "vm.getBack");
                    }
                }

            ];
            // End:column
            vm.signGetBackGrid = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                pageable: common.kendoGridConfig(vm.queryParams).pageable,
                dataBound: common.kendoGridConfig(vm.queryParams).dataBound,
                resizable: true
            };
        }//E_初始化signGetBackGrid

        //S_项目取回
        function getBack(taskId, businessKey, callBack) {
            //var activityId= "SIGN_FGLD_FB";根据角色判断回退到哪个环节
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/getBack",
                params: {
                    taskId: taskId,
                    businessKey: businessKey
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
                success: httpSuccess,
            });
        }//getBack

        //编辑模板打印
        function editTemplatePrint(vm) {
            if (vm.model.reviewstage == '项目建议书' || vm.model.reviewstage == '可行性研究报告' || vm.model.reviewstage == '其它') {
                templatePrintSvc.templatePrint("sign_fill_xmjys_templ");
            } else if (vm.model.reviewstage == '资金申请报告') {
                templatePrintSvc.templatePrint("sign_fill_zjsq_templ");
            } else if (vm.model.reviewstage == '进口设备') {
                templatePrintSvc.templatePrint("sign_fill_jksb_templ");
            } else if (vm.model.reviewstage == '设备清单（国产）' || vm.model.reviewstage == '设备清单（进口）') {
                templatePrintSvc.templatePrint("sign_fill_sbqd_templ");
            } else if (vm.model.reviewstage == '项目概算') {
                templatePrintSvc.templatePrint("sign_fill_xmgs_templ");
            }
        }

        /*//工作方案详细打印
         function workProgramPrint(id){
         var tempStr1;
         var tempStr2;
         if(id.indexOf("wpMain")>-1){
         if(id=='wpMain'){
         tempStr1 = "wp1";
         tempStr2 = "wp2";
         }else{
         tempStr1 = "wpEdit1";
         tempStr2 = "wpEdit2";
         }
         var LODOP = getLodop();
         var strStylePath = rootPath +"/contents/shared/templatePrint.css";
         var strStyleCSS="<link href="+strStylePath+" type='text/css' rel='stylesheet'>";
         var strFormHtml1="<head>"+strStyleCSS+"</head><body>"+$("#"+tempStr1).html()+"</body>";
         LODOP.PRINT_INIT("");
         LODOP.ADD_PRINT_HTML(10,20,"100%","100%",strFormHtml1);
         LODOP.NewPage();
         var strFormHtml2="<head>"+strStyleCSS+"</head><body>"+$("#"+tempStr2).html()+"</body>";
         LODOP.ADD_PRINT_HTML(50,20,"100%","100%",strFormHtml2);
         LODOP.PREVIEW();
         }else if(id.indexOf("wp") > -1 ){
         var strArr  = id.split("_");
         var LODOP = getLodop();
         var strStylePath = rootPath +"/contents/shared/templatePrint.css";
         var strStyleCSS="<link href="+strStylePath+" type='text/css' rel='stylesheet'>";
         var strFormHtml1="<head>"+strStyleCSS+"</head><body>"+$("#wpTempl1"+strArr[1]+strArr[2]).html()+"</body>";
         LODOP.PRINT_INIT("");
         LODOP.ADD_PRINT_HTML(10,20,"100%","100%",strFormHtml1);
         LODOP.NewPage();
         var strFormHtml2="<head>"+strStyleCSS+"</head><body>"+$("#wpTempl2"+strArr[1]+strArr[2]).html()+"</body>";
         LODOP.ADD_PRINT_HTML(50,20,"100%","100%",strFormHtml2);
         LODOP.PREVIEW();
         }
         }*/

        //begin_signDeletGrid
        //作废项目
        function signDeletGrid(vm) {
            var dataSource = common.kendoGridDataSource(rootPath + "/signView/getSignList?$orderby=receivedate", $("#deletform"), vm.queryParams.page, vm.queryParams.pageSize, vm.gridParams);

            // Begin:column
            var columns = [
                {
                    field: "",
                    title: "序号",
                    template: "<span class='row-number text-center'></span>",
                    width: 40
                },
                {
                    field: "",
                    title: "项目名称",
                    width: 160,
                    filterable: false,
                    template: function (item) {
                        if (item.processInstanceId) {
                            return '<a ng-click="vm.saveView()" href="#/signDetails/' + item.signid + '/' + item.processInstanceId + '" >' + item.projectname + '</a>';
                        } else {
                            return '<a ng-click="vm.saveView()" href="#/signDetails/' + item.signid + '/" >' + item.projectname + '</a>';
                        }

                    }
                },
                {
                    field: "reviewstage",
                    title: "项目阶段",
                    width: 110,
                    filterable: false,
                },
                {
                    field: "projectcode",
                    title: "项目代码",
                    width: 100,
                    filterable: false
                },
                {
                    field: "signdate",
                    title: "签收日期",
                    width: 100,
                    filterable: false,
                    format: "{0:yyyy/MM/dd HH:mm:ss}"
                },
                {
                    field: "",
                    title: "操作",
                    width: 90,
                    filterable: false,
                    template: function (item) {
                        return common.format($('#columnBtns').html(), "vm.editSignState('" + item.signid + "')");

                    }
                }
            ];

            // End:column
            vm.signListOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                pageable: common.kendoGridConfig(vm.queryParams).pageable,
                dataBound: common.kendoGridConfig(vm.queryParams).dataBound
            };
        }//signDeletGrid


        //negin editSignState
        function editSignState(vm, callBack) {
            var httpOptions = {
                method: "post",
                url: rootPath + "/sign/editSignState",
                params: {signId: vm.signid, stateProperty: "signState", stateValue: vm.stateValue}
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

        }

        //end editSignState
        //begin editSignState
        //维护项目列表
        function MaintenanProjectGrid(vm) {
            var dataSource = common.kendoGridDataSource(rootPath + "/signView/getSignList", $("#Maintenanform"), vm.queryParams.page, vm.queryParams.pageSize, vm.gridParams);
            // Begin:column
            var columns = [
                {
                    field: "",
                    title: "序号",
                    template: "<span class='row-number text-center'></span>",
                    width: 50
                },
                {
                    field: "",
                    title: "项目名称",
                    width: 260,
                    filterable: false,
                    template: function (item) {
                        if (item.processInstanceId) {
                            return '<a ng-click="vm.saveView()" href="#/MaintainProjectEdit/' + item.signid + '/' + item.processInstanceId + '" >' + item.projectname + '</a>';
                        } else {
                            return '<a ng-click="vm.saveView()" href="#/MaintainProjectEdit/' + item.signid + '/" >' + item.projectname + '</a>';
                        }

                    }
                }, {
                    field: "builtcompanyname",
                    title: "建设单位",
                    width: 210,
                    filterable: false,
                },
                {
                    field: "reviewstage",
                    title: "评审阶段",
                    width: 120,
                    filterable: false,
                },
                {
                    field: "",
                    title: "项目状态",
                    width: 80,
                    filterable: false,
                    template: function (item) {
                        var returnStr = "";
                        switch (item.signState) {
                            case "1":
                                returnStr = "进行中";
                                break;
                            case "2":
                                returnStr = "暂停";
                                break;
                            case "8":
                                returnStr = "强制结束";
                                break;
                            case "9":
                                returnStr = "已完成";
                                break;
                            default:
                                ;
                        }
                        return returnStr;
                    }
                }, {
                    field: "signNum",
                    title: "收文编号",
                    width: 100,
                    filterable: false,
                }, {
                    field: "signdate",
                    title: "收文日期",
                    width: 100,
                    filterable: false,
                }, {
                    field: "dfilenum",
                    title: "发文号",
                    width: 100,
                    filterable: false,
                }, {
                    field: "ffilenum",
                    title: "归档编号",
                    width: 130,
                    filterable: false,
                }, {
                    field: "reviewOrgName",
                    title: "所属部门",
                    width: 100,
                    filterable: false,
                }, {
                    field: "allPriUser",
                    title: "项目负责人",
                    width: 100,
                    filterable: false,
                }, {
                    field: "dispatchType",
                    title: "发文类型",
                    width: 100,
                    filterable: false,
                }, {
                    field: "appalyInvestment",
                    title: "申报金额",
                    width: 100,
                    filterable: false,
                }, {
                    field: "authorizeValue",
                    title: "审定金额",
                    width: 100,
                    filterable: false,
                }, {
                    field: "extraValue",
                    title: "核减",
                    width: 100,
                    filterable: false,
                }, {
                    field: "approveValue",
                    title: "批复金额",
                    width: 100,
                    filterable: false,
                }
            ];

            // End:column
            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                pageable: common.kendoGridConfig(vm.queryParams).pageable,
                dataBound: common.kendoGridConfig(vm.queryParams).dataBound
            };
        }

        //end editSignState

        //begin addAOrg
        function addAOrg(orgIds , signId , callBack){
            var httpOptions = {
                method : "post" ,
                url : rootPath + "/sign/addAOrg",
                params : {signId : signId  , orgIds : orgIds }
            }

            var httpSuccess = function success(response){
                if(callBack != undefined || typeof callBack == 'function'){
                    callBack(response.data);
                }
            }

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //end addAOrg

        //begin deleteAOrg
        function deleteAOrg(orgId  , signId , callBack){
            var httpOptions = {
                method : "delete" ,
                url : rootPath + "/sign/deleteOrg",
                params : {orgIds : orgId  , signId : signId}
            }

            var httpSuccess = function success(response){
                if(callBack != undefined || typeof callBack == 'function'){
                    callBack(response.data);
                }
            }

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //end deleteAOrg

        //begin addSecondUser
        function addSecondUser(userId ,  signId , callBack){
            var httpOptions = {
                method : "post" ,
                url : rootPath + "/sign/addSecondUser",
                params : {userId : userId  ,   signId : signId}
            }

            var httpSuccess = function success(response){
                if(callBack != undefined || typeof callBack == 'function'){
                    callBack(response.data);
                }
            }

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //end addSecondUser

        //begin deleteSecondUser
        function deleteSecondUser(userId , signId , callBack){
            var httpOptions = {
                method : "delete" ,
                url : rootPath + "/sign/deleteSecondUser",
                params : {userId : userId  ,   signId : signId}
            }

            var httpSuccess = function success(response){
                if(callBack != undefined || typeof callBack == 'function'){
                    callBack(response.data);
                }
            }

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //end deleteSecondUser

        //begin saveMoreExpert
        function saveMoreExpert(signId , isMoreExpert , callBack){
            var httpOptions = {
                method : "post",
                url : rootPath + "/sign/saveMoreExpert",
                params : {signId : signId , isMoreExpert : isMoreExpert}
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
        //end saveMoreExpert
    }
})();