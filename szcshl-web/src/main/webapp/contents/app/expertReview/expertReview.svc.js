(function () {
    'use strict';

    angular.module('app').factory('expertReviewSvc', expertReview);

    expertReview.$inject = ['$http', '$interval'];

    function expertReview($http, $interval) {
        var service = {
            initExpertGrid: initExpertGrid,	            //初始化待抽取专家列表
            initSelfExpert: initSelfExpert,	            //初始化自选专家页面
            saveSelfExpert: saveSelfExpert,		        //保存自选专家
            updateExpertState: updateExpertState,	    //更改专家状态
            showOutExpertGrid: showOutExpertGrid,       //境外专家选择框
            saveOutExpert: saveOutExpert,               //保存选择的境外专家
            countMatchExperts: countMatchExperts,       //计算符合条件的专家
            getReviewList: getReviewList,               //查询专家评分

            //以下为新方法
            initReview: initReview,                      //初始化评审方案信息
            delSelectedExpert: delSelectedExpert,         //删除已选专家信息
            queryAutoExpert: queryAutoExpert,           //查询符合抽取条件的专家
            validateAutoExpert: validateAutoExpert,     //验证查询的专家是否符合条件
            affirmAutoExpert: affirmAutoExpert,	     //确认已经抽取的专家
            updateJoinState: updateJoinState,           //更改是否参加状态
            initParamValue: initParamValue,             //初始化值
        };
        return service;

        function initParamValue(vm) {
            vm.conditions = new Array();          //条件列表
            vm.customCondition = new Array();
            vm.expertReview = {};                 //评审方案对象
            vm.selfExperts = [],
            vm.selectExperts = [],
            vm.selectIds = [],
            vm.autoExperts = [],
            vm.autoSelExperts = [],
            vm.outsideExperts = [];
        }

        //S_initReview
        function initReview(vm) {
            vm.iscommit = true;
            initParamValue(vm);
            var httpOptions = {
                method: 'get',
                url: rootPath + "/expertReview/html/initByWorkProgramId",
                params: {workProgramId: vm.workProgramId}
            };
            var httpSuccess = function success(response) {
                vm.iscommit = false;
                vm.expertReview = response.data;
                //专家抽取条件
                if (vm.expertReview.expertSelConditionDtoList && vm.expertReview.expertSelConditionDtoList.length > 0) {
                    vm.conditions = vm.expertReview.expertSelConditionDtoList;
                    vm.conditionIndex = vm.expertReview.expertSelConditionDtoList.length;//下标值
                }
                //获取已经抽取的专家
                if (vm.expertReview.expertSelectedDtoList && vm.expertReview.expertSelectedDtoList.length > 0) {
                    vm.expertReview.expertSelectedDtoList.forEach(function (sep, index) {
                        vm.selectIds.push(sep.expertDto.expertID);
                        vm.selectExperts.push(sep);
                        if (sep.selectType == '1') {           //抽取专家
                            vm.autoExperts.push(sep);
                            vm.autoSelExperts.push(sep.expertDto)
                        } else if (sep.selectType == '2') {     //自选专家
                            vm.selfExperts.push(sep);
                        } else if (sep.selectType == '3') {     //境外专家
                            vm.outsideExperts.push(sep);
                        }
                    });
                    if (vm.selectIds.length > 0) {
                        vm.excludeIds = vm.selectIds.join(',');
                    } else {
                        vm.excludeIds = '';
                    }
                }
            };
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_initReview


        function getMinColumns() {
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />", item.expertID)
                    },
                    filterable: false,
                    width: 25,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "name",
                    title: "姓名",
                    width: 100,
                    filterable: false
                },
                {
                    field: "degRee",
                    title: "学位",
                    width: 100,
                    filterable: false
                },
                {
                    field: "sex",
                    title: "性别",
                    width: 50,
                    filterable: true
                },
                {
                    field: "comPany",
                    title: "工作单位",
                    width: 100,
                    filterable: false
                },
                {
                    field: "degRee",
                    title: "职务",
                    width: 100,
                    filterable: false
                }, {
                    field: "expeRttype",
                    title: "专家类别",
                    width: 100,
                    filterable: false
                }
            ];
            return columns;
        }

        function initExpertGrid(vm) {
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

            //S_专家自选
            var dataSource2 = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/expert/findByOData", $("#selfSelExpertForm")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            vm.selfExpertOptions = {
                dataSource: common.gridDataSource(dataSource2),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: getMinColumns(),
                dataBound: dataBound,
                resizable: true
            };//E_专家自选


            //S_市外专家
            var dataSource3 = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/expert/findByOData", $("#outSelExpertForm"), {filter: "state eq '3'"}),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            vm.outExpertOptions = {
                dataSource: common.gridDataSource(dataSource3),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: getMinColumns(),
                dataBound: dataBound,
                resizable: true
            };//E_市外专家
        }

        //S initSelfExpert
        function initSelfExpert(vm) {
            vm.selfExpertOptions.dataSource.read();
            $("#selfExpertDiv").kendoWindow({
                width: "860px",
                height: "500px",
                title: "自选评审专家",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }//E initSelfExpert

        //S_saveSelfExpert
        function saveSelfExpert(vm) {
            var selectIds = common.getKendoCheckId('#selfExpertGrid');
            if (selectIds.length == 0) {
                $("#selfExpertError").html("请选择一条专家数据才能保存！");
            } else if (selectIds.length > 1) {
                $("#selfExpertError").html("自选专家最多只能选择一个！");
            } else {
                $("#selfExpertError").html("");
                window.parent.$("#selfExpertDiv").data("kendoWindow").close();
                vm.iscommit = true;
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/expertReview/saveExpertReview",
                    params: {
                        expertIds: selectIds[0].value,
                        reviewId: vm.expertReview.id,
                        selectType: "2"
                    }
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            vm.iscommit = false;
                            initReview(vm); //重新加载
                            common.alert({
                                vm: vm,
                                msg: "操作成功！",
                                closeDialog: true
                            })
                        }
                    });
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess,
                    onError: function (response) {
                        vm.iscommit = false;
                    }
                });
            }

        }//E_saveSelfExpert


        //S_updateExpertState
        function updateExpertState(vm, expertIds, state) {
            vm.iscommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/updateExpertState",
                params: {expertIds: expertIds, state: state}
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.iscommit = false;
                        common.alert({
                            vm: vm,
                            msg: "操作成功！",
                            closeDialog: true
                        })
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                    vm.iscommit = false;
                }
            });
        }//E_updateExpertState

        //S_showOutExpertGrid
        function showOutExpertGrid(vm) {
            vm.outExpertOptions.dataSource.read();
            $("#outExpertDiv").kendoWindow({
                width: "860px",
                height: "500px",
                title: "自选新专家、市外、境外专家",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }//E_showOutExpertGrid

        //S_saveOutExpert
        function saveOutExpert(vm) {
            var selectIds = common.getKendoCheckId('#outExpertGrid');
            if (selectIds.length == 0) {
                $("#outExpertError").html("请选择一条专家数据才能保存！");
            } else {
                $("#outExpertError").html("");
                window.parent.$("#outExpertDiv").data("kendoWindow").close();
                vm.iscommit = true;
                var selExpertIds = "";
                $.each(selectIds, function (i, obj) {
                    if (i == 0) {
                        selExpertIds += obj.value;
                    } else {
                        selExpertIds += "," + obj.value;
                    }
                });

                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/expertReview/saveExpertReview",
                    params: {
                        expertIds: selExpertIds,
                        reviewId: vm.expertReview.id,
                        selectType: "3"
                    }
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            vm.iscommit = false;
                            initReview(vm); //重新加载
                            common.alert({
                                vm: vm,
                                msg: "操作成功！",
                                closeDialog: true
                            })
                        }
                    });
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess,
                    onError: function (response) {
                        vm.iscommit = false;
                    }
                });
            }
        }//E_saveOutExpert

        //S_countMatchExperts
        function countMatchExperts(vm, sortIndex) {
            var data = {};
            vm.conditions.forEach(function (t, number) {
                if (t.sort == sortIndex) {
                    data = t;
                    data.maJorBig = $("#maJorBig" + t.sort).val();
                    data.maJorSmall = $("#maJorSmall" + t.sort).val();
                    data.expeRttype = $("#expeRttype" + t.sort).val();
                }
            });
            data.expertReviewDto = {};
            data.expertReviewDto.id = vm.expertReview.id;   //抽取方案ID
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expert/countReviewExpert",
                data: data,
                params: {
                    workprogramId: vm.workProgramId,
                    reviewId: vm.expertReview.id
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        $("#expertCount" + sortIndex).html(response.data);
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_countMatchExperts

        //begin##getReviewList
        function getReviewList(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/expertReview/html/getReviewList"
            }
            var httpSuccess = function success(response) {
                vm.reviewList = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                    vm.iscommit = false;
                }
            });
        }//end##getReviewList

        //S_queryAutoExpert
        function queryAutoExpert(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expert/findReviewExpert",
                headers: {
                    "contentType": "application/json;charset=utf-8"  //设置请求头信息
                },
                traditional: true,
                dataType: "json",
                data: angular.toJson(vm.conditions),//将Json对象序列化成Json字符串，JSON.stringify()原生态方法
                params: {
                    workprogramId: vm.workProgramId,
                    reviewId: vm.expertReview.id
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        if (response.data) {
                            vm.autoExpertList = response.data;
                            validateAutoExpert(vm);
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
        }//E_queryAutoExpert


        //S_validateAutoExpert
        function validateAutoExpert(vm) {
            if (!vm.autoExpertList || vm.autoExpertList.length < 1) {
                common.alert({
                    vm: vm,
                    msg: "本次被抽取的专家人数不满足抽取条件，抽取无效！请重新设置抽取条件！"
                })
                return;
            }
            //重置参数
            var totalExpertCount = 0;
            var officeExperts = new Array();
            var nativeExperts = new Array();

            vm.autoExpertList.forEach(function (e, number) {
                if (e.state == '2' || e.state == 2) {
                    officeExperts.push(e);
                } else {
                    nativeExperts.push(e);
                }
            });
            vm.conditions.forEach(function (c, number) {
                totalExpertCount += parseInt(c.officialNum);
            });

            if (totalExpertCount > officeExperts.length) {
                common.alert({
                    vm: vm,
                    msg: "本次被抽取的正选专家人数不满足抽取条件，抽取无效！请重新设置抽取条件！"
                })
                return;
            } else if (totalExpertCount > nativeExperts.length) {
                common.alert({
                    vm: vm,
                    msg: "本次被抽取的备选专家人数不满足抽取条件，抽取无效！请重新设置抽取条件！"
                })
                return;
            } else {
                vm.showAutoExpertWin();

                //随机抽取
                var timeCount = 0;
                var selAutoExpertIds = "";
                vm.t = $interval(function () {
                    if (totalExpertCount == 0) {
                        $interval.cancel(vm.t);
                        //保存抽取的专家
                        vm.iscommit = true;
                        var httpOptions = {
                            method: 'post',
                            url: rootPath + "/expertReview/saveExpertReview",
                            params: {
                                expertIds: selAutoExpertIds,
                                reviewId: vm.expertReview.id,
                                selectType: "1",
                                isDraw : true
                            }
                        }
                        var httpSuccess = function success(response) {
                            common.requestSuccess({
                                vm: vm,
                                response: response,
                                fn: function () {
                                    vm.iscommit = false;
                                    initReview(vm);
                                    common.alert({
                                        vm: vm,
                                        msg: "操作成功！",
                                        closeDialog: true
                                    })
                                }
                            });
                        }
                        common.http({
                            vm: vm,
                            $http: $http,
                            httpOptions: httpOptions,
                            success: httpSuccess,
                            onError: function (response) {
                                vm.iscommit = false;
                            }
                        });
                    }else{
                        var selscope = Math.floor(Math.random() * (vm.autoExpertList.length));
                        vm.showAutoExpertName = vm.autoExpertList[selscope].name;
                        timeCount++;
                        if (timeCount % 10 == 0) {
                            var selskey = Math.floor(Math.random() * (officeExperts.length));
                            vm.autoSelExperts.push(officeExperts[selskey]);
                            selAutoExpertIds += officeExperts[selskey].expertID + ",";
                            officeExperts.forEach(function (t, number) {
                                if (officeExperts[selskey].expertID == t.expertID) {
                                    officeExperts.splice(number, 1);
                                }
                            });
                            selskey = Math.floor(Math.random() * (nativeExperts.length));
                            vm.autoSelExperts.push(nativeExperts[selskey]);
                            selAutoExpertIds += nativeExperts[selskey].expertID + ",";
                            nativeExperts.forEach(function (t, number) {
                                if (nativeExperts[selskey].expertID == t.expertID) {
                                    nativeExperts.splice(number, 1);
                                }
                            });
                            totalExpertCount--;
                        }
                    }
                }, 200);
            }
        }//E_validateAutoExpert

        //S_updateJoinState
        function updateJoinState(vm, ids, joinState) {
            vm.iscommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/updateJoinState",
                params: {expertIds: ids, state: joinState}
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.iscommit = false;
                        vm.selectExperts.forEach(function (e, number) {
                            if (ids.indexOf(e.id) >= 0) {
                                e.isJoin = joinState;
                            }
                        });
                        common.alert({
                            vm: vm,
                            msg: "操作成功！",
                            closeDialog: true
                        })
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                    vm.iscommit = false;
                }
            });
        }//E_updateJoinState

        //S_affirmAutoExpert(确认抽签结果)
        function affirmAutoExpert(vm) {
            vm.iscommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/affirmAutoExpert",
                params: {
                    reviewId: vm.expertReview.id
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.iscommit = false;
                        window.parent.$("#aotuExpertDiv").data("kendoWindow").close();
                        common.alert({
                            vm: vm,
                            msg: "操作成功！",
                            closeDialog: true
                        })
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                    vm.iscommit = false;
                }
            });
        }//E_affirmAutoExpert

        //S_delSelectedExpert(删除已选专家)
        function delSelectedExpert(vm, delIds) {
            vm.iscommit = true;
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/expertSelected",
                params: {
                    id: delIds,
                    reviewId: vm.expertReview.id,
                    deleteAll: false
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.iscommit = false;
                        initReview(vm);
                        common.alert({
                            vm: vm,
                            msg: "操作成功！",
                            closeDialog: true
                        })
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                    vm.iscommit = false;
                }
            });
        }//E_delSelectedExpert

    }
})();