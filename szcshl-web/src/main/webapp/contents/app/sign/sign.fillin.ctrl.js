(function () {
    'use strict';

    angular.module('app').controller('signFillinCtrl', sign);

    sign.$inject = ['signSvc', 'sysfileSvc', '$state', '$http', 'bsWin', '$scope','addRegisterFileSvc'];

    function sign(signSvc, sysfileSvc, $state, $http, bsWin, $scope,addRegisterFileSvc) {
        var vm = this;
        vm.model = {};		//创建一个form对象
        vm.title = '填写报审登记表';        		//标题
        vm.model.signid = $state.params.signid;	//收文ID
        vm.flowDeal = false;		//是否是流程处理标记
        vm.busiObj = {};             //业务对象，用于记录页面操作对象等信息
        vm.otherFile = [];//其他申报资料
        vm.supply={};    //拟补充资料
        vm.isControl= $state.params.isControl; //按钮显示
        active();
        function active() {
            signSvc.initFillData(vm.model.signid, function (data) {
                vm.model = data.reObj.sign;
                vm.deptlist = data.reObj.deptlist;
                //不同的类型的评审项目名称
                vm.orgNameGroupMap = data.reObj.orgNameGroupMap || {};

                if (data.reObj.mainOfficeList) {
                    vm.mainOfficeList = data.reObj.mainOfficeList;
                }
                if (data.reObj.assistOfficeList) {
                    vm.assistOfficeList = data.reObj.assistOfficeList;
                }
                //建设单位和编制单位
                vm.companyList = data.reObj.companyList;
                //分管领导信息
                vm.busiObj.leaderList = data.reObj.leaderList;

                //创建附件对象
                vm.sysFile = {
                    businessId: vm.model.signid,
                    mainId: vm.model.signid,
                    mainType: sysfileSvc.mainTypeValue().SIGN,
                    sysfileType: sysfileSvc.mainTypeValue().FILLSIGN,
                };
                sysfileSvc.initUploadOptions({
                    inputId: "sysfileinput",
                     vm: vm
                });

                //初始化，打印预览的stageType
                if(vm.model.reviewstage == "项目建议书" || vm.model.reviewstage == "可行性研究报告" || vm.model.reviewstage == "登记赋码"){
                    vm.stageType = "STAGESUG";
                }
                else if(vm.model.reviewstage == "资金申请报告"){
                    vm.stageType = "STAGEREPORT";
                }
                else if(vm.model.reviewstage == "设备清单（国产）" || vm.model.reviewstage == "设备清单（进口）"){
                    vm.stageType = "STAGEHOMELAND";
                }
                else if(vm.model.reviewstage == "进口设备"){
                    vm.stageType = "STAGEDEVICE";
                }
                else if(vm.model.reviewstage == "项目概算"){
                    vm.stageType = "STAGEBUDGET";
                }
                //其它资料信息
                if(data.reObj.registerFileDtoDtoList!=undefined){
                    data.reObj.registerFileDtoDtoList.forEach(function(registerFile  , x){
                        if(registerFile.businessType == "3"){
                            vm.supply.push(registerFile);
                        }else if(registerFile.businessType == "2"){
                            vm.drawingFile.push(registerFile);
                        }else{
                            vm.otherFile.push(registerFile);
                        }
                    })
                }
            });
        }

        //选择默认办理部门
        vm.checkOrgType = function ($event) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            var checkboxValue = checkbox.value;
            if (checked) {
                vm.model.leaderName = signcommon.getDefaultLeader(checkboxValue);
                //设置综合部和分管领导ID
                $.each(vm.busiObj.leaderList, function (i, leader) {
                    if (leader.mngOrgType == checkboxValue) {
                        vm.model.leaderId = leader.id;
                        vm.model.leaderName = leader.displayName;
                        vm.model.comprehensivehandlesug = "请" + leader.displayName + "同志阅示。";
                    }
                })
                if (checkboxValue == signcommon.getBusinessType().GX) {
                    var orgName = vm.orgNameGroupMap[signcommon.getBusinessType().GX];
                    vm.model.leaderhandlesug = "请（"+orgName+"）组织评审。";
                } else {
                    var orgName = vm.orgNameGroupMap[signcommon.getBusinessType().PX];
                    vm.model.leaderhandlesug = "请（"+orgName+"）组织评审。";
                }
                if (!vm.model.leaderId) {
                    bsWin.alert("选择的默认办理部门没有合适的分管领导，请先设置分管领导角色用户！");
                }
            }
        }

        //发起流程，发起流程先保存数据
        vm.startNewFlow = function () {
            common.initJqValidation($('#sign_fill_form'));
            var isValid = $('#sign_fill_form').valid();
            if (isValid) {
                vm.isSubmit = true;
                bsWin.confirm({
                    title: "询问提示",
                    message: "确定发起流程么，请确保填写的信息已经保存正确！",
                    onOk: function () {
                        signSvc.updateFillin(vm.model, function (data) {
                            if(data.flag){
                                var httpOptions = {
                                    method: 'post',
                                    url: rootPath + "/sign/startNewFlow",
                                    params: {
                                        signid: vm.model.signid
                                    }
                                }
                                var httpSuccess = function success(response) {
                                    vm.isSubmit = false;
                                    if (response.data.reCode == "ok") {
                                        bsWin.success("操作成功！", function () {
                                            $state.go('gtasks');
                                        });
                                    } else {
                                        bsWin.error(response.data.reMsg);
                                    }
                                }
                                common.http({
                                    $http: $http,
                                    httpOptions: httpOptions,
                                    success: httpSuccess
                                });
                            }else{
                                bsWin.alert(data.reMsg);
                            }
                        });
                    }
                });
            } else {
                bsWin.alert("操作失败，有红色*号的选项为必填项，请按要求填写！");
            }

        }

        //打印预览
        vm.signPreview = function (oper) {

            var htmlBody = $(".well").parents("body");
            var htmlsidebar = htmlBody.find(".main-sidebar");
            var htmlhedaer = htmlBody.find(".main-header");
            var htmlContentwrapper = htmlBody.find(".content-wrapper");
            //隐藏不需打印的区域;
            htmlsidebar.hide();
            htmlhedaer.hide();
            $(".toolbar").hide();

            //修改打印显示样式

            //添加替换input的显示内容，打印后自动删除
            $(".well input").each(function () {
                var inptTpye = $(this).attr("type");
                if (inptTpye == "text") {
                    $(this).before('<span class="printmesge" data="text" style="white-space : nowrap;">' + $(this).val() + '</span>');
                }
                ;
                if (inptTpye == "checkbox") {
                    if ($(this).is(':checked')) {
                        $(this).before('<span class="printmesge" data="text">有</span>');
                    } else {
                        $(this).before('<span class="printmesge" data="text">无</span>');
                    }
                }
            });
            $(".printmesge").show();
            $(".well input[type=text]").hide();
            $(".well input[type=checkbox]").hide();
            $(".well button").hide();
            htmlContentwrapper.find("td div select").hide();
            htmlContentwrapper.find("td div span").css("margin", "0");

            /*自定义表格样式*/
            $(".well").addClass("printbody");
            $(".well .table-bordered").addClass("tableBOX");
            htmlContentwrapper.find("input").addClass("noborder");
            htmlContentwrapper.addClass("nomargin");

            window.print();

            // 恢复原有
            htmlsidebar.show();
            htmlhedaer.show();
            $(".toolbar").show();
            $(".printmesge").hide();
            $(".well input[type=text]").show();
            $(".well input[type=checkbox]").show();
            $(".well button").show();
            htmlContentwrapper.find("td div select").show();
            htmlContentwrapper.find("td div span").css("margin-left", "100px");
            $(".well").removeClass("printbody");
            $(".well .table-bordered").removeClass("tableBOX");
            $("[data=text]").remove();//删除临时添加的内容

            htmlContentwrapper.find("input").removeClass("noborder");
            htmlContentwrapper.removeClass("nomargin");
        }


        //申报登记编辑
        vm.updateFillin = function () {
            common.initJqValidation($('#sign_fill_form'));
            var isValid = $('#sign_fill_form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.leaderhandlesug = $("#leaderhandlesug").val();
                signSvc.updateFillin(vm.model, function (data) {
                    vm.isSubmit = false;
                    if(data.flag){
                        bsWin.alert("操作成功！");
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            } else {
                bsWin.alert("操作失败，有红色*号的选项为必填项，请按要求填写！");
            }

        }

        //根据协办部门查询用户
        vm.findOfficeUsersByDeptName = function (status) {
            var param = {};
            if ("main" == status) {
                param.maindeptName = vm.model.maindeptName;
            } else {
                param.assistdeptName = vm.model.assistdeptName;
            }
            signSvc.findOfficeUsersByDeptName(param, function (data) {
                if ("main" == status) {
                    vm.mainOfficeList = {};
                    vm.mainOfficeList = data;
                } else {
                    vm.assistOfficeList = {};
                    vm.assistOfficeList = data;
                }
            });
        }

        /******以下是其它资料添加*****/

        vm.addOtherFile = function (businessId, businessType) {
            if(!vm.addRegisters){
                vm.addRegisters = [];
            }
            if (!businessId) {
                bsWin.alert("请先保存数据！");
            } else {
                if(businessType == "4"){
                    vm.addRegisters = vm.otherFile;
                    vm.showFilePage = false;
                    vm.showFileOther = false;
                    vm.showSignOther = true;
                }
                if(businessType == "2"){
                    vm.addRegisters = vm.drawingFile;
                    vm.showFilePage = true;
                    vm.showFileOther = false;
                    vm.showSignOther = false;
                }

                vm.businessId = businessId;
                vm.businessType = businessType;

                $("#addOtherFile").kendoWindow({
                    width: "840px",
                    height: "480px",
                    title: "补充资料编辑",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            }

        }


        //新建其它资料
        vm.addRegisterFile = function () {
            vm.addRegister = {};
            vm.addRegister.businessId = vm.businessId;
            vm.addRegister.businessType = vm.businessType;
            vm.addRegister.id = common.uuid();
            vm.addRegisters.push(vm.addRegister);
        }

        //保存其它资料
        vm.saveRegisterFile = function () {
            addRegisterFileSvc.saveRegisterFile(vm.addRegisters, function (data) {
                if (data.flag || data.reCode == 'ok') {
                    bsWin.alert("操作成功");
                    vm.addRegisters = data.reObj;
                } else {
                    bsWin.alert(data.reMsg);
                }
            });
        }
        //删除其它资料
        vm.deleteRegisterFile = function () {
            var isCheked = $("#addOtherFile input[name='addRegistersCheck']:checked")
            if (isCheked.length < 1) {
                bsWin.alert("请选择要删除的记录！");
            } else {
                var ids = [];
                for (var i = 0; i < isCheked.length; i++) {
                    vm.addRegisters.forEach(function (f, number) {
                        if (f.id && isCheked[i].value == f.id) {
                            ids.push(isCheked[i].value);
                            vm.addRegisters.splice(number, 1);
                        }
                    });
                }
                if (ids.length > 0) {
                    addRegisterFileSvc.deleteByIds(ids.join(","), function (data) {
                        bsWin.alert("删除成功！");
                    });
                }
            }
        }
        /******以下是其它资料添加END*****/

        //弹出修改文号信息框
        vm.updateProjCode = function(){
            $("#projCodeWin").kendoWindow({
                width: "660px",
                height: "400px",
                title: "项目文号编辑",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Close"]
            }).data("kendoWindow").center().open();
        }

    }
})();
