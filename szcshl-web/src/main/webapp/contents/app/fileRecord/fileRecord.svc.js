(function () {
    'use strict';

    angular.module('app').factory('fileRecordSvc', fileRecord);

    fileRecord.$inject = ['bsWin', '$http'];

    function fileRecord(bsWin, $http) {
        var service = {
            initFileRecordData: initFileRecordData,		//初始化流程数据
            saveFileRecord: saveFileRecord,				//保存

        };
        return service;

        //S_初始化
        function initFileRecordData(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/fileRecord/initFillPage",
                params: {signId: vm.fileRecord.signId}
            }
            var httpSuccess = function success(response) {
                if (response.data != null && response.data != "") {
                    vm.fileRecord = response.data.file_record;

                    vm.fileRecord.signId = vm.signId;
                    vm.signUserList = response.data.sign_user_List;

                    //初始化附件上传
                    vm.initFileUpload();
                }
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_初始化

        //S_保存
        function saveFileRecord(vm) {
            common.initJqValidation($("#fileRecord_form"));
            var isValid = $("#fileRecord_form").valid();
            if (isValid) {
                vm.signUserList.forEach(function(su,index){
                    if(vm.fileRecord.signUserid == su.id){
                        vm.fileRecord.signUserName = su.displayName;
                    }
                })

                vm.isCommit = true;
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/fileRecord",
                    data: vm.fileRecord
                }
                var httpSuccess = function success(response) {
                    vm.isCommit = false;
                    if(response.data.flag || response.data.reCode == 'ok'){
                        vm.fileRecord = response.data.reObj;
                        vm.fileRecord.signId = vm.signId;
                        bsWin.success("操作成功！")
                    }else{
                        bsWin.error(response.data.reMsg);
                    }
                }
                common.http({
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess,
                    onError: function (response) {
                        vm.isCommit = false;
                    }
                });
            }
        }//E_保存

    }
})();