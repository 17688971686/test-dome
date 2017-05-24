(function() {
	'dispatch strict';
	
	angular.module('app').factory('dispatchSvc', dispatch);
	
	dispatch.$inject = ['$rootScope','$http'];

	function dispatch($rootScope,$http) {
		var service = {
			initDispatchData : initDispatchData,		//初始化流程数据	
			saveDispatch : saveDispatch,				//保存
			gotoMergePage : gotoMergePage,               //打开合并发文页面
			chooseProject : chooseProject,			     //选择待选项目
			getsign : getsign,							 //显示待选项目
			cancelProject : cancelProject,				 //取消选择
			mergeDispa : mergeDispa,					 //合并发文
			fileNum : fileNum,                           //生成文件字号
			
		};
		return service;			
		
		function fileNum(vm){
			vm.isSubmit = false;
			if(!vm.dispatchDoc.id){
				common.alert({
					vm:vm,
					msg:"请先保存再生成文件字号",
					fn:function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
					}
				})	
				return;
			}
			
			/*if(vm.dispatchDoc.fileNum){
				common.alert({
					vm:vm,
					msg:"已生成文件字号",
					fn:function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
					}
				})	
				return;
			}*/
			
			var httpOptions = {
					method : 'post',
					url : rootPath+"/dispatch/fileNum",
					params:{dispaId:vm.dispatchDoc.id}
				}
			
			var httpSuccess = function success(response) {	
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.dispatchDoc.fileNum=response.data;	
						vm.isSubmit = true;
						common.alert({ 
							vm : vm,
							msg : "操作成功"
						})							
					}
				
				});
			}

			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess,
				//onError: function(response){vm.isSubmit = true;}
			});
		}
		
		
		// begin#gotoWPage
		function gotoMergePage(vm) {
			if(!vm.dispatchDoc.id){
				common.alert({
					vm:vm,
					msg:"请先保存再进行关联！",
					fn:function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
					}
				})	
				return;
			}
			var WorkeWindow = $("#mwindow");
			WorkeWindow.kendoWindow({
				width : "1200px",
				height : "630px",
				title : "合并发文",
				visible : false,
				modal : true,
				closable : true,
				actions : [ "Pin", "Minimize", "Maximize", "Close" ]
			}).data("kendoWindow").center().open();
			getSeleSignBysId(vm);
			getsign(vm);
			
		}
		// end#gotoWPage
		
		//S_初始化
		function initDispatchData(vm){
			var httpOptions = {
					method : 'get',
					url : rootPath+"/dispatch/initData",
					params : {signid : vm.dispatchDoc.signId}						
				}
			
			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {
						vm.dispatchDoc=response.data.dispatch;
						vm.proofread = response.data.mainUserList;
						vm.org = response.data.orgList;
						//console.log(vm.dispatchDoc.fileNum);
						
						//如果是合并发文则显示主次项目选项
						if(vm.dispatchDoc.dispatchWay=="合并发文"){
							vm.isHide=false;
		        		}else{
		        			vm.isHide=true;
		        		}
						//如果是主项目则显示关联项目
						if (vm.dispatchDoc.isMainProject=="9") {
			    			vm.isHide2=false;
			    		}else{
			    			vm.isHide2=true;
			    		}
						
						if(vm.dispatchDoc.fileNum){
							vm.isSubmit = true;
						}
						
					}		
				})
			}

			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		
		}//E_初始化
		
		function mergeDispa(vm){
			var httpOptions = {
					method : 'get',
					url : rootPath+"/dispatch/mergeDispa",
					params : {signId : vm.dispatchDoc.signId,linkSignId : vm.linkSignId}						
				}
			
			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function(){		
						window.parent.$("#mwindow").data("kendoWindow").close();
						common.alert({
							vm:vm,
							msg:"操作成功,请继续处理流程！",
							fn:function() {
								$('.alertDialog').modal('hide');
								$('.modal-backdrop').remove();
								$rootScope.back();	//返回到流程页面
							}
						})								
					}						
				});
			}

			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		
		}
		
		
		//S_保存
		function saveDispatch(vm){
			common.initJqValidation($("#dispatch_form"));
			var isValid = $("#dispatch_form").valid();
			vm.saveProcess = false;
			if(isValid){
				var httpOptions = {
						method : 'post',
						url : rootPath+"/dispatch",
						data : vm.dispatchDoc
					}
				var httpSuccess = function success(response) {									
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function(){		
							common.alert({
								vm:vm,
								msg:"操作成功,请继续处理流程！",
								fn:function() {
									vm.saveProcess = true;
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
									//$rootScope.back();	//返回到流程页面
								}
							})								
						}						
					});
				}
				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess,
					//onError: function(response){vm.saveProcess = false;}
				});
				
				//初始化数据获得保存后的数据
				initDispatchData(vm);
			}
		}//E_保存
		
		//begin##chooseProject
		function chooseProject(vm){
			var idStr=vm.linkSignId;
			var linkSignId=$("input[name='checksign']:checked");
			
			var ids=[];
			if(linkSignId){
				 $.each(linkSignId, function(i, obj) {
					ids.push(obj.value);
				 });
				 if(idStr){
					 idStr+=","+ids.join(',');
				 }else{
					 idStr=ids.join(',');
				 }
				 vm.linkSignId=idStr;
				 getselectedSign(vm);
				 getsign(vm);
			}
		}
		//end##chooseProject
		
		//begin##chooseProject
		function cancelProject(vm){
			var idStr=vm.linkSignId;
			var linkSignId=$("input[name='checkss']:checked");
			if(linkSignId){
				$.each(linkSignId, function(i, obj) {
					if(idStr.lastIndexOf(obj.value)==0){
						idStr=idStr.replace(obj.value,"");
					}else{
						idStr=idStr.replace(","+obj.value,"");
					}
				});
				vm.linkSignId=idStr
				console.log(vm.linkSignId);
				getselectedSign(vm);
				getsign(vm);
			}
		}
		//end##chooseProject
		
		//begin##getSeleSignBysId
		function getSeleSignBysId(vm){
			var httpOptions = {
					method : 'get',
					url : rootPath+"/dispatch/getSignByDId",
					params:{
						bussnessId:vm.dispatchDoc.id
					}
				}
			var httpSuccess = function success(response) {
				vm.selectedSign=response.data.signDtoList;
				vm.linkSignId=response.data.linkSignId;
			} 
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}
		//end##getSeleSignBysId
		
		//获取已选项目
		//begin##getselectedSign
		function getselectedSign(vm){
			
			var httpOptions = {
					method : 'get',
					url : rootPath+"/dispatch/getSelectedSign",
					params:{
						linkSignIds:vm.linkSignId
					}
				}
			var httpSuccess = function success(response) {
				vm.selectedSign=response.data;
			} 
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}
		
		//begin##getsign
		function getsign(vm){
				vm.sign.signid=vm.linkSignId;
				var httpOptions = {
						method : 'post',
						url : rootPath+"/dispatch/getSign",
						data : vm.sign
					}
				var httpSuccess = function success(response) {	
					vm.signs=response.data;
				}
				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess,
					onError: function(response){}
				});
				
		}//end##getsign
		
	}
})();