(function() {
	'dispatch strict';

	angular.module('app').controller('dispatchEditCtrl', dispatch);

	dispatch.$inject = ['$location', 'dispatchSvc', '$state',"$http"];

	function dispatch($location, dispatchSvc, $state,$http) {
		var vm = this;
		vm.title = '项目发文编辑';
		vm.isHide = true;
		vm.isHide2 = true;
		vm.saveProcess = false;
		vm.showFileNum = false;
		vm.mwindowHide = true;
		vm.showCreate = false;
		vm.linkSignId = "";
		vm.sign = {};
		vm.dispatchDoc = {};
		
		vm.dispatchDoc.signId = $state.params.signid;
		
		// 创建发文
		vm.create = function() {
			dispatchSvc.saveDispatch(vm);
		}
		// 核减（增）/核减率（增）计算
		vm.count = function() {
			var declareValue = vm.dispatchDoc.declareValue;
			var authorizeValue = vm.dispatchDoc.authorizeValue;
			if (declareValue && authorizeValue) {
				var dvalue = declareValue - authorizeValue;
				//console.log((dvalue / declareValue).toFixed(4));
				var extraRate = ((dvalue / declareValue)* 100).toFixed(2);
				vm.dispatchDoc.extraRate = extraRate;
				vm.dispatchDoc.extraValue = dvalue;
			}
		}
		
		//上传附件窗口
 	    vm.dispatchUpload =function(options){
 	    	common.initcommonUploadWin({businessId:vm.dispatchDoc.id});
        }
        
 	    //查看上传附件列表
 	    vm.dispatchQuery =function(){
 	    	common.initcommonQueryWin(vm);
        	vm.sysSignId=vm.dispatchDoc.id;
        	common.commonSysFilelist(vm,$http);
        	
        }
		
 	   //删除系统文件
        vm.commonDelSysFile = function(id){
        	common.commonDelSysFile(vm,id,$http);
        }
        //附件下载
        vm.commonDownloadSysFile = function(id){
        	common.commonDownloadFile(vm,id);
        }
        
		vm.relateHandle=function(){
			if(vm.dispatchDoc.isRelated=="是" && !vm.linkSignId){
				
				common.alert({
							vm : vm,
							msg : "请关联它信息！",
							fn : function() {
								$('.alertDialog').modal('hide');
								$('.modal-backdrop').remove();
							}
						})
			   vm.isnotEdit=true;
			}else if(vm.dispatchDoc.isRelated=="否" && vm.linkSignId){
				dispatchSvc.deletemerge(vm);
				vm.isnotEdit=false;
			}else{
			    vm.isnotEdit=false;
			}
		}
		
		vm.sigleProject = function() {
			if (vm.dispatchDoc.dispatchWay == "1" && vm.dispatchDoc.id &&　vm.linkSignId) {
					common.confirm({
					title : "删除提示",
					vm : vm,
					msg : "你确定删除关联信息?",
					fn : function() {
						$('.confirmDialog').modal('hide'); 
							dispatchSvc.deletemerge(vm);
							vm.dispatchDoc.isRelated = "否";
						
						}
					});
				}
				//console.log(vm.dispatchDoc.isRelated);
		}

		vm.isrelated = function() {
			//选择主项目
				vm.dispatchDoc.isRelated = "是";
			/*if (vm.dispatchDoc.id　&&　vm.linkSignId) {
				console.log(vm.dispatchDoc.isRelated);
			}*/

		}
        //选择次项目
		vm.isrelated2 = function() {
			vm.dispatchDoc.isRelated = "否";
			if (vm.dispatchDoc.id　&&　vm.linkSignId) {
			common.confirm({
				title : "删除提示",
				vm : vm,
				msg : "你确定删除关联信息?",
				fn : function() {
					$('.confirmDialog').modal('hide'); 
						dispatchSvc.deletemerge(vm);
				}
			});
			}
			
		}

		// 打开合并页面
		vm.gotoMergePage = function() {
			dispatchSvc.gotoMergePage(vm);
		}

		vm.search = function() {
			dispatchSvc.getSign(vm);
		}

		// 选择待选项目
		vm.chooseProject = function() {
			dispatchSvc.chooseProject(vm);
		}

		// 取消选择
		vm.cancelProject = function() {
			dispatchSvc.cancelProject(vm);
		}

		// 关闭窗口
		vm.onClose = function() {
			window.parent.$("#mwindow").data("kendoWindow").close();
		}

		// 确定合并
		vm.mergeDispa = function() {
			dispatchSvc.mergeDispa(vm);
		}

		vm.formReset = function() {
			var values = $("#searchform").find("input,select");
			values.val("");
		}

		vm.search = function() {
			dispatchSvc.getsign(vm);
		}
		// 生成文件字号
		vm.fileNum = function() {
			dispatchSvc.fileNum(vm);
		}
		activate();
		function activate() {
			dispatchSvc.initDispatchData(vm);
		}
	}
})();
