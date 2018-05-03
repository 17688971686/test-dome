(function () {
    'use strict';

    angular.module('app').controller('MaintainProjectEditCtrl', MaintainProjectEdit);

    MaintainProjectEdit.$inject = ['pauseProjectSvc', 'signSvc', '$state', 'flowSvc', '$scope', 'sysfileSvc', 'addRegisterFileSvc', 'bsWin'];

    function MaintainProjectEdit(pauseProjectSvc, signSvc, $state, flowSvc, $scope, sysfileSvc, addRegisterFileSvc, bsWin) {
        var vm = this;
        vm.title = "维护项目修改";
        vm.model = {};
        vm.model.signid = $state.params.signid;   //业务ID
        vm.model.processInstanceId = $state.params.processInstanceId;	//流程实例ID
        vm.isHaveWork = false;
        vm.isHaveDis = false;
        vm.isHaveFile = false;
        vm.signWorkList = {};
        //初始化附件上传控件
        vm.initFileUpload = function () {
            //创建附件对象
            vm.sysFile = {
                businessId: vm.model.signid,
                mainId: vm.model.signid,
                mainType: sysfileSvc.mainTypeValue().SIGN,
                sysfileType: sysfileSvc.mainTypeValue().WORKPROGRAM,
                sysBusiType: sysfileSvc.mainTypeValue().WORKPROGRAM,
            };
            sysfileSvc.initUploadOptions({
                inputId: "sysfileinput",
                vm: vm,
                uploadSuccess: function () {
                    //5、附件
                    sysfileSvc.findByMianId(vm.model.signid, function (data) {
                        if (data && data.length > 0) {
                            vm.isDisplay = true;//删除附件按钮
                            vm.sysFileList = data;
                            sysfileSvc.initZtreeClient(vm, $scope);//树形图
                        }
                    });
                }
            });
        }
        active();
        function active() {
            $('#myTab li').click(function (e) {
                var aObj = $("a", this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#" + showDiv).addClass("active").addClass("in").show(500);
                vm.model.showDiv = showDiv;
            })
            // 初始化业务信息
            signSvc.initFlowPageData(vm.model.signid, function (data) {
                vm.model = data;
                if(vm.model.dispatchDocDto){
                    vm.dispatchDoc = vm.model.dispatchDocDto;
                    vm.isHaveDis = true;
                }
                if(vm.model.fileRecordDto){
                    vm.fileRecordDto = vm.model.fileRecordDto;
                    vm.isHaveFile = true;
                }
                if (vm.model.workProgramDtoList) {
                    vm.isHaveWork = true;
                    for (var i = 0; i < vm.model.workProgramDtoList.length; i++) {
                        if (vm.model.workProgramDtoList[i].branchId == "1") {
                            vm.work = vm.model.workProgramDtoList[i];//主的工作方案
                            //初始化第二负责人
                            if (vm.work.secondChargeUserName) {
                                vm.secondChargeUserName = vm.work.secondChargeUserName.split(",")
                            }
                        }
                    }
                }
                //5、附件
                sysfileSvc.findByMianId(vm.model.signid, function (data) {
                    if (data && data.length > 0) {
                        vm.isDisplay = true;//删除附件按钮
                        vm.sysFileList = data;
                        sysfileSvc.initZtreeClient(vm, $scope);//树形图
                    }
                });

            });
            vm.initFileUpload();
        }

        //  登记表补充资料
        vm.addRegisterFile = function () {
            $state.go('registerFile', {businessId: vm.model.signid});
        }
        // S_跳转到 工作方案 编辑页面
        vm.addWorkProgram = function () {
            if( vm.isHaveWork){
                $state.go('maintWorkprogramEdit', {signid: vm.model.signid});
            }else{
                bsWin.alert("该项目没有工作方案！");
            }
        }
        // E_跳转到 工作方案 编辑页面

        // S_跳转到 发文 编辑页面
        vm.addDisPatch = function () {
            if( vm.isHaveDis) {
                $state.go('dispatchEdit', {signid: vm.model.signid, isControl: true});
            }else{
                bsWin.alert("该项目没有发文！");
            }
        }// E_跳转到 发文 编辑页面

        //跳转到归档页面
        vm.addDoFile = function () {
            if (vm.isHaveFile) {
                $state.go('fileRecordEdit', {signid: vm.model.signid, isControl: true});
            } else {
                bsWin.alert("该项目还没有填写归档");
            }
        }

        //start  作废项目
        vm.scrap = function (signid) {
            bsWin.confirm({
                title: "询问提示",
                message: "确认作废该条项目吗？",
                onOk: function () {
                    signSvc.deleteSign(signid, function (data) {
                        if (data.flag || data.reCode == 'ok') {
                            bsWin.alert("操作成功！", function () {
                                $state.go('MaintainProjectList');
                            })
                        } else {
                            bsWin.alert(data.reMsg);
                        }
                    });
                }
            });
        }
        //end 作废项目

        /**
         * 项目暂停
         * @param signid
         */
        vm.pauseProject = function () {
            pauseProjectSvc.findPausingProject(vm, vm.model.signid);
        }
        //S_链接到拟补充资料函
        vm.addSuppLetter = function () {
            $state.go('addSupp', {businessId: vm.model.signid, businessType: "SIGN", isControl: true});
        }
        // E_跳转到 拟补充资料函 编辑页面

        //关联其他项目阶段
        vm.relation = function () {
            if (vm.model.dispatchDocDto) {
                if (!vm.ss) {
                    vm.page = lgx.page.init({
                        id: "demo5", get: function (o) {
                            //根据项目名称，查询要关联阶段的项目
                            if (!vm.price) {
                                vm.price = {
                                    signid: vm.model.signid,
                                    projectname: vm.model.projectname,
                                };
                            }
                            vm.price.reviewstage = vm.model.reviewstage; //设置评审阶段
                            var skip;
                            //oracle的分页不一样。
                            if (o.skip != 0) {
                                skip = o.skip + 1
                            } else {
                                skip = o.skip
                            }
                            vm.price.skip = skip;//页码
                            vm.price.size = o.size + o.skip;//页数
                            signSvc.getAssociateSignGrid(vm, function (data) {
                                vm.associateSignList = [];
                                if (data) {
                                    vm.associateSignList = data.value;
                                    vm.page.callback(data.count);//请求回调时传入总记录数
                                }
                                vm.ss = true;
                            });
                            //alert("当前页："+o.number+"，从数据库的位置1"+o.skip+"起，查"+o.size+"条数据");
                            //需在这里发起ajax请求查询数据，请求成功后需调用callback方法重新计算分页

                        }
                    });

                } else {
                    vm.page.selPage(1);
                }


                //选中要关联的项目
                $("#associateWindow").kendoWindow({
                    width: "75%",
                    height: "750px",
                    title: "项目关联",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "close"],
                }).data("kendoWindow").center().open();

            } else {
                bsWin.alert("该项目还没有填写发文");
            }
        }

        //S_查找项目信息
        vm.findExpertReview = function (signid, signState) {
            if (signState == 9) {
                bsWin.alert("该项目已归档，不能再修改！");
            } else {
                vm.signWorkList = {};
                signSvc.findExpertReview(signid, function (data) {
                    if (data || data.length > 0) {
                        vm.signWorkList = data;
                        $("#signWorkDiv").kendoWindow({
                            width: "860px",
                            height: "320px",
                            title: "抽取专家维护",
                            visible: false,
                            modal: true,
                            closable: true,
                            actions: ["Pin", "Minimize", "Maximize", "close"]
                        }).data("kendoWindow").center().open();
                    }
                });
            }
        }

        vm.colseOpenWin = function () {
            window.parent.$("#signWorkDiv").data("kendoWindow").close();
        }

        //附件下载
        vm.commonDownloadSysFile = function (sysFileId) {
            sysfileSvc.downloadFile(sysFileId);
        }
        /**
         * 删除附件
         * @param fileId
         */
        vm.delFile = function (fileId) {
            bsWin.confirm({
                title: "询问提示",
                message: "确认删除么？",
                onOk: function () {
                    sysfileSvc.delSysFile(fileId, function () {
                        bsWin.alert("删除成功", function () {
                            sysfileSvc.findByMianId(vm.model.signid, function (data) {
                                if (data && data.length > 0) {
                                    vm.sysFileList = data;
                                    sysfileSvc.initZtreeClient(vm, $scope);//树形图
                                }
                            });
                        })
                    });
                }
            });
        }

        //关联项目条件查询
        vm.associateQuerySign = function () {
            signSvc.getAssociateSignGrid(vm, function (data) {
                vm.associateSignList = [];
                if (data) {
                    vm.associateSignList = data.value;
                    vm.page.callback(data.count);//请求回调时传入总记录数
                }

            });
        }

        //start 保存项目关联
        vm.saveAssociateSign = function (associateSignId) {
            if (vm.model.signid == associateSignId) {
                bsWin.alert("不能关联自身项目");
                return;
            }
            //保存成功之后，返回关联的项目信息
            signSvc.saveAssociateSign(vm.model.signid, associateSignId, function (data) {
                if (data.flag || data.reCode == 'ok') {
                    if (associateSignId) {
                        vm.model.isAssociate = 1;
                        vm.associateDispatchs = data.reObj;
                    } else {
                        vm.associateDispatchs = []; //解除关联也要重新设置值
                    }
                    bsWin.alert(associateSignId != undefined ? "项目关联成功" : "项目解除关联成功", function () {
                        window.parent.$("#associateWindow").data("kendoWindow").close();
                    });
                } else {
                    bsWin.alert(data.reMsg);
                }

            });
        }
    }
})();
