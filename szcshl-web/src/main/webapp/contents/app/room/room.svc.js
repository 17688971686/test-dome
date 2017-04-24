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
			//deleteRoom : deleteRoom,
			showMeeting : showMeeting,
			findMeeting : findMeeting,
			exportWeek : exportWeek,
			exportThisWeek : exportThisWeek,
			exportNextWeek : exportNextWeek,
			stageNextWeek : stageNextWeek,
			//findUser : findUser,
		};

		return service;
		
		//start0
		function initRoom(vm){
			
			//start1
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
					  //alert(url);
					  $http.get(url 
					  ).success(function(data) {  
						  //console.log(data);
						//  console.log(data.value);
						  options.success(data.value);
						 // console.log(vm.success);
					  }).error(function(data) {  
					     
						  alert("查询失败");
					  });  
				  },
				
				  create:function(vm){
					  createRoom(vm);
						
				  },
				  update:function(vm){
					
					  updateRoom(vm);
				  },
				  destroy:function(vm){
					//console.log(vm);
						deleteRoom(vm);
				  },

				  parameterMap: function(options, operation) {
					//  console.log(options);
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
						host:{ type: "string", from: "host"},
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
			            
			 
			          /*  resources: [
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
			//end1
			
			 
		}
		//end0
		
		//start#showMeeting
		function showMeeting(vm){
		
			 $http.get(url_room+"/meeting" 
			  ).success(function(data) {  
				  //console.log(data);
				  vm.meeting ={};
				  vm.meeting=data;
				
			  }).error(function(data) {  
			      //处理错误  
				  alert("查询会议室失败");
			  }); 
		}
		
		//查询会议室
		function findMeeting(vm){
			

			vm.schedulerOptions.dataSource.read({"mrID":common.format("$filter=mrID eq '{0}'", vm.mrID)});
		}
		//start#showMeeting
		
		//start#sava
		function createRoom(vm) {
			var model = vm.data.models[0];
			var rb = {};
			rb.rbName=model.rbName;
			
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
			alert(isValid);
			if (isValid) {
				vm.isSubmit = true;
				var httpOptions = {
					method : 'post',
					url : url_room,
					data : rb
				}
				var httpSuccess = function success(response) {
					 vm.isSubmit = false;   
					//console.log(response.data.message);
					common.requestSuccess({
                    	vm:vm,
                    	response:response,
                    	fn:function () {
                            
                            var isSuccess = response.data.isSuccess;
                        //  console.log(isSuccess);
                            if (isSuccess) {
                                vm.message = "";
                               
                                //common.cookie().set("data", "token", response.data.Token, "", "/");
                              //  location.href = "${path}/admin/index.html";
                            } else {
                                
                                vm.message=response.data.message
                              //  console.log(vm.message);
                            }
                    	}
                    });
					 alert("添加成功");

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

							/*common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									vm.isSubmit = false;
									$('.alertDialog').modal('hide');
									//$('.modal-backdrop').remove();
									location.href = url_back;
								}
							})*/
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

