(function() {
	'use strict';

	angular.module('app').factory('dictSvc', dict);

	dict.$inject = [ '$http' ,'$state','$location'];

	function dict($http,$state,$location) {
		var url_back = '#/dict';
		var url_dictgroup = rootPath + "/dict";
		var url_dictitems = rootPath + "/dict/dictNameData";
		var service = {
			initDictTree:initDictTree,			//初始化数字字典
			createDict:createDict,              //创建字典信息
			getDictById:getDictById,            //根据ID查询字典信息
			updateDict:updateDict,              //更改字典信息
			deleteDict:deleteDict,              //删除数字字典，包含子类
			initpZtreeClient:initpZtreeClient,
			getTreeData:getTreeData,
			getdictItems:getdictItems
		};

		return service;

		function getdictItems(vm){
			var dictCode = 'DICT_SEX';
			
			
			var httpOptions = {
					method : 'get',
					url : common.format(url_dictitems + "?dictCode={0}", dictCode)
			};
			
			var httpSuccess = function success(response) {
			
				
			}

			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
			
		}
		
		function getTreeData(callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/dict/fingByOdata?$orderby=dictSort"
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
			common.http({
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
			
		}
		

		//beginDeleteGroup
		function deleteDict(id,isSubmit,callBack){
            isSubmit = true;
			var httpOptions = {
				method : 'delete',
				url : rootPath + "/dict",
				data : id
			}
            var httpSuccess = function success(response) {
                isSubmit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
			common.http({
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess,
                onError:function(){
                    isSubmit = false;
                }
			});
		}

		//begin#createDict
		function createDict(dictModel,isCommit,callBack){
            isCommit = true;
            var httpOptions = {
                method : 'post',
                url : rootPath + "/dict",
                data : dictModel
            }
            var httpSuccess = function success(response) {
                isCommit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess,
                onError:function(){
                    isCommit = false;
                }
            });
		}
		
		//updateDict
		function updateDict(dictModel,isCommit,callBack){
            isCommit = true;
            var httpOptions = {
                method : 'put',
                url : rootPath + "/dict",
                data : dictModel
            }
            var httpSuccess = function success(response) {
                isCommit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess,
                onError:function(){
                    isCommit = false;
                }
            });
		}
		
		// begin#initZtreeClient
		function initDictTree(callBack) {
			var httpOptions = {
				method : 'post',
				url : rootPath + "/dict/fingByOdata?$orderby=dictSort"
			}
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
			common.http({
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}

		// begin#initpZtreeClient
		function initpZtreeClient(callBack) {
			var httpOptions = {
				method : 'post',
				url : rootPath + "/dict/fingByOdata?$orderby=dictSort"
			}
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
			common.http({
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}

		
		
		//begin#getDictGroupByCode
		function getDictById(dictId,callBack){
			var httpOptions = {
                method : 'post',
                url : rootPath + "/dict/fingById",
                params:{
                    id : dictId
                }
			};
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
			common.http({
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}
		
	
	}
})();