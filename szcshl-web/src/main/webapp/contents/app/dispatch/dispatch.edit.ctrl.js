(function () {
    'dispatch strict';

    angular.module('app').controller('dispatchEditCtrl', dispatch);

    dispatch.$inject = ['$location', 'dispatchSvc', '$state', 'bsWin'];

    function dispatch($location, dispatchSvc, $state, bsWin) {
        var vm = this;
        vm.title = '项目发文编辑';
        vm.sign = {};
        vm.searchSign = {};        //发文查询对象
        vm.dispatchDoc = {};       //发文对象
        vm.dispatchDoc.signId = $state.params.signid;

        vm.showFlag = {
            buttSysFile : false,        //显示附件按钮
        }
        vm.busiFlag = {
            isMerge : false,            //是否合并发文
            isMain : false,             //是否合并发文主项目
        }

        activate();
        function activate() {
            dispatchSvc.initDispatchData(vm);
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
                            $('.confirmDialog').modal('hide');
                            dispatchSvc.cancelProject(vm.dispatchDoc.signId,null,function (data) {
                                if (data.flag || data.reCode == "ok") {
                                    vm.dispatchDoc.isMainProject = "0";
                                    vm.busiFlag.isMerge=false;
                                    vm.busiFlag.isMain=false;
                                }
                                bsWin.alert(data.reMsg);
                            });
                        },
                        onClose : function(){
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
            dispatchSvc.saveDispatch(vm);
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
            if( vm.dispatchDoc.isMainProject == 9 || !vm.dispatchDoc.id){
                bsWin.alert("请先保存！");
            }else{
                //初始化合并评审信息
                dispatchSvc.initMergeInfo(vm,vm.dispatchDoc.signId);
                $("#mwindow").kendoWindow({
                    width: "1200px",
                    height: "630px",
                    title: "合并发文",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            }
        }

        // 选择合并发文项目
        vm.chooseProject = function () {
            var selIds = $("input[name='checksign']:checked");
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
        vm.cancelProject = function () {
            var linkSignId = $("input[name='checkss']:checked");
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
        vm.filterMergeSign = function(item){
            var isMatch = true;
            if(vm.searchSign.projectname && (item.projectname).indexOf(vm.searchSign.projectname) == -1){
                isMatch = false;
            }
            if(vm.searchSign.reviewstage && (item.reviewstage != vm.searchSign.reviewstage)){
                isMatch = false;
            }
            if(vm.searchSign.builtcompanyName && (item.builtcompanyName).indexOf(vm.searchSign.builtcompanyName) == -1){
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

    }
})();
