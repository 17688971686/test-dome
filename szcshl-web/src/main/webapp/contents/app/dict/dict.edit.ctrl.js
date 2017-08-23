(function () {
    'use strict';

    angular
        .module('app')
        .controller('dictEditCtrl', dict);

    dict.$inject = ['$scope','bsWin','dictSvc','$state'];
    function dict($scope,bsWin, dictSvc,$state) {

    	var vm = this;
        vm.title = '增加字典';
        vm.model = {};
      
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '编辑字典';
        }
        vm.model.dictSort=0;//默认排序序号为0

        activate();
        function activate() {
            if (vm.isUpdate) {
                dictSvc.getDictById(vm.id,function (data) {
                    if (data.flag || data.reCode == "ok"){
                        vm.model = data.reObj;
                        dictSvc.getTreeData(function (data) {
                            vm.treeData = {};
                            vm.treeData = data.value;
                            if(vm.isUpdate&&vm.treeData&&vm.model.parentId){
                                for(var i = 0;i<vm.treeData.length;i++){
                                    if(vm.treeData[i].dictId == vm.model.parentId){
                                        vm.model.parentDictName = vm.treeData[i].dictName;
                                        break;
                                    }
                                }
                            }
                        });
                    }else{
                        bsWin.alert(data.reMsg);
                    }
                })
            } else {
                vm.model.dictCode = '';
                dictSvc.initpZtreeClient(function (data) {
                    var setting = {
                        check: {enable: true,chkStyle: "radio",radioType: "all"},
                        callback: {
                            //onCheck: zTreeOnCheck,
                            //onClick: zTreeOnClick
                        },
                        data: {
                            simpleData: {
                                enable: true,
                                idKey: "id",
                                pIdKey: "pId"
                            }
                        }
                    };
                    function zTreeOnCheck(event, treeId, treeNode) {
                    };

                    function zTreeOnClick(event, treeId, treeNode,clickFlag) {
                    };
                    var zNodes = $linq(data.value).select(
                        function(x) {
                            var pId;
                            if(x.parentId){
                                pId = x.parentId;
                            }
                            return {
                                id : x.dictId,
                                name : x.dictName,
                                pId:pId
                            };
                        }).toArray();
                    vm.zpTree = $.fn.zTree.init($("#pzTree"), setting,zNodes);
                });
            }
        }

        //新增字典
        vm.createDict = function(){
            common.initJqValidation();
            var isValid = $('form').valid();
            if(isValid){
                var nodes = vm.zpTree.getCheckedNodes(true);
                if(nodes&&nodes.length>0){
                    vm.model.parentId = nodes[0].id;
                }
                dictSvc.createDict(vm.model,vm.isSubmit,function(data){
                    vm.isSubmit = false;
                    if (data.flag || data.reCode == "ok"){
                        $('.alertDialog').modal('hide');
                        $('.modal-backdrop').remove();
                        bsWin.alert("系统提示","操作成功！",function () {
                            $state.go('dict',{},{reload:true});
                        });
                    }else{
                        bsWin.alert(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("数据填写正确，请检查修改后再提交");
            }
        };

        //修改字典
        vm.updateDict = function(){
            common.initJqValidation();
            var isValid = $('form').valid();
            if(isValid){
                dictSvc.updateDict(vm.model,vm.isSubmit,function(data){
                    vm.isSubmit = false;
                    if (data.flag || data.reCode == "ok"){
                        $('.alertDialog').modal('hide');
                        $('.modal-backdrop').remove();
                        bsWin.alert("系统提示","操作成功！",function () {
                            $state.go('dict.edit', { id: vm.model.dictId},{reload:true});
                        });
                    }else{
                        bsWin.alert(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("数据填写正确，请检查修改后再提交");
            }
        }

    	vm.dictTypeChange = function(){
    		if(vm.model.dictType){
    			vm.model.dictKey = '';
    		}
    	};
    	
    	vm.apply = function(){
    		$scope.$apply();
    	}

    }
    
})();
