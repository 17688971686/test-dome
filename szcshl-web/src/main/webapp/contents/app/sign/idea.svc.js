(function() {
	'use strict';

	angular.module('app').factory('ideaSvc', idea);

	idea.$inject = [ '$http','$compile' ];	
	function idea($http,$compile) {	
			
		var service = {
				getIdea:getIdea,	//获取常用意见
				ideaEditWindow:ideaEditWindow, //弹出审批意见编辑框
				saveCommonIdea:saveCommonIdea, //保存常用意见
				deleteCommonIdea:deleteCommonIdea  //删除常用意见
				
		};		
		return service;	
		
		//begin getIdea
		
		function getIdea(vm){
			var httpOptions={
					method:'get',
					url:rootPath+"/idea"
			}
			
			var httpSuccess=function success(response){
				vm.commonIdeas=response.data;
			}
			
			common.http({
	              vm:vm,
	              $http:$http,
	              httpOptions:httpOptions,
	              success:httpSuccess
	          });
			
		}
		//end  getIdea
		
		 //begin ideaEditWindow
        function ideaEditWindow(vm){
        	var ideaEditWindow=$("#ideaWindow");
        	ideaEditWindow.kendoWindow({
        		width:"50%",
        		height:"80%",
        		title:"意见选择",
        		visible:false,
        		modal:true,
        		closable:true,
        		actions:["Pin","Minimize","Maximize","close"]
        	}).data("kendoWindow").center().open();
        	
        }//end  ideaEditWindow
        
        
        //begin  saveCommonIdea
      function saveCommonIdea(vm){
    	  
    	  var httpOptions={
    			  method:'post',
    			  url:rootPath+"/idea",
    			  headers:{
                      "contentType":"application/json;charset=utf-8"  //设置请求头信息
                   },
    			  dataType : "json",
    			  data:angular.toJson(vm.commonIdeas)
    	  }
    	  var httpSuccess=function success(response){
    		  
    	  }
    	  
    	  common.http({
              vm:vm,
              $http:$http,
              httpOptions:httpOptions,
              success:httpSuccess
          });
    	  
        	
        }//end  saveCommonIdea
      
      //begin  deleteCommonIdea
      function deleteCommonIdea(vm){
    	  var isCheck=$("input[name='ideaCheck']")
      }
      //end   deleteCommonIdea
		
	}
	
	
	
})();