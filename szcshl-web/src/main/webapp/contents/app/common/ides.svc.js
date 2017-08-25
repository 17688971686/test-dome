/**
 * Created by ldm on 2017/8/22.
 */
(function () {
    'use strict';
    angular.module('app').factory('ideaSvc', idea);

    idea.$inject = ['$http','bsWin'];

    function idea($http, bsWin) {
        var service = {
            initIdeaData: initIdeaData,                 // 初始化选择意见窗口数据
            initIdea : initIdea //初始化个人常用意见
        };
        return service;

        // 初始化常用意见
        function initIdeaData(vm,options) {
            vm.ideaContent = '';// 初始化当前意见
            $("#ideaWindow").kendoWindow({
                width: "70%",
                height: "660px",
                title: "意见选择",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "close"]
            }).data("kendoWindow").center().open();

            //如果没有加载，则加载个人意见信息
            if(!vm.ideas){
                $http({
                    method: 'post',
                    url: rootPath + "/idea/findMyIdea"
                }).then(function (response) {
                    vm.ideas = response.data;
                });
            }

            //删除常用意见
            vm.deleteCommonIdea = function () {
                var isCheck = $("#commonIdeaTable input[type='checkbox']:checked");
                if (isCheck.length < 1) {
                    bsWin.alert("请选择要删除的意见！");
                } else {
                    var ids = [];
                    for (var i = 0; i < isCheck.length; i++) {
                        if(isCheck[i].value){
                            ids.push(isCheck[i].value);
                        }
                        $.each(vm.ideas,function(c,ideaObj){
                            if(c == isCheck[i].name ){
                                vm.ideas.splice(c, 1);
                            }
                        })
                    }
                    if(ids.length > 0){
                        var idsStr = ids.join(",");
                        $http({
                            method: 'delete',
                            url: rootPath + '/idea',
                            params: {
                                ideas: idsStr
                            }
                        }).then(function (response) {
                            bsWin.alert("删除成功！",function () {
                                $http({
                                    method: 'post',
                                    url: rootPath + "/idea/findMyIdea"
                                }).then(function (response) {
                                    vm.ideas = response.data;
                                });
                            });
                        });
                    }
                }
            };

            //拼接评审意见
            vm.addCorrentIdea = function (content) {
                vm.ideaContent += content;
            };

            vm.addCommonIdea = function () {// 添加常用意见
                if(!vm.ideas){
                    vm.ideas = [];
                }
                vm.newIdea = {};
                vm.newIdea.ideaType = "1";     //1、表示个人常用意见
                vm.ideas.push(vm.newIdea);
            }

            vm.saveCommonIdea = function () {// 保存常用意见
                if(vm.ideas.length == 0){
                    bsWin.alert("请编辑你要保存的意见信息！");
                    return ;
                }
                $http({
                    method: 'post',
                    url: rootPath + "/idea",
                    headers: {
                        "contentType": "application/json;charset=utf-8" // 设置请求头信息
                    },
                    dataType: "json",
                    data: angular.toJson(vm.ideas)
                }).then(function (response) {
                    if(response.data.flag || response.data.reCode == "ok"){
                        vm.ideas = response.data.reObj;
                        bsWin.alert("添加成功！");
                    }else{
                        bsWin.alert(response.data.reMsg);
                    }
                });
            }

            vm.saveCurrentIdea = function () {
                var targetObj = $("#" + options.targetId);
                targetObj.val(targetObj.val() + vm.ideaContent);
                window.parent.$("#ideaWindow").data("kendoWindow").close();
                targetObj.focus();
            }
        }//end

        //初始化个人常用意见
        function initIdea(vm){
            var httpOptions={
                method: 'post',
                url: rootPath + "/idea/findMyIdea"
            }

            var httpSuccess=function success(response){
                vm.ideas = response.data;
            }

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
    }
})();