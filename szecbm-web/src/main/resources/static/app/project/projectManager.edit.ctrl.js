(function () {
    'use strict';

    angular.module('myApp').config(governmentInvestProjectEditConfig);

    governmentInvestProjectEditConfig.$inject = ["$stateProvider"];

    function governmentInvestProjectEditConfig($stateProvider) {
        $stateProvider.state('projectManageEdit', {
            url: "/projectManageEdit/:id/:flag",
            controllerAs: "vm",
            templateUrl: util.formatUrl('project/html/edit'),
            controller: projectManagerEditCtrl
        });
    }

    projectManagerEditCtrl.$inject = ["$scope", "projectManagerSvc", "$state", "bsWin"];

    function projectManagerEditCtrl($scope, projectManagerSvc, $state, bsWin) {
        $scope.csHide("cjgl");
        var vm = this;
        vm.model = {};
        vm.model.id = $state.params.id;
        vm.flag = $state.params.flag;
        vm.attachments = [];


        projectManagerSvc.findOrgUser(function (data) {
            vm.principalUsers = data;
            vm.initFileUpload();
        });
        projectManagerSvc.findAllOrgDelt(function (data) {
            vm.orgDeptList = data;
        });

        if (vm.model.id) {
            projectManagerSvc.findGovernmentInvestProjectById(vm, function () {
                /**
                 * 查询附件列表
                 */
         /*       projectManagerSvc.getAttachments(vm, {
                    "businessId": vm.model.id
                }, function (data) {
                    angular.forEach(vm.attachments.concat(data), function (o, i) {
                        vm.attachments = vm.attachments.concat(o);
                    });
                });*/
            });
        } else {
            projectManagerSvc.createUUID(vm);
        }

        //初始化附件上传控件
        vm.initFileUpload = function(){
            if (!vm.model.id) {
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.model.id", function (newValue, oldValue) {
                    if (newValue && newValue != oldValue && !vm.initUploadOptionSuccess) {
                        vm.initFileUpload();
                    }
                });
            }
            vm.sysFile = {
                businessId: vm.model.id,
                mainId: "",
                mainType: "",
                sysfileType: "",
                sysBusiType: "",
                detailBt: "detail_file_bt",
            };
            projectManagerSvc.initUploadOptions({
                inputId: "sysfileinput",
                vm: vm
            });
        }


        /**
         * 删除附件
         */
        vm.removeFile = function (fileId, index) {
            bsWin.confirm("是否移除附件", function () {
                projectManagerSvc.deleteFileById(fileId, vm);
                vm.attachments.splice(index, 1);
            })
        };
        /**
         * 日期比较
         */
        function compareDate(d1, d2) {
            return ((new Date(d1.replace(/-/g, "\/"))) > (new Date(d2.replace(/-/g, "\/"))));
        }


        vm.checkLength = function (obj, max, id) {
            util.checkLength(obj, max, id);
        };


        //新增与编辑
        vm.save = function () {
            util.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                var selUser = []
                var selUserName = []
                $('#principalUser_ul input[selectType="assistUser"]:checked').each(function () {
                    selUser.push($(this).attr("value"));
                    selUserName.push($(this).attr("tit"));
                });
                vm.model.mainUserName = $.trim($("#mainUser").find("option:selected").text());
                vm.model.assistUser = selUser.join(",");
                vm.model.assistUserName = selUserName.join(",");
                if (vm.model.id) {
                    projectManagerSvc.updateGovernmentInvestProject(vm);
                } else {
                    vm.model.id = vm.UUID;
                    vm.model.status = '1';
                    projectManagerSvc.createGovernmentInvestProject(vm);
                }
            }

        };

        //检查项目负责人
        vm.checkPrincipal = function () {
            var selUserId = $("#mainUser").val();
            if (selUserId) {
                $('#principalUser_ul input[selectType="assistUser"]').each(
                    function () {
                        var value = $(this).attr("value");
                        if (value == selUserId) {
                            $(this).removeAttr("checked");
                            $(this).attr("disabled", "disabled");
                        } else {
                            $(this).removeAttr("disabled");
                        }
                    }
                );
            }
        }


    }
})();