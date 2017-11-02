(function () {
    'use strict';

    angular.module('app').controller('addSuppLetterEditCtrl', addSuppLetter);

    addSuppLetter.$inject = ['$location', 'addSuppLetterSvc','sysfileSvc', '$state','bsWin','$scope'];

    function addSuppLetter($location, addSuppLetterSvc,sysfileSvc, $state,bsWin,$scope) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加登记补充资料';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        vm.suppletter = {}; //补充资料对象$state
        vm.suppletter.businessId = $state.params.businessId;        //业务ID
        vm.suppletter.businessType = $state.params.businessType;    //业务类型
        vm.suppletter.id = $state.params.id;

        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新登记补充资料';
        }
        vm.businessFlag ={
                isInitFileOption : false,   //是否已经初始化附件上传控件
        }

        //初始化附件上传控件
        vm.initFileUpload = function(){

            if (!vm.suppletter.id) {
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.suppletter.id", function (newValue, oldValue) {
                    if (newValue && newValue != oldValue && !vm.initUploadOptionSuccess) {
                        vm.initFileUpload();
                    }
                });
            }
            vm.sysFile = {
                businessId: vm.suppletter.id,
                mainId: vm.suppletter.id,
                mainType: sysfileSvc.mainTypeValue().SIGN,
                sysfileType: sysfileSvc.mainTypeValue().AADSUPP_FILE,
                sysBusiType: sysfileSvc.mainTypeValue().AADSUPP_FILE,
            };
            sysfileSvc.initUploadOptions({
                inputId: "sysfileinput",
                vm: vm
            });
        }
        //拟补充资料函查看附件
        vm.addSuppContent = function () {
        	if(vm.suppletter.id){
            vm.showsupp = true;
            var ideaEditWindow = $("#addsuppletter");
            ideaEditWindow.kendoWindow({
                width: "60%",
                height: "90%",
                title: "拟补资料函正文",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "close"]
            }).data("kendoWindow").center().open();
            addSuppLetterSvc.findByBusinessId(vm);
        	}else{
        		bsWin.alert("请先保存业务数据");
        	}
        }
        //保存拟补资料函
        vm.saveAddSuppletter = function () {
            common.initJqValidation($("#suppletter_form"));
            var isValid = $("#suppletter_form").valid();
            if (isValid) {
                addSuppLetterSvc.saveSuppLetter(vm.suppletter,vm.isCommit,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        vm.suppletter = data.reObj;
                        bsWin.success("操作成功！");
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            } else {
                bsWin.alert("表格填写不正确，请检查相应的必填项信息！");
            };
        };
        
        //返回拟补资料涵列表页面
        vm.comeBack = function(){
        	$state.go('addSuppletterList',{businessId: vm.suppletter.businessId});
        }
        
        vm.update = function () {
            addSuppLetterSvc.updateAddSuppLetter(vm);
        };
        
        
        activate();
        function activate() {
        	//根根ID查询拟补资料函
            addSuppLetterSvc.getAddSuppLetterById(vm.suppletter.id,function (data) {
                vm.suppletter = data;
                //初始化附件上传
                vm.initFileUpload();
            });

        }
    }
})();
