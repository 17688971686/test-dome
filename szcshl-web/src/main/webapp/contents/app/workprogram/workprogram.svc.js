(function () {
    'use strict';

    angular.module('app').factory('workprogramSvc', workprogram);

    workprogram.$inject = ['sysfileSvc', '$http', '$state','$rootScope'];
    function workprogram(sysfileSvc, $http, $state,$rootScope) {
        var url_company = rootPath + "/company";
        var service = {
            initPage: initPage,				        //初始化页面参数
            createWP: createWP,				        //新增操作
            findCompanys: findCompanys,		        //查找主管部门
            findUsersByOrgId: findUsersByOrgId,     //查询评估部门
            selectExpert: selectExpert,			    //选择专家
            saveRoom: saveRoom,					    //添加会议预定
            findAllMeeting: findAllMeeting,         //查找会议室地点

            initMergeWP: initMergeWP,		        //项目关联弹窗
            getSeleWPByMainId: getSeleWPByMainId,   //初始化已选项目列表
            waitSeleWP: waitSeleWP,			        //待选项目列表

            chooseWP: chooseWP,                     //选择合并评审的工作方案
            cancelWP: cancelWP,                     //取消合并评审的工作方案
            deleteAllMerge:deleteAllMerge,          //删除所有合并评审的工作方案
        };

        return service;

        //S_初始化已选项目列表
        function getSeleWPByMainId(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/workprogram/getSeleWPByMainId",
                params: {
                    mainBussnessId:vm.work.id
                }
            }
            var httpSuccess = function success(response) {
                vm.selectedWP = [];
                vm.selectedWP = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //E_初始化已选项目列表

        //S_待选项目列表
        function waitSeleWP(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/workprogram/waitSeleWP",
                params: {
                    mainBussnessId: vm.work.id
                }
            }
            var httpSuccess = function success(response) {
                vm.unSeledWork = [];
                vm.unSeledWork = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //E_待选项目列表

        //S_取消合并评审工作方案
        function cancelWP(vm) {
            var selIds = $("input[name='cancelSeledWPid']:checked");
            if(selIds.length == 0){
                common.alert({
                    vm: vm,
                    msg: "请选择要取消合并评审的工作方案！",
                    closeDialog: true
                });
                return ;
            }else{
                var workIdArr = [];
                $.each(selIds, function (i, obj) {
                    workIdArr.push(obj.value);
                });
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/workprogram/deleteMergeWork",
                    params: {
                        mainBusinessId: vm.work.id,
                        businessId: workIdArr.join(","),
                    }
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            getSeleWPByMainId(vm);//初始化
                            waitSeleWP(vm);//待选
                            common.alert({
                                vm: vm,
                                msg: "操作成功！",
                                closeDialog: true
                            });
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
        }//E_cancelWP

        //S_选择合并评审工作方案
        function chooseWP(vm) {
            var selIds = $("input[name='checkwork']:checked");
            if(selIds.length == 0){
                common.alert({
                    vm: vm,
                    msg: "请选择要合并评审的工作方案！",
                    closeDialog: true
                });
                return ;
            }else{
                var workIdArr = [];
                var signIdArr = [];
                $.each(selIds, function (i, obj) {
                    var idArr = (obj.value).split("$");
                    workIdArr.push(idArr[0]);
                    signIdArr.push(idArr[1]);
                });
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/workprogram/mergeWork",
                    params: {
                        mainBusinessId: vm.work.id,
                        signId: vm.work.signId,
                        businessId: workIdArr.join(","),
                        linkSignId:signIdArr.join(",")
                    }
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            getSeleWPByMainId(vm);//初始化
                            waitSeleWP(vm);//待选
                            common.alert({
                                vm: vm,
                                msg: "操作成功！",
                                closeDialog: true
                            });
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

        }//E_chooseWP

        //S_关联项目弹窗
        function initMergeWP(vm) {
            getSeleWPByMainId(vm);//初始化
            waitSeleWP(vm);//待选
            $(".workPro").kendoWindow({
                width: "1200px",
                height: "600px",
                title: "合并评审",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //S_关联项目弹窗

        // 清空页面数据
        // begin#cleanValue
        function cleanValue() {
            var tab = $("#stageWindow").find('input');
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }

        //S_会议预定添加
        function saveRoom(vm) {

            common.initJqValidation($('#stageForm'));
            var isValid = $('#stageForm').valid();
            if (isValid) {
                vm.roombook.workProgramId = vm.work.id;
                vm.roombook.stageOrgName = vm.work.reviewOrgName;
                vm.roombook.stageProject = "项目名称:" + vm.work.projectName + ":" + vm.work.buildCompany + ":" + vm.work.reviewOrgName;
                vm.roombook.beginTimeStr = $("#beginTime").val();
                vm.roombook.endTimeStr = $("#endTime").val();
                vm.roombook.beginTime = $("#rbDay").val() + " " + $("#beginTime").val() + ":00";
                vm.roombook.endTime = $("#rbDay").val() + " " + $("#endTime").val() + ":00";

                if (new Date(vm.roombook.endTime) < new Date(vm.roombook.beginTime)) {
                    $("#errorTime").html("开始时间不能大于结束时间!");
                    return;
                }
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/room/saveRoom",
                    data: vm.roombook
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            window.parent.$("#stageWindow").data("kendoWindow").close();
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                }
                            })
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
        }
        //E_会议预定添加

        //S_查找所有会议室地点
        function findAllMeeting(vm) {
            var httpOptions = {
                method: 'get',
                url: common.format(rootPath + "/room/meeting")
            }
            var httpSuccess = function success(response) {
                vm.roombookings = {};
                vm.roombookings = response.data;

            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //E_查找所有会议室地点

        //start 查找主管部门
        function findCompanys(vm) {
            var httpOptions = {
                method: 'get',
                url: common.format(url_company + "/findCcompanys")
            }
            var httpSuccess = function success(response) {
                vm.companys = {};
                vm.companys = response.data;
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //end 查找主管部门

        //S_根据部门ID选择用户
        function findUsersByOrgId(vm, type) {
            var param = {};
            if ("main" == type) {
                param.orgId = vm.work.reviewDept;
            }
            var httpOptions = {
                method: 'get',
                url: rootPath + "/user/findUsersByOrgId",
                params: param
            };
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        if ("main" == type) {
                            vm.mainUserList = {};
                            vm.mainUserList = response.data;
                        }
                    }
                });
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //E_根据部门ID选择用户

        //S_初始化页面参数
        function initPage(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/workprogram/html/initWorkProgram",
                params: {
                    signId: vm.work.signId,
                    workProgramId:vm.work.id
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        if (response.data != null && response.data != "") {
                            vm.work = response.data;
                            vm.work.signId = $state.params.signid;		//收文ID(重新赋值)
                            if(vm.work.projectType){
                                vm.work.projectTypeDicts = $rootScope.topSelectChange(vm.work.projectType,$rootScope.DICT.PROJECTTYPE.dicts)
                            }
                            //初始化数值
                            if(vm.work.reviewType == "自评"){
                                vm.businessFlag.isSelfReview = true;           //是否自评
                            }
                            if(vm.work.isSigle == "合并评审"){
                                vm.businessFlag.isSingleReview = false;         //是否单个评审
                            }
                            if(vm.work.isMainProject == "9"){
                                vm.businessFlag.isMainWorkProj = true;           //合并评审主项目
                            }
                            if (response.data.roomBookingDtos && response.data.roomBookingDtos.length > 0) {
                                vm.businessFlag.isRoomBook = true;
                                vm.RoomBookings = {};
                                vm.RoomBookings = response.data.roomBookingDtos;
                                vm.roombook = vm.RoomBookings[0];

                                if (vm.RoomBookings.length > 1) {
                                    vm.businessFlag.isHaveNext = true;
                                }
                            }

                            if (vm.work.id) {
                                var sysfileType = "工作方案";
                                if (!angular.isUndefined(vm.work.isMain)) {
                                    if (vm.work.isMain == '9') {
                                        sysfileType = "工作方案[主]"
                                    } else {
                                        sysfileType = "工作方案[协]"
                                    }
                                }
                                //初始化附件上传
                                sysfileSvc.initUploadOptions({
                                    businessId: vm.work.id,
                                    sysSignId: vm.work.signId,
                                    sysfileType: sysfileType,
                                    uploadBt: "upload_file_bt",
                                    detailBt: "detail_file_bt",
                                    vm: vm
                                });
                            }
                        }

                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//S_初始化页面参数

        //S_保存操作
        function createWP(vm, isNeedWorkProgram) {
            common.initJqValidation($("#work_program_form"));
            var isValid = $("#work_program_form").valid();
            if (isValid) {
                vm.iscommit = true;
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/workprogram/addWork",
                    data: vm.work,
                    params: {
                        isNeedWorkProgram: isNeedWorkProgram
                    }
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            vm.iscommit = false;
                            common.alert({
                                vm: vm,
                                msg: response.data.reMsg,
                                closeDialog: true,
                                fn: function () {
                                    if (response.data.reCode == "ok") {
                                        //如果没有没有工作方案ID，则刷新路由
                                        vm.work.id = response.data.reObj.id;
                                        //初始化数值
                                        if(response.data.reObj.reviewType == "自评"){
                                            vm.businessFlag.isSelfReview = true;           //是否自评
                                        }
                                        if(response.data.reObj.isSigle == "合并评审"){
                                            vm.businessFlag.isSingleReview = false;         //是否单个评审
                                        }
                                        if(response.data.reObj.isMainProject == "9"){
                                            vm.businessFlag.isMainWorkProj = true;           //合并评审主项目
                                        }
                                    }
                                }
                            })
                        }
                    });
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess,
                    onError: function (response) {
                        vm.iscommit = false;
                    }
                });
            }
        }//E_保存操作

        //S_selectExpert
        function selectExpert(vm) {
            if (vm.work.id && vm.work.id != '') {
                $state.go('expertReviewEdit', {workProgramId: vm.work.id});
            } else {
                common.alert({
                    vm: vm,
                    msg: "请先保存，再继续执行操作！"
                })
            }
        }//E_selectExpert

        //S_删除所有合并评审工作方案
        function deleteAllMerge(vm){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/workprogram/deleteMergeWork",
                params: {
                    mainBusinessId: vm.work.id,
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: "操作成功！",
                            closeDialog: true
                        });
                    }
                });
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_deleteAllMerge
    }
})();