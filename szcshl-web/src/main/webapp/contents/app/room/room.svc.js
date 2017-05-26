(function() {
	'use strict';

	angular.module('app').factory('roomSvc', room);

	room.$inject = [ '$http' ];

	function room($http) {
		var url_room = rootPath + "/room";
		var url_back = '#/room';
		
		var service = {
			initRoom : initRoom,
			createRoom : createRoom,
			updateRoom : updateRoom,
			showMeeting : showMeeting,
			findMeeting : findMeeting,
			exportWeek : exportWeek,
			exportThisWeek : exportThisWeek,
			exportNextWeek : exportNextWeek,
			stageNextWeek : stageNextWeek,
			addRoom : addRoom,  //添加会议室预定
			editRoom : editRoom,//编辑
		};

		return service;
		
		function editRoom(vm){
			alert(vm.id);
			common.initJqValidation($('#formRoom'));
			var isValid = $('#formRoom').valid();
			if (isValid) {
			vm.model.rbDay = $("#rbDay").val();
			vm.model.beginTime = $("#beginTime").val(); 
			vm.model.endTime = $("#endTime").val();
			vm.model.id = vm.id;// id
			 console.log(vm.model);
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
		//	common.initJqValidation($('#stageForm'));
		//	var isValid = $('#stageForm').valid();
			//if (isValid) {
			vm.model.rbDay = $("#rbDay").val();
			vm.model.beginTime = $("#beginTime").val(); 
			vm.model.endTime = $("#endTime").val();
			console.log(vm.model);
			
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

			//}
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
					 //console.log(options);
					  var mrID = options.data.mrID;
					  var url =  rootPath + "/room" ;
					  if(mrID){
						  url += "?"+mrID;
					  }
					  $http.get(url 
					  ).success(function(data) {  
						  options.success(data.value);
						 // console.log(vm.success);
					  }).error(function(data) {  
						  alert("查询失败");
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
	                        //type: ""
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
			            startTime: new Date(),
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
			            
			          /* resources: [
			                {
			                    field: "ownerId",
			                    title: "Owner",
			                    dataSource: [
			                        { text: "Alex", value: 1, color: "#f8a398" },
			                        { text: "Bob", value: 2, color: "#51a0ed" },
			                        { text: "Charlie", value: 3, color: "#56ca85" }
			                    ]
			                }
			            ]*/
			          
			            
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
		
		//start#添加会议预定
		function createRoom(vm) {
			common.initJqValidation();
			var isValid = $('form').valid();
			var model = vm.data.models[0];
			var rb = {};
			rb.rbName=model.rbName;
			rb.rbDay=$("#rbDay").val();
			rb.beginTime = $("#beginTime").val();
			rb.endTime = $("#endTime").val();
			rb.host = model.host;
			rb.mrID = model.mrID;
			rb.rbType=model.rbType;
			rb.dueToPeople=model.dueToPeople;
			rb.content=model.content;
			rb.remark=model.remark;
			
			if (isValid) {
				vm.isSubmit = true;
				var httpOptions = {
					method : 'post',
					url : url_room+"/addRoom",
					data : rb
				}
				var httpSuccess = function success(response) {
					 vm.isSubmit = false;   
					common.requestSuccess({
                    	vm:vm,
                    	response:response,
                    	fn:function () {
                    		$('.alertDialog').modal('hide');
							$('.modal-backdrop').remove();
							location.href = url_back;
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
		//end#save
		
		//start#update
		function updateRoom(vm){
			alert(1);
			var model = vm.data.models[0];
			var rb = {};
			rb.rbName=model.rbName;
			rb.id=model.id;
			//rb.beginTime = kendo.toString(model.beginTime, "yyyy-MM-dd HH:mm");
			rb.rbDay=$("#rbDay").val();
			rb.beginTime = $("#beginTime").val();
			rb.endTime = $("#endTime").val();
			//alert(rb.rbDay);
			//alert(rb.beginTime);
			//alert(rb.endTime);
			rb.host = model.host;
			rb.mrID = model.mrID;
			rb.rbType=model.rbType;
			rb.dueToPeople=model.dueToPeople;
			rb.content=model.content;
			rb.remark=model.remark;
			
			common.initJqValidation();
			var isValid = $('form').valid();
			if (isValid) {
				vm.isSubmit = true;
				var httpOptions = {
					method : 'put',
					url : url_room,
					data : rb
				}
				var httpSuccess = function success(response) {

					common.requestSuccess({
						vm : vm,
						response : response,
						
						fn : function() {
							var msg ="编辑成功";
							alert(msg);
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
		//end#update
		
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
//					fn : function() {
//						vm.isSubmit = false;
//						vm.dataSource.dataSource.read();
//					}

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
		function startEnd(){
			var start = $("#beginTime").val();
			var end = $("#endTime").val();
			if(end<start){
				alert("结束时间不能小于开始时间");
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

