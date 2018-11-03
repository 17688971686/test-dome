(function(){
    'use strict';
    angular.module('app').controller('postdoctorSubjectEditCtrl' , postdoctorSubjectEdit);
    postdoctorSubjectEdit.$inject = ['$state' , 'bsWin' , '$scope' , 'postdoctorSubjectSvc' , 'sysfileSvc'];
    function postdoctorSubjectEdit($state , bsWin , $scope , postdoctorSubjectSvc , sysfileSvc){
        var vm = this ;
        vm.title = "博士基地课题编辑";
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
                mainType: vm.type,
                sysfileType: vm.type,
                sysBusiType: vm.type,
            };
            sysfileSvc.initUploadOptions({
                inputId: "sysfileinput",
                vm: vm
            });
        }

        activate();
        function activate(){
            if(vm.id){
                postdoctorSubjectSvc.findBySubjectId(vm , function(data){
                    vm.subject = data;
                });
            }
            vm.initFileUpload();
        }

        /**
         * 创建课题
         */
        vm.createSubject = function(){
            postdoctorSubjectSvc.createSubject(vm , function(data){
                if (data.flag || data.reCode == 'ok') {
                    vm.subject = data.reObj;
                    vm.id = vm.subject.id;
                    bsWin.success("保存成功！");
                    $state.go('postdoctoralSubjectList');
                } else {
                    bsWin.error(data.reMsg);
                }
            })
        }


        /**
         * 更新课题
         */
        vm.updateSubject = function(){

        }

        /**
         * 上传课题研究大纲
         */
        vm.addKTYJDG = function(){
            vm.type = sysfileSvc.mainTypeValue().DOCTOR_KTYJDG;
            vm.initFileUpload();
        }

        /**
         * 上传课题外委合同
         */
        vm.addKTYWHT = function(){
            vm.type = sysfileSvc.mainTypeValue().DOCTOR_KTWWHT;
        }



    }
})();