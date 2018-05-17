(function () {
    'use strict';
    angular.module('app').controller('takeUserCtrl', takeUser);

    takeUser.$inject = ['bsWin', 'takeUserSvc','userSvc'];

    function takeUser(bsWin, takeUserSvc,userSvc) {
        var vm = this;
        vm.title = "个人代办";
        vm.model = {};

        activate();
        function activate() {
            takeUserSvc.getUser(function(data) {
                vm.model = data;
                takeUserSvc.initAllUser(function(data) {
                    vm.takeUserList = data;
                    vm.initTaskUser(vm.model.takeUserId);
                });

                //查询个人待办任务列表
                userSvc.getAllTaskList(vm.model.id,function(data){
                    console.log(data);
                    if(data.ruTaskList){
                        vm.ruTaskList = data.ruTaskList;        //待办任务
                    }
                    if(data.ruProcessTaskList){
                        vm.ruProcessTaskList = data.ruProcessTaskList;  //待办项目
                    }
                });
            });

            userSvc.getOrg(function(data){
                vm.org = {};
                vm.org = data;
            });
        }

        //  取消代办人
        vm.cancelTakeUser = function () {
            if(!vm.model.takeUserId) {
                bsWin.alert("你还没有设置代办人！");
            } else {
                bsWin.confirm({
                    title: "询问提示",
                    message: "确认取消代办人么？",
                    onOk: function () {
                        takeUserSvc.cancelTakeUser(function(){
                            vm.model.takeUserId = "";
                            vm.taskUserName = "";
                            bsWin.alert("操作成功!");
                        });
                    }
                });
            }
        }


        /**
         * 保存代办人
         * */
        vm.saveTakeUser = function () {
            if(vm.takeUserId){
                takeUserSvc.saveTakeUser(vm.takeUserId, function (data) {
                    vm.model.takeUserId = vm.takeUserId;
                    vm.initTaskUser(vm.takeUserId);
                    bsWin.alert("操作成功！");
                });
            }else{
                bsWin.alert("请选择代办人！");
            }
        }

        vm.initTaskUser = function(userId){
            angular.forEach(vm.takeUserList,function(u,index){
                if(u.id == userId){
                    vm.taskUserName = u.displayName;
                }
            })
        }


        /**
         * 任务流转
         */
        vm.transTask = function(userId){

        }
    }

})();