(function () {
    'use strict';

    angular.module('app').factory('expertReviewSvc', expertReview);

    expertReview.$inject = ['$http', '$interval' , 'bsWin'];

    function expertReview($http, $interval , bsWin) {
        var service = {
            initExpertGrid: initExpertGrid,	            //初始化待抽取专家列表
            saveSelfExpert: saveSelfExpert,		        //保存自选专家
            saveOutExpert: saveOutExpert,               //保存选择的境外专家
            countMatchExperts: countMatchExperts,       //计算符合条件的专家
            getReviewList: getReviewList,               //查询专家评分

            //以下为新方法
            initReview: initReview,                      //初始化评审方案信息
            delSelectedExpert: delSelectedExpert,        //删除已选专家信息
            queryAutoExpert: queryAutoExpert,            //查询符合抽取条件的专家
            validateAutoExpert: validateAutoExpert,      //显示抽取专家效果(抽取方法已在后台封装)
            affirmAutoExpert: affirmAutoExpert,	         //确认已经抽取的专家
            updateJoinState: updateJoinState,            //更改是否参加状态
            findByBusinessId : findByBusinessId,         //根据业务ID查询评审方案信息

            saveMark: saveMark,                         // 保存专家评分
            savePayment: savePayment,                   // 保存专家费用
            countTaxes: countTaxes,                     // 计算应纳税额
            refleshBusinessEP : refleshBusinessEP,      //刷新业务的专家信息（已经确认和确定参加会议的专家）

            saveNewExpert:saveNewExpert,                //保存新的聘请专家信息,
            initNewExpertInfo:initNewExpertInfo,        // 初始化调整后的专家信息
            saveSplit : saveSplit ,                     // 保存评审费发放打印方案信息
        };
        return service;

        //S_saveSplit
        function saveSplit(vm){
            var httpOptions = {
                method: 'put',
                url: rootPath + "/expertReview/saveSplit",
                data : vm.expertSelect
            };
            var httpSuccess = function success(response) {
                bsWin.success("操作成功！", function(){
                    window.parent.$("#splitPayment").data("kendoWindow").close();
                });
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //E_saveSplit

        //S_initReview
        function initReview(businessId,minBusinessId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/initBybusinessId",
                params: {
                    businessId:businessId,
                    minBusinessId:minBusinessId
                }
            };
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_initReview

        function getMinColumns() {
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />", item.expertID)
                    },
                    filterable: false,
                    width: 25,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "name",
                    title: "姓名",
                    width: 100,
                    filterable: false,
                  template: function (item) {
                      return '<a  ng-click="vm.findExportDetail(\''+item.expertID+'\')">'+item.name+'</a>'
                  }
                },
                {
                    field: "degRee",
                    title: "学位",
                    width: 100,
                    filterable: false
                },

                {
                    field: "sex",
                    title: "性别",
                    width: 50,
                    filterable: true
                },
                {
                    field: "comPany",
                    title: "工作单位",
                    width: 100,
                    filterable: false
                },
                {
                    field: "degRee",
                    title: "职务",
                    width: 100,
                    filterable: false
                },{
                    field: "",
                    title: "专业大类",
                    width: 100,
                    template : function(item) {
                        if(item.expertTypeDtoList){
                            if(item.expertTypeDtoList.length > 1){
                                return "";
                            }else if(item.expertTypeDtoList.length == 1){

                                return item.expertTypeDtoList[0].maJorBig;
                            }
                        }else{
                            return "";
                        }
                    }
                },{
                    field: "",
                    title: "专业小类",
                    width: 100,
                    template : function(item) {
                        if(item.expertTypeDtoList){
                            if(item.expertTypeDtoList.length > 1){
                                return "";
                            }else if(item.expertTypeDtoList.length == 1){
                                return item.expertTypeDtoList[0].maJorSmall;
                            }
                        }else{
                            return "";
                        }
                    }
                },{
                    field: "",
                    title: "专家类型",
                    width: 100,
                    template : function(item) {
                        if(item.expertTypeDtoList){
                            if(item.expertTypeDtoList.length > 1){
                                return "";
                            }else if(item.expertTypeDtoList.length == 1){
                                return item.expertTypeDtoList[0].expertType;
                            }
                        }else{
                            return "";
                        }
                    }

                }
            ];
            return columns;
        }

        function initExpertGrid(vm) {
            var dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            }

            //S_专家自选
            var dataSource2 = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/expert/findByOData", $("#selfSelExpertForm")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            vm.selfExpertOptions = {
                dataSource: common.gridDataSource(dataSource2),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: getMinColumns(),
                dataBound: dataBound,
                resizable: true
            };//E_专家自选


            //S_市外专家
            var dataSource3 = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/expert/findExpertFieldByOData", $("#outSelExpertForm")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            vm.outExpertOptions = {
                dataSource: common.gridDataSource(dataSource3),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: getMinColumns(),
                dataBound: dataBound,
                resizable: true
            };//E_市外专家
        }

        //S_saveSelfExpert
        function saveSelfExpert(businessId,minBusinessId,businessType,expertId,expertReviewId,isCommit,callBack) {
            isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/saveExpertReview",
                params: {
                    businessId:businessId,
                    minBusinessId : minBusinessId,
                    businessType : businessType,
                    reviewId: angular.isUndefined(expertReviewId)?"":expertReviewId,
                    expertIds: expertId,
                    selectType: "2"
                }
            }
            var httpSuccess = function success(response) {
                isCommit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                    isCommit = false;
                }
            });
        }//E_saveSelfExpert

        //S_保存境外专家
        function saveOutExpert(businessId,minBusinessId,businessType,selExpertIds,expertReviewId,isCommit,callBack) {
            isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/saveExpertReview",
                params: {
                    businessId:businessId,
                    minBusinessId:minBusinessId,
                    businessType : businessType,
                    reviewId: angular.isUndefined(expertReviewId)?"":expertReviewId,
                    expertIds: selExpertIds,
                    selectType: "3"
                }
            }
            var httpSuccess = function success(response) {
                isCommit = false;
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
        }//E_saveOutExpert

        //S_countMatchExperts
        function countMatchExperts(postData,minBusinessId,expertReviewId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expert/countReviewExpert",
                data: postData,
                params: {
                    minBusinessId:minBusinessId,
                    reviewId: expertReviewId
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
                success: httpSuccess
            });
        }//E_countMatchExperts

        //begin##getReviewList
        function getReviewList(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/expertReview/html/getReviewList"
            }
            var httpSuccess = function success(response) {
                vm.reviewList = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                    vm.isCommit = false;
                }
            });
        }//end##getReviewList

        //S_queryAutoExpert
        function queryAutoExpert(conditionArr,minBusinessId,expertReviewId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expert/autoExpertReview",
                headers: {
                    "contentType": "application/json;charset=utf-8"  //设置请求头信息
                },
                traditional: true,
                dataType: "json",
                data: angular.toJson(conditionArr),//将Json对象序列化成Json字符串，JSON.stringify()原生态方法
                params: {
                    minBusinessId: minBusinessId,
                    reviewId: expertReviewId
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
        }//E_queryAutoExpert


        //S_validateAutoExpert
        function validateAutoExpert(allEPList,vm) {
            //随机抽取
            var timeCount = 0,totalExpertCount = vm.autoSelectedEPList.length,index = 0;
            var interValVar = $interval(function () {
                if (totalExpertCount == 0) {
                    $interval.cancel(interValVar);
                }else{
                    var selscope = Math.floor(Math.random() * (allEPList.length));
                    vm.showAutoExpertName = allEPList[selscope].name;
                    timeCount++;
                    if (timeCount % 10 == 0) {
                        vm.autoSelectedEPList[index].show = true;
                        vm.autoSelectedEPList[index].official = true;       //正式专家
                        vm.autoSelectedEPList[index+1].show = true;
                        vm.autoSelectedEPList[index+1].official = false;    //备选专家
                        index = index + 2;
                        totalExpertCount = totalExpertCount-2;
                    }
                }
            }, 200);
        }//E_validateAutoExpert

        //S_updateJoinState
        function updateJoinState(minBusinessId,businessType,ids, joinState,isCommit,callBack) {
            isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/updateJoinState",
                params: {
                    minBusinessId : minBusinessId,
                    businessType : businessType,
                    expertSelId: ids,
                    state: joinState
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
                success: httpSuccess,
                onError: function (response) {
                    isCommit = false;
                }
            });
        }//E_updateJoinState

        //S_affirmAutoExpert(确认抽取专家)
        function affirmAutoExpert(minBusinessId,businessType,seletedIds,joinState,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/affirmAutoExpert",
                params: {
                    minBusinessId: minBusinessId,
                    businessType: businessType,
                    expertSelId:seletedIds,
                    state: joinState
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
                success: httpSuccess,
                onError: function (response) {
                }
            });
        }//E_affirmAutoExpert

        //S_delSelectedExpert(删除已选专家)
        function delSelectedExpert(expertReviewId, delIds,isCommit,callBack) {
            isCommit = true;
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/expertSelected",
                params: {
                    reviewId: expertReviewId,
                    id: delIds,
                    deleteAll: false
                }
            }
            var httpSuccess = function success(response) {
                isCommit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                    isCommit = false;
                }
            });
        }//E_delSelectedExpert


        //S_根据业务ID查询评审方案信息
        function findByBusinessId(businessId,callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/initBybusinessId",
                params: {
                    businessId: businessId,
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
                success: httpSuccess,
                onError: function (response) {

                }
            });
        }

        // S_保存专家评分
        function saveMark(expertScore,callBack) {
            var httpOptions = {
                method: 'put',
                url: rootPath + "/expertSelected",
                data: expertScore,
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }// E_saveMark

        // S_保存专家评审费
        function savePayment(expertReview,isCommit,callBack) {
            isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/saveExpertReviewCostSingle",
                data: expertReview
            }
            var httpSuccess = function success(response) {
                isCommit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError:function () {
                    isCommit = false;
                }
            });
        }// E_savePayment

        // S_计算费用
        function countTaxes(reviewId,epIds,reviewDateStr,callBack) {
            var url = rootPath + "/expertReview/getExpertReviewCost?reviewId={0}&expertIds={1}&month={2}";
            //取得该评审方案评审专家在这个月的所有评审费用
            var httpOptions = {
                method: 'get',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                url: common.format(url, reviewId,epIds, reviewDateStr)
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }// E_countTaxes

        // S_validateNum
        function validateNum(expertReviews) {
            var isVilad = true;
            //计算每个评审的评审费是否正确
            if (expertReviews != undefined && expertReviews.length > 0) {
                expertReviews.forEach(function (v, i) {
                    if (v.payDate == undefined) {
                        v.errorMsg = "请选择发放日期";
                        isVilad = false;
                        return;
                    }
                    v.errorMsg = "";
                    //总评审费
                    var totalReviewCost = v.reviewCost == undefined ? 0 : v.reviewCost;
                    //总税额
                    var totalReviwTaxes = v.reviewTaxes == undefined ? 0 : v.reviewTaxes;
                    //总合计
                    var totalCost = v.totalCost == undefined ? 0 : v.totalCost;

                    //计算每个专家
                    if (v.expertSelectedDtoList != undefined && v.expertSelectedDtoList.length > 0) {

                        var tempTotalReviewCost = 0;
                        var tempTotalReviwTaxes = 0;
                        var tempTotalCost = 0;

                        v.expertSelectedDtoList.forEach(function (expertSelected, i) {
                            //评审费用
                            var reviewCost = expertSelected.reviewCost == undefined ? 0 : expertSelected.reviewCost;
                            //税额
                            var reviewTaxes = expertSelected.reviewTaxes == undefined ? 0 : expertSelected.reviewTaxes;
                            //合计
                            var totalCost = expertSelected.totalCost == undefined ? 0 : expertSelected.totalCost;
                            var tempCost = parseFloat(reviewCost) + parseFloat(reviewTaxes);
                            if (tempCost.toFixed(2) != parseFloat(totalCost).toFixed(2)) {
                                isVilad = false;
                                return;
                            }
                            tempTotalReviewCost = parseFloat(tempTotalReviewCost) + parseFloat(reviewCost);
                            tempTotalReviwTaxes = parseFloat(tempTotalReviwTaxes) + parseFloat(reviewTaxes);
                            tempTotalCost = parseFloat(tempTotalCost) + parseFloat(totalCost);
                        });

                        if (parseFloat(tempTotalReviewCost).toFixed(2) != parseFloat(totalReviewCost).toFixed(2)) {
                            isVilad = false;
                            return;
                        }
                        if (parseFloat(tempTotalReviwTaxes).toFixed(2) != parseFloat(totalReviwTaxes).toFixed(2)) {
                            isVilad = false;
                            return;
                        }
                        if (parseFloat(tempTotalCost).toFixed(2) != parseFloat(totalCost).toFixed(2)) {
                            isVilad = false;
                            return;
                        }
                    }
                });
            }

            return isVilad;
        }// E_validateNum

        //S_刷新业务的专家信息
        function refleshBusinessEP(businessId,callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/refleshBusinessEP",
                params: {
                    businessId : businessId
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
                onError:function () {

                }
            });
        }//E_refleshBusinessEP

        // S_保存修改过的最新聘请专家信息
        function saveNewExpert(expertNewInfo,callBack) {
            var httpOptions = {
                method : 'post',
                url : rootPath + "/expertReview/expertNewInfo",
                headers:{
                    "contentType":"application/json;charset=utf-8"  //设置请求头信息
                },
                traditional: true,
                dataType : "json",
                data : angular.toJson(expertNewInfo)//将Json对象序列化成Json字符串，JSON.stringify()原生态方法
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess,
            })

        }
        // end#saveExpert

        function initNewExpertInfo(businessId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/getExpertInfo",
                params: {
                    businessId : businessId
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
                onError:function () {

                }
            });
        }
    }
})();