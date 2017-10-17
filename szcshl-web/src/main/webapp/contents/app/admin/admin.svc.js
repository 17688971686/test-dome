(function () {
    'use strict';

    angular.module('app').factory('adminSvc', admin);

    admin.$inject = ['$rootScope', '$http' , 'bsWin'];

    function admin($rootScope, $http , bsWin) {

        var service = {
            gtasksGrid: gtasksGrid,		                //个人待办项目
            etasksGrid: etasksGrid,		                //个人办结项目
            dtasksGrid: dtasksGrid,                     //在办项目
            doingTaskGrid : doingTaskGrid,              //所有在办项目
            persontasksGrid : persontasksGrid,          //个人在办项目
            countWorakday: countWorakday,	            //计算工作日

            agendaTaskGrid:agendaTaskGrid,              //个人待办任务（除项目流程外）

            initFile: initFile,	        //初始化附件
            upload: upload,	            //	下载附件
            getSignList: getSignList,   //项目查询统计
            initSignList: initSignList, //初始化項目查詢統計
           // <!-- 以下是首页方法-->
            initAnnountment: initAnnountment,	    //初始化通知公告栏
            findendTasks: findendTasks,             //已办项目列表
            findtasks: findtasks,                   //待办项目列表
            findHomePluginFile :findHomePluginFile, //获取首页安装文件
            excelExport : excelExport,              //项目统计导出
            statisticalGrid : statisticalGrid,
            initProjectStop : initProjectStop ,//初始化项目暂停审批信息
            findHomeAppraise : findHomeAppraise, //初始化评审报告评优审批 信息
            findHomeAddSuppLetter : findHomeAddSuppLetter ,//初始化 拟补充资料函信息
            findHomeMonthly : findHomeMonthly , //初始化主页上的月报简报信息

        }
        return service;

        //begin findHomeMonthly
        function findHomeMonthly(vm){
            var httpOptions = {
                method : 'get',
                url : rootPath + '/monthlyNewsletter/findHomeMonthly'
            }
            var httpSuccess = function success(response){
                vm.monthlyList = response.data;
            }
            common.http({
                vm :vm,
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end findHomeMonthly

        //begin findHomeAddSuppLetter
        function findHomeAddSuppLetter(vm){
            var httpOptions = {
                method : 'get',
                url : rootPath + '/addSuppLetter/findHomeAddSuppLetter'
            }
            var httpSuccess = function success(response){
                vm.addSupLetterList = response.data;
            }
            common.http({
                vm :vm,
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end findHomeAddSuppLetter

        //begin initProjectStop
        function initProjectStop(vm){
            var httpOptions = {
                method : 'get',
                url : rootPath + '/projectStop/findHomeProjectStop'
            }
            var httpSuccess = function success(response){
                vm.projectStopList = response.data;
            }
            common.http({
                vm :vm,
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end initProjectStop

        //begin findHomeAppraise
        function findHomeAppraise(vm){
            var httpOptions = {
                method : 'get',
                url : rootPath + '/reviewProjectAppraise/findHomeAppraise'
            }
            var httpSuccess = function success(response){
                vm.appraiseList = response.data;
            }
            common.http({
                vm :vm,
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end findHomeAppraise

        //begin excelExport
        function excelExport(vm , fileName , project){
            var fileName = escape(encodeURIComponent(fileName));
            window.open(rootPath + "/signView/excelExport?filterData=" + project + "&fileName=" +fileName);
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

        //begin initAnnountment
        function initAnnountment(vm) {
            vm.i = 1;
            var httpOptions = {
                method: "get",
                url: rootPath + "/annountment/getHomePageAnnountment"
            }

            var httpSuccess = function success(response) {
                vm.annountmentList = response.data;
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }//end initAnnountment

        //S_获取本地安装包
        function findHomePluginFile(vm){
            var httpOptions = {
                method: "post",
                url: rootPath + "/file/listHomeFile"
            }
            var httpSuccess = function success(response) {
                vm.pluginFileList = {};
                vm.pluginFileList = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_findHomePluginFile

        //查找待办
        function findtasks(vm) {
            var httpOptions = {
                method: "post",
                url: rootPath + "/flow/getMyHomeTasks"
            }
            var httpSuccess = function success(response) {
                vm.tasksList = {};
                vm.tasksList = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //查找已办
        function findendTasks(vm) {
            vm.endTasksList = {};
            var httpOptions = {
                method: "post",
                url: rootPath + "/flow/getMyHomeEndTask"
            }

            var httpSuccess = function success(response) {
                vm.endTasksList = response.data;
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

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
                transport: common.kendoGridConfig().transport(rootPath + "/flow/html/tasks"),
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
                    title: "",
                    width: 30,
                    template: function (item) {
                        switch (item.lightState) {
                            case "4":          //暂停
                                return $('#span1').html();
                                break;
                            case "8":         	//存档超期
                                return $('#span5').html();
                                break;
                            case "7":           //超过25个工作日未存档
                                return $('#span4').html();
                                break;
                            case "6":          	//发文超期
                                return $('#span3').html();
                                break;
                            case "5":          //少于3个工作日
                                return $('#span2').html();
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
                    template: "<span class='row-number'></span>",
                    width: 50
                },
                {
                    field: "",
                    title: "项目名称",
                    filterable: false,
                    width: "15%",
                    template: function (item) {
                        if(checkCanEdit(item)){
                            return '<a href="#/signFlowDetail/'+item.businessKey+'/'+item.taskId+'/'+item.processInstanceId+'" >'+item.projectName+'</a>';
                        }else{
                            return '<a href="#/signFlowDeal/'+item.businessKey+'/'+item.taskId+'/'+item.processInstanceId+'" >'+item.projectName+'</a>';
                        }
                    }
                },
                {
                    field: "reviewStage",
                    title: "项目阶段",
                    filterable: false,
                    width: "10%"
                },
                {
                    field: "nodeName",
                    title: "当前环节",
                    width: "10%",
                    filterable: false
                },
                {
                    field: "",
                    title: "合并评审",
                    width: "10%",
                    filterable: false,
                    template : function(item){
                        if(item.reviewType){
                            if(item.reviewType == 9 || item.reviewType == '9'){
                                return "合并评审[主项目]";
                            }else{
                                return "合并评审[次项目]";
                            }
                        }else{
                            return "否";
                        }
                    }
                },
                {
                    field: "",
                    title: "合并项目",
                    width: "10%",
                    filterable: false,
                    template : function(item){
                        if(item.reviewSignDtoList){
                            var projectName = '';
                            angular.forEach(item.reviewSignDtoList, function(data,index,array){
                                if(index > 0){
                                    projectName += ",";
                                }
                                projectName += data.projectname;
                            });
                            return projectName;
                        }else{
                            return "";
                        }
                    }
                },
                {
                    field: "preSignDate",
                    title: "预签收时间",
                    width: "10%",
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "signDate",
                    title: "签收时间",
                    width: "10%",
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "",
                    title: "剩余工作日",
                    width: "10%",
                    filterable: false,
                    template : function(item){
                        if(item.surplusDays != undefined){
                            return (item.surplusDays > 0)?item.surplusDays:0;
                        }else{
                            return "";
                        }
                    }
                },
                {
                    field: "displayName",
                    title: "处理人",
                    width: "10%",
                    filterable: false
                },
                {
                    field: "",
                    title: "状态",
                    width: "6%",
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
                    width: "6%",
                    template: function (item) {
                        if(checkCanEdit(item)){
                            return common.format($('#detailBtns').html(), "signFlowDetail", item.businessKey, item.taskId, item.processInstanceId);
                        }else{
                            return common.format($('#columnBtns').html(), "signFlowDeal", item.businessKey, item.taskId, item.processInstanceId);
                        }
                    }
                }
            ];// End:column

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
        }//E_gtasksGrid

        //判断是否显示处理按钮（主要针对合并评审项目,部长审核和分管领导审核两个环节）
        function checkCanEdit(item){
            var isDetailBt = (item.processState == 2)?true:false;
            if(!isDetailBt){
                if(item.reviewType && (item.reviewType == 0 || item.reviewType == '0') && (item.nodeDefineKey == 'SIGN_BMLD_SPW1' || item.nodeDefineKey =='SIGN_FGLD_SPW1')){
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
                    width: 40
                },
                {
                    field: "businessName",
                    title: "任务名称",
                    filterable: false,
                    width: 180
                },
                {
                    field: "createDate",
                    title: "开始时间",
                    width: 150,
                    filterable: false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "endDate",
                    title: "结束时间",
                    width: 150,
                    filterable: false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "",
                    title: "用时",
                    width: 180,
                    filterable: false,
                    template: function (item) {
                        if (item.durationTime) {
                            return item.durationTime;
                        } else {
                            return '<span style="color:orangered;">已办结</span>';
                        }
                    }
                },
                {
                    field: "",
                    title: "流程状态",
                    width: 120,
                    filterable: false,
                    template: function (item) {
                        return '<span style="color:orangered;">已办结</span>';
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 80,
                    template: function (item) {
                        if (item.flowKey == flowcommon.getFlowDefinedKey().FINAL_SIGN_FLOW) {
                            return common.format($('#columnBtns').html(), "endSignDetail", item.businessKey, item.processInstanceId);
                        }else if(item.flowKey){
                            return common.format($('#columnBtns').html(), "flowEnd/"+item.businessKey, item.flowKey, item.processInstanceId);
                        }else{
                            return "";
                        }
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

        //S_在办项目
        function dtasksGrid(vm) {
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/flow/html/doingtasks"),
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
                            case "8":         	//存档超期
                                return $('#span5').html();
                                break;
                            case "7":           //超过25个工作日未存档
                                return $('#span4').html();
                                break;
                            case "6":          	//发文超期
                                return $('#span3').html();
                                break;
                            case "5":          //少于3个工作日
                                return $('#span2').html();
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
                    template: "<span class='row-number'></span>",
                    width: 50
                },
                {
                    field: "",
                    title: "项目名称",
                    filterable: false,
                    width: "18%",
                    template: function (item) {
                        return '<a href="#/signFlowDetail/'+item.businessKey+'/'+item.taskId+'/'+item.processInstanceId+'" >'+item.projectName+'</a>';
                    }
                },
                {
                    field: "reviewStage",
                    title: "项目阶段",
                    filterable: false,
                    width: "10%"
                },
                {
                    field: "nodeName",
                    title: "当前环节",
                    width: "10%",
                    filterable: false
                },
                {
                    field: "preSignDate",
                    title: "预签收时间",
                    width: "10%",
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "signDate",
                    title: "签收时间",
                    width: "10%",
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "",
                    title: "剩余工作日",
                    width: "10%",
                    filterable: false,
                    template : function(item){
                        if(item.surplusDays != undefined){
                            return (item.surplusDays > 0)?item.surplusDays:0;
                        }else{
                            return "";
                        }
                    }
                },
                {
                    field: "displayName",
                    title: "处理人",
                    width: "10%",
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
                        return common.format($('#columnBtns').html(), "signFlowDetail", item.businessKey, item.taskId, item.processInstanceId);
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
        }//E_dtasksGrid

        //begin_getSignList
        function getSignList(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
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
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            // End:dataSource

            // Begin:column
            var columns = [
                {
                    field: "",
                    title: "序号",
                    template: "<span class='row-number'></span>",
                    width: 50
                },
                {
                    field: "",
                    title: "项目名称",
                    width: 160,
                    filterable: false,
                    template: function (item) {
                        if(item.processInstanceId){
                            return '<a href="#/signDetails/'+item.signid+'/'+item.processInstanceId+'" >'+item.projectname+'</a>';
                        }else{
                            return '<a href="#/signDetails/'+item.signid+'/" >'+item.projectname+'</a>';
                        }

                    }
                },
                {
                    field: "reviewstage",
                    title: "评审阶段",
                    width: 140,
                    filterable: false
                },
                {
                    field: "signdate",
                    title: "收文日期",
                    width: 200,
                    filterable: false
                },
                {
                    field: "dispatchDate",
                    title: "发文日期",
                    width: 160,
                    filterable: false
                },
                {
                    field: "reviewdays",
                    title: "评审天数",
                    width: 140,
                    filterable: false
                },
                {
                    field: "",
                    title: "剩余工作日",
                    width: 140,
                    filterable: false,
                    template : function(item){
                        if(item.surplusdays != undefined){
                            if(item.surplusdays >=0){
                                return item.surplusdays;
                            }else{
                                return 0;
                            }
                        }else{
                            return "";
                        }

                    }
                },
                {
                    field: "reviewOrgName",
                    title: "评审部门",
                    width: 140,
                    filterable: false
                },
                {
                    field: "aUserName",
                    title: "项目负责人",
                    width: 140,
                    filterable: false
                },
                {
                    field: "ffilenum",
                    title: "归档编号",
                    width: 140,
                    filterable: false
                },
                {
                    field: "dfilenum",
                    title: "文件字号",
                    width: 140,
                    filterable: false
                },
                {
                    field: "appalyinvestment",
                    title: "申报投资",
                    width: 140,
                    filterable: false
                },
                {
                    field: "authorizeValue",
                    title: "审定投资",
                    width: 140,
                    filterable: false
                },
                {
                    field: "extraValue",
                    title: "核减（增）投资",
                    width: 140,
                    filterable: false
                },
                {
                    field: "extraRate",
                    title: "核减率",
                    width: 140,
                    filterable: false
                },
                {
                    field: "approveValue",
                    title: "批复金额",
                    width: 140,
                    filterable: false
                },
                {
                    field: "receivedate",
                    title: "批复来文时间",
                    width: 140,
                    filterable: false
                },
                {
                    field: "dispatchType",
                    title: "发文类型",
                    width: 140,
                    filterable: false
                },
                {
                    field: "fileDate",
                    title: "归档日期",
                    width: 140,
                    filterable: false
                },
                {
                    field: "builtcompanyname",
                    title: "建设单位",
                    width: 140,
                    filterable: false
                },
                {
                    field: "isassistproc",
                    title: "是否协审",
                    width: 140,
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
                    width: 140,
                    filterable: false
                }
            ];
            
            // End:column
            vm.signListOptions = {
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
        function  persontasksGrid(vm) {
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/flow/html/personDtasks"),
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
                            case "8":         	//存档超期
                                return $('#span5').html();
                                break;
                            case "7":           //超过25个工作日未存档
                                return $('#span4').html();
                                break;
                            case "6":          	//发文超期
                                return $('#span3').html();
                                break;
                            case "5":          //少于3个工作日
                                return $('#span2').html();
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
                    template: "<span class='row-number'></span>",
                    width: 50
                },
                {
                    field: "projectName",
                    title: "项目名称",
                    filterable: false,
                    width: 150
                },
                {
                    field: "reviewStage",
                    title: "项目阶段",
                    filterable: false,
                    width: 150
                },
                {
                    field: "nodeName",
                    title: "当前环节",
                    width: 120,
                    filterable: false
                },
                {
                    field: "preSignDate",
                    title: "预签收时间",
                    width: 120,
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "signDate",
                    title: "正式签收时间",
                    width: 120,
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "",
                    title: "剩余工作日",
                    width: 100,
                    filterable: false,
                    template : function(item){
                        if(item.surplusDays != undefined){
                            if(item.surplusDays >=0){
                                return item.surplusDays;
                            }else{
                                return 0;
                            }
                        }else{
                            return "";
                        }

                    }
                },
                {
                    field: "displayName",
                    title: "处理人",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "",
                    title: "流程状态",
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
                    title: "操作",
                    width: 150,
                    template: function (item) {
                        var isstart = false;
                        if (item.processState == "2") {
                            isstart = true;//显示已暂停，提示启动
                        } else {
                            isstart = false;//显示暂停
                        }
                        //项目签收流程，则跳转到项目签收流程处理野人
                        if (item.processKey == "FINAL_SIGN_FLOW" || item.processKey == "SIGN_XS_FLOW") {
                            return common.format($('#columnBtns').html(), "signFlowDetail", item.businessKey, item.taskId, item.processInstanceId,
                                "vm.pauseProject('"+item.businessKey+"')",isstart,"vm.startProject('"+item.businessKey+"')",isstart);
                        } else {
                            return '<a class="btn btn-xs btn-danger" >流程已停用</a>';
                        }
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
        }
        //end persontasksGrid

        //S_个人待办任务
        function agendaTaskGrid(vm){
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/flow/queryMyAgendaTask"),
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
                    field: "instanceName",
                    title: "流程名称",
                    filterable: false,
                    width: "20%"
                },
                {
                    field: "nodeName",
                    title: "当前环节",
                    width: "10%",
                    filterable: false
                },
                {
                    field: "displayName",
                    title: "处理人",
                    width: "10%",
                    filterable: false,
                },
                {
                    field: "processName",
                    title: "流程类别",
                    width: "20%",
                    filterable: false,
                },
                {
                    field: "",
                    title: "流程状态",
                    width: "5%",
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
                        return common.format($('#columnBtns').html(), item.businessKey,item.processKey,item.taskId,item.instanceId);
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

        //S_所有在办任务
        function doingTaskGrid(vm){
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/flow/queryAgendaTask"),
                schema: {
                    data: "value",
                    total: function (data) {
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
                    field: "instanceName",
                    title: "流程名称",
                    filterable: false,
                    width: "20%"
                },
                {
                    field: "nodeName",
                    title: "当前环节",
                    width: "10%",
                    filterable: false
                },
                {
                    field: "displayName",
                    title: "处理人",
                    width: "10%",
                    filterable: false,
                },
                {
                    field: "processName",
                    title: "流程类别",
                    width: "20%",
                    filterable: false,
                },
                {
                    field: "",
                    title: "流程状态",
                    width: "5%",
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
                        return common.format($('#columnBtns').html(), item.businessKey,item.processKey,item.taskId,item.instanceId);
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


    }
})();