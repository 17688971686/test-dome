(function () {
    'use strict';

    angular.module('app').controller('signFlowDealCtrl', sign);

    sign.$inject = ['sysfileSvc', 'signSvc', '$state', 'flowSvc', 'signFlowSvc',
        '$http'];

    function sign(sysfileSvc, signSvc, $state, flowSvc, signFlowSvc, $http) {
        var vm = this;
        vm.title = "项目流程处理";
        vm.model = {};          //收文对象
        vm.flow = {};           //流程对象
        vm.work = {};           //工作方案
        vm.dispatchDoc = {};    //发文
        vm.fileRecord = {};     //归档
        vm.expertReview = {};   //评审方案

        //按钮显示控制，全部归为这个对象控制
        vm.showFlag = {
            businessTr:false,          //显示业务办理tr
            businessDis:false,         //显示直接发文复选框
            businessNext:false,        //显示下一环节处理人或者部门

            nodeNext : true,           //下一环节名称
            nodeSelViceMgr:false,      // 选择分管副主任环节
            nodeSelOrgs:false,         // 选择分管部门
            nodeSelPrincipal:false,    // 选择项目负责人
            nodeWorkProgram:false,     // 工作方案
            nodeDispatch:false,        // 发文
            nodeFileRecord:false,      // 归档
            nodeSelXSOrg:false,        // 协审选择分管部门
            nodeSelXSPri:false,        // 选择负责人
            nodeXSWorkProgram:false,   // 协审工作方案

            tabWorkProgram:false,       // 显示工作方案标签tab
            tabBaseWP:false,            // 项目基本信息tab
            tabDispatch:false,          // 发文信息tab
            tabFilerecord:false,        // 归档信息tab
            tabExpert:false,            // 专家信息tab
            tabSysFile:false,           // 附件信息tab

            buttBack:false,             // 回退按钮
            expertRemark:false,         // 专家评分弹窗内容显示
            expertpayment:false,        // 专家费用弹窗内容显示
            expertEdit:true,            // 专家评分费用编辑权限
        };

        //业务控制对象
        vm.businessFlag = {
            isLoadSign : false,         // 是否加载收文信息
            isLoadFlow : false,         // 是否加载流程信息
            isGotoDis : false,          // 是否直接发文
            isMakeDisNum : false,       // 是否生成发文编号
            isHaveSePri : false,        // 是否有第二负责人
            principalUsers : [],         // 部门负责人列表
            isSelMainPriUser:false,     // 是否已经设置主要负责人
            expertReviews : [],         // 专家评审方案
            editExpertSC : false,      // 编辑专家评审费和评分,只有专家评审方案环节才能编辑
            expertScore:{},             // 专家评分对象
        }

        vm.model.signid = $state.params.signid;
        vm.flow.taskId = $state.params.taskId; // 流程任务ID
        vm.flow.processInstanceId = $state.params.processInstanceId; // 流程实例ID

        active();
        function active() {
            $('#myTab li').click(function (e) {
                var aObj = $("a", this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#" + showDiv).addClass("active").addClass("in").show(500);
            })
            // 初始化业务信息
            signSvc.initFlowPageData(vm);
            // 初始化流程数据
            flowSvc.getFlowInfo(vm);
            // 初始化办理信息
            flowSvc.initFlowData(vm);
            // 初始化上传附件
            signSvc.uploadFilelist(vm);
            //项目关联初始化
            signSvc.associateGrid(vm);
        }

        //初始化业务参数
        vm.initBusinessParams = function(){
            if(vm.businessFlag.isLoadSign && vm.businessFlag.isLoadFlow){
                signFlowSvc.initBusinessParams(vm);
            }
        }

        //检查项目负责人
        vm.checkPrincipal = function(){
            var selUserId = $("#selPrincipalMainUser").val();
            if(selUserId){
                $('#principalAssistUser input[selectType="assistUser"]').each(
                    function () {
                        var value = $(this).attr("value");
                        if (value == selUserId) {
                            $(this).removeAttr("checked");
                            $(this).attr("disabled", "disabled");
                        } else {
                            $(this).removeAttr("disabled");
                        }
                    }
                );
            }
        }

        // 编辑专家评分
        vm.editSelectExpert = function (id) {
            vm.businessFlag.expertScore = {};
            vm.businessFlag.expertReviews.forEach(function(epRw,index){
               epRw.expertSelectedDtoList.forEach(function(epSel,i){
                   if(epSel.id == id){
                       vm.businessFlag.expertScore = epSel;
                       return ;
                   }
               })
            });
            $("#star").raty({
                score: function () {
                    $(this).attr("data-num", angular.isUndefined(vm.businessFlag.expertScore.score)?0:vm.businessFlag.expertScore.score);
                    return $(this).attr("data-num");
                },
                starOn: '../contents/libs/raty/lib/images/star-on.png',
                starOff: '../contents/libs/raty/lib/images/star-off.png',
                starHalf: '../contents/libs/raty/lib/images/star-half.png',
                readOnly: false,
                halfShow: true,
                size: 34,
                click: function (score, evt) {
                    vm.businessFlag.expertScore.score = score;
                }
            });

            $("#expertmark").kendoWindow({
                width: "820px",
                height: "365px",
                title: "编辑-专家星级",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }
        // 关闭专家评分
        vm.closeEditMark = function () {
            window.parent.$("#expertmark").data("kendoWindow").close();
        }

        // 保存专家评分
        vm.saveMark = function () {
            flowSvc.saveMark(vm,function(){

            });
        }
        //获取专家评星
        vm.getExpertStar = function(id ,score){
            var returnStr = "";
            if (score != undefined) {
                for (var i = 0; i <score; i++) {
                    returnStr += "<span style='color:gold;font-size:20px;'><i class='fa fa-star' aria-hidden='true'></i></span>";
                }
            }
            $("#"+id+"_starhtml").html(returnStr);
        }

        vm.editpayment = function (id) {
            vm.expertReview.expertId = id;
            flowSvc.gotopayment(vm);
        }

        // 计算应纳税额
        vm.countTaxes = function (expertReview) {
            if(expertReview == undefined){
                return ;
            }
            if(expertReview.payDate == undefined){
                expertReview.errorMsg = "请选择发放日期";
                return ;
            }
            var reg = /^(\d{4}-\d{1,2}-\d{1,2})$/;
            if(!reg.exec(expertReview.payDate)){
                 expertReview.errorMsg = "请输入正确的日期格式";
                 return ;
            }
            expertReview.errorMsg = "";
            common.initJqValidation($('#payform'));
            var isValid = $('#payform').valid();
            if(isValid){

                 flowSvc.countTaxes(vm,expertReview);
            }


        }
        // 关闭专家费用
        vm.closeEditPay = function () {
            window.parent.$("#payment").data("kendoWindow").close();
        }
        // 保存专家费用
        vm.savePayment = function () {
            flowSvc.savePayment(vm,function(){
                 signSvc.paymentGrid(vm);
            });
        }
        // begin 添加审批意见
        vm.ideaEdit = function (options) {
            common.initIdeaData(vm, $http, options);
        }
        // end 添加审批意见

        //流程提交
        vm.commitNextStep = function () {
            if (signFlowSvc.checkBusinessFill(vm)) {
                flowSvc.commit(vm);
            } else {
                common.alert({
                    vm: vm,
                    msg: "请先完成相应的业务操作才能提交"
                })
            }
        }

        vm.commitBack = function () {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认回退吗？",
                fn: function () {
                    flowSvc.rollBack(vm); // 回退到上一个环节
                }
            })
        }

        vm.deleteFlow = function () {
            common.confirm({
                vm: vm,
                title: "",
                msg: "终止流程将无法恢复，确认挂起么？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    flowSvc.deleteFlow(vm);
                }
            })
        }

        vm.initDealUerByAcitiviId = function () {
            flowSvc.initDealUerByAcitiviId(vm);
        }

        // 根据特定的环节隐藏相应的业务按钮
        vm.showBtByAcivitiId = function (acivitiId) {
            return vm.flow.curNodeAcivitiId == acivitiId ? true : false;
        }

        // S_跳转到 工作方案 编辑页面
        vm.addWorkProgram = function () {
            $state.go('workprogramEdit', {signid: vm.model.signid });
        }// E_跳转到 工作方案 编辑页面

        //S_跳转到 工作方案 基本信息
        vm.addBaseWP = function(){
            $state.go('workprogramBaseEdit', {signid: vm.model.signid });
        }

        // S_跳转到 发文 编辑页面
        vm.addDisPatch = function () {
            $state.go('dispatchEdit', {
                signid: vm.model.signid
            });
        }// E_跳转到 发文 编辑页面

        vm.addDoFile = function () {
            $state.go('fileRecordEdit', {
                signid: vm.model.signid
            });
        }

        // 业务判断
        vm.checkBox = function ($event, type, disabletype) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            var checkboxValue = checkbox.value;
            if (checked) {
                $('.seleteTable input[selectType=\"' + type + '\"]').each(
                    function () {
                        var id = $(this).attr("id");
                        var value = $(this).attr("value");
                        if (id != (type + "_" + checkboxValue)) {
                            $("#" + disabletype + "_" + value)
                                .removeAttr("disabled");
                            $(this).removeAttr("checked");
                        } else {
                            $("#" + disabletype + "_" + checkboxValue)
                                .attr("disabled", "disabled");
                        }
                    });
            } else {
                $("#" + disabletype + "_" + checkboxValue)
                    .removeAttr("disabled");
            }
        }

        // checkbox 单选
        vm.checkBoxSingle = function ($event, type) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            var checkboxValue = checkbox.value;
            if (checked) {
                $('.seleteTable input[selectType=\"' + type + '\"]').each(function () {
                    var id = $(this).attr("id");
                    var value = $(this).attr("value");
                    if (id != (type + "_" + checkboxValue)) {
                        $("#" + disabletype + "_" + value).removeAttr("disabled");
                        $(this).removeAttr("checked");
                    } else {
                        $("#" + disabletype + "_" + checkboxValue).attr("disabled", "disabled");
                    }
                });
            } else {
                $("#" + disabletype + "_" + checkboxValue).removeAttr("disabled");
            }
        }

        //checkbox 单选
        vm.checkBoxSingle = function ($event, type) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            var checkboxValue = checkbox.value;
            if (checked) {
                $('#xs_table input[selectType=\"' + type + '\"]').each(function () {
                    var id = $(this).attr("id");
                    var value = $(this).attr("value");
                    if (value != checkboxValue) {
                        $(this).removeAttr("checked");
                    }
                });
            }
        }

        //项目关联弹窗
        vm.showAssociate = function(){
            vm.currentAssociateSign = vm.model;

            //选中要关联的项目
            var signAssociateWindow = $("#associateWindow");
            signAssociateWindow.kendoWindow({
                width: "60%",
                height: "625px",
                title: "项目关联",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "close"]
            }).data("kendoWindow").center().open();
            //初始化associateGrid
        }

        //start 保存项目关联
        vm.saveAssociateSign = function(associateSignId){
            if(vm.model.signid == associateSignId){
                common.alert({
                    vm:vm,
                    msg:"不能关联自身项目",
                    closeDialog:true,
                    fn:function() {
                    }
                });
                return ;
            }
            signSvc.saveAssociateSign(vm,vm.model.signid,associateSignId,function(){
                //回调
                $state.reload();
            });
        }
        //end 保存项目关联

        //start 查询功能
        vm.associateQuerySign = function(){
            vm.associateGridOptions.dataSource.read();
        }//end 查询功能

        //start 解除项目关联
        vm.disAssociateSign = function(){
            signSvc.saveAssociateSign(vm,vm.model.signid,null,function(){
                 //回调
                 $state.reload();
            });
        }
        //end 解除项目关联
        
        //选择负责人
        vm.addPriUser = function () {
            var isCheck = $("#xs_bmfb input[name='unSelPriUser']:checked");
            if (isCheck.length < 1) {
                common.alert({
                    vm: vm,
                    msg: "请选择负责人"
                })
            }else{
                if(vm.isMainPriUser == 9 && isCheck.length > 1){
                    common.alert({
                        vm: vm,
                        msg: "总负责人只能选一个"
                    })
                    return ;
                }
                if(vm.businessFlag.isSelMainPriUser == false && (angular.isUndefined(vm.isMainPriUser) || vm.isMainPriUser == 0)){
                    common.alert({
                        vm: vm,
                        msg: "请先选择总负责人！"
                    })
                    return ;
                }
                if(vm.businessFlag.isSelMainPriUser == true && vm.isMainPriUser == 9){
                    common.alert({
                        vm: vm,
                        msg: "你已经选择了一个总负责人，不能再次选择负责人"
                    })
                    return ;
                }
                if(vm.businessFlag.principalUsers && (vm.businessFlag.principalUsers.length + isCheck.length) > 3){
                    common.alert({
                        vm: vm,
                        msg: "最多只能选择3个负责人，请重新选择！"
                    })
                    return ;
                }

                for (var i = 0; i < isCheck.length; i++) {
                    var priUser = {};
                    priUser.userId = isCheck[i].value;
                    priUser.userType = $("#userType").val();
                    if(vm.isMainPriUser == 9){
                        vm.businessFlag.isSelMainPriUser = true;
                        priUser.isMainUser = 9;
                        vm.isMainPriUser == 0;
                    }else{
                        priUser.isMainUser = 0;
                    }
                    vm.xsusers.forEach(function(u,index){
                       if(u.id == isCheck[i].value){
                           u.isSelected = true;
                           priUser.userName = u.loginName;
                       }
                    });
                    vm.businessFlag.principalUsers.push(priUser);
                }
            }

        }

        //删除负责人
        vm.delPriUser = function () {
            var isCheck = $("#xs_bmfb input[name='selPriUser']:checked");
            if (isCheck.length < 1) {
                common.alert({
                    vm: vm,
                    msg: "请选择取消的负责人"
                })
            }else{
                for (var i = 0; i < isCheck.length; i++) {
                    vm.xsusers.forEach(function(u,index){
                        if(u.id == isCheck[i].value){
                            u.isSelected = false;
                        }
                    });
                    vm.businessFlag.principalUsers.forEach(function(pu,index){
                        if(pu.userId == isCheck[i].value){
                            if(pu.isMainUser == 9){
                                vm.businessFlag.isSelMainPriUser = false;
                            }
                            vm.businessFlag.principalUsers.splice(index,1);
                        }
                    });
                }
            }
        }//E_删除负责人

        //直接发文判断
        vm.checkNeedWP = function($event){
            var checkbox = $event.target;
            var checked = checkbox.checked;
            if(checked){
                vm.showFlag.nodeWorkProgram = false; //显示工作方案和会签准备材料按钮
                //如果有发文信息，询问是否删除
                if(vm.mainwork && vm.mainwork.id){
                    common.confirm({
                        vm:vm,
                        title:"",
                        msg:"取消会对填报的工作方案进行删除，确认删除么？",
                        fn:function () {
                            $('.confirmDialog').modal('hide');
                            signSvc.removeWP(vm);
                        },
                        cancel:function(){
                            checkbox.checked = !checked;
                            $('.confirmDialog').modal('hide');
                        }
                    })
                }
            }else{
                vm.showFlag.nodeWorkProgram = true; //显示工作方案和会签准备材料按钮
            }
        }

        //生产会前准备材料
        vm.meetingDoc = function(){
            common.confirm({
                vm:vm,
                title:"",
                msg:"如果之前已经生成会前准备材料，则本次生成的文档会覆盖之前产生的文档，确定执行操作么？",
                fn:function () {
                    $('.confirmDialog').modal('hide');
                    signSvc.meetingDoc(vm);
                }
            })
        }

        //附件下载
        vm.commonDownloadSysFile = function(sysFileId){
            sysfileSvc.commonDownloadFile(vm,sysFileId);
        }

    }
})();
