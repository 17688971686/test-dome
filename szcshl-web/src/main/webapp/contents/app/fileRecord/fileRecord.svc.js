(function () {
    'use strict';

    angular.module('app').factory('fileRecordSvc', fileRecord);

    fileRecord.$inject = ['bsWin', '$http' , 'FileSaver'];

    function fileRecord(bsWin, $http , FileSaver) {
        var service = {
            initFileRecordData: initFileRecordData,		//初始化流程数据
            saveFileRecord: saveFileRecord,				//保存
            otherFilePrint : otherFilePrint , //归档资料打印
        };
        return service;
        function otherFilePrint(businessId , fileId){
            if (!businessId) {
                bsWin.alert("没有项目阶段，找不到对应的打印模板，打印预览失败！");
            } else {
               /* var url = rootPath + "/contents/libs/pdfjs-dist/web/viewer.html?version=" + (new Date()).getTime() + "&file=" + rootPath + "/file/otherFilePrint/" + businessId + "/" +fileId ;
                $("#iframePreview").attr("src", url);
                $("#previewModal").kendoWindow({
                    width: "80%",
                    height: "730px",
                    title: "",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();*/
                var httpOptions = {
                    method: 'get',
                    url: rootPath + "/file/otherFileDownload/" + businessId + "/" +fileId,
                    headers : {
                        "contentType" : "application/json;charset=utf-8"
                    },
                    traditional : true,
                    dataType : "json",
                    responseType: 'arraybuffer',
                }
                var httpSuccess = function success(response) {
                    var blob = new Blob([response.data] , {type : "application/msword"});
                    var d = new Date();
                    // var fileName =  d.getFullYear() + (d.getMonth() + 1) + d.getDate()  + d.getHours()  + d.getMinutes() +  d.getSeconds();
                   var fileName =  "其他资料.doc";
                    FileSaver.saveAs(blob, fileName);
                };
                common.http({
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }


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
                        if(registerFile.businessType == "5" ||registerFile.businessType == "6" ||registerFile.businessType == "7" || registerFile.businessType == "3" ){
                            vm.otherFile.push(registerFile);
                        }else if(registerFile.businessType == "2"){
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
                //查找选定的签收人
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