(function () {
    'expertType strict';

    angular.module('app').factory('expertTypeSvc', expertType);

    expertType.$inject = ['$http', 'expertSvc', '$rootScope', 'bsWin'];

    function expertType($http, expertSvc, $rootScope, bsWin) {
        var service = {
            cleanValue: cleanValue,
            getExpertType: getExpertType,	        //通过专家id获取专家类型
            getExpertTypeById: getExpertTypeById,	//	通过专家类型ID获取专家类型
            saveUpdate: saveUpdate,	            //保存更新数据
            deleteExpertType: deleteExpertType,	//删除专家类型
            saveExpertType: saveExpertType,	    //添加专家类型
        };

        return service;

        // 清空页面数据
        // begin#cleanValue
        function cleanValue() {
            var tab = $("#addExpertType").find('input');
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }

        //begin getExpertTypeByExpertId
        function getExpertType(vm) {
            var httpOptions = {
                method: 'GET',
                url: rootPath + "/expertType/getExpertType?$filter=expert.expertID eq '" + vm.model.expertID + "'"
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.expertTypeList = response.data;
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }


        //begin getExpertTypeById
        function getExpertTypeById(vm) {
            var httpOptions = {
                method: "get",
                url: rootPath + "/expertType/getExpertTypeById",
                params: {expertTypeId: vm.expertTypeId}
            }

            var httpSuccess = function success(response) {
                vm.expertType = response.data;
                vm.expertType.majobSmallDicts = $rootScope.topSelectChange(vm.expertType.maJorBig, $rootScope.DICT.MAJOR.dicts)
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }

        //end getExpertTypeById

        //S_保存专家类别信息
        function saveExpertType(expertType, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertType",
                data: expertType
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//end createExpertType

        //begin
        function saveUpdate(vm) {
            common.initJqValidation($('#expertTypeForm'));
            var isValid = $('#expertTypeForm').valid();
            if (isValid) {
//			vm.model.id=vm.id;
                vm.expertType.expertID = vm.expertID;
                var httpOptions = {
                    method: "put",
                    url: rootPath + "/expertType",
                    data: vm.expertType
                }
                var httpSuccess = function success(response) {

                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            window.parent.$("#addExpertType").data("kendoWindow").close();
//						getExpertType(vm);
                            expertSvc.getExpertById(vm);
                            cleanValue();
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                }
                            })
                        }

                    })
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }//end

        function deleteExpertType(ids,callBack) {
            var httpOptions = {
                method: "delete",
                url: rootPath + "/expertType",
                params:{
                    ids : ids
                }
            }

            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
    }

})();