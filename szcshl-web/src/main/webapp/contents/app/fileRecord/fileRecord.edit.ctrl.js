(function () {
    'use strict';

    angular.module('app').controller('fileRecordEditCtrl', fileRecord);

    fileRecord.$inject = ['fileRecordSvc','$state','sysfileSvc', 'bsWin','$scope' , 'templatePrintSvc' , 'addRegisterFileSvc'];

    function fileRecord(fileRecordSvc,$state,sysfileSvc,bsWin,$scope , templatePrintSvc , addRegisterFileSvc) {
        var vm = this;
        vm.title = '项目归档编辑';

        vm.fileRecord = {};
        vm.fileRecord.signId = $state.params.signid;
        vm.fileRecord.fileRecordId = "";
        vm.signId = $state.params.signid;
        //是否协审归档（默认不是）
        vm.isassistproc = false;
      
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

        /**
         * 打印功能 -分页
         */
        vm.templatePage = function(id){
            templatePrintSvc.templatePage(id);
        }

        //S_新增资料
        vm.addOtherFile = function(){
            if(!vm.fileRecord.registerFileDto){
                vm.fileRecord.registerFileDto = [];
            }
            var newFile = {};
            newFile.id = common.uuid();
            vm.fileRecord.registerFileDto.push(newFile);
        }//E_addZL

        //S_删除资料
        vm.delOtherFile = function(){
            var isCheck = $("#fileRecord_form input:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择要删除的意见！");
            } else {
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    if(isCheck[i].value){
                        ids.push(isCheck[i].value);
                    }
                    $.each(vm.fileRecord.registerFileDto,function(c,obj){
                        if(obj.id == isCheck[i].value ){
                            vm.fileRecord.registerFileDto.splice(c, 1);
                        }
                    })
                }
                if(ids.length > 0){
                    addRegisterFileSvc.deleteByIds(ids.join(","),function(data){
                        bsWin.alert("删除成功！");
                    });
                }
            }
        }//E_delZL
    }
})();
