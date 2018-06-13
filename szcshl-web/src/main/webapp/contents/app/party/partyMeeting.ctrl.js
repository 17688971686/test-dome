(function () {
    'use strict';

    angular.module('app').controller('partyMeetingCtrl', partyMeeting);

    partyMeeting.$inject = ['partyMeetSvc' , 'bsWin' , '$scope' , "$state" , 'sysfileSvc'];

    function partyMeeting(partyMeetSvc , bsWin , $scope , $state , sysfileSvc) {
        var vm = this;
        vm.title = '党员会议管理';        		//标题
        vm.party = {};
        vm.businessFlag = {};
        vm.id = $state.params.id;

        //初始化附件上传控件
        vm.initFileUpload = function () {
            if (!vm.id) {
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.id", function (newValue, oldValue) {
                    if (newValue && newValue != oldValue && !vm.initUploadOptionSuccess) {
                        vm.initFileUpload();
                    }
                });
            }

            //创建附件对象
            vm.sysFile = {
                businessId: vm.id,
                mainId: vm.id,
                mainType: "党务会议附件",
                sysBusiType: "",
                showBusiType: false,
            };
            sysfileSvc.initUploadOptions({
                inputId: "sysfileinput",
                vm: vm,
                uploadSuccess: function () {
                    sysfileSvc.findByBusinessId(vm.id, function (data) {
                        vm.sysFilelists = data;
                    });
                }
            });
        }

        active();
        function active(){
            partyMeetSvc.partyMeetGrid(vm);
            // sharingPlatlformSvc.initOrgAndUser(vm);
            if(vm.id){
                vm.isShowUpdate = true;
                partyMeetSvc.findMeetById(vm.id , function(data){
                    vm.partyMeet = data;
                });
                //初始化附件
                sysfileSvc.findByBusinessId(vm.id , function(data){
                    vm.sysFilelists = data;
                });
            }

            vm.initFileUpload();
        }

        /**
         * 保存党务会议
         */
        vm.savePartyMeet = function(){
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                partyMeetSvc.createPartyMeet(vm, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        vm.id = data.reObj;
                        vm.isShowUpdate = true;
                        bsWin.alert("保存成功！");
                    }
                });
            }
        }

        /**
         * 更新会议信息
         * @constructor
         */
        vm.UpdatePartyMeet = function(){
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                partyMeetSvc.updatePartyMeet(vm, function (data) {
                    bsWin.alert("保存成功！");
                });
            }
        }

        /**
         * 删除会议信息
         * @param id
         */
        vm.deletePartyMeet = function(id){
            bsWin.confirm("确定删除？" , function(){
                partyMeetSvc.deletePartyMeet(id , function(data){
                    if(data.flag || data.reCode == 'ok'){
                        bsWin.alert("删除成功！");
                        vm.gridOptions.dataSource.read();
                    }
                });
            });

        }

    }
})();
