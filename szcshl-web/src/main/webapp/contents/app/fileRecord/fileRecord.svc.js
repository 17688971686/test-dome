(function () {
    'use strict';

    angular.module('app').factory('fileRecordSvc', fileRecord);

    fileRecord.$inject = ['sysfileSvc', '$http'];

    function fileRecord(sysfileSvc, $http) {
        var service = {
            initFileRecordData: initFileRecordData,		//初始化流程数据
            saveFileRecord: saveFileRecord,				//保存

        };
        return service;

        //S_初始化
        function initFileRecordData(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/fileRecord/html/initFillPage",
                params: {signId: vm.fileRecord.signId}
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        if (response.data != null && response.data != "") {
                            vm.fileRecord = response.data.file_record;
                            vm.fileRecord.signId = vm.signId;
                            vm.signUserList = response.data.sign_user_List;

                            //初始化附件上传
                            sysfileSvc.initUploadOptions({
                                businessId: vm.fileRecord.fileRecordId,
                                sysSignId: vm.fileRecord.signId,
                                sysfileType: "归档",
                                uploadBt: "upload_file_bt",
                                detailBt: "detail_file_bt",
                                vm: vm
                            });
                        }
                    }

                });
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
                if (vm.signUser) {
                    vm.fileRecord.signUserid = vm.signUser.id;
                    vm.fileRecord.signUserName = vm.signUser.displayName;
                }
                vm.saveProcess = true;
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/fileRecord",
                    data: vm.fileRecord
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            vm.saveProcess = false;
                            common.alert({
                                vm: vm,
                                msg: "操作成功！",
                            })
                        }
                    });
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess,
                    onError: function (response) {
                        vm.saveProcess = false;
                    }
                });
            }
        }//E_保存

    }
})();