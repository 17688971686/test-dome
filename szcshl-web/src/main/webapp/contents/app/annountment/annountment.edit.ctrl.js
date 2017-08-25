(function () {
    'use strict';

    angular.module('app').controller('annountmentEditCtrl', annountmentEdit);

    annountmentEdit.$inject = ['$state', 'annountmentSvc','sysfileSvc','$scope'];

    function annountmentEdit($state, annountmentSvc,sysfileSvc,$scope) {
        var vm = this;
        vm.title = "通知公告编辑";
        vm.annountment = {};        //通知公告对象
        vm.annountment.anId = $state.params.id;
        vm.businessFlag ={
            isInitFileOption : false,   //是否已经初始化附件上传控件
        }
        //初始化附件上传控件
        vm.initFileUpload = function(){
            if(!vm.annountment.anId){
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.annountment.anId",function (newValue, oldValue) {
                    if(newValue && newValue != oldValue && !vm.initUploadOptionSuccess){
                        vm.initFileUpload();
                    }
                });
            }

            //创建附件对象
            vm.sysFile = {
                businessId : vm.annountment.anId,
                mainId : vm.annountment.anId,
                mainType : sysfileSvc.mainTypeValue().NOTICE,
            };
            sysfileSvc.initUploadOptions({
                inputId:"sysfileinput",
                vm:vm,
                uploadSuccess:function(){
                    sysfileSvc.findByBusinessId(vm.annountment.anId,function(data){
                        vm.sysFilelists = data;
                    });
                }
            });
        }
        active();
        function active() {
           $('#froalaEditor') .froalaEditor({
                language: 'zh_cn',
                inlineMode: false,
                placeholderText:'请输入内容' ,
                imageUploadURL: rootPath +"/froala/uploadImg",
                imageUploadParams:{rootPath:rootPath},//接口其他传参,默认为空对象{},
                height: '260px', //高度
                enter: $.FroalaEditor.ENTER_BR,
                toolbarButtons: [
                    'bold', 'italic', 'underline','strikeThrough','fontFamily', 'paragraphFormat', 'align','color','fontSize','outdent',
                    'indent','insertImage','insertTable','undo', 'redo','insertLink','fullscreen'
                ]
            });

            if (vm.annountment.anId) {
            	vm.isUpdate=true;
                annountmentSvc.findAnnountmentById(vm);
                sysfileSvc.findByBusinessId(vm.annountment.anId,function(data){
                    vm.sysFilelists = data;
                });
            }else{
                vm.initFileUpload();
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