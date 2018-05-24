(function () {
    'use strict';

    angular.module('app').factory('userSvc', user);

    user.$inject = ['$http', 'bsWin'];

    function user($http, bsWin) {
        var url_user = rootPath + "/user", url_back = '#/user', url_role = rootPath + "/role/fingByOData",
            url_dictgroup = rootPath + "/dict";
        var service = {
            grid: grid,
            getUserById: getUserById,
            initZtreeClient: initZtreeClient,
            createUser: createUser,
            deleteUser: deleteUser,
            updateUser: updateUser,
            getOrg: getOrg,
            queryUser: queryUser,
            getZtreeChecked: getZtreeChecked,
            resetPwd: resetPwd, //重置密码
            findUserAndOrg : findUserAndOrg,  //获取部门下的所有用户,
            getAllTaskList : getAllTaskList,    //获取可以设置代办的人员列表
        };

        return service;

        /**
         * 获取该用户所有的
         * @param userId
         * @param callBack
         */
        function getAllTaskList(userId,callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/user/findAllTaskList",
                params : {
                    userId : userId
                }
            }
            var httpSuccess = function success(response){
                if(callBack != undefined && typeof  callBack == 'function'){
                    callBack(response.data);
                }
            }
            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }

        //begin findUserAndOrg
        function findUserAndOrg(callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/user/findUserAndOrg"
            }

            var httpSuccess = function success(response){
                if(callBack != undefined && typeof  callBack == 'function'){
                    callBack(response.data);
                }
            }

            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end findUserAndOrg

        //begin resetPwd
        function resetPwd(vm, ids) {
            var httpOptions = {
                method: 'put',
                url: url_user + '/resetPwd',
                params: {ids: ids}
            }
            var httpSuccess = function success(response) {
                bsWin.success("重置密码成功！",function(){
                    vm.gridOptions.dataSource.read();
                });
            }
            common.http({
                vm: vm,
                httpOptions: httpOptions,
                success: httpSuccess,
                $http: $http
            });
        }
        //end resetPwd


        //begin initUserNo
        /*function initUserNo(vm){
        
        	var httpOptions={
        		method : "get",
        		url : url_user +"/createUserNo"
        	}
        	
        	var httpSuccess=function success(response){
        	
        		vm.model={};
        		var userNo=response.data;
        		vm.model.userNo=userNo.substring(1,userNo.length-1);
        	}
         common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
        	
        }*///end initUserNo

        // begin#updateUser
        function updateUser(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

                // zTree
                var nodes = getZtreeChecked();
                var nodes_role = $linq(nodes).where(function (x) {
                    return x.isParent == false;
                }).select(function (x) {
                    return {
                        id: x.id,
                        roleName: x.name
                    };
                }).toArray();
                vm.model.roleDtoList = nodes_role;

                var httpOptions = {
                    method: 'put',
                    url: url_user,
                    data: vm.model
                }

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
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

            } else {
                // common.alert({
                // vm:vm,
                // msg:"您填写的信息不正确,请核对后提交!"
                // })
            }
        }

        // begin#deleteUser
        function deleteUser(vm, id,callBack) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_user,
                data: id
            }
            var httpSuccess = function success(response) {
                vm.isSubmit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError : function(){vm.isSubmit = false;}
            });
        }

        // begin#createUser
        function createUser(userModel, isSubmit, callBack) {
            isSubmit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/user",
                data: userModel
            }

            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError:function(){
                    isSubmit = false;
                }
            });
        }

        //获取部门信息
        function getOrg(callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/org/listAll",
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

        // begin#initZtreeClient
        function initZtreeClient(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/role/findAllRoles"
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        var zTreeObj;
                        var setting = {
                            check: {
                                chkboxType: {
                                    "Y": "ps",
                                    "N": "ps"
                                },
                                enable: true
                            }
                        };
                        var zNodes = $linq(response.data).select(
                            function (x) {
                                return {
                                    id: x.id,
                                    name: x.roleName
                                };
                            }).toArray();
                        var rootNode = {
                            id: '',
                            name: '角色集合',
                            children: zNodes
                        };
                        zTreeObj = $.fn.zTree.init($("#zTree"), setting, rootNode);
                        if (vm.isUpdate) {
                            updateZtree(vm);
                        }
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

        // begin#getUserById
        function getUserById(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/user/findUserById",
                params: {
                    userId: vm.id
                }
            }
            var httpSuccess = function success(response) {
                vm.model = response.data;
                if (vm.isUpdate) {
                    initZtreeClient(vm);
                }
            }

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
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/user/fingByOData", $("#usersform"),{filter: "loginName ne 'admin'"}),
                schema: common.kendoGridConfig().schema({
                    id: "id"
                }),
                serverPaging: false,
                serverSorting: false,
                serverFiltering: false,
                pageSize: 10
            });
            // End:dataSource
            //S_序号
            var dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            }
            //S_序号
            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />", item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox' />"
                },
                {
                    field: "rowNumber",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                }
                ,
                {
                    field: "loginName",
                    title: "登录名",
                    width: 100,
                    filterable: false
                },
                {
                    field: "displayName",
                    title: "显示名",
                    width: 100,
                    filterable: false
                },
                {
                    field: "orgDto.name",
                    title: "所属部门",
                    width: 100,
                    filterable: false
                },
                {
                    field: "userIP",
                    title: "登录IP",
                    width: 160,
                    filterable: false
                },
                {
                    field: "lastLogin",
                    title: "最后登录时间",
                    width: 160,
                    filterable: false
                },
                {
                    field: "",
                    title: "所属角色",
                    width: 160,
                    filterable: false,
                    template: function (item) {
                        if (item.roleDtoList) {
                            var resultStr = "";
                            for (var i = 0, l = item.roleDtoList.length; i < l; i++) {
                                if (i == 0) {
                                    resultStr += item.roleDtoList[i].roleName
                                } else {
                                    resultStr += ", " + item.roleDtoList[i].roleName;
                                }
                            }
                            return resultStr;
                        }
                        else {
                            return " ";
                        }
                    }
                },
                {
                    field: "",
                    title: "在职情况",
                    width: 80,
                    filterable: false,
                    template: function (item) {
                        if (item.jobState && item.jobState == "t") {
                            return "在职";
                        } else {
                            return "已撤销";
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),"vm.del('" + item.id + "')", item.id);

                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound: dataBound,
                resizable: true
            };

        }// end fun grid

        //查询
        function queryUser(vm) {
            vm.gridOptions.dataSource._skip = 0;
            vm.gridOptions.dataSource.read();
        }

        // begin common fun
        function getZtreeChecked() {
            var treeObj = $.fn.zTree.getZTreeObj("zTree");
            var nodes = treeObj.getCheckedNodes(true);
            return nodes;
        }

        function updateZtree(vm) {
            var treeObj = $.fn.zTree.getZTreeObj("zTree");
            var checkedNodes = $linq(vm.model.roleDtoList).select(function (x) {
                return x.roleName;
            }).toArray();
            var allNodes = treeObj.getNodesByParam("level", 1, null);

            var nodes = $linq(allNodes).where(function (x) {
                return $linq(checkedNodes).contains(x.name);
            }).toArray();

            for (var i = 0, l = nodes.length; i < l; i++) {
                treeObj.checkNode(nodes[i], true, true);
            }
        }

        // end common fun
    }
})();