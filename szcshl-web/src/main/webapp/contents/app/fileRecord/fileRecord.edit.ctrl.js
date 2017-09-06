(function () {
    'use strict';

    angular.module('app').controller('fileRecordEditCtrl', fileRecord);

    fileRecord.$inject = ['fileRecordSvc','$state','sysfileSvc', 'bsWin','$scope'];

    function fileRecord(fileRecordSvc,$state,sysfileSvc,bsWin,$scope) {
        var vm = this;
        vm.title = '项目归档编辑';

        vm.fileRecord = {};
        vm.fileRecord.signId = $state.params.signid;
        vm.fileRecord.fileRecordId = "";
        vm.signId = $state.params.signid;

        //初始化附件上传控件
        vm.initFileUpload = function(){
            if(!vm.fileRecord.fileRecordId){
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.fileRecord.fileRecordId",function (newValue, oldValue) {
                    if(newValue && newValue != oldValue && !vm.initUploadOptionSuccess){
                        vm.initFileUpload();
                    }
                });
            }
            vm.sysFile = {
                businessId : vm.fileRecord.fileRecordId,
                mainId : vm.signId,
                mainType : sysfileSvc.mainTypeValue().SIGN,
                sysfileType:sysfileSvc.mainTypeValue().DOFILE,
                sysBusiType:sysfileSvc.mainTypeValue().DOFILE,
            };
            sysfileSvc.initUploadOptions({
                inputId:"sysfileinput",
                vm:vm
            });
        }

        activate();
        function activate(){
            fileRecordSvc.initFileRecordData(vm);
        }

        vm.create = function(){
            fileRecordSvc.saveFileRecord(vm);
        }


    }
})();
