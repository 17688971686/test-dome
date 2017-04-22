(function() {
	'use strict';

	angular.module('app').factory('dictSvc', dict);

	dict.$inject = [ '$http' ,'$state'];

	function dict($http,$state) {
		//var url_user = rootPath + "/dict";
		var url_back = '#/dict';
		var url_dictgroup = rootPath + "/dict";
		var service = {
			initDictGroupTree:initDictGroupTree,
			createDictGroup:createDictGroup,
			getDictById:getDictById,
			updateDictGroup:updateDictGroup,
			deleteDictData:deleteDictData,
			initpZtreeClient:initpZtreeClient,
			getTreeData:getTreeData
		};

		return service;

		
		function getTreeData(vm){
			
			var httpOptions = {
					method : 'get',
					url : url_dictgroup
			};
			
			var httpSuccess = function success(response) {

				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						
						vm.treeData = {};
						vm.treeData = response.data.value;
					
						if(vm.isUpdate&&vm.treeData&&vm.model.parentId){
							for(var i = 0;i<vm.treeData.length;i++){
								if(vm.treeData[i].dictId == vm.model.parentId){
									vm.model.parentDictName = vm.treeData[i].dictName;
									break;
								}
							}
						}
						
					}

				});

			};
			
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
			
		}
		
		

		
		//beginDeleteGroup
		function deleteDictData(vm,id){
		
           
			vm.isSubmit = true;
			var httpOptions = {
				method : 'delete',
				url : url_dictgroup,
				data : id

			}
			var httpSuccess = function success(response) {

				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.isSubmit = false;	

						common.alert({
							vm : vm,
							msg : "操作成功",
							fn : function() {	
								$('.alertDialog').modal('hide');
								$('.modal-backdrop').remove();
								//location.href = url_back;
							
							}
						});
						
						
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

		//begin#createDictGroup
		function createDictGroup(vm){
			common.initJqValidation();
		
			var isValid = $('form').valid();
			
			if(vm.model.dictType == "1"&&!vm.model.parentId){
				vm.message = "字典数据项必须选择字典类型";
				isValid = false;
			}
			
			if(isValid){
				vm.isSubmit = true;
				
				var httpOptions = {
					method : 'post',
					url : url_dictgroup,
					data : vm.model
				}
				
				
				var httpSuccess = function success(response) {

					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {

							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {	
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
									location.href = url_back;
								
								}
							});
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
		
		//updateDictGroup
		function updateDictGroup(vm){
			common.initJqValidation();
			
			var isValid = $('form').valid();
			
			if(isValid){
				vm.isSubmit = true;
		
				var httpOptions = {
					method : 'put',
					url : url_dictgroup,
					data : vm.model
				}

				var httpSuccess = function success(response) {

					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {

							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {	
									vm.isSubmit = false;
									$('.alertDialog').modal('hide');									
									//location.href = url_back;
								
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
		
		// begin#initZtreeClient
		function initDictGroupTree(vm) {
			var httpOptions = {
				method : 'get',
				url : url_dictgroup
			}

			var httpSuccess = function success(response) {

				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						var zTreeObj;
						var setting = {
							check : {
								chkboxType : {
									"Y" : "s",
									"N" : "s"
								},
								enable : true
							},
							callback: {
								onClick: zTreeOnClick
								//onCheck: zTreeOnCheck
							},
							data: {
								simpleData: {
									enable: true,
									idKey: "id",
									pIdKey: "pId",
									rootPId: 0
								}
							}
						};
						
						function zTreeOnClick(event, treeId, treeNode) {
						   $state.go('dict.edit', { id: treeNode.id});
						};
						function zTreeOnCheck(event, treeId, treeNode) {
							var selId = treeNode.id;
							if(!vm.model.dels){
								vm.model.dels = [];
							}
							var delIds = vm.model.dels;
							if(treeNode.checked){
								delIds.push(selId);
							}else{
								for(var i =0;i<delIds.length;i++){
									if(delIds[i] == selId){
										delIds.splice(i);
										break;
									}
								}
							}
							
						};
						var zNodes = $linq(response.data.value).select(
								function(x) {
									var isParent = false;
									var pId = null;
									
									if(x.dictType == "0"){//编码
										isParent = true;
									
										if(x.parentId){										
											pId = x.parentId;
										}
										return {
											id : x.dictId,
											name : x.dictName,
											isParent:isParent,
											pId:pId
										};
										
									}else if(x.dictType == "1"){//数据项
										if(x.parentId){										
											pId = x.parentId;
										}
										return {
											id : x.dictId,
											name : x.dictName,
											isParent:isParent,
											pId:pId
										};
									}
									
									
								}).toArray();
						/*var rootNode = {
							id : '',
							name : '字典集合',
							children : zNodes
						};*/
						zTreeObj = $.fn.zTree.init($("#zTree"), setting,
								zNodes);
						vm.dictsTree = zTreeObj;
						if (vm.isUpdate) {
							//updateZtree(vm);

						}
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

		
		
		// begin#initpZtreeClient
		function initpZtreeClient(vm) {
			var httpOptions = {
				method : 'get',
				url : url_dictgroup
			}
			var httpSuccess = function success(response) {

				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						
						var zTreeObj;
						var setting = {
							check: {enable: true,chkStyle: "radio",radioType: "all"},
							callback: {
								onCheck: zTreeOnCheck
							},
							data: {
								simpleData: {
									enable: true,
									idKey: "id",
									pIdKey: "pId",
									rootPId: 0
								}
							}
						};
						
						
						function zTreeOnCheck(event, treeId, treeNode) {
							var selId = treeNode.id;
							vm.model.parentId = selId;
							vm.model.parent = treeNode;
							if(vm.model.dictType == '1'){
								vm.model.dictCode = treeNode.dictCode;
							}
							vm.apply();	
						};
						var zNodes = $linq(response.data.value).where(function (x) { return x.dictType == "0"; }).select(
								function(x) {
									if(x.dictType == "0"){
										var isParent = false;
										var pId = null;
										if(x.parentId){
											isParent = true;
											pId = x.parentId;
										}
										return {
											id : x.dictId,
											name : x.dictName,
											isParent:true,
											pId:pId,
											dictCode:x.dictCode
										};
									}
									
								}).toArray();
					
						zTreeObj = $.fn.zTree.init($("#pzTree"), setting,zNodes);
					
						
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

		
		
		//begin#getDictGroupByCode
		function getDictById(vm){
			var httpOptions = {
					method : 'get',
					url : common.format(url_dictgroup + "?$filter=dictId eq '{0}' ", vm.id)
			};
			
			var httpSuccess = function success(response) {
				vm.model = response.data.value[0];
				if(vm.model.dictType=="0"){
					vm.model.dictTypeName = "字典类型";
				}else if(vm.model.dictType=="1"){
					vm.model.dictTypeName = "字典数据";
				}
				getTreeData(vm);
			}

			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}
		
	
	}
})();