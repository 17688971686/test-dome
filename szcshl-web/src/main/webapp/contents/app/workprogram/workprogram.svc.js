(function () {
    'use strict';

    angular.module('app').factory('workprogramSvc', workprogram);

    workprogram.$inject = [ '$http', '$state','$rootScope'];
    function workprogram( $http, $state,$rootScope) {
        var url_company = rootPath + "/company";
        var service = {
            initPage: initPage,				        //初始化页面参数
            createWP: createWP,				        //新增操作
            findCompanys: findCompanys,		        //查找主管部门
            findUsersByOrgId: findUsersByOrgId,     //查询评估部门
            saveRoom: saveRoom,					    //添加会议预定
            deleteBookRoom:deleteBookRoom,          //删除会议室
            findAllMeeting: findAllMeeting,         //查找会议室地点

            initMergeInfo : initMergeInfo,          //初始化合并项目信息
            getMergeSignBySignId: getMergeSignBySignId,   //初始化已选项目列表
            unMergeWPSign: unMergeWPSign,			//待选项目列表

            chooseSign: chooseSign,                 //选择合并评审的工作方案
            cancelMergeSign: cancelMergeSign,       //取消合并评审的工作方案
            deleteAllMerge:deleteAllMerge,          //删除所有合并评审的工作方案
        };

        return service;

        //S_初始化已选项目列表
        function getMergeSignBySignId(signId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/getMergeSignBySignId",
                params: {
                    signId:signId
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
        //E_初始化已选项目列表

        //S_待选项目列表
        function unMergeWPSign(signId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/unMergeWPSign",
                params: {
                    signId: signId
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
        //E_待选项目列表

        //S_initMergeInfo
        function initMergeInfo(vm,signId){
            unMergeWPSign(signId,function (data) {
                vm.unMergeSign = [];
                vm.unMergeSign = data;
            });//待选
            getMergeSignBySignId(signId,function (data) {
                vm.mergeSign = [];
                vm.mergeSign = data;
            });//初始化已选项目
        }
        //S_取消合并评审工作方案
        function cancelMergeSign(signId,cancelIds,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/cancelMergeSign",
                params: {
                    signId: signId,
                    cancelIds: cancelIds,
                    mergeType:"1"
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
        }//E_cancelWP

        //S_选择合并评审工作方案
        function chooseSign(signId,mergeIds,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/mergeSign",
                params: {
                    signId:signId ,
                    mergeIds: mergeIds,
                    mergeType:"1"
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
        }//E_chooseWP

        //S_会议预定添加
        function saveRoom(roombook,work,callBack) {
            common.initJqValidation($('#stageForm'));
            var isValid = $('#stageForm').valid();
            if (isValid) {
                if (new Date(roombook.endTime) < new Date(roombook.beginTime)) {
                    bsWin.error("开始时间不能大于结束时间!");
                    return;
                }
                roombook.workProgramId = work.id;
                roombook.stageOrgName = work.reviewOrgName;
                roombook.stageProject = "项目名称:" + work.projectName + ":" + work.buildCompany + ":" + work.reviewOrgName;
                roombook.beginTimeStr = $("#beginTime").val();
                roombook.endTimeStr = $("#endTime").val();
                roombook.beginTime = $("#rbDay").val() + " " + $("#beginTime").val() + ":00";
                roombook.endTime = $("#rbDay").val() + " " + $("#endTime").val() + ":00";

                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/room/saveRoom",
                    data: roombook
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
        //E_会议预定添加

        //S_查找所有会议室地点
        function findAllMeeting(callBack) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/room/meeting"
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
                method: 'post',
                url: rootPath + "/workprogram/html/initWorkProgram",
                params: {
                    signId: vm.work.signId,
                }
            }
            var httpSuccess = function success(response) {
                if (response.data != null && response.data != "") {
                    vm.work = response.data.eidtWP;
                    //如果选了专家，并且评审费有变动，则更改
                    if(vm.work.expertDtoList && vm.work.expertDtoList.length > 0){
                        if(!vm.work.expertCost || vm.work.expertCost < 1000*(vm.work.expertDtoList.length) ){
                            vm.work.expertCost = 1000*(vm.work.expertDtoList.length);
                        }
                    }
                    vm.model.workProgramDtoList = {};
                    if(response.data.WPList && response.data.WPList.length > 0){
                        vm.model.workProgramDtoList = response.data.WPList;
                    }
                    if(vm.work.branchId == "1"){
                        findCompanys(vm);//查找主管部门
                    }
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

                    //初始化控件
                    vm.initFileUpload();
                }
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//S_初始化页面参数

        //S_保存操作
        function createWP(work, isNeedWorkProgram,isCommit,callBack) {
            isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/workprogram/addWork",
                data: work,
                params: {
                    isNeedWorkProgram: isNeedWorkProgram
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                    isCommit = false;
                }
            });
        }//E_保存操作


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


        //S_删除会议室
        function deleteBookRoom(bookId,callBack){
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/room",
                params: {
                    id: bookId
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack();
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_deleteBookRoom
    }
})();