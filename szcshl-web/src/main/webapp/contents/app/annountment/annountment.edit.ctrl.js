(function () {
    'use strict';

    angular.module('app').controller('annountmentEditCtrl', annountmentEdit);

    annountmentEdit.$inject = ['$state', 'annountmentSvc','sysfileSvc','$scope'];

    function annountmentEdit($state, annountmentSvc,sysfileSvc,$scope) {
        var vm = this;
        vm.title = "通知公告编辑";
        vm.annountment = {};        //通知公告对象
        vm.annountment.anId = $state.params.id;
        vm.editor=undefined;
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
          /*  UE.delEditor("editor");//可能是缓存问题导致的，因此先删除缓存中已有的富文本，
            vm.editor= UE.getEditor("editor");//再重新渲染*/
            //渲染百度Ueditor的编辑器
            vm.editor = new UE.ui.Editor();
             vm.editor.render('editor');
            if (vm.annountment.anId) {
            	vm.isUpdate=true;
                annountmentSvc.findAnnountmentById(vm,function (data) {
                    vm.annountment = data;
                    vm.initFileUpload();
                    vm.editor.ready(function(){//在初始化后，填充编辑器的值
                      if(vm.annountment.anContent!=undefined){
                          vm.editor.setContent(vm.annountment.anContent);
                      }
                    })
                });
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