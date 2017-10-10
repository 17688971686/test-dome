(function () {
    'use strict';

    angular.module('app').factory('roomSvc', room);

    room.$inject = ['$http','bsWin'];

    function room($http,bsWin) {
        var url_room = rootPath + "/room";

        var service = {
            initRoom: initRoom,
            showMeeting: showMeeting,
            findMeeting: findMeeting,
            exportThisWeekStage: exportThisWeekStage,//导出本周评审会会议安排
            exportNextWeekStage: exportNextWeekStage,//下周评审会议安排
            exportThisWeek: exportThisWeek,
            editRoom: editRoom,                      //编辑
            initDefaultValue : initDefaultValue,     //初始化会议信息
            updateDataSource : updateDataSource,     //重新设置DataSource
            saveRoom : saveRoom,                     //保存会议预定信息
        };
        return service;

        //S_会议预定编辑
        function editRoom(vm) {
            vm.model.id = $("#id").val();
            vm.model.rbName = $("#rbName").val();
            vm.model.mrID = $("#mrID").val();
            vm.model.rbType = $("#rbType").val();
            vm.model.host = $("#host").val();
            vm.model.dueToPeople = $("#dueToPeople").val();
            vm.model.rbDay = $("#rbDay").val();
            vm.model.beginTime = $("#beginTime").val();
            vm.model.endTime = $("#endTime").val();
            vm.model.content = $("#content").val();
            vm.model.content = $("#remark").val();
            common.initJqValidation($('#formRoom'));
            var isValid = $('#formRoom').valid();
            if (isValid) {
                vm.iscommit = true;
                var httpOptions = {
                    method: 'put',
                    url: rootPath + "/room/updateRoom",
                    data: vm.model
                }
                var httpSuccess = function success(response) {
                    bsWin.alert("操作成功");
                    $('.alertDialog').modal('hide');
                    $('.modal-backdrop').remove();
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }
        //E_会议预定编辑

        function updateDataSource(vm){
            if(vm.mrID){
                vm.model.mrID = vm.mrID;
            }
            var dataSource = new kendo.data.SchedulerDataSource({
                batch: true,
                sync: function () {
                    this.read();
                },
                transport: {
                    read: function (options) {
                        var mrID = options.data.mrID;
                        var url = rootPath + "/room";
                        if (mrID) {
                            url += "?" + mrID;
                        }
                        $http.get(
                            url
                        ).success(function (data) {
                            options.success(data.value);
                        }).error(function (data) {
                            console.log("查询数据失败！");
                        });
                    },
                    create: function (options) {
                        saveBookRoom(vm);
                    },
                    update: function (options) {
                        saveBookRoom(vm);
                    },
                    destroy: function (options) {
                        var id = options.data.models[0].id;
                        var httpOptions = {
                            method: 'delete',
                            url: url_room,
                            params: {
                                id: id
                            }
                        }
                        var httpSuccess = function success(response) {
                            bsWin.alert("删除成功");
                        }
                        common.http({
                            $http: $http,
                            httpOptions: httpOptions,
                            success: httpSuccess
                        });
                    },
                    parameterMap: function (options, operation) {
                        if (operation !== "read" && options.models) {
                            return {models: kendo.stringify(options.models)};
                        }
                    }
                },
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                schema: {
                    model: {
                        id: "taskId",
                        fields: {
                            bookId: {from: "id"},
                            title: {from: "addressName", defaultValue: "会议室"},
                            start: {type: "date", from: "beginTime"},
                            end: {type: "date", from: "endTime"},
                            businessId :{from: "businessId", defaultValue: vm.model.businessId},
                            businessType :{from: "businessType", defaultValue: vm.model.businessType},
                            rbName :{from: "rbName", defaultValue: vm.model.rbName},
                            stageOrgName :{from: "stageOrgName", defaultValue: vm.model.stageOrgName},
                            host :{from: "host", defaultValue: vm.model.host},
                            dueToPeople :{from: "dueToPeople", defaultValue: vm.model.dueToPeople},
                            mrID:{from: "mrID", defaultValue: vm.model.mrID},
                            content:{from: "content", defaultValue: vm.model.content},
                            remark:{from: "remark", defaultValue: vm.model.remark},
                        }
                    }
                },
            });
            var ss =  $("#scheduler").data("kendo-scheduler");
            ss.setDataSource(dataSource);
            if(vm.mrID){
                ss.dataSource.read({"mrID": common.format("$filter=mrID eq '{0}'", vm.mrID)});
            }else{
                ss.dataSource.read();
            }
        }

        function saveBookRoom(vm){
            common.initJqValidation($('#formRoom'));
            var isValid = $('#formRoom').valid();
            if (isValid) {
                var model = {};
                if($("#bookId").val()){
                    model.id =  $("#bookId").val();
                }
                model.rbName = $("#rbName").val();
                model.mrID = $("#mrID").val();
                model.stageOrgName = $("#stageOrgName").val();
                model.rbDay = $("#rbDay").val();
                model.host = $("#host").val();
                model.dueToPeople = $("#dueToPeople").val();
                model.beginTimeStr = $("#beginTime").val();
                model.endTimeStr = $("#endTime").val();
                model.beginTime = $("#rbDay").val() + " " + $("#beginTime").val() + ":00";
                model.endTime = $("#rbDay").val() + " " + $("#endTime").val() + ":00";
                model.content = $("#content").val();
                model.remark = $("#remark").val();
                model.businessId = vm.model.businessId;
                model.businessType = vm.model.businessType;

                if (new Date(model.endTime) < new Date(model.beginTime)) {
                    bsWin.alert("开始时间不能大于结束时间!");
                    return;
                }
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/room/addRoom",
                    data: model
                }
                var httpSuccess = function success(response) {
                    if(response.data.flag || response.data.reCode == 'ok'){
                        bsWin.alert("操作成功",function(){
                            $('.alertDialog').modal('hide');
                            $('.modal-backdrop').remove();
                        });
                    }else{
                        bsWin.alert(response.data.reMsg);
                    }
                    findMeeting(vm);
                }
                common.http({
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }
        //start 初始化会议预定页面
        function initRoom(vm) {
            vm.schedulerOptions = {
                // toolbar: ["pdf"],
                // pdf: {
                //     fileName: "会议室一览表.pdf",
                //     proxyURL: "https://demos.telerik.com/kendo-ui/service/export"
                // },
                date: new Date(),
                startTime: vm.startDateTime,
                endTime: vm.endDateTime,
                height: 600,
                views: [
                    "day",
                    "workWeek",
                    {type: "week", selected: true},
                    "month",
                    "agenda",
                ],
                editable: {
                    template: $("#customEditorTemplate").html(),
                },
                eventTemplate: $("#event-template").html(),
                timezone: "Etc/UTC",
                footer: false,
                remove: function(){
                    bsWin.alert("已经预定的会议室不能删除！");
                    updateDataSource(vm);
                },
                cancel: function() {
                    updateDataSource(vm);
                }
            };
        }

        //end 初始化会议预定页面

        //start#会议室地点查询
        function showMeeting(vm,callBack) {
            $http.get(
                url_room + "/meeting"
            ).success(function (data) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(data);
                }
            }).error(function (data) {
                //alert("查询会议室失败");
            });
        }

        //end #会议室地点查询

        //查询会议室
        function findMeeting(vm) {
            if (vm.mrID) {
                vm.model.mrID = vm.mrID;
            }
            updateDataSource(vm);
        }

        //start#deleteRoom
        function deleteRoom(vm) {
            var model = vm.data.models[0];
            var id = model.id;
            var httpOptions = {
                method: 'delete',
                url: url_room,
                data: id
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: "删除成功",
                            fn: function () {
                                $('.alertDialog').modal('show');
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
        //end#deleteRoom

        //S_初始化会议信息
        function initDefaultValue(businessId,businessType,callBack){
            var httpOptions = {
                method: 'post',
                url:  rootPath + "/room/initDefaultValue",
                params: {
                    businessId : businessId,
                    businessType : businessType
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

        //S_会议预定添加
        function saveRoom(roombook,callBack) {
            common.initJqValidation($('#stageForm'));
            var isValid = $('#stageForm').valid();
            if (isValid) {
                roombook.beginTime = $("#rbDay").val() + " " + $("#beginTime").val() + ":00";
                roombook.endTime = $("#rbDay").val() + " " + $("#endTime").val() + ":00";
                if (new Date(roombook.endTime) < new Date(roombook.beginTime)) {
                    bsWin.error("开始时间不能大于结束时间!");
                    return;
                }
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

        //start#exportWeek
        //本周评审会议
        function exportThisWeekStage(vm) {
            var httpOptions = {
                method: 'get',
                url: url_room + "/exportThisWeekStage",
                params: {currentDate: vm.currentDate, rbType: vm.rbType, mrId: vm.mrID}
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
            });
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

