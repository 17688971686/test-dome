(function () {
    'use strict';

    angular.module('app').factory('roomSvc', room);

    room.$inject = ['$http'];

    function room($http) {
        var url_room = rootPath + "/room";
        var url_back = '#/room';

        var service = {
            initRoom: initRoom,
            showMeeting: showMeeting,
            findMeeting: findMeeting,
            exportThisWeekStage: exportThisWeekStage,//导出本周评审会会议安排
            exportNextWeekStage: exportNextWeekStage,//下周评审会议安排
            exportThisWeek: exportThisWeek,
            addRoom: addRoom,  //添加会议室预定
            editRoom: editRoom,//编辑
            initFindUserName:initFindUserName,//初始化会议预订人
            initWorkProgram:initWorkProgram,//初始化工作方案
        };

        return service;
        
        //S 初始化工作方案
        function initWorkProgram(vm){
        	
        	if(!vm.workProgramId){
        		return;
        	}else{
        		var httpOptions = {
            			method: 'get',
            			url: rootPath + "/workprogram/html/initWorkProgramById",
            			params:{workId:vm.workProgramId}
            	}
            	var httpSuccess = function success(response) {
            		vm.model = {}
            		vm.model = response.data;
            		vm.model.rbName = vm.model.projectName;
                	vm.model.stageOrgName = vm.model.reviewOrgName;
                	console.log(vm.model.projectName);
                	console.log(vm.model.stageOrgName);
            	}
            	common.http({
            		vm: vm,
            		$http: $http,
            		httpOptions: httpOptions,
            		success: httpSuccess
            	});
        	}
        
        }
        //E 初始化工作方案
        
        //S 初始化预订人
        function initFindUserName(vm){
        	
            var httpOptions = {
                    method: 'get',
                    url: rootPath + "/room/findUser",
                    data: vm.model
                }
                var httpSuccess = function success(response) {
	            	vm.model = {}
	            	vm.model = response.data;
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
        }
        //E 初始化预订人
        
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
                    })
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

        // 清空页面数据
        // begin#cleanValue
        function cleanValue() {
            var tab = $("#stageWindow").find('input');
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }

        // end#cleanValue

        //S_添加会议室预定(停用)
        function addRoom(vm) {
            common.initJqValidation($('#formRoom'));
            var isValid = $('#formRoom').valid();
            if (isValid) {
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/room/addRoom",
                    data: vm.model
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
        }

        //E_添加会议室预定

        //start 初始化会议预定页面
        function initRoom(vm) {
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
                        common.initJqValidation($('#formRoom'));
                        var isValid = $('#formRoom').valid();
                        if (isValid) {
                            var model = options.data.models[0];
                            model.rbName = $("#rbName").val();
                            model.stageOrgName = $("#stageOrgName").val();
                            model.rbDay = $("#rbDay").val();
                            model.beginTimeStr = $("#beginTime").val();
                            model.endTimeStr = $("#endTime").val();
                            model.beginTime = $("#rbDay").val() + " " + $("#beginTime").val() + ":00";
                            model.endTime = $("#rbDay").val() + " " + $("#endTime").val() + ":00";
                            console.log(model);
                            if (vm.workProgramId) {
                                model.workProgramId = vm.workProgramId;
                            }
                            if (new Date(model.endTime) < new Date(model.beginTime)) {
                                $("#errorTime").html("开始时间不能大于结束时间!");
                                return;
                            }
                            var httpOptions = {
                                method: 'post',
                                url: rootPath + "/room/addRoom",
                                data: model
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
                                                findMeeting(vm);
                                                $('.alertDialog').modal('hide');
                                                $('.modal-backdrop').remove();
                                                //vm.schedulerOptions.cancelEvent();
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
                    },
                    update: function (options) {
                        common.initJqValidation($('#formRoom'));
                        var isValid = $('#formRoom').valid();
                        if (isValid) {
                            var model = options.data.models[0];
                           
                            model.rbDay = $("#rbDay").val();
                            model.beginTimeStr = $("#beginTime").val();
                            model.endTimeStr = $("#endTime").val();
                            model.beginTime = $("#rbDay").val() + " " + $("#beginTime").val() + ":00";
                            model.endTime = $("#rbDay").val() + " " + $("#endTime").val() + ":00";
                            if (new Date(model.endTime) < new Date(model.beginTime)) {
                                $("#errorTime").html("开始时间不能大于结束时间!");
                                return;
                            }
                            var httpOptions = {
                                method: 'put',
                                url: rootPath + "/room/updateRoom",
                                data: model
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
                                                findMeeting(vm);
                                                $('.alertDialog').modal('hide');
                                                $('.modal-backdrop').remove();
                                                vm.schedulerOptions.cancelEvent();
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
                    },
                    destroy: function (options) {
                        var id = options.data.models[0].id;
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
                                        closeDialog: true
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
                    },
                    parameterMap: function (options, operation) {
                        console.log(operation);
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
                            taskId: {from: "id"},
                            title: {from: "addressName", defaultValue: "会议室"},
                            start: {type: "date", from: "beginTime"},
                            end: {type: "date", from: "endTime"}
                        }
                    }
                },
            });

            vm.schedulerOptions = {
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
                dataSource: dataSource,
                footer: false,
            };
        }

        //end 初始化会议预定页面

        //start#会议室地点查询
        function showMeeting(vm) {
            $http.get(
                url_room + "/meeting"
            ).success(function (data) {
                vm.meetings = {};
                vm.meetings = data;
                if(vm.meetings && vm.meetings.length > 0){
                    vm.mrID = vm.meetings[0].id;
                    findMeeting(vm)
                }
            }).error(function (data) {
                //alert("查询会议室失败");
            });
        }

        //end #会议室地点查询

        //查询会议室
        function findMeeting(vm) {
            if (vm.mrID) {
                vm.schedulerOptions.dataSource.read({"mrID": common.format("$filter=mrID eq '{0}'", vm.mrID)});
            } else {
                vm.schedulerOptions.dataSource.read();
            }
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

        //start#exportWeek
        //本周评审会议
        function exportThisWeekStage(vm) {

             var httpOptions = {
                 method: 'get',
                 url: url_room+"/exportThisWeekStage",
                
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
        function exportNextWeekStage(vm) {
        
            var httpOptions = {
                    method: 'get',
                    url: url_room+"/exportNextWeekStage",
                   
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

