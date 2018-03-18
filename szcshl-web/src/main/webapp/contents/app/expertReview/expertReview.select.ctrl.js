(function () {
    'use strict';

    angular.module('app').controller('expertSelectCtrl', expertReview);

    expertReview.$inject = [ 'expertReviewSvc', 'expertConditionSvc', 'expertSvc','$state','bsWin','$scope'];

    function expertReview(expertReviewSvc, expertConditionSvc,expertSvc, $state,bsWin,$scope) {
        var vm = this;
        vm.title = '选择专家';
        vm.conMaxIndex = 0;                   //条件号
        vm.customCondition = new Array();
        vm.expertReview = {};                 //评审方案对象
        vm.confirmEPList = [];                //拟聘请专家列表（已经经过确认的专家）
        vm.matchEPMap = {};                   //保存符合条件的专家信息
        vm.selectIds = [],                    //已经抽取的专家信息ID（用于排除查询）
        vm.autoSelectedEPList = [];           //抽取结果列表，抽取方法在后面封装
        vm.businessId = $state.params.businessId;       //专家评审方案业务ID
        vm.minBusinessId = $state.params.minBusinessId; //专家抽取方案业务ID
        vm.businessType = $state.params.businessType;   //专家业务类型
        var expertID = $state.params.expertID;   //专家ID
        vm.isSuperUser = isSuperUser;
        //S 查看专家详细
        vm.findExportDetail = function (id) {
            expertSvc.getExpertById(id, function (data) {
                vm.model = data;
                $("#selectExportDetail").kendoWindow({
                    width: "80%",
                    height: "auto",
                    title: "专家详细信息",
                    visible: false,
                    modal: true,
                    open:function(){
                        $("#expertPhotoSrc").attr("src", rootPath + "/expert/transportImg?expertId=" + vm.model.expertID + "&t=" + Math.random());
                    },
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            });
        }
        //S 查看专家详细

        //刷新已经选择的专家信息
        vm.reFleshSelEPInfo = function(explist) {
            $.each(explist,function(i, obj){

                vm.confirmEPList.push(obj);
                vm.selectIds.push(obj.expertDto.expertID);
            })
            vm.excludeIds = vm.selectIds.join(',');
        }

        //删除后刷新
        vm.reFleshAfterRemove = function(ids){
            $.each(ids,function(i, obj){
                //1、删除已确认的专家
                $.each(vm.confirmEPList,function(index, epObj){
                    if(epObj && obj == epObj.id){
                        vm.confirmEPList.splice(index, 1);
                    }
                })
            })
        }

        //更新参加未参加状态
        vm.reFleshJoinState = function(ids,state){
            $.each(ids,function(i, obj){
                //1、删除已确认的专家
                $.each(vm.confirmEPList,function(index, epObj){
                    if(obj == epObj.id){
                        epObj.isJoin = state;
                    }
                })
            })
        }

        //更新是否确认状态
        vm.reFleshConfirmState = function(ids,state){
            $.each(ids,function(i, obj){
                //1、删除已确认的专家
                $.each(vm.confirmEPList,function(index, epObj){
                    if(obj == epObj.id){
                        epObj.isConfrim = state;
                    }
                })
            })
        }

        //更新抽取条件的抽取次数
        vm.updateSelectedIndex = function(id){
            if(id){
                $.each(vm.expertReview.expertSelConditionDtoList,function(i,con){
                    if(con.id == id){
                        con.selectIndex = (!con.selectIndex)?1:con.selectIndex+1;
                    }
                })
            }else{
                $.each(vm.expertReview.expertSelConditionDtoList,function(i,con){
                    con.selectIndex = (!con.selectIndex)?1:con.selectIndex+1;
                })
            }
        }

        vm.init = function(businessId,minBusinessId){
            vm.expertReview = {};
            vm.confirmEPList = [];
            vm.selectIds = [];
            expertReviewSvc.initReview(businessId,minBusinessId,function(data){
                vm.expertReview = data;
                //获取已经抽取的专家
                if (!angular.isUndefined(vm.expertReview.expertSelectedDtoList) && angular.isArray(vm.expertReview.expertSelectedDtoList)) {
                    $.each(vm.expertReview.expertSelectedDtoList,function(i, sep){
                        vm.selectIds.push(sep.expertDto.expertID);
                        //判断是否有专业类别
                        if(sep.expertDto.expertTypeDtoList){
                            var expertTypeList=sep.expertDto.expertTypeDtoList;
                            var major="";//专业
                            var expertCategory=""//专业类别
                            for(var i=0;i<expertTypeList.length;i++){
                                if(i>0){
                                    major+="、"
                                }
                                major+=expertTypeList[i].maJorBig+"、"+expertTypeList[i].maJorSmall;
                                if(expertCategory!=expertTypeList[i].expertType){//如果专业类别一样。只显示一个就行了
                                    if(i>0){
                                        expertCategory+="、"
                                    }
                                    expertCategory+=expertTypeList[i].expertType;
                                }

                            }
                            sep.expertDto.major=major;//专业
                            sep.expertDto.expertCategory=expertCategory;//专业类别
                        }else{
                            sep.expertDto.major="";
                            sep.expertDto.expertCategory="";
                        }
                        vm.confirmEPList.push(sep);
                    })
                    if (vm.selectIds.length > 0) {
                        vm.excludeIds = vm.selectIds.join(',');
                    } else {
                        vm.excludeIds = '';
                    }
                }
            });
        }

        activate();
        function activate() {
            expertReviewSvc.initExpertGrid(vm);
            vm.init(vm.businessId,vm.minBusinessId);
        }

        //弹出自选专家框
        vm.showSelfExpertGrid = function () {
            vm.selfExpertOptions.dataSource._skip=0;
            vm.selfExpertOptions.dataSource.read();
            $("#selfExpertDiv").kendoWindow({
                width: "80%",
                height: "680px",
                title: "自选评审专家",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //保存自选的专家
        vm.saveSelfExpert = function () {
            var selectIds = common.getKendoCheckId('#selfExpertGrid');
            if (selectIds.length == 0) {
                bsWin.alert("请先选择专家！");
            } else if (selectIds.length > 1) {
                bsWin.alert("自选专家最多只能选择一个！");
            }else{
                expertReviewSvc.saveSelfExpert(vm.businessId,vm.minBusinessId,vm.businessType,selectIds[0].value,vm.expertReview.id,vm.isCommit,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        var ids = [];
                        $.each(vm.confirmEPList,function(i, obj){
                            if(obj.selectType == '2'){
                                ids.push(obj.id)
                            }
                        })
                        if(!vm.expertReview.id){
                            vm.expertReview.id = data.idCode;
                        }
                        //刷新
                        vm.reFleshSelEPInfo(data.reObj);
                        vm.reFleshAfterRemove(ids);
                        bsWin.success("操作成功！",function(){
                            window.parent.$("#selfExpertDiv").data("kendoWindow").close();
                        });
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }
        }

        //删除自选专家
        vm.delertSelfExpert = function () {
            var isCheck = $("input[name='seletedEp']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择要删除的专家");
            } else {
                bsWin.confirm({
                    title: "询问提示",
                    message: "删除数据不可恢复，确定删除么？",
                    onOk: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < isCheck.length; i++) {
                            ids.push(isCheck[i].value);
                        }
                        expertReviewSvc.delSelectedExpert(vm.expertReview.id, ids.join(','),vm.isCommit,function(data){
                            if(data.flag || data.reCode == 'ok'){
                                vm.reFleshAfterRemove(ids);
                                bsWin.success("操作成功！");
                            }else{
                                bsWin.error(data.reMsg);
                            }
                        });
                    },
                });
            }
        }

        //自选专家查询
        vm.querySelfExpert = function(){
            vm.selfExpertOptions.dataSource._skip=0;
            vm.selfExpertOptions.dataSource.read();
        }

        //境外专家
        vm.showOutExpertGrid = function () {
            vm.outExpertOptions.dataSource._skip=0;
            vm.outExpertOptions.dataSource.read();
            $("#outExpertDiv").kendoWindow({
                width: "70%",
                height: "680px",
                title: "自选新专家、市外、境外专家",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //删除选择的境外专家
        vm.delertOutSelfExpert = function () {
            var isCheck = $("input[name='seletedOutEp']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择要删除的专家");
            } else {
                bsWin.confirm({
                    title: "询问提示",
                    message: "删除数据不可恢复，确定删除么？",
                    onOk: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < isCheck.length; i++) {
                            ids.push(isCheck[i].value);
                        }
                        expertReviewSvc.delSelectedExpert(vm.expertReview.id, ids.join(','),vm.isCommit,function(data){
                            if(data.flag || data.reCode == 'ok'){
                                vm.reFleshAfterRemove(ids);
                                bsWin.success("操作成功！");
                            }else{
                                bsWin.error(data.reMsg);
                            }
                        });
                    },
                });
            }
        }

        //保存选择的境外专家
        vm.saveOutExpert = function () {
            var selectIds = common.getKendoCheckId('#outExpertGrid');
            if (selectIds.length == 0) {
                bsWin.alert("请先选择专家！");
            } else {
                var selExpertIdArr = [];
                $.each(selectIds, function (i, obj) {
                    selExpertIdArr.push(obj.value);
                });
                expertReviewSvc.saveOutExpert(vm.businessId,vm.minBusinessId,vm.businessType,selExpertIdArr.join(","), vm.expertReview.id, vm.isCommit, function (data) {
                    if(data.flag || data.reCode == 'ok'){
                        if(!vm.expertReview.id){
                            vm.expertReview.id = data.idCode;
                        }
                        vm.reFleshSelEPInfo(data.reObj);
                        bsWin.success("操作成功！",function(){
                            window.parent.$("#outExpertDiv").data("kendoWindow").close();
                        });
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }
        }

        //境外专家查询
        vm.queryOutExpert = function(){
            vm.outExpertOptions.dataSource._skip=0;
            vm.outExpertOptions.dataSource.read();
        }

        //计算符合条件的专家
        vm.countMatchExperts = function (id) {
            if (vm.expertReview.id) {
                var postData = {};
                vm.expertReview.expertSelConditionDtoList.forEach(function (t, number) {
                    if (t.id == id) {
                        postData = t;
                    }
                });
                expertReviewSvc.countMatchExperts(postData,vm.minBusinessId,vm.expertReview.id,function(data){
                    vm.matchEPMap[id] = data;
                    $("#expertCount" + id).html(data.length);
                });
            } else {
                bsWin.alert("请保存整体抽取方案再计算");
            }
        }

        //查看符合条件的专家信息
        vm.showMatchExperts = function(sortIndex){
            vm.matchExpertList = [];
            vm.matchExpertList = vm.matchEPMap[sortIndex];
            $("#matchExpertDiv").kendoWindow({
                width: "80%",
                height: "600px",
                title: "统计专家信息列表",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        vm.checkIntegerValue = function (checkValue, idStr, idSort) {
            if (expertConditionSvc.isUnsignedInteger(checkValue)) {
                $("#errorsOfficialNum" + idSort).html("");
                $("#errorsAlternativeNum" + idSort).html("");
                return checkValue;
            } else {
                $("#errorsOfficialNum" + idSort).html("只能填写数字");
                $("#errorsAlternativeNum" + idSort).html("只能填写数字");
                return ;
            }
        }

        //添加随机抽取条件
        vm.addCondition = function () {
            if (!vm.isSuperUser && (vm.expertReview.state == 9 || vm.expertReview.state == '9')) {
                bsWin.alert("当前项目已经进行整体专家方案的抽取，不能再修改方案！");
            } else {
                if(!vm.expertReview.expertSelConditionDtoList){
                    vm.expertReview.expertSelConditionDtoList = [];
                }
                vm.condition = {};
                vm.condition.id = common.uuid();            //设置ID
                vm.condition.businessId = vm.minBusinessId; //设置业务ID
                vm.condition.selectType = "1";              //选择类型，这个一定不能少
                vm.expertReview.expertSelConditionDtoList.push(vm.condition);
            }
        }

        //删除专家抽取条件
        vm.removeCondition = function () {
            if (!vm.isSuperUser && (vm.expertReview.state == 9 || vm.expertReview.state == '9')) {
                bsWin.alert("当前项目已经进行整体专家方案的抽取，不能再修改方案！");
            } else {
                var isCheck = $("#conditionTable input[name='epConditionSort']:checked");
                if (isCheck.length > 0) {
                    bsWin.confirm({
                        title: "询问提示",
                        message: "删除数据的同时会删除该条件所抽取的专家，删除数据不可恢复，确定删除么？",
                        onOk: function () {
                            var ids = [];
                            for (var i = 0; i < isCheck.length; i++) {
                                $.each(vm.expertReview.expertSelConditionDtoList,function(c,con){
                                    if (isCheck[i].value == con.id) {
                                        ids.push(con.id);
                                    }
                                })
                            }
                            if(ids.length > 0){
                                expertConditionSvc.deleteSelConditions(ids.join(","),vm.isCommit,function(data){
                                    if(data.flag || data.reCode == 'ok'){
                                        bsWin.success("操作成功！",function(){
                                            vm.init(vm.businessId,vm.minBusinessId);
                                        });
                                    }else{
                                        bsWin.error(data.reMsg);
                                    }
                                });
                            }else{
                                bsWin.success("操作成功！");
                            }
                        },
                    });
                }else{
                    bsWin.alert("请选择要删除的抽取条件！");
                }
            }
        }

        //检查是否为正整数
        function isUnsignedInteger(value) {
            if ((/^(\+|-)?\d+$/.test(value)) && value > 0) {
                return true;
            } else {
                return false;
            }
        }

        /******************************  以下是专家抽取方法 ***********************************/
        //封装专家抽取条件信息
        function buildCondition() {
            if (vm.expertReview.expertSelConditionDtoList.length > 0) {
                var validateResult = true;
                vm.expertReview.expertSelConditionDtoList.forEach(function (t, number) {
                    if (vm.expertReview.id) {
                        t.expertReviewDto = {};
                        t.expertReviewDto.id = vm.expertReview.id;   //抽取方案ID
                    }
                    if (!t.officialNum || !isUnsignedInteger(t.officialNum)) {
                        $("#errorsOfficialNum" + t.id).html("必填，且为数字");
                        validateResult = false;
                    }
                    if (!t.alternativeNum || !isUnsignedInteger(t.alternativeNum)) {
                        $("#errorsAlternativeNum" + t.id).html("必填，且为数字");
                        validateResult = false;
                    }
                    if (validateResult) {
                        $("#errorsOfficialNum" + t.id).html("");
                        $("#errorsAlternativeNum" + t.id).html("");
                    }
                });
                return validateResult;
            } else {
                return false;
            }
        }

        //保存专家抽取条件
        vm.saveCondition = function () {
            if (!vm.isSuperUser && (vm.expertReview.state == 9 || vm.expertReview.state == '9')) {
                bsWin.alert("当前项目已经进行整体专家方案的抽取，不能再修改方案！");
            }else {
                if (buildCondition()) {
                    expertConditionSvc.saveCondition(vm.businessId,vm.minBusinessId,vm.businessType,vm.expertReview.id,vm.expertReview.expertSelConditionDtoList,function(data){
                        if(data.flag || data.reCode == 'ok'){
                            vm.expertReview.expertSelConditionDtoList = data.reObj;
                            if(!vm.expertReview.id){
                                vm.expertReview.id = vm.expertReview.expertSelConditionDtoList[0].expertReviewId;
                            }
                            bsWin.success("保存成功！");
                        }else{
                            bsWin.error(data.reMsg);
                        }
                    });
                } else {
                    bsWin.alert("专家抽取条件设置不完整！");
                }
            }
        }

        //（整体方案抽取）开始随机抽取
        vm.startAutoExpertWin = function () {
            if(!vm.expertReview.id){
                bsWin.alert("请先进行整体专家抽取条件设置并保存！");
                return ;
            }
            if (!isSuperUser && (vm.expertReview.state == 9 || vm.expertReview.state == '9')) {
                bsWin.alert("当前项目已经进行整体专家方案的抽取，不能再修改方案！");
                return ;
            }
            if (buildCondition()) {
                expertConditionSvc.saveCondition(vm.businessId,vm.minBusinessId,vm.businessType,vm.expertReview.id,vm.expertReview.expertSelConditionDtoList,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        vm.expertReview.expertSelConditionDtoList = data.reObj;
                        if(!vm.expertReview.id){
                            vm.expertReview.id = vm.expertReview.expertSelConditionDtoList[0].expertReviewId;
                        }
                        expertReviewSvc.queryAutoExpert(vm.expertReview.expertSelConditionDtoList,vm.minBusinessId,vm.expertReview.id,function(data){
                            if(data.flag || data.reCode == 'ok'){
                                //刷新页面抽取的专家
                                vm.reFleshSelEPInfo(data.reObj.autoEPList);
                                //抽取结果数组
                                vm.autoSelectedEPList = [];
                                vm.autoSelectedEPList = data.reObj.autoEPList;
                                //刷新抽取次数
                                vm.expertReview.state = '9';
                                //弹框
                                vm.showAutoExpertWin();
                                //显示抽取效果
                                expertReviewSvc.validateAutoExpert(data.reObj.allEPList,vm);
                            }else{
                                bsWin.error(data.reMsg);
                            }
                        });
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            } else {
                bsWin.alert("专家抽取条件设置不完整！");
            }
        }

        //显示随机抽取框
        vm.showAutoExpertWin = function () {
            $("#aotuExpertDiv").kendoWindow({
                width: "90%",
                height: "700px",
                title: "专家抽取",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //显示随机抽取结果
        vm.showAutoMatchResultWin = function () {
            $("#aotuMatchResultDiv").kendoWindow({
                width: "90%",
                height: "700px",
                title: "专家抽取结果",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //再次抽取专家
        vm.repeatAutoExpert = function(id) {
            var condition = [];
            $.each(vm.expertReview.expertSelConditionDtoList,function(i,con){
                if(con.id == id){
                    condition.push(con);
                }
            })
            if(condition[0].selectIndex > 3){
                bsWin.alert("该条件已经进行了3次抽取，不能再继续抽取！");
                return ;
            }
            expertReviewSvc.queryAutoExpert(condition,vm.minBusinessId,vm.expertReview.id,function(data){
                if(data.flag || data.reCode == 'ok'){
                    //刷新页面抽取的专家
                    vm.reFleshSelEPInfo(data.reObj.autoEPList);
                    //抽取次数加一
                    vm.expertReview.state = '9';
                    //抽取结果数组
                    vm.autoSelectedEPList = [];
                    vm.autoSelectedEPList = data.reObj.autoEPList;
                    //刷新抽取次数
                    vm.updateSelectedIndex(id);
                    //弹框
                    vm.showAutoExpertWin();
                    //显示抽取效果
                    expertReviewSvc.validateAutoExpert(data.reObj.allEPList,vm);
                }else{
                    bsWin.error(data.reMsg);
                }
            });
        }

        //确认已抽取的专家
        vm.affirmAutoExpert = function () {
            var isCheck = $("#allAutoEPTable input[name='autoEPCheck']:checked");
            if(isCheck.length < 1){
                bsWin.confirm({
                    title: "询问提示",
                    message: "您还没选择专家，确定没有合适的专家么？",
                    onOk: function () {
                        $scope.$apply(function(){
                            //每个抽取条件的抽取次数加1
                            $.each(vm.expertReview.expertSelConditionDtoList, function (c, con) {
                                if(!con.selectIndex || con.selectIndex < 1){
                                    con.selectIndex = 1;
                                }else{
                                    con.selectIndex = con.selectIndex + 1;
                                }
                                con.showDraftBt = true;
                            })
                        });
                        window.parent.$("#aotuExpertDiv").data("kendoWindow").close();
                    }
                });
            }else{
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    ids.push(isCheck[i].value);
                }
                expertReviewSvc.affirmAutoExpert(vm.minBusinessId,vm.businessType,ids.join(","),'9',function(data){
                    if(data.flag || data.reCode=='ok'){
                        vm.reFleshConfirmState(ids,"9");
                        vm.checkAutoOfficeExpert = false;
                        vm.checkAutoAntiExpert = false;
                        bsWin.success(data.reMsg);
                    }else{
                        bsWin.error(data.reMsg);
                    }
                })
            }
        }

        //确定实际参加会议的专家
        vm.affirmJoinExpert = function () {
            $("#confirmJoinExpert").kendoWindow({
                width: "960px",
                height: "600px",
                title: "参加评审会专家确认",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //未参加改为参加
        vm.updateToJoin = function () {
            var isCheck = $("#notJoinExpertTable input[name='notJoinExpert']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择要改为参加会议的专家");
            } else {
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    ids.push(isCheck[i].value);
                }
                expertReviewSvc.updateJoinState(vm.minBusinessId,vm.businessType, ids.join(','), '9',vm.isCommit,function(data){
                    bsWin.success("操作成功！");
                    vm.reFleshJoinState(ids,'9');
                });
            }
        }

        //参加改为未参加
        vm.updateToNotJoin = function () {
            var isCheck = $("#joinExpertTable input[name='joinExpert']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择未参加会议的专家");
            } else {
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    ids.push(isCheck[i].value);
                }
                expertReviewSvc.updateJoinState(vm.minBusinessId,vm.businessType, ids.join(','), '0',vm.isCommit,function(data){
                    bsWin.success("操作成功！");
                    vm.reFleshJoinState(ids,'0');
                });
            }
        }

        //判断分值大小
        vm.checkScore = function(expertSelObj){
            if(expertSelObj.compositeScoreEnd){
                if(expertSelObj.compositeScore && (expertSelObj.compositeScore>expertSelObj.compositeScoreEnd)){
                    $("#compositeScoreEnd_"+expertSelObj.id).html("分值设置错误");
                    expertSelObj.compositeScoreEnd = "";
                }else{
                    $("#compositeScoreEnd_"+expertSelObj.id).html("");
                }
            }else{
                $("#compositeScoreEnd_"+expertSelObj.id).html("");
            }
        }

        //超级管理员删除抽取的专家
        vm.deleteAutoSelectEP = function(){
            var isCheck = $("#autoDraftExpertTable input[name='checkSelectExpert']:checked");
            if (isCheck.length > 0) {
                bsWin.confirm({
                    title: "询问提示",
                    message: "删除数据不可恢复，确定删除么？",
                    onOk: function () {
                        var ids = [];
                        for (var i = 0; i < isCheck.length; i++) {
                            ids.push(isCheck[i].value);
                        }
                        expertReviewSvc.delSelectedExpert(vm.expertReview.id,ids.join(","),vm.isCommit,function (data) {
                            if(data.flag || data.reCode == 'ok'){
                                bsWin.alert("删除成功！",function () {
                                    vm.reFleshAfterRemove(ids);
                                });
                            }else{
                                bsWin.alert(data.reMsg);
                            }
                        })
                    }
                });
            }else{
                bsWin.alert("请选择要删除的抽取专家！");
            }
        }

        // 交换数组元素
        var swapItems = function(arr, index1, index2) {
            arr[index1] = arr.splice(index2, 1, arr[index1])[0];
            return arr;
        };

        // 上移
        vm.upRecord = function(arr, $index) {
            if($index == 0) {
                return;
            }
            swapItems(arr, $index, $index - 1);
        };

        // 下移
        vm.downRecord = function(arr, $index) {
            if($index == arr.length -1) {
                return;
            }
            swapItems(arr, $index, $index + 1);
        };

        vm.saveExpert=function () {
            console.log(vm.confirmEPList);
            for(var i=0;i<vm.confirmEPList.length;i++){
                if(vm.confirmEPList[i].isConfrim=="9" && vm.confirmEPList[i].isJoin=="9"){//确认参加的
                    vm.confirmEPList[i].expertDto.expertNewTypeDtoList=vm.confirmEPList[i].expertDto.expertTypeDtoList;//专家专业
                    vm.confirmEPList[i].expertDto.businessId=vm.confirmEPList[i].businessId;//工作方案的id
                     expertReviewSvc.saveNewExpert(vm.confirmEPList[i].expertDto,function (data) {

                     })
                }


            }


        }

    }
})();
