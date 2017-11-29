(function () {
    'use strict';

    angular.module('app').factory('roomSvc', room);

    room.$inject = ['$http', 'bsWin'];

    function room($http, bsWin) {
        var url_room = rootPath + "/room";

        var service = {
            initScheduler: initScheduler,               //初始化会议室预定列表
            queryBookRoom: queryBookRoom,               //查询预订数据
            setSCDataSource: setSCDataSource,           //设置数据源
            showMeeting: showMeeting,                   //查询所有系统会议
            exportThisWeekStage: exportThisWeekStage,   //导出本周评审会会议安排
            exportNextWeekStage: exportNextWeekStage,   //下周评审会议安排
            exportThisWeek: exportThisWeek,
            initDefaultValue: initDefaultValue,         //初始化会议信息
            saveBookRoom: saveBookRoom,                 //保存会议预定信息
            deleteRoom : deleteRoom ,                //删除会议室
        };
        return service;
        //start 初始化日程控件
        function initScheduler(vm) {
            $("#scheduler").kendoScheduler({
                date: new Date(),
                width:"100%",
                startTime: new Date("2005/6/1 08:00"),
                endTime: new Date("2030/6/1 21:00"),
                views: [
                    {   type: "week",
                        selected: true,
                        allDaySlot: false,
                        selectedDateFormat: "{0:yyyy-MM-dd}"
                    },
                ],
                editable: {
                    destroy: true,      //不可删除
                    template: $("#customEditorTemplate").html(),
                },
                navigate: function (e) {
                    vm.reQueryDate(e);
                },
                edit: function (e) {         //新增或者修改时触发事件
                    //vm.editScheduler(e);
                },
                save: function (e) {
                    saveBookRoom(e.event,function(data){
                        bsWin.alert(data.reMsg,function(){
                            vm.findMeeting();
                        });
                        /*if (data.flag || data.reCode == 'ok') {
                            bsWin.alert("操作成功");
                        } else {
                            bsWin.alert(data.reMsg);
                        }*/
                    });
                },
                remove: function(e) {
                    deleteRoom(e.event , function(data){
                        if(data.flag || data.reCode == "ok"){
                            bsWin.alert("删除成功！",function(){
                                vm.findMeeting();
                            })
                        }else{
                            bsWin.alert(data.reMsg,function(){
                                vm.findMeeting();
                            });
                        }
                    });
                },
                eventTemplate: $("#event-template").html(),
                timezone: "Etc/UTC",
                footer: false,
            });
            var scheduler = $("#scheduler").data("kendoScheduler");
            var formattedShortDate = scheduler._model.formattedShortDate;
            var timeRange = formattedShortDate.split("-");
            vm.search.beginTimeStr = (new Date(timeRange[0].trim())).Format("yyyy-MM-dd");
            vm.search.endTimeStr = (new Date(timeRange[1].trim())).Format("yyyy-MM-dd");
        }

        /**
         * 删除会议室
         */
        function deleteRoom(room , callBack){
            var httpOptions = {
                method : 'delete' ,
                url : rootPath + "/room",
                params : {id : room.id , dueToPeople : room.dueToPeople}
            }
            var httpSuccess = function success(response){
                if(callBack != undefined && typeof callBack == 'function'){
                    callBack(response.data);
                }
            }
            common.http({
                httpOptions : httpOptions ,
                $http : $http,
                success : httpSuccess
            });

        }

        /**
         * 查询数据
         */
        function queryBookRoom(meeting, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/room/queryBookInfo",
                data: meeting,
            };
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

        /**
         * 设置行程控件数据源
         */
        function setSCDataSource(vm, data) {
            var scheduler = $("#scheduler").data("kendoScheduler");
            var dataSource = new kendo.data.SchedulerDataSource({
                data: data,
                schema: {
                    model: {
                        id: "bookId",
                        fields: {
                            bookId: {from: "id"},
                            title: {from: "addressName", defaultValue: "会议室",validation: { required: true }},
                            start: {type: "date", from: "beginTime"},
                            end: {type: "date", from: "endTime"},
                            rbDay: {type: "date", from: "rbDay",format:"yyyy-MM-dd"},
                            businessId: {from: "businessId", defaultValue: vm.model.businessId},
                            businessType: {from: "businessType", defaultValue: vm.model.businessType},
                            rbName: {from: "rbName", defaultValue: vm.model.rbName},
                            stageOrgName: {from: "stageOrgName", defaultValue: vm.model.stageOrgName},
                            host: {from: "host", defaultValue: vm.model.host},
                            dueToPeople: {from: "dueToPeople", defaultValue: vm.model.dueToPeople},
                            mrID: {from: "mrID", defaultValue: vm.model.mrID},
                            content: {from: "content", defaultValue: vm.model.content},
                            remark: {from: "remark", defaultValue: vm.model.remark},
                        }
                    }
                },
            });
            scheduler.setDataSource(dataSource);
        }

        function saveBookRoom(event,callBack) {
            var model = event;

            if(!model.rbName || !model.dueToPeople || !model.rbDay || !model.start|| !model.end ||!model.content){
                var resultMsg = {};
                resultMsg.flag = false;
                resultMsg.reCode = 'error';
                resultMsg.reMsg = '保存失败！有红色*号的是必填项，请按要求填写再提交！';
                callBack(resultMsg);
            }else{
                model.id = model.bookId;
                var beginTime = (model.start).Format("yyyy-MM-dd hh:mm:ss");
                model.beginTime = beginTime;
                model.rbDay = (model.start).Format("yyyy-MM-dd");
                var endTime = (model.end).Format("hh:mm:ss");
                model.endTime = model.rbDay+ " "+endTime;

                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/room/addRoom",
                    data: model
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

        //start#会议室地点查询
        function showMeeting(vm, callBack) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/room/meeting",
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

        //end #会议室地点查询


        //S_初始化会议信息
        function initDefaultValue(businessId, businessType, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/room/initDefaultValue",
                params: {
                    businessId: businessId,
                    businessType: businessType
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

        //start#exportWeek
        //导出会议室安排
        function exportThisWeekStage(vm) {
            var fileName = escape(encodeURIComponent(vm.reportName));
            window.open(url_room + "/exportThisWeekStage?currentDate=" + vm.currentDate + "&rbType="
                + vm.rbType + "&mrId=" + vm.mrID + "&fileName=" + fileName);

            /* var httpOptions = {
             method: 'get',
             url: url_room + "/exportThisWeekStage",
             params: {
             currentDate: vm.currentDate,
             rbType: vm.rbType,
             mrId: vm.mrID,
             fileName :vm.reportName
             }
             }
             var httpSuccess = function success(response) {
             var fileName =vm.reportName + ".doc";
             var fileType ="msword";
             common.downloadReport(response.data , fileName , fileType);

             }
             common.http({
             vm: vm,
             $http: $http,
             httpOptions: httpOptions,
             success: httpSuccess
             });*/
        }

        //S 下周评审会议
        function exportNextWeekStage(vm) {
            var httpOptions = {
                method: 'get',
                url: url_room + "/exportThisWeekStage",
                params: {currentDate: vm.currentDate}

            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
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

        //S 下周评审会议

        //本周全部会议
        function exportThisWeek() {
            window.open(url_room + "/exportWeek");
        }

        //下周全部会议
        function exportNextWeek() {
            window.open(url_room + "/exportNextWeek");
        }

        //end#exportWeek

    }
})();

