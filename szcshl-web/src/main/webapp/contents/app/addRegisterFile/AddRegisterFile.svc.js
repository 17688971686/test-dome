(function () {
    'use strict';

    angular.module('app').factory('addRegisterFileSvc', addRegisterFile);

    addRegisterFile.$inject = ['$http'];

    function addRegisterFile($http) {
        var url_addRegisterFile = rootPath + "/addRegisterFile", url_back = '#/addRegisterFileList';
        var service = {
            deleteAddRegisterFile: deleteAddRegisterFile,	//删除登记补充材料
            initAddRegisterFile: initAddRegisterFile,		//初始化登记补充资料
            saveRegisterFile:saveRegisterFile,				//保存登记补充材料
            isUnsignedInteger:isUnsignedInteger,			//	数字校验
            initRegisterWinDow:initRegisterWinDow,			//	初始化登记补充资料页面
        };

        return service;
        
     function initRegisterWinDow(vm,opation){
    	 $("#addRegister").kendoWindow({
             width: "70%",
             height: "660px",
             title: "意见选择",
             visible: false,
             modal: true,
             closable: true,
             actions: ["Pin", "Minimize", "Maximize", "close"]
         }).data("kendoWindow").center().open();
     }
      //检查是否为正整数
        function isUnsignedInteger(value){
            if((/^(\+|-)?\d+$/.test(value)) && value>0 ){
                return true;
            }else{
                return false;
            }
        }
        //S 保存登记补充材料
        function saveRegisterFile(vm){
        	var httpOptions = {
        			method : 'post',
        			url : rootPath + "/addRegisterFile/save",
        			headers:{
        				"contentType":"application/json;charset=utf-8"  //设置请求头信息
        			},
        			traditional: true,
        			dataType : "json",
        			data : angular.toJson(vm.addRegisters),//将Json对象序列化成Json字符串，JSON.stringify()原生态方法
        	}
        	var httpSuccess = function success(response) {
        		common.requestSuccess({
        			vm : vm,
        			response : response,
        			fn : function() {
        				common.alert({
        					vm: vm,
        					msg: "操作成功",
        					fn: function () {
        						myrefresh();
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
        //E 保存登记补充材料
        
        //刷新页面
        function myrefresh(){
        	 window.location.reload();
        } 
        
        //S 初始化登记补充资料
        function initAddRegisterFile(vm){
        	var httpOptions = {
                    method: 'post',
                    url: rootPath + "/addRegisterFile/initRegisterData",
                    params:{
                    	signid:vm.addRegister.fileRecordId
                    }
                };
                var httpSuccess = function success(response) {
                    vm.addRegisters = response.data.financiallist;
                    console.log(vm.addRegisters);
                };
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });       
        }
       // E 初始化登记补充资料
        
        

        // begin#deleteAddRegisterFile
        function deleteAddRegisterFile(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_addRegisterFile+"/deleteFile",
                data:id
            };
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                    	common.alert({
                            vm: vm,
                            msg: "操作成功",
                            closeDialog :true,
                            fn: function () {
                            	vm.isSubmit = false;
                            	//myrefresh();
                            }
                        })
                    }
                });
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }


    }
})();