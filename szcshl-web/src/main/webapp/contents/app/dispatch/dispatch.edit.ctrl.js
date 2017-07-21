(function () {
    'dispatch strict';

    angular.module('app').controller('dispatchEditCtrl', dispatch);

    dispatch.$inject = ['$location', 'dispatchSvc', '$state', "$http"];

    function dispatch($location, dispatchSvc, $state, $http) {
        var vm = this;
        vm.title = '项目发文编辑';
        vm.sign = {};
        vm.serarchSign = {};        //发文查询对象
        vm.dispatchDoc = {};        //发文对象
        vm.dispatchDoc.signId = $state.params.signid;

        vm.showFlag = {
            buttSysFile : false,        //显示附件按钮
        }
        vm.busiFlag = {
            signleToMerge : false,      //单个发文改成合并发文(多余的,后期修改)
            isMerge : false,            //是否合并发文
            isMain : false,             //是否合并发文主项目
        }

        activate();
        function activate() {
            dispatchSvc.initDispatchData(vm);
        }

        //发文方式改变事件
        vm.sigleProject = function () {
        	 //console.log(vm.dispatchDoc.isMainProject);
            //1、由合并发文主项目改为单个发文
            if(vm.dispatchDoc.dispatchWay == "1" ){
                if(vm.busiFlag.signleToMerge){
                    vm.busiFlag.signleToMerge = false;
                }
                //console.log(vm.busiFlag.isMain);
                //console.log(vm.busiFlag.isMerge);
                if(vm.busiFlag.isMerge && vm.busiFlag.isMain){
                    common.confirm({
                        title: "温馨提示",
                        vm: vm,
                        msg: "该项目已经设为合并发文，并且已经有关联项目，如果现在要取消合并发文，以前的关联信息将被删除，您确定要取消合并发文么?",
                        fn: function () {
                            $('.confirmDialog').modal('hide');
                            vm.dispatchDoc.isMainProject = "0";
                            dispatchSvc.deleteAllMerge(vm);
                            vm.busiFlag.isMerge=false;
                            vm.busiFlag.isMain=false;
                        },
                        cancel:function(){
                            vm.dispatchDoc.dispatchWay = "2";
                            $('.confirmDialog').modal('hide');
                        }
                    });
                }
            //2、由单个发文改为合并发文
            }else if(vm.dispatchDoc.dispatchWay == "2" ){
            	 console.log(vm.dispatchDoc.isMainProject);
                if(!vm.busiFlag.isMerge){
                	vm.busiFlag.isMerge=true;
                    vm.busiFlag.signleToMerge = true;  //单个发文改成合并发文
                    vm.busiFlag.isMain=(vm.dispatchDoc.isMainProject=="9")?true:false;//判断是否为主项目
                }
            }
        }

        //待选择过来器
        vm.filterSign = function(item){
            if(vm.dispatchDoc.signId != item.signid){
                return item;
            }
        }
        // 创建发文
        vm.create = function () {
            dispatchSvc.saveDispatch(vm);
            //vm.busiFlag.signleToMerge = "";  //单个发文改成合并发文(除去改标签）
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
           // if((vm.busiFlag.signleToMerge && vm.dispatchDoc.isMainProject == 9) || !vm.dispatchDoc.id){
        	 if(!vm.dispatchDoc.id){
                common.alert({
                    vm: vm,
                    msg: "请先进行保存！",
                    closeDialog:true,
                })
            }else{
                dispatchSvc.gotoMergePage(vm);
            }
        }

        vm.searchMergeSign = function () {
            dispatchSvc.getSignForMerge(vm);
        }

        // 选择合并发文项目
        vm.chooseProject = function () {
            dispatchSvc.chooseProject(vm);
        }

        // 取消选择
        vm.cancelProject = function () {
            dispatchSvc.cancelProject(vm);
        }

        // 关闭窗口
        vm.onClose = function () {
            window.parent.$("#mwindow").data("kendoWindow").close();
        }

        vm.formReset = function () {
            vm.serarchSign = {};
        }

    }
})();
