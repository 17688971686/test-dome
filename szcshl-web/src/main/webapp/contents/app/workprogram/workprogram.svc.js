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
            deleteBookRoom:deleteBookRoom,          //删除会议室
            findAllMeeting: findAllMeeting,         //查找会议室地点

            initMergeInfo : initMergeInfo,          //初始化合并项目信息
            getMergeSignBySignId: getMergeSignBySignId,   //初始化已选项目列表
            unMergeWPSign: unMergeWPSign,			//待选项目列表

            chooseSign: chooseSign,                 //选择合并评审的工作方案
            cancelMergeSign: cancelMergeSign,       //取消合并评审的工作方案
            deleteAllMerge:deleteAllMerge,          //删除所有合并评审的工作方案
            
            initReview: initReview,                      //初始化评审方案信息
            initParamValue:initParamValue,				//初始化参数值
        };

        return service;
        
        //初始化参数值
        function initParamValue(vm) {
            vm.conditions = new Array();          //条件列表
            vm.customCondition = new Array();
            vm.expertReview = {};                 //评审方案对象
            vm.selfExperts = [],
            vm.selectExperts = [],
            vm.selectIds = [],
            vm.autoExperts = [],
            vm.autoSelExperts = [],
            vm.outsideExperts = [];
        }
        
        //S_initReview
        function initReview(vm) {
        	
            vm.iscommit = true;
            initParamValue(vm);
            var httpOptions = {
                method: 'get',
                url: rootPath + "/expertReview/html/initByWorkProgramId",
                params: {workProgramId: vm.work.id}
            };
            var httpSuccess = function success(response) {
                vm.iscommit = false;
                vm.expertReview = response.data;
                //专家抽取条件
                if (vm.expertReview.expertSelConditionDtoList && vm.expertReview.expertSelConditionDtoList.length > 0) {
                    vm.conditions = vm.expertReview.expertSelConditionDtoList;
                    vm.conditionIndex = vm.expertReview.expertSelConditionDtoList.length;//下标值
                }
                //获取已经抽取的专家
                if (vm.expertReview.expertSelectedDtoList && vm.expertReview.expertSelectedDtoList.length > 0) {
                    vm.expertReview.expertSelectedDtoList.forEach(function (sep, index) {
                        vm.selectIds.push(sep.expertDto.expertID);
                        vm.selectExperts.push(sep);
                        if (sep.selectType == '1') {           //抽取专家
                            vm.autoExperts.push(sep);
                            vm.autoSelExperts.push(sep.expertDto)
                        } else if (sep.selectType == '2') {     //自选专家
                            vm.selfExperts.push(sep);
                        } else if (sep.selectType == '3') {     //境外专家
                            vm.outsideExperts.push(sep);
                        }
                    });
                    if (vm.selectIds.length > 0) {
                        vm.excludeIds = vm.selectIds.join(',');
                    } else {
                        vm.excludeIds = '';
                    }
                }
            };
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //E_initReview
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
                    if (vm.work.id) {
                        var sysfileType = "工作方案";
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