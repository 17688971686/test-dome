(function () {
    'projectExpe strict';

    angular.module('app').factory('projectExpeSvc', projectExpe);

    projectExpe.$inject = ['$http'];

    function projectExpe($http) {
        var service = {
            getProject: getProject,
            saveProject: saveProject,
            updateProject: updateProject,
            updateProjectPage: updateProjectPage,
            getProjectById: getProjectById,
            delertProject: delertProject,
            initProjectType: initProjectType
        };
        return service;

        //begin  initProjectType
        function initProjectType(vm) {
            var code = "PROJECTTYPE";
            var httpOptions = {
                method: "get",
                url: rootPath + "/dict/getAllDictByCode",
                params: {dictCode: code}
            }
            var httpSuccess = function success(response) {
                vm.projectTypes = response.data;
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//end initProjectType

        //begin#delertProject
        function delertProject(ids,callBack) {
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/projectExpe/deleteProject",
                params:{
                    ids : ids
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        ////end#delertProject


        //begin#getProjectById
        function getProjectById(vm) {
            var httpOptions = {
                method: 'get',
                url: common.format(rootPath + "/projectExpe/getProject?$filter=peID eq '{0}'", vm.peID)
            }
            var httpSuccess = function success(response) {
                //vm.model = response.data[0];
                vm.project = {};
                vm.project.projectName = response.data[0].projectName;
                vm.project.projectType = response.data[0].projectType;
                vm.project.projectbeginTime = response.data[0].projectbeginTime;
                vm.project.projectendTime = response.data[0].projectendTime;

                //$('#projectbeginTime').val(response.data[0].projectbeginTime);
                //$('#projectendTime').val(response.data[0].projectendTime);
                if (vm.isUpdate) {
                    //initZtreeClient(vm);
                }
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });


        }


        //begin#updateProject
        function updateProject(vm) {
            common.initJqValidation($('#ProjectForm'));
            var isValid = $('#ProjectForm').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.project.peID = vm.peID;
                vm.project.expertID = vm.expertID;
                vm.project.projectbeginTime = $('#projectbeginTime').val();
                vm.project.projectendTime = $('#projectendTime').val();
                //alert(vm.model.projectendTime);
                var httpOptions = {
                    method: 'put',
                    url: rootPath + "/projectExpe/updateProject",
                    data: vm.project
                }

                var httpSuccess = function success(response) {

                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            window.parent.$("#pjwindow").data("kendoWindow").close();
                            getProject(vm);
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                }
                            })
                        }

                    })
                }
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

            //} else {
            // common.alert({
            // vm:vm,
            // msg:"您填写的信息不正确,请核对后提交!"
            // })
            //}

        }


        //begin#getProject
        function getProject(vm) {
            var httpOptions = {
                method: 'GET',
                url: rootPath + "/projectExpe/getProject?$filter=expert.expertID eq '" + vm.model.expertID + "'"
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.projectList = response.data;
                    }

                });
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }

        //begin#updateProject
        function updateProjectPage(vm) {
            var isCheck = $("input[name='checkpj']:checked");
            if (isCheck.length < 1) {
                common.alert({
                    vm: vm,
                    msg: "请选择操作对象",
                    fn: function () {
                        $('.alertDialog').modal('hide');
                        $('.modal-backdrop').remove();
                        return;
                    }
                })
            } else if (isCheck.length > 1) {
                common.alert({
                    vm: vm,
                    msg: "无法同时操作多条数据",
                    fn: function () {
                        $('.alertDialog').modal('hide');
                        $('.modal-backdrop').remove();
                        return;
                    }
                })
            } else {
                vm.peID = isCheck.val();
                getProjectById(vm);
                vm.expertID = vm.model.expertID;
                gotoJPage(vm);
            }
        }

        // begin#保存专家项目经历
        function saveProject(project,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/projectExpe/saveExpe",
                data: project
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
    }
})();