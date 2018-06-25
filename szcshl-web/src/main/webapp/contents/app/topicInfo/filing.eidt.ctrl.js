(function () {
    'use strict';

    angular.module('app').controller('filingEditCtrl', filing);

    filing.$inject = ['bsWin', '$scope', 'addRegisterFileSvc', 'filingSvc','$state','sysfileSvc'];

    function filing(bsWin, $scope, addRegisterFileSvc, filingSvc,$state,sysfileSvc) {
        var vm = this;
        vm.title = '课题研究存档表';
        vm.filing = {};
        vm.zlList = [];     //新增的资料列表
        if($state.params.curNodeId){
            vm.curNodeId = $state.params.curNodeId;

        }
        activate();
        function activate() {
            filingSvc.findByTopicId($state.params.topicId,function(data){
                vm.filing = data.file_record;
                vm.topicUserList = data.topic_user_List;
                vm.filing.topicId = $state.params.topicId;
                vm.initFileUpload();
            })
        }

        //初始化附件上传控件
        vm.initFileUpload = function(){
            if(!vm.filing.id){
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.filing.id",function (newValue, oldValue) {
                    if(newValue && newValue != oldValue && !vm.initUploadOptionSuccess){
                        vm.initFileUpload();
                    }
                });
            }

            //创建附件对象
            vm.sysFile = {
                businessId : vm.filing.id,
                mainId : vm.filing.topicId,
                mainType : sysfileSvc.mainTypeValue().TOPIC,
                sysfileType:sysfileSvc.mainTypeValue().TOPIC_FILING,
                sysBusiType:sysfileSvc.mainTypeValue().TOPIC_FILING,
            };
            sysfileSvc.initUploadOptions({
                inputId:"sysfileinput",
                vm:vm
            });
        }

        //S_保存存档信息
        vm.saveFiling = function(){
            if(vm.curNodeId == 'TOPIC_ZLGD'){
                vm.filing.isGdy = '1';
            }
            filingSvc.save(vm.filing,function (data) {
                if(data.flag || data.reCode == 'ok'){
                   // vm.filing = data.reObj;
                    vm.filing.topicId = $state.params.topicId;
                    bsWin.alert("保存成功！");
                }else{
                    bsWin.error(data.reMsg);
                }
            })
        }//E_saveFiling

        //S_新增资料
        vm.addZL = function(){
            if(!vm.filing.registerFileDto){
                vm.filing.registerFileDto = [];
            }
            var newFile = {};
            newFile.id = common.uuid();
            vm.filing.registerFileDto.push(newFile);
        }//E_addZL

        //S_删除资料
        vm.delZL = function(){
            var isCheck = $("#filingform input[tit='dynamicCheckbox']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择要删除的意见！");
            } else {
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    if(isCheck[i].value){
                        ids.push(isCheck[i].value);
                    }
                    $.each(vm.filing.registerFileDto,function(c,obj){
                        if(obj.id == isCheck[i].value ){
                            vm.filing.registerFileDto.splice(c, 1);
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

        //S_初始化input框的值
        /*       vm.initInputValue = function($event,defaultValue){
         var checkbox = $event.target;
         var checked = checkbox.checked;
         if (checked && !defaultValue) {
         return 1;
         }else{
         return defaultValue;
         }
         }*///E_initInputValue
    }
})();
