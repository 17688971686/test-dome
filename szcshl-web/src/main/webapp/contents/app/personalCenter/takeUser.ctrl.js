(function(){

    'use strict';
    angular.module('app').controller('takeUserCtrl',takeUser);

    takeUser.$inject=['bsWin','takeUserSvc'];

        function takeUser(bsWin,takeUserSvc){

            var vm = this;
            vm.title="个人代办";

            activate();
            function activate(){
                takeUserSvc.getUser();
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
                common.initJqValidation();
                var isValid = $('#form').valid();
                if (isValid) {
                    takeUserSvc.saveTakeUser(vm.takeUserId,function(data){
                        takeUserSvc.getUser(vm.takeUserId,function (data) {
                            vm.takeUser = data.displayName;
                        });
                        bsWin.alert("操作成功！",function(){
                            window.parent.$("#chooseTakeUserWindow").data("kendoWindow").close();
                        });
                    });
                }
            }

        }

})();