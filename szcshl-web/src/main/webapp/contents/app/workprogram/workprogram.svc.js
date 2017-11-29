(function () {
    'use strict';

    angular.module('app').factory('workprogramSvc', workprogram);

    workprogram.$inject = ['$http', '$state', '$rootScope'];
    function workprogram($http, $state, $rootScope) {
        var url_company = rootPath + "/company";
        var service = {
            initPage: initPage,				        //初始化页面参数
            createWP: createWP,				        //新增操作
            findCompanys: findCompanys,		        //查找主管部门
            findUsersByOrgId: findUsersByOrgId,     //查询评估部门
            deleteBookRoom: deleteBookRoom,          //删除会议室
            findAllMeeting: findAllMeeting,         //查找会议室地点

            initMergeInfo: initMergeInfo,          //初始化合并项目信息
            getMergeSignBySignId: getMergeSignBySignId,   //初始化已选项目列表
            unMergeWPSign: unMergeWPSign,			//待选项目列表

            chooseSign: chooseSign,                 //选择合并评审的工作方案
            cancelMergeSign: cancelMergeSign,       //取消合并评审的工作方案
            deleteAllMerge: deleteAllMerge,          //删除所有合并评审的工作方案

            findById: findById,                    //根据主键查询
            updateReviewType: updateReviewType,        //工作方案由专家评审会改成专家函评
        };

        return service;

        //S_工作方案由专家评审会改成专家函评
        function updateReviewType(signId, workprogramId, reviewType, callBack) {
            var wpObj = {
                signId: signId,
                id: workprogramId,
                reviewType: reviewType
            }
            var httpOptions = {
                method: 'post',
                url: rootPath + "/workprogram/updateReviewType",
                data: wpObj,
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
        }//E_expertToLetter

        /**
         * 根据主键查询工作方案信息
         * @param wpId
         */
        function findById(workId, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/workprogram/initWorkProgramById",
                params: {
                    workId: workId
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

        //S_初始化已选项目列表
        function getMergeSignBySignId(signId, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/getMergeSignBySignId",
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

        //E_初始化已选项目列表

        //S_待选项目列表
        function unMergeWPSign(signId, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/unMergeWPSign",
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
        function initMergeInfo(vm, signId) {
            unMergeWPSign(signId, function (data) {
                vm.unMergeSign = [];
                vm.unMergeSign = data;
            });//待选
            getMergeSignBySignId(signId, function (data) {
                vm.mergeSign = [];
                vm.mergeSign = data;
            });//初始化已选项目
        }

        //S_取消合并评审工作方案
        function cancelMergeSign(signId, cancelIds, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/cancelMergeSign",
                params: {
                    signId: signId,
                    cancelIds: cancelIds,
                    mergeType: "1"
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
        function chooseSign(signId, mergeIds, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/mergeSign",
                params: {
                    signId: signId,
                    mergeIds: mergeIds,
                    mergeType: "1"
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

        //S_删除所有合并评审工作方案
        function deleteAllMerge(signId, businessId, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/deleteAllMerge",
                params: {
                    signId: signId,
                    mergeType: "1",
                    businessId: businessId
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
        }//E_deleteAllMerge

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
                    console.log(vm.work.id);
                    //如果首次填写公安方案，则默认评审方式为：自评、单个评审
                    if(! vm.work.id){
                        vm.work.reviewType = "自评";
                        vm.work.isSigle = '单个评审';
                    }

                    //如果选了专家，并且评审费有变动，则更改
                    if (vm.work.expertDtoList && vm.work.expertDtoList.length > 0) {
                        if (!vm.work.expertCost || vm.work.expertCost < 1000 * (vm.work.expertDtoList.length)) {
                            vm.work.expertCost = 1000 * (vm.work.expertDtoList.length);
                        }
                    }
                    vm.model.workProgramDtoList = {};
                    if (response.data.WPList && response.data.WPList.length > 0) {
                        vm.model.workProgramDtoList = response.data.WPList;
                        vm.showTotalInvestment = true;
                    }

                    if (vm.work.branchId == "1") {
                        findCompanys(vm);//查找主管部门
                    }
                    vm.work.signId = $state.params.signid;		//收文ID(重新赋值)
                    if (vm.work.projectType) {
                        vm.work.projectTypeDicts = $rootScope.topSelectChange(vm.work.projectType, $rootScope.DICT.PROJECTTYPE.dicts)
                    }
                    /*//初始化数值
                     if(vm.work.reviewType == "自评"){
                     vm.businessFlag.isSelfReview = true;           //是否自评
                     }
                     if(vm.work.isSigle == "合并评审"){
                     vm.businessFlag.isSingleReview = false;         //是否单个评审
                     }
                     if(vm.work.isMainProject == "9"){
                     vm.businessFlag.isMainWorkProj = true;           //合并评审主项目
                     }
                     */
                    //如果是合并评审次项目，则不允许修改
                    if (vm.work.isSigle == "合并评审" && (vm.work.isMainProject == "0" || vm.work.isMainProject == 0)) {
                        vm.businessFlag.isReveiwAWP = true;
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
        function createWP(work, isNeedWorkProgram, isCommit, callBack) {
            isCommit = true;
            if ($("#studyBeginTime").val()) {
                work.studyBeginTimeStr = $("#studyBeginTime").val();
            }
            if ($("#studyEndTime").val()) {
                work.studyEndTimeStr = $("#studyEndTime").val();
            }
            if ($("#studyAllDay").val() && $("#studyBeginTime").val()) {
                work.studyBeginTime = $("#studyAllDay").val() + " " + $("#studyBeginTime").val() + ":00";
            }
            if ($("#studyAllDay").val() && $("#studyEndTime").val()) {
                work.studyEndTime = $("#studyAllDay").val() + " " + $("#studyEndTime").val() + ":00";
            }
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

        //S_删除会议室
        function deleteBookRoom(bookId, callBack) {
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