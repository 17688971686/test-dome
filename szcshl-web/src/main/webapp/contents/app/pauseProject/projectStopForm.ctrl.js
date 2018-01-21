(function(){
    'use strict';
    angular.module('app').controller('projectStopFormCtrl' , projectStopForm);
    projectStopForm.$inject = ['$state' , 'pauseProjectSvc','bsWin','$scope','sysfileSvc'];
    function projectStopForm($state, pauseProjectSvc,bsWin,$scope,sysfileSvc){
        var vm = this;
        vm.sign = {};
        var signId = $state.params.signId;
        vm.projectStop = {};
        vm.projectStop.signid = signId;
        vm.projectStop.stopId = $state.params.stopId;

        //用于控制发起流程，返回流程等按钮
        vm.showStartFlow = true;
        if(vm.projectStop.stopId != ""){
            vm.showStartFlow = false;
        }


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
                showBusiType:false,
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
            pauseProjectSvc.initProject(signId,function(data){
                vm.sign = data;
                if(vm.sign.reviewstage == '可行性研究报告' || vm.sign.reviewstage == '项目概算'){
                    vm.sign.countUsedWorkday = 15-vm.sign.surplusdays;
                }else{
                    vm.sign.countUsedWorkday = 12-vm.sign.surplusdays;
                }
            });
            pauseProjectSvc.getProjectStopBySignId(signId,function (data) {
                if(data.length>0) {
                    vm.projectStop = data[0];
                    if (vm.projectStop.isSupplementMaterial == "9") {
                        vm.projectStop.sysBusiType = "中心发补充材料函";
                    }
                    if (vm.projectStop.isPuaseApprove == "9") {
                        vm.projectStop.sysBusiType = "申报单位要求暂停审核函";
                    }
                    sysfileSvc.findByBusinessId(vm.projectStop.stopid, function (data) {
                        vm.sysFilelists = data;
                    });
                }
                vm.initFileUpload();
            })

        }

        vm.Checked = function($event,isHasFile){
            var checkbox = $event.target;
            var checked = checkbox.checked;
            if(isHasFile){
                if(checked == 9 || checked == '9'){
                    vm.noFile = false;
                }else{
                    vm.noFile = true;
                }
            }else{
                if(vm.noFile){
                    vm.projectStop.isSupplementMaterial = 0;
                    vm.projectStop.isPuaseApprove = 0;
                }
            }
        }

        /**
         *更新暂停项目信息
         */
        vm.commitProjectStop = function () {
            common.initJqValidation();
            var isValid = $('#form').valid();
            if (isValid) {
                vm.projectStop.processName = "《"+vm.sign.projectname+"》暂停申请";
                if(!vm.projectStop.userDays){
                    vm.projectStop.userDays = vm.sign.countUsedWorkday;
                }
                vm.projectStop.signid=vm.sign.signid;
                pauseProjectSvc.pauseProject(vm.projectStop,function(data){
                    if(data.flag || data.reCode=="ok"){
                        bsWin.alert("操作成功！",function(){$state.go("personDtasks");})
                    }else{
                        bsWin.alert(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("项目暂停表填写不符合要求！");
            }
        }

        /**
         *保存更新暂停项目信息
         */
        vm.saveProjectStop = function () {
            common.initJqValidation();
            var isValid = $('#form').valid();
            if (isValid) {
                vm.projectStop.signid= vm.sign.signid;
                pauseProjectSvc.saveProjectStop(vm.projectStop,function(data){
                    if(data.flag || data.reCode=="ok"){
                        bsWin.alert("操作成功！")
                        vm.projectStop=data.reObj;
                        if (vm.projectStop.isSupplementMaterial == "9") {
                            vm.projectStop.sysBusiType = "中心发补充材料函";
                        }
                        if (vm.projectStop.isPuaseApprove == "9") {
                            vm.projectStop.sysBusiType = "申报单位要求暂停审核函";
                        }

                    }else{
                        bsWin.alert(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("项目暂停表填写不符合要求！");
            }
        }

        /**
         * 选择部长意见
         */
        vm.selectMinisterIdea=function(){

            vm.projectStop.directorIdeaContent += vm.directorIdea;
        }

        /**
         * 选择分管副主任意见
         */
        vm.selectDirectorIdea=function(){
            vm.projectStop.leaderIdeaContent += vm.leaderIdea;
        }

    }
})();