(function(){

    'use strict';
    angular.module('app').controller('takeUserCtrl',takeUser);

    takeUser.$inject=['$location','takeUserSvc'];

        function takeUser($location,takeUserSvc){

            var vm = this;
            vm.title="个人代办";
            // vm.takeUserId="";

            activate();
            function activate(){
                takeUserSvc.getUser(vm);
            }

            //  取消代办人
            vm.cancelTakeUser =function(){
                if(vm.takeUser=="" || vm.takeUser==null){
                    common.confirm({
                        vm: vm,
                        title: "",
                        msg: "暂无代办人！？",
                        fn: function () {
                            $('.confirmDialog').modal('hide');
                        }
                    });
                }else{
                    common.confirm({
                        vm: vm,
                        title: "",
                        msg: "确认取消该代办人？",
                        fn: function () {
                            $('.confirmDialog').modal('hide');
                            takeUserSvc.cancelTakeUser(vm);
                        }
                    });
                }
            }

            //选择代办人-初始化所有用户
            vm.showChooseTakeUser=function(){
                $("#chooseTakeUserWindow").kendoWindow({
                    width: "500px",
                    height: "300px",
                    title: "选择代办人",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();

                takeUserSvc.initAllUser(vm);

            }

            /**
             * 保存代办人
             * */
            vm.saveTakeUser=function(){
                takeUserSvc.saveTakeUser(vm);
            }

        }

})();