(function () {
    'use strict';

    angular.module('app').factory('sharingPlatlformSvc', sharingPlatlform);

    sharingPlatlform.$inject = ['$http','bsWin'];

    function sharingPlatlform($http,bsWin) {
        var url_sharingPlatlform = rootPath + "/sharingPlatlform", url_back = '#/sharingPlatlform';

        var service = {
            grid: grid,
            getSharingPlatlformById: getSharingPlatlformById,
            createSharingPlatlform: createSharingPlatlform,
            deleteSharingPlatlform: deleteSharingPlatlform,
            getSharingDetailById: getSharingDetailById,	    //获取详情页
            initOrgAndUser: initOrgAndUser,                 //初始化部门和用户
            initSeleObj : initSeleObj,                      //初始化选择的用户
            updatePublish : updatePublish,                  //批量更改发布状态
        };

        return service;

        //S_初始化选择的用户
        function initSeleObj(vm){
            //没有加载完整或者是所有人可见的，不需要初始化选择的用户
            if(!vm.businessFlag.isLoadOrgUser || !vm.businessFlag.isLoadModel || (vm.model.isNoPermission && vm.model.isNoPermission == '9')){
                return ;
            }
            if((vm.shareOrgList && vm.shareOrgList.length > 0)){
                //1、先计算选择的部门
                if(vm.model.privilegeDtoList && vm.model.privilegeDtoList.length > 0){
                    vm.shareOrgList.forEach(function (so,i){
                        vm.model.privilegeDtoList.forEach(function (p,index) {
                            if(p.businessType == 1 || p.businessType == "1"){
                                if(p.businessId == so.id){
                                    so.isChecked = true;
                                    so.userDtos.forEach(function (u,k){
                                        u.isDisabled = true;
                                    });
                                }
                            }else if(p.businessType == 2 || p.businessType == "2"){
                                so.userDtos.forEach(function (u,k){
                                    if(p.businessId == u.id){
                                        u.isChecked = true;
                                    }
                                });
                            }
                        })

                    });
                }

                if(vm.noOrgUsetList && vm.noOrgUsetList.length > 0 &&  vm.model.privilegeDtoList!=undefined){
                    vm.noOrgUsetList.forEach(function (nu,i){
                        vm.model.privilegeDtoList.forEach(function (p,index) {
                             if( (p.businessType == 2 || p.businessType == "2") && p.businessId == nu.id){
                                 nu.isChecked = true;
                            }
                        })
                    });
                }
                vm.businessFlag.isInitSeled = true;
            }
        }//E_initSeleObj

        //S_初始化部门和用户
        function initOrgAndUser(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sharingPlatlform/initOrgAndUser",
            };
            var httpSuccess = function success(response) {
                vm.shareOrgList = response.data.orgDtoList;
                vm.noOrgUsetList = response.data.noOrgUserList;
                vm.businessFlag.isLoadOrgUser = true;
                initSeleObj(vm);
            };
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_initOrgAndUser

        //S 详情页面
        function getSharingDetailById(vm, id) {
            var httpOptions = {
                method: 'get',
                url: url_sharingPlatlform + "/html/sharingDeatilById",
                params: {id: id}
            };
            var httpSuccess = function success(response) {
                vm.model = response.data;
            };
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }


        // begin#deleteSharingPlatlform
        function deleteSharingPlatlform(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_sharingPlatlform + "/sharingDelete",
                data: id
            };

            var httpSuccess = function success(response) {
                vm.isSubmit = false;
                vm.gridOptions.dataSource.read();
                bsWin.alert("操作成功");
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#createSharingPlatlform
        function createSharingPlatlform(vm) {
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                //如果不是全员可看，则要获取选择的部门和用户
                if(vm.model.isNoPermission != 9){
                    vm.model.privilegeDtoList = [];
                    //1、先计算选择的部门
                    var oCheck = $("input[name='shareOrg']:checked");
                    if (oCheck.length > 0) {
                        for (var i = 0; i < oCheck.length; i++) {
                            var shareOrg = {};
                            shareOrg.businessId = oCheck[i].value;
                            shareOrg.businessType = "1";
                            vm.model.privilegeDtoList.push(shareOrg);
                        }
                    }
                    //2、再选择人员
                    var uCheck = $("input[name='shareUser']:not(:disabled):checked");
                    if (uCheck.length > 0) {
                        for (var i = 0; i < uCheck.length; i++) {
                            var shareUser = {};
                            shareUser.businessId = uCheck[i].value;
                            shareUser.businessType = "2";
                            vm.model.privilegeDtoList.push(shareUser);
                        }
                    }

                }
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/sharingPlatlform/saveSharing",
                    data: vm.model
                };

                var httpSuccess = function success(response) {
                    vm.isSubmit = false;
                    vm.model.sharId = response.data.sharId;
                    bsWin.alert("操作成功");
                };

                common.http({
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess,
                    onError:function () {
                        vm.isSubmit = false;
                    }
                });

            }
        }

        // begin#getSharingPlatlformById
        function getSharingPlatlformById(vm,callBack) {
            var httpOptions = {
                method: 'get',
                url: url_sharingPlatlform + "/html/findById",
                params: {
                    id: vm.model.sharId
                }
            };
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#grid
        function grid(vm) {
            // Begin:dataSource
            var dataSource = common.kendoGridDataSource(url_sharingPlatlform + "/findByCurUser",$("#formSharing"),vm.queryParams.page,vm.queryParams.pageSize,vm.gridParams);
            // End:dataSource
            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.sharId)
                    },
                    filterable: false,
                    width: 30,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "unitSort",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "theme",
                    title: "主题",
                    width: "35%",
                    filterable: false
                },
                {
                    field: "publishUsername",
                    title: "发布人",
                    width: "10%",
                    filterable: false
                },
                {
                    field: "publishDate",
                    title: "发布时间",
                    format: "{0:yyyy-MM-dd hh24:mm:ss}",
                    width: "18%",
                    filterable: false
                },
                {
                    field: "isPublish",
                    title: "发布状态",
                    width: "12%",
                    template: function (item) {
                       if(item.isPublish && item.isPublish == 9){
                           return "已发布";
                       }else{
                           return "未发布";
                       }
                    },
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: "20%",
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            item.sharId, item.sharId, "vm.del('" + item.sharId + "')");
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                pageable : common.kendoGridConfig(vm.queryParams).pageable,
                dataBound:common.kendoGridConfig(vm.queryParams).dataBound,
                columns: columns,
                resizable: true
            };

        }// end fun grid


        //S_批量发布
        function updatePublish(vm,isUpdate){
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: "请选择要批量发布的数据"
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/sharingPlatlform/updatePublish",
                    params: {
                        ids: ids.join(','),
                        status: (isUpdate == true)?'9':'0'
                    }
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            vm.isSubmit = false;
                            vm.gridOptions.dataSource.read();
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                closeDialog: true
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
        }//E_bathPublish

    }
})();