(function () {
    'use strict';

    angular.module('myApp').config(function ($stateProvider) {
        $stateProvider.state('attachmentEdit', {
            url: "/attachmentEdit/:id",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/attachment/html/edit'),
            controller: function ($scope, attachmentSvc, $state, bsWin) {
                $scope.csHide("bm");
                var vm = this;
                vm.attachment = {};
                vm.attachmentId = $state.params.id;
                vm.attachment.publicAtt = true;
                if (vm.attachmentId) {
                    attachmentSvc.findDocById(vm.attachmentId, vm);
                }

                vm.save = function () {
                    if (vm.attachmentId) {
                        vm.attachment.id = vm.attachmentId;
                        attachmentSvc.update(vm);//更新
                    } else {
                        if (!vm.attachment.id) {
                            bsWin.alert("请先上传文档！");
                            return;
                        }
                        attachmentSvc.create(vm);//创建
                    }
                }

                //初始化上传控件
                $('#orginalFiles').uploadify({
                    uploader: util.formatUrl('sys/attachment/upload'),
                    swf: util.formatUrl("libs/uploadify/uploadify.swf"),
                    method: 'post',
                    multi: true,
                    auto: true,//自动上传
                    fileObjName: 'files',// 上传参数名称
                    fileSizeLimit: "10MB",//上传文件大小限制
                    buttonText: '选择文档',
                    fileExt: '*.pdf;*.txt;*.png;*.doc',
                    fileTypeExts: '*.pdf;*.txt;*.png;*.doc;*png;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.ceb',
                    fileTypeDesc: "请选择*.pdf;*.txt;*.png;*.doc文件",     // 文件说明
                    removeCompleted: true,   //设置已完成上传的文件是否从队列中移除，默认为true
                    onUploadStart: function (file) {
                        if (!vm.attachment.tableKey) {
                            bsWin.error("参数错误！");
                            return false;
                        }
                        //var filters=[];
                        if (vm.attachment.id) {
                            $('#orginalFiles').uploadify("settings", "formData", {"attId": vm.attachment.id});//传参到后台
                        }
                        $('#orginalFiles').uploadify("settings", "formData", {"tableKey": vm.attachment.tableKey});//传参到后台
                    },
                    onUploadSuccess: function (file, data, response) {
                        angular.element("body").scope().$apply(function () {
                            vm.attachment.originalName = (file.name).substring(0, (file.name).indexOf("."));
                            bsWin.alert("上传成功");
                        })
                    },
                    onUploadError: function (file, errorCode, errorMsg, errorString) {
                        angular.element("body").scope().$apply(function () {
                            bsWin.error("上传失败");
                        })
                    },
                });


                vm.backPrevPage = function (backUrl) {
                    $scope.backPrevPage(backUrl);
                };

            }
        });
    });

})();