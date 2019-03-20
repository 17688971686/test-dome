(function(){
    'use strict';
    angular.module('app').controller('projectStopFormEditCtrl' , projectStopFormEdit);
    projectStopFormEdit.$inject = ['$state' , 'pauseProjectSvc','bsWin','$scope','sysfileSvc'];
    function projectStopFormEdit($state, pauseProjectSvc,bsWin,$scope,sysfileSvc){
        var vm = this;
        vm.stopId = $state.params.stopId;

        //初始化附件上传控件
        vm.initFileUpload = function () {
            if (!vm.projectStop.stopid) {
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.projectStop.stopid", function (newValue, oldValue) {
                    if (newValue && newValue != oldValue && !vm.initUploadOptionSuccess) {
                        vm.initFileUpload();
                    }
                });
            }

            //创建附件对象
            vm.sysFile = {
                businessId: vm.projectStop.stopid,
                mainId: vm.projectStop.stopid,
                mainType: "暂停项目",
                sysBusiType: vm.projectStop.sysBusiType,
                showBusiType:true,      //是否显示业务类型
            };
            sysfileSvc.initUploadOptions({
                inputId: "sysfileinput",
                vm: vm,
                uploadSuccess: function () {
                    sysfileSvc.findByBusinessId(vm.projectStop.stopid, function (data) {
                        vm.sysFilelists = data;
                    });
                }
            });
        }
        activate();
        function activate(){
            pauseProjectSvc.getProjectStopByStopId(vm.stopId,function(data){
                vm.projectStop = data;
                vm.sign = vm.projectStop.signDispaWork;
                //评审天数-剩余工作日
                vm.sign.countUsedWorkday = vm.sign.reviewdays-vm.sign.surplusdays;
                if(!vm.projectStop.stopid){
                    vm.projectStop.userDays = vm.sign.countUsedWorkday;
                }
                if (vm.projectStop.isSupplementMaterial == "9") {
                    vm.projectStop.sysBusiType = "中心发补充材料函";
                }
                if (vm.projectStop.isPuaseApprove == "9") {
                    vm.projectStop.sysBusiType = "申报单位要求暂停审核函";
                }
                sysfileSvc.findByBusinessId(vm.projectStop.stopid, function (data) {
                    vm.sysFilelists = data;
                });
                vm.initFileUpload();

            });
        }

        /**
         *保存更新暂停项目信息
         */
        vm.saveProjectStop = function () {
            common.initJqValidation();
            var isValid = $('#form').valid();
            if (isValid) {
                pauseProjectSvc.saveProjectStop(vm.projectStop,function(data){
                    if(data.flag || data.reCode=="ok"){
                        bsWin.alert("操作成功！")
                    }else{
                        bsWin.alert(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("项目暂停表填写不符合要求！");
            }
        }


    }
})();