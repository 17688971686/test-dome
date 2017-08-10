(function() {
	'use strict';

	angular.module('app').factory('registerFileSvc', registerFile);

	registerFile.$inject = ['$rootScope', '$http'];

	function registerFile($rootScope, $http) {
		var addregister_url = rootPath + "/addRegisterFile";
		
		return {
			grid : grid
		};

		// begin#grid
		function grid(vm) {
			
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
						type : 'odata',
						transport : common.kendoGridinlineConfig().transport(
								vm, {
									readUrl : addregister_url
											+ "/findByOData",
									updateUrl : addregister_url + "/update",
									destroyUrl : addregister_url + "/delete",
									createUrl : addregister_url + "/create/"+vm.model.signid
								},{filter: "signid eq '"+vm.model.signid+"'"}),
						schema : common.kendoGridConfig().schema({
									id : "id",
									fields : {
										createdDate : {
											type : "date"
										},
										modifiedDate : {
											type : "date"
										},
										fileName : {
											nullable : true,
											type : "String"
										},
										totalNum : {
											type : "number",
											validation : {
												required : true
											}
										},
										isHasOriginfile : {
											type : "boolean"
										},
										isHasCopyfile : {
											type : "boolean"
										},
										suppleDeclare : {
											type : "string"
										},
										suppleDate : {
											type : "date"
										},
										createdDate : {
											type : "date"
										},
										modifiedDate : {
											type : "date"
										}
									}
								}),
						batch : true,
						pageSize : 10,
						serverPaging : true,
						serverSorting : true,
						serverFiltering : true,
						sort : {
							field : "createdDate",
							dir : "desc"
						}
					});

			// End:dataSource

			// S_序号
			var dataBound = function() {
				var rows = this.items();
				var page = this.pager.page() - 1;
				var pagesize = this.pager.pageSize();
				$(rows).each(function() {
							var index = $(this).index() + 1 + page * pagesize;
							var rowLabel = $(this).find(".row-number");
							$(rowLabel).html(index);
						});
			}
			// S_序号
			// Begin:column
			var columns = [{
						field : "",
						title : "序号",
						width : 50,
						filterable : false,
						template : "<span class='row-number'></span>"
					}, {
						field : "fileName",
						title : "资料名称",
						width : "120px",
						filterable : false
					}, {
						field : "totalNum",
						title : "份数",
						width : "120px",
						filterable : false
					}, {
						field : "isHasOriginfile",
						title : "原件",
						width : "120px",
						filterable : false,
						template : function(item) {
							if (item.isHasOriginfile) {
								return "是";
							} else {
								return "否";
							}
						}
					}, {
						field : "isHasCopyfile",
						title : "复印件",
						width : "120px",
						filterable : false,
						template : function(item) {
							if (item.isHasCopyfile) {
								return "是";
							} else {
								return "否";
							}
						}
					}, {
						field : "suppleDeclare",
						title : "补充说明",
						width : "120px",
						filterable : false
					}, {
						field : "suppleDate",
						title : "补充日期",
						width : "120px",
						format : "{0:yyyy-MM-dd}",
						filterable : false
					}, {
						command : ["destroy"],
						title : "&nbsp;",
						width : "250px"
					}];// 列
			// End:column

			vm.gridOptions = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridinlineConfig().filterable,
				pageable : common.kendoGridinlineConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : columns,
				toolbar : ["create", "save", "cancel"],
				dataBound : dataBound,
				editable : true,
				navigatable : true,
				resizable : true
			};

		}// end fun grid

	}
})();