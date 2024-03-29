(function () {
    'use strict';

    angular.module('app').factory('adminSvc', admin);

    admin.$inject = ['$http', 'bsWin'];

    function admin($http, bsWin) {

        var service = {
            gtasksGrid: gtasksGrid,		                //个人待办项目
            etasksGrid: etasksGrid,		                //办结项目
            dtasksGrid: dtasksGrid,                     //在办项目
            doingTaskGrid: doingTaskGrid,               //在办任务
            endTaskGrid : endTaskGrid,                  //办结任务
            personMainTasksGrid: personMainTasksGrid,   //个人经办项目
            countWorakday: countWorakday,	            //计算工作日
            agendaTaskGrid: agendaTaskGrid,             //个人待办任务（除项目流程外）
            initFile: initFile,	        //初始化附件
            upload: upload,	            //	下载附件
            getSignList: getSignList,   //项目查询统计
            initSignList: initSignList, //初始化項目查詢統計
            // <!-- 以下是首页方法-->
            getHomeInfo: getHomeInfo,                   //初始化首页待办任务和通知公告方法
            getHomeProjInfo:getHomeProjInfo,            //获取首页统计信息
            getHomeMeetInfo:getHomeMeetInfo,            //获取首页会议和调研时间统计信息
            excelExport: excelExport,                   //项目统计导出
            statisticalGrid: statisticalGrid,           //(停用)
            workName: workName,                         //获取流程列表
            QueryStatistics: QueryStatistics,           //通过条件，对项目进行查询统计
            signDetails: signDetails,                   //获取项目查看权限
            encodePwd : encodePwd , //对秘密加密
        }
        return service;

        function encodePwd(callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/account/encodePwd",
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof  callBack == 'function') {
                    callBack(response.data);
                }
            }

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        function signDetails(signId, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/findSecretProPermission",
                params: {signId: signId}
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof  callBack == 'function') {
                    callBack(response.data);
                }
            }

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //begin excelExport
        function excelExport(vm, fileName, project) {
            var fileName = escape(encodeURIComponent(fileName));
            window.open(rootPath + "/signView/excelExport?filterData=" + project + "&fileName=" + fileName);
            /* var httpOptions ={
             method : 'post',
             url : rootPath + "/signView/excelExport",
             headers : {
             "contentType" : "application/json;charset=utf-8"
             },
             traditional : true,
             dataType : "json",
             responseType: 'arraybuffer',
             data : angular.toJson(exportData),
             params:{
             fileName :fileName
             }

             }
             var httpSuccess = function success(response){
             fileName =fileName + ".xls";
             var fileType ="vnd.ms-excel";
             common.downloadReport(response.data , fileName , fileType);
             }
             common.http({
             vm : vm,
             $http : $http ,
             httpOptions : httpOptions,
             success : httpSuccess
             });*/
        }

        //end excelExport

        //S_初始化首页方法
        function getHomeInfo(callBack) {
            var httpOptions = {
                method: "post",
                url: rootPath + "/admin/getHomeInfo"
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
        }//E_getHomeInfo

        //S_首页项目统计信息
        function getHomeProjInfo(callBack) {
            var httpOptions = {
                method: "post",
                url: rootPath + "/admin/getHomeProjInfo"
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
        }//E_getHomeProjInfo

        //S_首页会议和调研时间统计
        function getHomeMeetInfo(callBack) {
            var httpOptions = {
                method: "post",
                url: rootPath + "/admin/getHomeMeetInfo"
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
        }//E_getHomeProjInfo

        //begin countWorakday
        function countWorakday(vm) {
            var httpOptions = {
                method: "get",
                url: rootPath + "/workday/countWorkday"
            }

            var httpSuccess = function success(response) {
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }//end countWorakday

        //begin initFile
        function initFile(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/file/findByBusinessId",
                params: {businessId: vm.anId}
            }

            var httpSuccess = function success(response) {
                vm.file = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//end initFile

        //begin upload
        function upload(vm, sysFileId) {
            window.open(rootPath + "/file/fileDownload?sysfileId=" + sysFileId);
        }//end upload

        //S_gtasksGrid
        function gtasksGrid(vm) {
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/flow/html/tasks", $("#searchform"), vm.gridParams),
                schema: {
                    data: "value",
                    total: function (data) {
                        if (data['count']) {
                            $('#DO_SIGN_COUNT').html(data['count']);
                        } else {
                            $('#DO_SIGN_COUNT').html(0);
                        }
                        return data['count'];
                    },
                    model: {
                        id: "id",
                        fields: {
                            createdDate: {
                                type: "date"
                            }
                        }
                    }
                },
                serverPaging: false,
                serverSorting: false,
                serverFiltering: false,
                pageSize: vm.queryParams.pageSize || 10,
                page: vm.queryParams.page || 1,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            var columns = [
                {
                    field: "",
                    title: "",
                    width: 30,
                    template: function (item) {
                        switch (item.lightState) {
                            case "4":          //暂停
                                return $('#span1').html();
                                break;
                            case "5":          //少于3个工作日
                                return $('#span2').html();
                                break;
                            case "6":          	//发文超期
                                return $('#span3').html();
                                break;
                            case "7":           //超过25个工作日未存档
                                return $('#span4').html();
                                break;
                            case "8":         	//存档超期
                                return $('#span5').html();
                                break;
                            case "1":          //在办
                                return "";
                                break;
                            case "2":           //已发文
                                return "";
                                break;
                            case "3":           //已发送存档
                                return "";
                                break;
                            default:
                                return "";
                                ;
                        }
                    }
                },
                {
                    field: "",
                    title: "序号",
                    width: 50,
                    attributes: {
                        "class": "table-cell",
                        style: "text-align: center"
                    },
                    template: "<span class='row-number'></span>",
                },
                {
                    field: "",
                    title: "项目名称",
                    filterable: false,
                    width: 200,
                    template: function (item) {
                        if (checkCanEdit(item)) {
                            if(item.isAgent == 9){
                                return '<span class="label label-primary">代办</span><a ng-click="vm.saveView()" href="#/signFlowDetail/' + item.businessKey + '/' + item.taskId + '/' + item.processInstanceId + '" >' + item.projectName + '</a>';
                            }else{
                                return '<a ng-click="vm.saveView()" href="#/signFlowDetail/' + item.businessKey + '/' + item.taskId + '/' + item.processInstanceId + '" >' + item.projectName + '</a>';
                            }
                        } else {
                            if(item.isAgent == 9){
                                return '<span class="label label-primary">代办</span><a ng-click="vm.saveView()" href="#/signFlowDeal/' + item.businessKey + '/' + item.taskId + '/' + item.processInstanceId + '" >' + item.projectName + '</a>';
                            }else{
                                return '<a ng-click="vm.saveView()" href="#/signFlowDeal/' + item.businessKey + '/' + item.taskId + '/' + item.processInstanceId + '" >' + item.projectName + '</a>';
                            }
                        }
                    }
                },
                {
                    field: "reviewStage",
                    title: "项目阶段",
                    filterable: false,
                    width: 100
                },
                {
                    field: "filecode",
                    title: "委内收文编号",
                    filterable: false,
                    width: 120
                },
                {
                    field: "nodeNameValue",
                    title: "当前环节",
                    width: 120,
                    filterable: false
                },
                {
                    field: "preSignDate",
                    title: "预签收时间",
                    width: 100,
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "signDate",
                    title: "签收时间",
                    width: 100,
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "",
                    title: "签收状态",
                    width: 100,
                    filterable: false,
                    template: function (item) {
                        if (!item.signDate) {
                            if(item.preSignDate){
                                return "<span style='color:#ff0000;'>预签收</span>";
                            }else{
                                return "";
                            }
                        } else {
                            return "<span style='color: #2b9d00;'>正式签收</span>";
                        }
                    }
                },
                {
                    field: "allPriUser",
                    title: "项目负责人",
                    width: 140,
                    filterable: false,
                },
                {
                    field: "",
                    title: "剩余工作日",
                    width: 100,
                    filterable: false,
                    template: function (item) {
                        if (item.surplusDays != undefined) {
                            return item.surplusDays;
                        } else {
                            return "";
                        }
                    }
                },
                {
                    field: "displayName",
                    title: "处理人",
                    width: 120,
                    filterable: false
                },
                {
                    field: "",
                    title: "状态",
                    width: 80,
                    filterable: false,
                    template: function (item) {
                        if (item.processState && item.processState == 2) {
                            return '<span style="color:orange;">已暂停</span>';
                        } else {
                            return '<span style="color:green;">进行中</span>';
                        }
                    }
                },
                {
                    field: "",
                    title: "合并评审",
                    width: 120,
                    filterable: false,
                    template: function (item) {
                        if (item.reviewType) {
                            if (item.reviewType == 9 || item.reviewType == '9') {
                                return "合并评审[主项目]";
                            } else {
                                return "合并评审[次项目]";
                            }
                        } else {
                            return "否";
                        }
                    }
                },
                {
                    field: "",
                    title: "合并项目",
                    width: 180,
                    filterable: false,
                    template: function (item) {
                        if (item.reviewSignDtoList) {
                            var projectName = '';
                            angular.forEach(item.reviewSignDtoList, function (data, index, array) {
                                if (index > 0) {
                                    projectName += ",";
                                }
                                projectName += '<a href="#/signDetails/' + data.signid + '/' + data.processInstanceId + '" >' + data.projectname + '</a>';
                            });
                            return projectName;
                        } else {
                            return "";
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 80,
                    template: function (item) {
                        if (checkCanEdit(item)) {
                            return common.format($('#detailBtns').html(), "signFlowDetail", item.businessKey, item.taskId, item.processInstanceId);
                        } else {
                            return common.format($('#columnBtns').html(), "signFlowDeal", item.businessKey, item.taskId, item.processInstanceId);
                        }
                    }
                }
            ];// End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                pageable: common.kendoGridConfig(vm.queryParams).pageable,
                dataBound: common.kendoGridConfig(vm.queryParams).dataBound
            };
        }//E_gtasksGrid

        //判断是否显示处理按钮（主要针对合并评审项目,部长审核和分管领导审核两个环节）
        function checkCanEdit(item) {
            var isDetailBt = (item.processState == 2) ? true : false;
            var reviewNode = ["SIGN_BMLD_SPW1","SIGN_FGLD_SPW1"];
            var mergeDisNode = ["SIGN_QRFW","SIGN_BMLD_QRFW","SIGN_FGLD_QRFW","SIGN_ZR_QRFW"];
            //合并评审
            if (!isDetailBt) {
                if (item.reviewType && (item.reviewType == 0 || item.reviewType == '0') && ($.inArray(item.nodeDefineKey, reviewNode) >= 0)) {
                    isDetailBt = true;
                }
            }
            //合并发文
            if (!isDetailBt) {
                if (item.mergeDis && item.mergeDis == 2 && item.mergeDisMain == 0 && ($.inArray(item.nodeDefineKey, mergeDisNode) >= 0) ) {
                    isDetailBt = true;
                }
            }
            return isDetailBt;
        }

        //S_etasksGrid
        function etasksGrid(vm) {
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/flow/html/endTasks"),
                schema: {
                    data: "value",
                    total: function (data) {
                        return data['count'];
                    },
                    model: {
                        id: "id",
                        fields: {
                            createdDate: {
                                type: "date"
                            }
                        }
                    }
                },
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            var columns = [
                {
                    field: "",
                    title: "序号",
                    template: "<span class='row-number'></span>",
                    width: 50
                },
                {
                    field: "projectname",
                    title: "项目名称",
                    filterable: false,
                    width: 180
                },
                {
                    field: "builtcompanyname",
                    title: "建设单位",
                    width: 150,
                    filterable: false,
                },
                {
                    field: "reviewstage",
                    title: "评审阶段",
                    width: 80,
                    filterable: false,

                },
                {
                    field: "signdate",
                    title: "签收日期",
                    width: 100,
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "reviewdays",
                    title: "评审天数",
                    width: 150,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 100,
                    template: function (item) {
                        /*       if (item.flowKey == flowcommon.getFlowDefinedKey().FINAL_SIGN_FLOW) {
                         return common.format($('#columnBtns').html(), "endSignDetail", item.businessKey, item.processInstanceId);
                         } else if (item.flowKey) {
                         return common.format($('#columnBtns').html(), "flowEnd/" + item.businessKey, item.flowKey, item.processInstanceId);
                         } else {
                         return "";
                         }*/
                        return common.format($('#columnBtns').html(), item.isAppraising, item.signid, "endSignDetail", item.signid, item.processInstanceId, item.mUserName);
                    }
                }
            ];
            // End:column
            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                dataBound: function () {
                    var rows = this.items();
                    var page = this.pager.page() - 1;
                    var pagesize = this.pager.pageSize();
                    $(rows).each(function () {
                        var index = $(this).index() + 1 + page * pagesize;
                        var rowLabel = $(this).find(".row-number");
                        $(rowLabel).html(index);
                    });
                }
            };
        }//E_etasksGrid

        //S_在办项目(改成页面分页)
        function dtasksGrid(vm) {
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/flow/html/doingtasks", $("#searchform"), vm.gridParams),
                schema: common.kendoGridConfig().schema({
                    id: "id"
                }),
                serverPaging: false,
                serverSorting: false,
                serverFiltering: false,
                pageSize: vm.queryParams.pageSize || 10,
                page: vm.queryParams.page || 1,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            var columns = [
                {
                    field: "",
                    title: "",
                    width: 30,
                    template: function (item) {
                        switch (item.lightState) {
                            case "4":          //暂停
                                return $('#span1').html();
                                break;
                            case "5":          //少于3个工作日
                                return $('#span2').html();
                                break;
                            case "6":          	//发文超期
                                return $('#span3').html();
                                break;
                            case "7":           //超过25个工作日未存档
                                return $('#span4').html();
                                break;
                            case "8":         	//存档超期
                                return $('#span5').html();
                                break;
                            case "1":          //在办
                                return "";
                                break;
                            case "2":           //已发文
                                return "";
                                break;
                            case "3":           //已发送存档
                                return "";
                                break;
                            default:
                                return "";
                                ;
                        }
                    }
                },
                {
                    field: "",
                    title: "序号",
                    width: 50,
                    attributes: {
                        style: "text-align: center;",
                    },
                    template: function (item) {
                        if (item.signprocessState) {
                            if (item.signprocessState == 6) {
                                return "<span class='row-number label-info' style='width: 100%;display: inline-block;'></span>";
                            }
                            else if (item.signprocessState == 7) {
                                return "<span class='row-number label-primary' style='width: 100%;display: inline-block;'></span>";
                            }
                            else if (item.signprocessState == 8) {
                                return "<span class='row-number label-success' style='width: 100%;display: inline-block;'></span>";
                            } else {
                                return "<span class='row-number'></span>";
                            }
                        }
                        return "<span class='row-number'></span>";
                    }
                },
                {
                    field: "",
                    title: "项目名称",
                    filterable: false,
                    width: 260,
                    template: function (item) {
                        return '<a ng-click="vm.saveView()" href="#/signFlowDetail/' + item.businessKey + '/' + item.taskId + '/' + item.processInstanceId + '" >' + item.projectName + '</a>';
                    }
                },
                {
                    field: "reviewStage",
                    title: "项目阶段",
                    filterable: false,
                    width: 120
                },
                {
                    field: "filecode",
                    title: "委内收文编号",
                    filterable: false,
                    width: 120
                },
                {
                    field: "nodeNameValue",
                    title: "办理环节",
                    filterable: false,
                    width: 120
                },
                {
                    field: "signDate",
                    title: "签收时间",
                    width: 100,
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "",
                    title: "签收状态",
                    width: 100,
                    filterable: false,
                    template: function (item) {
                        if (!item.signDate) {
                            if(item.preSignDate){
                                return "<span style='color:#ff0000;'>预签收</span>";
                            }else{
                                return "";
                            }
                        } else {
                            return "<span style='color: #2b9d00;'>正式签收</span>";
                        }
                    }
                },
                {
                    field: "expectdispatchdate",
                    title: "预发文时间",
                    width: 100,
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "",
                    title: "剩余工作日",
                    width: 100,
                    filterable: false,
                    template: function (item) {
                        if (item.surplusDays != undefined) {
                            return item.surplusDays;
                        } else {
                            return "";
                        }
                    }
                },
                {
                    field: "",
                    title: "评审部门",
                    width: 160,
                    filterable: false,
                    template: function (item) {
                        if(item.mOrgName){
                            if (item.aOrgName) {
                                return item.mOrgName+","+item.aOrgName;
                            } else {
                                return item.mOrgName;
                            }
                        }else{
                            return "";
                        }
                    }
                },
                {
                    field: "allPriUser",
                    title: "项目负责人",
                    width: 160,
                    filterable: false,
                },
                {
                    field: "",
                    title: "项目状态",
                    width: 90,
                    filterable: false,
                    template: function (item) {
                        if (item.processState && item.processState == 2) {
                            return '<span style="color:orange;">已暂停</span>';
                        } else {
                            return '<span style="color:green;">进行中</span>';
                        }
                    }
                },
                {
                    field: "preSignDate",
                    title: "预签收时间",
                    width: 100,
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "",
                    title: "操作",
                    width: 130,
                    template: function (item) {
                        return common.format($('#columnBtns').html(), "signFlowDetail", item.businessKey, item.taskId, item.processInstanceId);
                    }
                }
            ];
            // End:column
            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig(vm.queryParams).pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                dataBound: common.kendoGridConfig(vm.queryParams).dataBound
            };
        }//E_dtasksGrid

        //begin_getSignList
        function getSignList(vm) {
            var dataSource = common.kendoGridDataSource(rootPath + "/signView/getSignList", $("#searchform"), vm.queryParams.page, vm.queryParams.pageSize, vm.gridParams);
            // Begin:column
            var columns = [
                {
                    field: "",
                    title: "",
                    width: 30,
                    template: function (item) {
                        switch (item.lightState) {
                            case "4":          //暂停
                                return $('#span1').html();
                                break;
                            case "5":          //少于3个工作日
                                return $('#span2').html();
                                break;
                            case "6":          	//发文超期
                                return $('#span3').html();
                                break;
                            case "7":           //超过25个工作日未存档
                                return $('#span4').html();
                                break;
                            case "8":         	//存档超期
                                return $('#span5').html();
                                break;
                            case "1":          //在办
                                return "";
                                break;
                            case "2":           //已发文
                                return "";
                                break;
                            case "3":           //已发送存档
                                return "";
                                break;
                            default:
                                return "";
                                ;
                        }
                    }
                },
                {
                    field: "",
                    title: "序号",
                    width: 50,
                    attributes: {
                        style: "text-align: center;",
                    },
                    template: function (item) {
                        if (item.processState) {
                            if (item.processState == 6) {
                                return "<span class='row-number label-info' style='width: 100%;display: inline-block;'></span>";
                            }
                            else if (item.processState == 7) {
                                return "<span class='row-number label-primary' style='width: 100%;display: inline-block;'></span>";
                            }
                            else if (item.processState == 8) {
                                return "<span class='row-number label-success' style='width: 100%;display: inline-block;'></span>";
                            }else if(item.processState == 9){

                                return "<span class='row-number label-default' style='width: 100%;display: inline-block;'></span>";
                            } else {
                                return "<span class='row-number'></span>";
                            }
                        }
                        return "<span class='row-number'></span>";
                    }
                },
                {
                    field: "",
                    title: "项目名称",
                    width: 280,
                    filterable: false,
                    template: function (item) {
                        //秘密项目和未发文的项目，都只有经办人能看
                        if (item.secrectlevel == "秘密" || item.processState < 6) {
                            if (item.processInstanceId) {
                                return '<a ng-click="vm.signDetails(' + "'" + item.signid + "'," + "'" + item.processInstanceId + "'" + ')" >' + item.projectname + '</a>';
                            } else {
                                return '<a ng-click="vm.signDetails(' + "'" + item.signid + "'," + "'" + "'" + ')" >' + item.projectname + '</a>';
                            }
                        } else {
                            if (item.processInstanceId) {
                                return '<a ng-click="vm.saveView()" href="#/signDetails/' + item.signid + '/' + item.processInstanceId + '" >' + item.projectname + '</a>';
                            } else {
                                return '<a ng-click="vm.saveView()" href="#/signDetails/' + item.signid + '/" >' + item.projectname + '</a>';
                            }
                        }


                    }
                },
                {
                    field: "reviewstage",
                    title: "评审阶段",
                    width: 110,
                    filterable: false,
                },
                {
                    field: "filecode",
                    title: "委内收文编号",
                    filterable: false,
                    width: 120
                },
                {
                    field: "signdate",
                    title: "签收日期",
                    width: 100,
                    filterable: false,
                    format: "{0:yyyy-MM-dd}"
                },
                {
                    field: "dispatchDate",
                    title: "发文日期",
                    width: 100,
                    filterable: false,
                    format: "{0:yyyy-MM-dd}"
                },
                {
                    field: "reviewdays",
                    title: "评审天数",
                    width: 90,
                    filterable: false
                },
                {
                    field: "",
                    title: "剩余工作日",
                    width: 100,
                    filterable: false,
                    template: function (item) {
                        if (item.surplusdays != undefined) {
                            return item.surplusdays;
                        } else {
                            return "";
                        }
                    }
                },
                {
                    field: "",
                    title: "评审部门",
                    width: 140,
                    filterable: false,
                    template : function(item){
                        if(!item.aOrgName){
                            return item.mOrgName ? item.mOrgName : "";
                        }else{
                            return item.mOrgName + "," + item.aOrgName;
                        }
                    }
                },
                {
                    field: "allPriUser",
                    title: "项目负责人",
                    width: 110,
                    filterable: false
                },
                {
                    field: "ffilenum",
                    title: "归档编号",
                    width: 125,
                    filterable: false
                },
                {
                    field: "dfilenum",
                    title: "文件字号",
                    width: 130,
                    filterable: false
                },
                {
                    field: "appalyInvestment",
                    title: "申报投资",
                    width: 100,
                    filterable: false
                },
                {
                    field: "authorizeValue",
                    title: "审定投资",
                    width: 100,
                    filterable: false
                },
                {
                    field: "extraValue",
                    title: "核减（增）",
                    width: 100,
                    filterable: false
                },
                {
                    field: "extraRate",
                    title: "核减率",
                    width: 100,
                    filterable: false
                },
                {
                    field: "approveValue",
                    title: "批复金额",
                    width: 140,
                    filterable: false
                },
                {
                    field: "dispatchType",
                    title: "发文类型",
                    width: 100,
                    filterable: false
                },
                {
                    field: "fileDate",
                    title: "归档日期",
                    width: 100,
                    filterable: false,
                    format: "{0:yyyy-MM-dd}"
                },
                {
                    field: "builtcompanyname",
                    title: "建设单位",
                    width: 260,
                    filterable: false
                },
                {
                    field: "isassistproc",
                    title: "是否协审",
                    width: 80,
                    filterable: false,
                    template: function (item) {
                        if (item.isassistproc == 9) {
                            return "是";
                        } else {
                            return "否";
                        }
                    }
                },
                {
                    field: "daysafterdispatch",
                    title: "发文后工作日",
                    width: 120,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 90,
                    filterable: false,
                    template: function (item) {
                        if (item.signState == '2') {
                            return common.format($('#columnBtns').html(),
                                "vm.ProjectStopInfo('" + item.signid + "')");
                        } else {
                            return "";
                        }

                    }
                }
            ];

            // End:column
            vm.signListOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                /*  pageable: common.kendoGridConfig().pageable,*/
                /*pageable: common.kendoGridConfig(vm.queryParams).pageable,*/
                pageable: {
                    pageSize: 10,
                    previousNext: true,
                    buttonCount: 5,
                    refresh: true,
                    pageSizes: [10, 20, 30,50],
                    change: function () {
                        if (vm.queryParams && vm.queryParams.page) {
                            vm.queryParams.page = this.dataSource.page();
                        }
                    }
                },
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                dataBound: common.kendoGridConfig(vm.queryParams).dataBound
                 /* dataBound: function () {
                    var rows = this.items();
                    var page = this.pager.page() - 1;
                    var pagesize = this.pager.pageSize();
                    $(rows).each(function () {
                        var index = $(this).index() + 1 + page * pagesize;
                        var rowLabel = $(this).find(".row-number");
                        $(rowLabel).html(index);
                    });
                }*/
            };
        }//end_getSignList

        //begin_initSignList
        function initSignList(callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/initSignList"
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
        } //end_initSignList

        //begin persontasksGrid
        function personMainTasksGrid(vm) {
            /*            var dataSource = new kendo.data.DataSource({
                            type: 'odata',
                            transport: common.kendoGridConfig().transport(rootPath + "/signView/html/personMainTasks", $("#searchform")),
                            schema: {
                                data: "value",
                                total: function (data) {
                                    return data['count'];
                                },
                                model: {
                                    id: "id"
                                }
                            },
                            serverPaging: true,
                            serverSorting: true,
                            serverFiltering: true,
                            pageSize: 10,
                            sort: {
                                field: "signdate",
                                dir: "desc"
                            }
                        });*/
            var dataSource = common.kendoGridDataSource(rootPath + "/signView/html/personMainTasks?$orderby=signdate desc", $("#searchform"), vm.queryParams.page, vm.queryParams.pageSize, vm.gridParams);
            var columns = [
                {
                    field: "",
                    title: "",
                    width: 30,
                    template: function (item) {
                        switch (item.lightState) {
                            case "4":          //暂停
                                return $('#span1').html();
                                break;
                            case "5":          //少于3个工作日
                                return $('#span2').html();
                                break;
                            case "6":          	//发文超期
                                return $('#span3').html();
                                break;
                            case "7":           //超过25个工作日未存档
                                return $('#span4').html();
                                break;
                            case "8":         	//存档超期
                                return $('#span5').html();
                                break;
                            case "1":          //在办
                                return "";
                                break;
                            case "2":           //已发文
                                return "";
                                break;
                            case "3":           //已发送存档
                                return "";
                                break;
                            default:
                                return "";
                                ;
                        }
                    }
                },
                {
                    field: "",
                    title: "序号",
                    width: 50,
                    attributes: {
                        style: "text-align: center;",
                    },
                    template: function (item) {
                        if (item.processState) {
                            if (item.processState == 6) {
                                return "<span class='row-number label-info' style='width: 100%;display: inline-block;'></span>";
                            }
                            else if (item.processState == 7) {
                                return "<span class='row-number label-primary' style='width: 100%;display: inline-block;'></span>";
                            }
                            else if (item.processState == 8) {
                                return "<span class='row-number label-success' style='width: 100%;display: inline-block;'></span>";
                            } else {
                                return "<span class='row-number'></span>";
                            }
                        }
                        return "<span class='row-number'></span>";
                    }
                },
                {
                    field: "",
                    title: "项目名称",
                    width: 320,
                    filterable: false,
                    template: function (item) {
                        if (item.processInstanceId) {
                            return '<a ng-click="vm.saveView()" href="#/signDetails/' + item.signid + '/' + item.processInstanceId + '" >' + item.projectname + '</a>';
                        } else {
                            return '<a ng-click="vm.saveView()" href="#/signDetails/' + item.signid + '/" >' + item.projectname + '</a>';
                        }
                    }
                },
                {
                    field: "reviewstage",
                    title: "评审阶段",
                    width: 100,
                    filterable: false
                },
                {
                    field: "",
                    title: "项目状态",
                    width: 80,
                    filterable: false,
                    template: function (item) {
                        var returnStr = "";
                        switch (item.signState) {
                            case "1":
                                returnStr = "进行中";
                                break;
                            case "2":
                                returnStr = "暂停";
                                break;
                            case "8":
                                returnStr = "强制结束";
                                break;
                            case "9":
                                returnStr = "已完成";
                                break;
                            default:
                                ;
                        }
                        return returnStr;
                    }
                },
                {
                    field: "",
                    title: "签收时间",
                    width: 100,
                    filterable: false,
                    template: function (item) {
                        if (item.signdate) {
                            return (new Date((item.signdate).CompatibleDate())).Format("yyyy-MM-dd");
                        } else {
                            return "";
                        }
                    },
                },
                {
                    field: "",
                    title: "发文日期",
                    width: 100,
                    filterable: false,
                    template: function (item) {
                        if (item.dispatchDate) {
                            return (new Date((item.dispatchDate).CompatibleDate())).Format("yyyy-MM-dd");
                        } else {
                            return "";
                        }
                    },
                },
                {
                    field: "",
                    title: "剩余工作日",
                    width: 100,
                    filterable: false,
                    template: function (item) {
                        if (item.surplusdays != undefined) {
                            return item.surplusdays;
                            // return (item.surplusdays > 0) ? item.surplusdays : 0;
                        } else {
                            return "";
                        }
                    }
                },
                /*{
                 field: "receivedate",
                 title: "送来时间",
                 width: 100,
                 filterable: false
                 },*/
                {
                    field: "builtcompanyname",
                    title: "建设单位",
                    width: 250,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 100,
                    template: function (item) {
                        var isstart = false, applyArrrais = true, isStop = false;
                        if (item.signState == "2") {
                            isstart = true;//显示已暂停，提示启动
                        } else {
                            if (item.signState == "1")
                                isStop = true;//显示暂停
                        }

                        //已经完成并且还未申请的，可以申请优秀评审报告
                        if (item.signState == 9 && angular.isUndefined(item.isAppraising)) {
                            applyArrrais = false;
                        }

                        return common.format($('#columnBtns').html(), "signDetails", item.signid, item.processInstanceId,
                            "vm.pauseProject('" + item.signid + "')", isStop, "vm.startProject('" + item.signid + "')", isstart, item.signid, applyArrrais);
                    }
                }
            ];

            // End:column
            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                pageable: common.kendoGridConfig(vm.queryParams).pageable,
                dataBound: common.kendoGridConfig(vm.queryParams).dataBound,
            };

            // End:column
        }

        //end persontasksGrid

        //S_个人待办任务
        function agendaTaskGrid(vm) {
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/flow/queryMyAgendaTask", $('#agendaTaskForm')),
                schema: {
                    data: "value",
                    total: function (data) {
                        if (data['count']) {
                            $('#DO_TASK_COUNT').html(data['count']);
                        } else {
                            $('#DO_TASK_COUNT').html(0);
                        }
                        return data['count'];
                    },
                    model: {
                        id: "taskId"
                    }
                },
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createTime",
                    dir: "desc"
                }
            });
            var columns = [
                {
                    field: "",
                    title: "序号",
                    template: "<span class='row-number'></span>",
                    width: 50
                },
                {
                    field: "",
                    title: "任务名称",
                    filterable: false,
                    width: "25%",
                    template: function (item) {
                        return '<a href="#/flowDeal/' + item.businessKey + '/' + item.processKey + '/' + item.taskId + '/' + item.instanceId + '" >' + item.instanceName + '</a>';
                    }
                },
                {
                    field: "nodeNameValue",
                    title: "当前环节",
                    width: "15%",
                    filterable: false
                },
                {
                    field: "displayName",
                    title: "处理人",
                    width: "15%",
                    filterable: false,
                },
                {
                    field: "processName",
                    title: "流程类别",
                    width: "15%",
                    filterable: false,
                },
                {
                    field: "",
                    title: "流程状态",
                    width: "10%",
                    filterable: false,
                    template: function (item) {
                        if (item.processState && item.processState == 2) {
                            return '<span style="color:orange;">已暂停</span>';
                        } else {
                            return '<span style="color:green;">进行中</span>';
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: "15%",
                    template: function (item) {
                        return common.format($('#columnBtns').html(), item.businessKey, item.processKey, item.taskId, item.instanceId,"vm.deleteTask('"+item.instanceId+"')");
                    }
                }
            ];
            // End:column
            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                dataBound: function () {
                    var rows = this.items();
                    var page = this.pager.page() - 1;
                    var pagesize = this.pager.pageSize();
                    $(rows).each(function () {
                        var index = $(this).index() + 1 + page * pagesize;
                        var rowLabel = $(this).find(".row-number");
                        $(rowLabel).html(index);
                    });
                }

            };
        }//E_agendaTaskGrid

        //S_个人经办办结任务
        function endTaskGrid(vm){
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/flow/queryEndTask",$("#endTaskForm"),vm.gridParams,false),
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
                pageSize : vm.queryParams.pageSize||10,
                page:vm.queryParams.page||1,
                sort: {
                    field: "endDate",
                    dir: "desc"
                }
            });
            var columns = [
                {
                    field: "",
                    title: "序号",
                    template: "<span class='row-number'></span>",
                    width: 50,
                },
                {
                    field: "",
                    title: "任务名称",
                    filterable: false,
                    width: "30%",
                    template: function (item) {
                        return '<a ng-click="vm.saveView()" href="#/flowEnd/' + item.businessKey + '/' + item.flowKey + '/' + item.processInstanceId + '" >' + item.flowName + '</a>';
                    }
                },
                {
                    field: "createDate",
                    title: "流程开始时间",
                    width: "20%",
                    filterable: false
                },
                {
                    field: "endDate",
                    title: "流程结束时间",
                    width: "15%",
                    filterable: false,
                },
                {
                    field: "durationTime",
                    title: "总处理时长",
                    width: "25%",
                    filterable: false,
                },
                {
                    field: "",
                    title: "流程类别",
                    width: "25%",
                    filterable: false,
                    template:function(item){
                        var flowKeyNmae = "";
                        if(vm.workName){
                            vm.workName.forEach(function (flow, index) {
                                if(flow.KEY_ == item.flowKey){
                                    flowKeyNmae = flow.NAME_;
                                }
                            });
                        }
                        return flowKeyNmae;
                    }
                },

                {
                    field: "",
                    title: "操作",
                    width: "10%",
                    template: function (item) {
                        return common.format($('#columnBtns').html(), item.businessKey, item.flowKey, item.processInstanceId);
                    }
                }
            ];
            // End:column
            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                pageable: common.kendoGridConfig(vm.queryParams).pageable,
                dataBound: common.kendoGridConfig(vm.queryParams).dataBound,
            };
        }

        //S_所有在办任务
        function doingTaskGrid(vm) {
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/flow/queryAgendaTask",$("#doingTaskForm"),vm.gridParams,false),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: false,
                serverSorting: false,
                serverFiltering: false,
                pageSize : vm.queryParams.pageSize||10,
                page:vm.queryParams.page||1,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            //var dataSource = common.kendoGridDataSource(rootPath + "/flow/queryAgendaTask", $("#doingTaskForm"), vm.queryParams.page, vm.queryParams.pageSize, vm.gridParams);
            var columns = [
                {
                    field: "",
                    title: "序号",
                    template: "<span class='row-number'></span>",
                    width: 50,
                },
                {
                    field: "",
                    title: "任务名称",
                    filterable: false,
                    width: "30%",
                    template: function (item) {
                        return '<a ng-click="vm.saveView()" href="#/flowDetail/' + item.businessKey + '/' + item.processKey + '/' + item.taskId + '/' + item.instanceId + '" >' + item.instanceName + '</a>';
                    }
                },
                {
                    field: "nodeNameValue",
                    title: "当前环节",
                    width: "20%",
                    filterable: false
                },
                {
                    field: "displayName",
                    title: "处理人",
                    width: "15%",
                    filterable: false,
                },
                {
                    field: "processName",
                    title: "流程类别",
                    width: "25%",
                    filterable: false,
                },
                {
                    field: "",
                    title: "流程状态",
                    width: "10%",
                    filterable: false,
                    template: function (item) {
                        if (item.processState && item.processState == 2) {
                            return '<span style="color:orange;">已暂停</span>';
                        } else {
                            return '<span style="color:green;">进行中</span>';
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: "10%",
                    template: function (item) {
                        return common.format($('#columnBtns').html(), item.businessKey, item.processKey, item.taskId, item.instanceId,"vm.deleteTask('"+item.instanceId+"')");
                    }
                }
            ];
            // End:column
            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                pageable: common.kendoGridConfig(vm.queryParams).pageable,
                dataBound: common.kendoGridConfig(vm.queryParams).dataBound,
            };
        }//S_doingTaskGrid

        /**
         * 统计
         * @param vm
         */
        //begin statisticalGrid
        function statisticalGrid(vm) {
            vm.dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/signView/getSignList", $("#searchform")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: false,
                serverSorting: true,
                serverFiltering: true,
                // pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
        }

        //begin countWorakday
        function workName(vm) {
            var httpOptions = {
                method: "get",
                url: rootPath + "/flow/proc"
            }
            var httpSuccess = function success(response) {
                vm.workName = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }//end countWorakday

        /**
         * 通过条件对项目进行查询统计分析
         * @param vm
         */
        function QueryStatistics(vm, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/QueryStatistics",
                // data : vm.project,
                params: {queryData: vm.filters, page: vm.page}
            }

            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof  callBack == 'function') {
                    callBack(response.data);
                }
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

    }
})();