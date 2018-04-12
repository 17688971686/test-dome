(function () {
    'dispatch strict';

    angular.module('app').controller('dispatchEditCtrl', dispatch);

    dispatch.$inject = [ 'dispatchSvc','sysfileSvc', '$state', 'bsWin','$scope','signSvc' , 'templatePrintSvc'];

    function dispatch(dispatchSvc,sysfileSvc, $state, bsWin,$scope,signSvc , templatePrintSvc) {
        var vm = this;
        vm.title = '项目发文编辑';
        vm.sign = {};
        vm.searchSign = {};        //发文查询对象
        vm.dispatchDoc = {};       //发文对象
        vm.dispatchDoc.signId = $state.params.signid;
        vm.isControl=$state.params.isControl;//按钮控制
        vm.dispatchDoc.id = "";

        vm.showFlag = {
            buttSysFile : false,        //显示附件按钮
            isReveiwDS : false,         //是否合并发文次项目
        }
        vm.busiFlag = {
            isMerge : false,            //是否合并发文
            isMain : false,             //是否合并发文主项目
        }

        activate();
        function activate() {
            dispatchSvc.initDispatchData(vm);
        }
        //监听是否关联按钮
        vm.watchIsRelated = function(){
            //监听是否关联事件
            $scope.$watch("vm.dispatchDoc.isRelated",function (newValue, oldValue) {
                //由关联改成未关联
                if((newValue == 0 || newValue == '0')&& (oldValue == 9 || oldValue == '9') && (vm.sign.isAssociate == 1)){
                    bsWin.confirm({
                        title: "询问提示",
                        message: "该项目已经进行了关联，您要解除关联么？",
                        onOk: function () {
                            vm.isdik=true;//控制勾选框的勾选
                            $('.confirmDialog').modal('hide');
                            signSvc.saveAssociateSign($state.params.signid,null,function(){
                                bsWin.alert("项目解除关联成功");
                                /*window.location.reload();*/
                            });
                        },
                        onCancel : function () {
                            $scope.$apply(function(){//因为在窗口页面来改数据。数据不会作用到页面。所以需要重新加载
                                vm.dispatchDoc.isRelated = 9;
                            });
                        },
                        onClose : function () {
                            if(vm.isdik){//窗口关闭事件。判断。如果是点确定的关闭那状态就要改变
                                $scope.$apply(function(){
                                    vm.dispatchDoc.isRelated = 0;
                                });
                            }else{
                                $scope.$apply(function(){//因为在窗口页面来改数据。数据不会作用到页面。所以需要重新加载
                                    vm.dispatchDoc.isRelated = 9;
                                });
                            }
                        }
                    });
                }else if((oldValue == 0 || oldValue == '0')&& (newValue == 9 || newValue == '9') && (!vm.sign.isAssociate || vm.sign.isAssociate == 0)){
                  //其它、设备、进口阶段不能进行关联
                    if(signcommon.getReviewStage().OTHERS == vm.dispatchDoc.dispatchStage
                    || signcommon.getReviewStage().DEVICE_BILL_HOMELAND == vm.dispatchDoc.dispatchStage
                    || signcommon.getReviewStage().DEVICE_BILL_IMPORT == vm.dispatchDoc.dispatchStage
                    || signcommon.getReviewStage().IMPORT_DEVICE == vm.dispatchDoc.dispatchStage){
                        bsWin.alert("该阶段不能进行关联！");
                        vm.dispatchDoc.isRelated = 0;
                    }else{
                        bsWin.confirm({
                            title: "询问提示",
                            message: "您要进行项目关联么？",
                            onOk: function () {
                                if(!vm.ss){
                                    vm.page=lgx.page.init({id: "demo5",get:function(o){
                                        //根据项目名称，查询要关联阶段的项目
                                        if (!vm.price) {
                                            vm.price = {
                                                signid: vm.sign.signid,
                                                projectname: vm.sign.projectname,
                                            };
                                        }
                                        vm.price.reviewstage = vm.sign.reviewstage; //设置评审阶段
                                        var skip;
                                        //oracle的分页不一样。
                                        if(o.skip!=0){
                                            skip=o.skip+1
                                        }else{
                                            skip=o.skip
                                        }
                                        vm.price.skip=skip;//页码
                                        vm.price.size=o.size+o.skip;//页数
                                        signSvc.getAssociateSignGrid(vm, function (data) {
                                            vm.associateSignList =[];
                                            if (data) {
                                                vm.associateSignList = data.value;
                                                vm.page.callback(data.count);//请求回调时传入总记录数
                                            }
                                            vm.ss=true;
                                        });

                                        //alert("当前页："+o.number+"，从数据库的位置1"+o.skip+"起，查"+o.size+"条数据");
                                        //需在这里发起ajax请求查询数据，请求成功后需调用callback方法重新计算分页

                                    }});

                                }else{
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

                            },
                            onCancel : function () {
                                console.log(1);
                                $scope.$apply(function(){
                                    vm.dispatchDoc.isRelated = 0;
                                });


                            },
                            onClose : function () {
                                if(vm.ss){
                                    $scope.$apply(function(){
                                        vm.dispatchDoc.isRelated = 9;
                                    });
                                }else{
                                    $scope.$apply(function(){//因为在窗口页面来改数据。数据不会作用到页面。所以需要重新加载
                                        vm.dispatchDoc.isRelated = 0;
                                    });
                                }

                            }
                        });
                    }


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
        vm.saveAssociateSign = function(associateSignId){
            if(vm.sign.signid == associateSignId){
                bsWin.alert("不能关联自身项目");
                return ;
            }
            //保存成功之后，返回关联的项目信息
            signSvc.saveAssociateSign(vm.sign.signid,associateSignId,function(data){
                if(data.flag || data.reCode == 'ok'){
                    if(associateSignId){
                        vm.sign.isAssociate = 1;
                        vm.associateDispatchs = data.reObj;
                    }else{
                        vm.associateDispatchs = []; //解除关联也要重新设置值
                    }
                    bsWin.alert(associateSignId != undefined ? "项目关联成功" : "项目解除关联成功",function(){
                        window.parent.$("#associateWindow").data("kendoWindow").close();
                    });
                }else{
                    bsWin.alert(data.reMsg);
                }

            });
        }

        //初始化附件上传控件
        vm.initFileUpload = function(){
            if(!vm.dispatchDoc.id){
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.dispatchDoc.id",function (newValue, oldValue) {
                    if(newValue && newValue != oldValue && !vm.initUploadOptionSuccess){
                        vm.initFileUpload();
                    }
                });
            }
            vm.sysFile = {
                businessId : vm.dispatchDoc.id,
                mainId : vm.dispatchDoc.signId,
                mainType : sysfileSvc.mainTypeValue().SIGN,
                sysfileType:sysfileSvc.mainTypeValue().DISPATCH,
                sysBusiType:sysfileSvc.mainTypeValue().DISPATCH,
            };
            sysfileSvc.initUploadOptions({
                inputId:"sysfileinput",
                vm:vm
            });
        }

        //发文方式改变事件
        vm.sigleProject = function () {
            //1、由合并发文主项目改为单个发文
            if(vm.dispatchDoc.dispatchWay == "1" ){
                if(vm.busiFlag.isMerge && vm.busiFlag.isMain){
                    bsWin.confirm({
                        title: "询问提示",
                        message: "该项目已经设为合并发文，并且已经有关联项目，如果现在要取消合并发文，以前的关联信息将被删除，您确定要取消合并发文么?",
                        onOk: function () {
                            dispatchSvc.deleteAllMerge(vm.dispatchDoc.signId,function (data) {
                                if (data.flag || data.reCode == "ok") {
                                    vm.dispatchDoc.isMainProject = "0";
                                    vm.busiFlag.isMerge=false;
                                    vm.busiFlag.isMain=false;
                                }
                                bsWin.alert(data.reMsg);
                            });
                        },
                        onClose : function(){
                        },
                        onCancel : function () {
                            vm.dispatchDoc.dispatchWay = "2";
                            $('.confirmDialog').modal('hide');
                        }
                    });
                }
                //2、由单个发文改为合并发文
            }else if(vm.dispatchDoc.dispatchWay == "2" ){
                if(!vm.busiFlag.isMerge){
                    vm.busiFlag.isMerge=true;
                    vm.busiFlag.isMain=(vm.dispatchDoc.isMainProject=="9")?true:false;//判断是否为主项目
                }
            }
        }

        // 创建发文
        vm.create = function () {
            common.initJqValidation($('#dispatch_form'));
            var isValid = $('#dispatch_form').valid();
            if(isValid){
                dispatchSvc.saveDispatch(vm,function(data){
                    vm.isCommit = false;
                    if (data.flag || data.reCode == "ok") {
                        if(vm.dispatchDoc.dispatchWay && vm.dispatchDoc.dispatchWay == 2){
                            vm.busiFlag.isMerge = true;     //合并发文
                        }
                        if(vm.dispatchDoc.isMainProject && vm.dispatchDoc.isMainProject == 9){
                            vm.busiFlag.isMain = true;     //主项目
                        }

                        if(!vm.dispatchDoc.id){
                            vm.dispatchDoc.id = data.reObj.id;
                        }
                    }
                    bsWin.alert(data.reMsg);
                });
/*                if ($scope.$root.$$phase != '$apply' && $scope.$root.$$phase != '$digest') {
                    $scope.$apply();
                }*/
            }else{
                bsWin.alert("提交失败，有红色标识的是必填项，请确认是否填写！");
            }
        }

        // 核减（增）/核减率（增）计算
        vm.count = function () {
            var pt = /^(-)?(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,4})?$/;    //保留4个小数点
            if(!pt.test(vm.dispatchDoc.declareValue)){
                vm.dispatchDoc.declareValue = 0;
                $("span[data-valmsg-for='declareValue']").html("金额只能输入数字！");
                return ;
            }
            if(!pt.test(vm.dispatchDoc.authorizeValue)){
                vm.dispatchDoc.authorizeValue = 0;
                $("span[data-valmsg-for='authorizeValue']").html("金额只能输入数字！");
                return ;
            }
            $("span[data-valmsg-for='declareValue']").html("");
            $("span[data-valmsg-for='authorizeValue']").html("");

            var dvalue = (parseFloat(vm.dispatchDoc.declareValue) - parseFloat(vm.dispatchDoc.authorizeValue)).toFixed(2);
            var extraRate = parseFloat((dvalue/vm.dispatchDoc.declareValue * 10000)/100.00).toFixed(2);
            vm.dispatchDoc.extraRate = extraRate;
            vm.dispatchDoc.extraValue = dvalue;
        }

        // 打开合并页面
        vm.gotoMergePage = function () {
            vm.busiFlag.isMain=(vm.dispatchDoc.isMainProject=="9")?true:false;//判断是否为主项目
            //没保存或者单个发文改成合并发文主项目时候要先进行保存
            if( vm.dispatchDoc.isMainProject == 9 && !vm.dispatchDoc.id){
                bsWin.alert("请先保存！");
            }else{
                //初始化合并评审信息
                dispatchSvc.initMergeInfo(vm,vm.dispatchDoc.signId);
                $("#mergeSign").kendoWindow({
                    width: "75%",
                    height: "700px",
                    title: "合并发文",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            }
        }

        // 选择合并发文项目
        vm.chooseSign = function () {
            var selIds = $("input[name='mergeSign']:checked");
            if(selIds.length == 0){
                bsWin.alert("请选择要合并发文的项目！");
            }else{
                var signIdArr = [];
                $.each(selIds, function (i, obj) {
                    signIdArr.push(obj.value);
                });
                dispatchSvc.chooseProject(vm.dispatchDoc.signId,signIdArr.join(","),function (data) {
                    if (data.flag || data.reCode == "ok") {
                        dispatchSvc.initMergeInfo(vm,vm.dispatchDoc.signId);
                    }
                    bsWin.alert(data.reMsg);
                });
            }
        }

        // 取消选择
        vm.cancelSign = function () {
            var linkSignId = $("input[name='cancelMergeSignid']:checked");
            if (linkSignId.length < 1){
                bsWin.alert("请选择要取消合并发文的项目！");
            }else{
                var ids = [];
                $.each(linkSignId, function (i, obj) {
                    ids.push(obj.value);
                });
                dispatchSvc.cancelProject(vm.dispatchDoc.signId,ids.join(","),function (data) {
                    if (data.flag || data.reCode == "ok") {
                        dispatchSvc.initMergeInfo(vm,vm.dispatchDoc.signId);
                    }
                    bsWin.alert(data.reMsg);
                });
            }

        }

        // 关闭窗口
        vm.onClose = function () {
            window.parent.$("#mwindow").data("kendoWindow").close();
        }

        //合并发文待选过滤器
        vm.filterSign = function(item){
            var isMatch = true;
            if(vm.searchSign.projectname && (item.projectname).indexOf(vm.searchSign.projectname) == -1){
                isMatch = false;
            }
            if(vm.searchSign.reviewstage && (item.reviewstage != vm.searchSign.reviewstage)){
                isMatch = false;
            }
            if(isMatch){
                return item;
            }
        }

        //重置合并发文
        vm.formReset = function () {
            vm.searchSign = {};
        }

        /**
         * 打印功能 -分页
         * @param id
         */
        vm.templatePage = function(id){
            templatePrintSvc.templatePage(id);
        }

    }
})();
