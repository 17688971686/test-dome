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
            vm.otherFile=[];//定义归档其他资料的包含分类5、6、7
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
                    //是否协审
                    vm.isassistproc = (vm.fileRecord.isassistproc == '9')?true:false;
                    //其它资料信息
                    vm.fileRecord.registerFileDto.forEach(function(registerFile  , x){
                        if(registerFile.businessType == 5 ||registerFile.businessType == 6 ||registerFile.businessType == 7){
                            vm.otherFile.push(registerFile);
                        }else if(registerFile.businessType == 2){
                            vm.drawingFile.push(registerFile);
                        }
                        /*else if(registerFile.businessType == "XMJYS_DECLARE_FILE"){
                            vm.declareFile.push(registerFile);
                        }*/
                    })

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
        function saveFileRecord(vm,callBack) {
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
                    if (callBack != undefined && typeof callBack == 'function') {
                        callBack(response.data);
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
            }else{

            }
        }//E_保存

    }
})();