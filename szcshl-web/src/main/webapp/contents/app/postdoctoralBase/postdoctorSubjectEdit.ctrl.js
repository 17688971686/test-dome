(function(){
    'use strict';
    angular.module('app').controller('postdoctorSubjectEditCtrl' , postdoctorSubjectEdit);
    postdoctorSubjectEdit.$inject = ['$state' , 'bsWin' , '$scope' , 'postdoctorSubjectSvc' , 'sysfileSvc'];
    function postdoctorSubjectEdit($state , bsWin , $scope , postdoctorSubjectSvc , sysfileSvc){
        var vm = this ;
        vm.title = "博士基地课题编辑";
        vm.id = $state.params.id;
        vm.sysFilelistsYJDG = [];//课题研究大纲
        vm.sysFilelistsWWHT = [];//课题外委合同
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
                vm: vm,
                uploadSuccess: function () {
                    sysfileSvc.findByBusinessId(vm.id, function (data) {
                        console.log(234);
                        if(data){
                            vm.sysFilelistsYJDG = [];//课题研究大纲
                            vm.sysFilelistsWWHT = [];//课题外委合同
                            $.each(data , function( i , obj ){
                                if(sysfileSvc.mainTypeValue().DOCTOR_KTYJDG == obj.mainType){
                                    vm.sysFilelistsYJDG.push(obj);
                                }
                                if(sysfileSvc.mainTypeValue().DOCTOR_KTWWHT == obj.mainType){
                                    vm.sysFilelistsWWHT.push(obj);
                                }
                            })
                        }
                    });
                }
            });
        }


        activate();
        function activate(){
            if(vm.id){
                vm.isShowUpdate = true;
                postdoctorSubjectSvc.findBySubjectId(vm , function(data){
                    vm.subject = data;
                });
                sysfileSvc.findByBusinessId(vm.id, function (data) {
                    if(data){
                        $.each(data , function( i , obj ){
                            if(sysfileSvc.mainTypeValue().DOCTOR_KTYJDG == obj.mainType){
                                vm.sysFilelistsYJDG.push(obj);
                            }
                            if(sysfileSvc.mainTypeValue().DOCTOR_KTWWHT == obj.mainType){
                                vm.sysFilelistsWWHT.push(obj);
                            }
                        })
                    }
                });
            }
            postdoctorSubjectSvc.findStationStaff(function(data){
                vm.staffList = data;
            })
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
                    vm.isShowUpdate = true;
                    bsWin.success("保存成功！");
                    // $state.go('postdoctoralSubjectList');
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
            vm.initFileUpload();
        }

        /**
         * 重写附件方案-课题研究大纲
         * @param id
         */
        vm.delectYJDG = function (id) {
            bsWin.confirm({
                title: "询问提示",
                message: "确认删除么？",
                onOk: function () {
                    sysfileSvc.delSysFile(id, function (data) {
                        bsWin.alert(data.reMsg || "删除成功！");
                        $.each(vm.sysFilelistsYJDG, function (i, sf) {
                            if (!angular.isUndefined(sf) && sf.sysFileId == id) {
                                vm.sysFilelistsYJDG.splice(i, 1);
                            }
                        })
                    });
                }
            });
        }

        /**
         * 重写附件删除方法-课题外委合同
         * @param id
         */
        vm.delectWWHT = function(id){
            bsWin.confirm({
                title: "询问提示",
                message: "确认删除么？",
                onOk: function () {
                    sysfileSvc.delSysFile(id, function (data) {
                        bsWin.alert(data.reMsg || "删除成功！");
                        $.each(vm.sysFilelistsWWHT, function (i, sf) {
                            if (!angular.isUndefined(sf) && sf.sysFileId == id) {
                                vm.sysFilelistsWWHT.splice(i, 1);
                            }
                        })
                    });
                }
            });
        }

    }
})();