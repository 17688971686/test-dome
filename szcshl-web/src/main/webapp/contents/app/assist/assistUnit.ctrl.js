(function() {
	'use strict';

	angular.module('app').controller('assistUnitCtrl', assistUnit);

	assistUnit.$inject = ['$location', 'assistUnitSvc'];

	function assistUnit($location, assistUnitSvc) {
		var vm = this;
		vm.title = '协审单位';
		vm.resource = {};

		vm.del = function(id) {
			vm.id = id;
			assistUnitSvc.getUnitUser(vm, function() {
						if (vm.resource.count == 0) {
							common.confirm({
										vm : vm,
										title : "",
										msg : "确认要删除数据吗？",
										fn : function() {
											$('.confirmDialog').modal('hide');
											vm.resource = {};
											assistUnitSvc.deleteAssistUnit(vm, id);
										}
									});
						} else {
							common.confirm({
										vm : vm,
										title : "",
										msg : "必须先删除单位下的所有成员！",
										fn : function() {
											$('.confirmDialog').modal('hide');

										}
									});

						}
					});

		}

		vm.dels = function() {
			var selectIds = common.getKendoCheckId('.grid');
			if (selectIds.length == 0) {
				common.alert({
							vm : vm,
							mag : "请选择数据"
						});
			} else {
				var ids = [];
				for (var i = 0; i < selectIds.length; i++) {
					ids.push(selectIds[i].value);
				}
				var idStr = ids.join(",");
				assistUnitSvc.deleteAssistUnit(vm, idStr);

			}
		}

		vm.queryAssistUnit = function() {
			assistUnitSvc.queryAssistUnit(vm);
		}

		activate();
		function activate() {
			assistUnitSvc.grid(vm);
		}
	}
})();
