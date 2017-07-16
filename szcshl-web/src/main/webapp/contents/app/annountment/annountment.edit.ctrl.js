(function () {
    'use strict';

    angular.module('app').controller('annountmentEditCtrl', annountmentEdit);

    annountmentEdit.$inject = ['$state', 'annountmentSvc'];

    function annountmentEdit($state, annountmentSvc) {
        var vm = this;
        vm.title = "通知公告编辑";
        vm.annountment = {};        //通知公告对象
        vm.annountment.anId = $state.params.id;

        vm.businessFlag ={
            isInitFileOption : false,   //是否已经初始化附件上传控件
        }
        active();
        function active() {
            vm.froalaOptions = {
                language: 'zh_cn',
                inlineMode: false,
                placeholder: '请输入内容',
                imageUploadURL: rootPath +"/froala/uploadImg",
                imageUploadParams:{rootPath:rootPath},//接口其他传参,默认为空对象{},
                enter: $.FroalaEditor.ENTER_BR,
                toolbarButtons: [
                    'bold', 'italic', 'underline', 'paragraphFormat', 'align','color','fontSize','insertImage','insertTable','undo', 'redo'
                ]
            }

            if (vm.annountment.anId) {
            	vm.isUpdate=true;
                annountmentSvc.findAnnountmentById(vm);
                annountmentSvc.findFileList(vm)
            }else{
                //附件初始化
                annountmentSvc.initFileOption({
                    sysfileType: "通知公告",
                    uploadBt: "upload_file_bt",
                    vm: vm
                })
            }

        }

        //新增通知公告
        vm.create = function () {
            annountmentSvc.createAnnountment(vm);
        }

        //编辑通知公告
        vm.update = function () {
            annountmentSvc.updateAnnountment(vm);
        }
    }
})();