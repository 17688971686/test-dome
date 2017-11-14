(function () {
    'use strict';

    angular.module('app').controller('meetingEditCtrl', meeting);

    meeting.$inject = ['bsWin', 'meetingSvc', '$state'];

    function meeting(bsWin, meetingSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加会议室';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新会议室';
        }

        /**
         * 创建会议室
         */
        vm.create = function () {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                meetingSvc.createMeeting(vm.model, vm.isSubmit,function(data){
                    if (data.flag || data.reCode == "ok") {
                        bsWin.alert("消息提示", "操作成功！", function () {
                            location.href = '#/meeting';
                        });
                    } else {
                        bsWin.alert(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("信息没填写正确！");
            }

        };

        /**
         * 更改
         */
        vm.update = function () {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.model.id = vm.id;
                meetingSvc.updateMeeting(vm.model, vm.isSubmit,function(data) {
                    if (data.flag || data.reCode == "ok") {
                        bsWin.alert("操作成功");
                    } else {
                        bsWin.alert(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("信息没填写正确！");
            }
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                meetingSvc.getMeetingById(vm);
            } else {

            }
        }
    }
})();
