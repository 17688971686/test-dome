(function() {
	'use strict';

	angular.module('app').factory('roomSvc', room);

	room.$inject = [ '$http' ];

	function room($http) {
		var url_room = rootPath + "/room";
		var url_back = '#/room';
		
		var service = {
			initRoom : initRoom,
			showMeeting : showMeeting,
			findMeeting : findMeeting,
			exportWeek : exportWeek,
			exportThisWeek : exportThisWeek,
			exportNextWeek : exportNextWeek,
			stageNextWeek : stageNextWeek,
			addRoom : addRoom,  //添加会议室预定
			editRoom : editRoom,//编辑
			startEnd:startEnd	//结束时间不能小于开始时间
		};

		return service;
		
		//S_会议预定编辑
		function editRoom(vm){
			
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
					method : 'put',
					url : rootPath + "/room/updateRoom",
					data : vm.model
				}
				var httpSuccess = function success(response) {
					
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
						//	cleanValue();
						//window.parent.$("#roomWindow").data("kendoWindow").close();
							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									vm.showWorkHistory = true;
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
								}
							})
						}

					});

				}
				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});
			}
		}
		//E_会议预定编辑
		
		// 清空页面数据
		// begin#cleanValue
		function cleanValue() {
			var tab = $("#stageWindow").find('input');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
		}
		// end#cleanValue
		
		//S_添加会议室预定
		function addRoom(vm){
			common.initJqValidation($('#formRoom'));
			var isValid = $('#formRoom').valid();
			if (isValid) {
			vm.model.rbDay = $("#rbDay").val();
			vm.model.beginTime = $("#beginTime").val(); 
			vm.model.endTime = $("#endTime").val();
				var httpOptions = {
					method : 'post',
					url : rootPath + "/room/addRoom",
					data : vm.model
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
						//	cleanValue();
							//window.parent.$("#roomWindow").data("kendoWindow").close();
							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									vm.showWorkHistory = true;
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
								}
							})
						}

					});

				}
				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});

			}
		}
		//E_添加会议室预定
		
		//start 初始化会议预定页面
		function initRoom(vm){
			
			var dataSource = new kendo.data.SchedulerDataSource({
	          	 batch: true,
				sync: function() {
					  this.read();
					}, 
	           transport: {
				  read:function(options){
					  var mrID = options.data.mrID;
					  var url =  rootPath + "/room" ;
					  if(mrID){
						  url += "?"+mrID;
					  }
					  $http.get(url 
					  ).success(function(data) {  
						  options.success(data.value);
					  }).error(function(data) {  
//						  alert("查询失败");
					  });  
				  },
//				  create:function(vm){
//					 createRoom(vm);
//				  },
//				  update:function(vm){
//					  updateRoom(vm);
//				  },
				  destroy:function(vm){
						deleteRoom(vm);
				  },
				  parameterMap: function(options, operation) {
	                if (operation !== "read" && options.models) {
	                  return {models: kendo.stringify(options.models)};
	                } 
	              }
	           },
	           serverPaging : true,
				serverSorting : true,
				serverFiltering : true,
				pageSize : 10,
	          
				schema: {
	              model: {
	              	id: "taskId", 
	                fields: {
	                    taskId: {
	                        from: "id"
	                    },
	                    title: { from: "addressName", defaultValue: "addressName" },
	                    start: { type: "date", from: "beginTime" },
	                    end: { type: "date", from: "endTime" },
						
						}
				  }
	            },

	          });
		
			vm.schedulerOptions = {
			            date: new Date(),
			            startTime: new Date("2017/6/1 08:00 "),
			            height: 600,
			            views: [
			                "day",
			                "workWeek",
			                { type: "week", selected: true },
			                
			                "month",
			                "agenda",
			            ],
			           //statr 时间
			            editable: {
			                template: $("#customEditorTemplate").html(),
			              },
			            eventTemplate: $("#event-template").html(),
			            edit: function(e) {

			            },
			            //end
			            timezone: "Etc/UTC",
			            dataSource :dataSource,
			        };
		}
		//end 初始化会议预定页面
		
		//start#会议室地点查询
		function showMeeting(vm){
			 $http.get(
				  url_room+"/meeting" 
			  ).success(function(data) {  
				  vm.meetings ={};
				  vm.meetings=data;
			  }).error(function(data) {  
				  //alert("查询会议室失败");
			  }); 
		}
		//end #会议室地点查询
		
		//查询会议室
		function findMeeting(vm){
			vm.schedulerOptions.dataSource.read({"mrID":common.format("$filter=mrID eq '{0}'", vm.mrID)});
		}
		//start#deleteRoom
		function deleteRoom(vm){
			var model = vm.data.models[0];
			var id = model.id;
			var httpOptions = {
					method : 'delete',
					url : url_room,
					data : id
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						/*common.alert({
							vm : vm,
							msg : "删除成功",
							fn : function() {
								vm.showWorkHistory = true;
							$('.alertDialog').modal('show');
							$('.modal-backdrop').remove();
							}
						})*/
					}

				});

			}
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
			
		}
		//end#deleteRoom
		
		
		//start#time
		//校验结束时间不能小于开始时间
		function startEnd(vm){
			var start = $("#beginTime").val();
			var end = $("#endTime").val();
			if(end<start){
				//alert("结束时间不能小于开始时间");
				return;
			}
		}
		//endTime#time
		
		//start#exportWeek
		//本周评审会议
		function exportWeek(){
			window.open(url_room+"/exports");
		}
		//本周全部会议
		function exportThisWeek(){
			window.open(url_room+"/exportWeek");
		}
		//下周全部会议
		function exportNextWeek(){
			window.open(url_room+"/exportNextWeek");
		}
		//下周评审会议
		function stageNextWeek(){
			window.open(url_room+"/stageNextWeek");
		}
		//end#exportWeek
		
	}
})();

