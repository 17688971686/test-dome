(function () {
    'use strict';

    angular.module('app').controller('topicAddCtrl', topicCtrl);

    topicCtrl.$inject = ['bsWin', '$scope', 'sysfileSvc', 'topicSvc','$state'];

    function topicCtrl(bsWin, $scope, sysfileSvc, topicSvc,$state) {
        var vm = this;
        vm.title = '新增课题研究';
        vm.model = {};
        vm.model.fgwlx = 0;             //默认不是发改委立项
        vm.model.sendFgw = 0;           //默认不报发改委审批
        vm.conMaxIndex = 0;                   //条件号
        vm.conditions = [];         //条件列表
        vm.isCreate = false;      //是否已经创建课题
        vm.endTopic = false;      //结题标志
        if($state.params.id){
            vm.model.id = $state.params.id;
        }
        if($state.params.curNodeId){
            vm.curNodeId = $state.params.curNodeId;
            if(vm.curNodeId == 'TOPIC_KTFZR_QR'||vm.curNodeId == 'TOPIC_ZLGD'){
                vm.endTopic = true;
            }

        }

        if(vm.model.id){
            vm.isUpdate = true;
        }else{
            vm.isUpdate = false;
        }

        //初始化附件上传控件
        vm.initFileUpload = function(){
            if(!vm.model.id){
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.model.id",function (newValue, oldValue) {
                    if(newValue && newValue != oldValue && !vm.initUploadOptionSuccess){
                        vm.initFileUpload();
                    }
                });
            }
            //创建附件对象
            vm.sysFile = {
                businessId : vm.model.id,
                mainId : vm.model.id,
                mainType : sysfileSvc.mainTypeValue().TOPIC,
                sysfileType:sysfileSvc.mainTypeValue().TOPIC_PLAN,
                sysBusiType:sysfileSvc.mainTypeValue().TOPIC_PLAN,
                showBusiType : false,
            };
            sysfileSvc.initUploadOptions({
                inputId:"sysfileinput",
                vm:vm,
            });
        }
        activate();
        function activate() {
            if(vm.model.id){
                topicSvc.initDetail(vm.model.id,function(data){
                    vm.model = data;
                    if(vm.model.contractDtoList != undefined){
                        vm.conditions = vm.model.contractDtoList;
                        for(var i=0;i<vm.conditions.length;i++){
                            vm.conditions[i]["sort"]= (i+1);
                        }
                    }
                });
            }
            topicSvc.findOrgUser(function(data){
                vm.principalUsers = data;
            });
            //初始化上传附件
            vm.initFileUpload();
        }

        //检查项目负责人
        vm.checkPrincipal = function(){
            var selUserId = $("#mainPrinUserId").val();
            if(selUserId){
                $('#principalUser_ul input[selectType="assistUser"]').each(
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

        //保存
        vm.create = function(){
            common.initJqValidation($('#topicform'));
            var isValid = $('#topicform').valid();
            if (isValid) {
                var selUser = []
                $('#principalUser_ul input[selectType="assistUser"]:checked').each(function () {
                    selUser.push($(this).attr("value"));
                });
                vm.model.prinUserIds = selUser.join(",");
                topicSvc.createTopic(vm.model,vm.isCommit, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        vm.model = data.reObj;
                        vm.isCreate  = true;
                        bsWin.alert("操作成功！");
                    } else {
                        bsWin.alert(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("页面未填报完整或者为正确，请检查！");
            }
        }


        //保存结题方式信息
        vm.saveEndTopic = function(){
            common.initJqValidation($('#endTopicform'));
            var isValid = $('#endTopicform').valid();
            if (isValid) {
                topicSvc.updateTopic(vm.model,vm.isCommit, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        vm.model = data.reObj;
                        vm.isCreate  = true;
                        bsWin.alert("操作成功！");
                    } else {
                        bsWin.alert(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("页面未填报完整或者为正确，请检查！");
            }
        }


        //发起流程
        vm.startFlow = function(){
            common.initJqValidation($('#topicform'));
            var isValid = $('#topicform').valid();
            if (isValid) {
                bsWin.confirm({
                    title: "询问提示",
                    message: "发起流程后，当前页面数据将不能再修改！确认发起流程么？",
                    onOk: function () {
                        var selUser = []
                        $('#principalUser_ul input[selectType="assistUser"]:checked').each(function () {
                            selUser.push($(this).attr("value"));
                        });
                        vm.model.prinUserIds = selUser.join(",");
                        topicSvc.startFlow(vm.model,vm.isCommit,function(data){
                            if(data.flag || data.reCode == 'ok'){
                                bsWin.alert("保存成功！",function(){
                                    $state.go('myTopic');
                                });
                            }else{
                                bsWin.alert(data.reMsg);
                            }
                        });
                    }
                });
            }else{
                bsWin.alert("页面未填报完整或者为正确，请检查！");
            }
        }

        //添加合同信息
        vm.addContract = function () {
            if(vm.isCreate || vm.model.id){
                vm.condition = {};
                if(!vm.isUpdate){
                    vm.condition.sort = vm.conMaxIndex+1;
                }else{
                    vm.conMaxIndex = vm.conditions.length;
                    vm.condition.sort = vm.conditions.length+1;
                }
                vm.conditions.push(vm.condition);
                vm.conMaxIndex++;
            }else{
                bsWin.alert("请先保存课题，再添加合同信息！");
            }
        }

        //保存合同信息
        vm.saveContract = function () {
            if (buildCondition()) {
                var temp1 = vm.model.mainPrinUserId;
                var temp2 = vm.model.prinUserIds;
                topicSvc.saveContractDetailList(vm.conditions,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        vm.model = data.reObj;
                        vm.model.mainPrinUserId = temp1;
                        vm.model.prinUserIds = temp2;
                        bsWin.success("保存成功！");
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }
        }

        //删除合同信息
        vm.removeContract = function () {
            var isCheck = $("#contractTable input[name='contractInfo']:checked");
            if (isCheck.length > 0) {
                bsWin.confirm({
                    title: "询问提示",
                    message: "删除数据不可恢复，确定删除么？",
                    onOk: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < isCheck.length; i++) {
                            for(var k = 0; k < vm.conditions.length; k++){
                                var con = vm.conditions[k];
                                if (isCheck[i].value == con.sort) {
                                    if (con.contractId) {
                                        ids.push(con.contractId);
                                    }else{
                                        vm.conditions.splice(k, 1);
                                        $("#conTr"+con.sort).remove();
                                        break;
                                    }
                                }
                            }
                        }
                        if(ids.length > 0){
                            topicSvc.deleteContractConditions(ids.join(","),function(data){
                                if(data.flag || data.reCode == 'ok'){
                                    bsWin.success("操作成功！");
                                    for(var i = 0; i < ids.length; i++ ){
                                        for(var k = 0; k < vm.conditions.length; k++){
                                            var con = vm.conditions[k];
                                            if (ids[i] == con.contractId) {
                                                vm.conditions.splice(k, 1);
                                                $("#conTr"+con.sort).remove();
                                                break;
                                            }
                                        }
                                    }
                                }else{
                                    bsWin.error(data.reMsg);
                                }
                            });
                        }
                    },
                });
            }else{
                bsWin.alert("请选择要删除的合同信息！");
            }

        }


        function buildCondition() {
            common.initJqValidation($('#contractform'));
            var isValid = $('#contractform').valid();
            if (isValid) {
                if (vm.conditions.length > 0) {
                    var validateResult = true;
                    vm.conditions.forEach(function (p, number) {
                        p.cooperator = $("#cooperator" + p.sort).val();
                        p.entrustValue = $("#entrustValue" + p.sort).val();
                        p.purchaseType = $("#purchaseType" + p.sort).val();
                        p.contractPerson = $("#contractPerson" + p.sort).val();
                        p.contractTel = $("#contractTel" + p.sort).val();
                        p.topicId= vm.model.id;
                        if(p.cooperator=="" || p.cooperator==="undefined"){
                            bsWin.error("合作单位不能为空！");
                            validateResult = false;
                            return validateResult;
                        }
                        if(p.purchaseType=="" || p.purchaseType==="undefined"){
                            bsWin.error("采购方式不能为空！");
                            validateResult = false;
                            return validateResult;
                        }
                        var pc = /^(-)?(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,4})?$/;
                        if(!pc.test(p.entrustValue)){
                            bsWin.error("委托金额只能输入数字请核查！");
                            validateResult = false;
                            return validateResult;
                        }
                    });
                    return validateResult;
                } else {
                    bsWin.error("没有分录数据，无法保存！");
                    return false;
                }
            }else{
                bsWin.alert("页面未填报完整或者为正确，请检查！");
                return false;
            }
        }
        //校验金额
        vm.checkPrice = function(sort){
            var pc = /^(-)?(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,4})?$/;    //保留4个小数点
            var priceId = "entrustValue"+sort;
            if(!pc.test( $("#"+priceId).val())){
                $("#"+priceId).val("");
                $("span[data-valmsg-for='"+priceId+"']").html("价格只能输入数字！");
                return ;
            }
            $("span[data-valmsg-for='"+priceId+"']").html("");
        }
    }
})();
