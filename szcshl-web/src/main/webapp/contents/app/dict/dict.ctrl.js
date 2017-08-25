(function () {
    'use strict';
    angular.module('app').controller('dictCtrl', dict);

    dict.$inject = ['dictSvc','bsWin','$state'];

    function dict(dictSvc,bsWin,$state) {
    	  /* jshint validthis:true */
    	var vm = this;
        vm.title = '字典';
        vm.model = {};

        activate();
        function activate() {
            dictSvc.initDictTree(function(data){
                var zTreeObj;
                var setting = {
                    callback: {
                        onClick: zTreeOnClick
                    },
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: "id",
                            pIdKey: "pId"
                        }
                    }
                };
                function zTreeOnClick(event, treeId, treeNode) {
                    $state.go('dict.edit', { id: treeNode.id});
                };
                function zTreeOnCheck(event, treeId, treeNode) {
                    var selId = treeNode.id;
                    if(!vm.model.dels){
                        vm.model.dels = [];
                    }
                    var delIds = vm.model.dels;
                    if(treeNode.checked){
                        delIds.push(selId);
                    }else{
                        for(var i =0;i<delIds.length;i++){
                            if(delIds[i] == selId){
                                delIds.splice(i);
                                break;
                            }
                        }
                    }

                };
                var zNodes = $linq(data.value).select(
                    function(x) {
                        var isParent = false;
                        var pId = null;
                        if(x.parentId){
                            pId = x.parentId;
                        }
                        return {
                            id : x.dictId,
                            name : x.dictName,
                            pId:pId
                        };

                    }).toArray();
                zTreeObj = $.fn.zTree.init($("#zTree"), setting,zNodes);
                vm.dictsTree = zTreeObj;
            });
        }

        //执行删除操作
        vm.del = function (id) {
            bsWin.confirm({
                title: "询问提示",
                message: "删除字典将会连下级字典一起删除，确认删除数据吗？",
                onOk: function () {
                    dictSvc.deleteDict(id,vm.isSubmit ,function(data){
                        vm.isSubmit = false;
                        if (data.flag || data.reCode == "ok") {
                            bsWin.alert("消息提示","操作成功！",function(){
                                $('.alertDialog').modal('hide');
                                $('.modal-backdrop').remove();
                                $state.go('dict',{},{reload:true});
                            });
                        }else{
                            bsWin.alert(data.reMsg);
                        }
                    });
                }
            });
        }

        vm.dels = function () {
            var nodes = vm.dictsTree.getSelectedNodes();
            if (nodes&&nodes.length >0) {
            	 vm.del(nodes[0].id)
            } else {
                bsWin.alert("请选择要删除的数据！");
            }   
       }
    }

})();
