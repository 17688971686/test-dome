(function () {
    'expert strict';

    angular.module('app').controller('expertCtrl', expert);

    expert.$inject = ['$rootScope', 'expertSvc', '$state','templatePrintSvc', 'headerSvc'];

    function expert($rootScope, expertSvc, $state,templatePrintSvc , headerSvc) {
        var vm = this;
        vm.data = {};
        vm.title = '专家列表';
        vm.expertId = "";
        vm.headerType = "专家类型";
        vm.fileName = "专家信息";
        vm.expert = {};
        vm.expertList = new Array(10); // 控制空白行
        vm.searchmodel = {};

        //获取到当前的列表
        vm.stateName = $state.current.name;
        //查询参数
        vm.queryParams = {};
        //点击时。保存查询的条件和grid列表的条件
        vm.saveView = function(){
            $rootScope.storeView(vm.stateName,{gridParams:vm.gridOptions.dataSource.transport.options.read.data(),queryParams:vm.queryParams,data:vm});
        }

        activate();
        function activate() {
            if($rootScope.view[vm.stateName]){
                var preView = $rootScope.view[vm.stateName];
                //恢复查询条件
                if(preView.gridParams){
                    vm.gridParams = preView.gridParams;
                }
                //恢复表单参数
                if(preView.data){
                    vm.searchmodel = preView.data.searchmodel;
                }
                //恢复页数页码
                if(preView.queryParams){
                    vm.queryParams=preView.queryParams;
                }
                expertSvc.grid(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            }else {
                expertSvc.grid(vm);
            }

        }

        vm.search = function () {
            vm.saveView();
            expertSvc.searchMuti(vm);
        };

        vm.searchAudit = function () {
            expertSvc.searchMAudit(vm);
        };

        vm.formReset = function () {
            //expertSvc.formReset(vm);
            vm.searchmodel = {};
        }

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    expertSvc.deleteExpert(vm, id , function(data){
                        vm.isSubmit = false;
                        vm.gridOptions.dataSource.read();
                    });
                }
            })
        };

        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: '请选择数据'

                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.del(idStr);
            }
        };

        //S 查看专家详细
        vm.findExportDetail = function (id) {
            expertSvc.getExpertById(id, function (data) {
                vm.model = data;
                $("#queryExportDetail").kendoWindow({
                    width: "80%",
                    height: "620px",
                    title: "专家详细信息",
                    visible: false,
                    modal: true,
                    open:function(){
                        $("#expertPhotoSrc").attr("src", rootPath + "/expert/transportImg?expertId=" + vm.model.expertID + "&t=" + Math.random());
                        //tab标签
                        $('#myTab li').click(function (e) {
                            var aObj = $("a", this);
                            e.preventDefault();
                            aObj.tab('show');
                            var showDiv = aObj.attr("for-div");
                            $(".tab-pane").removeClass("active").removeClass("in");
                            $("#" + showDiv).addClass("active").addClass("in").show(500);
                        })
                        //项目签收编辑模板打印
                        vm.editPrint = function () {
                            templatePrintSvc.templatePrint("expertApply_templ");
                        }
                        //评审过项目
                        vm.reviewProjectList = [];
                        expertSvc.reviewProjectGrid(vm.model.expertID,function(data){
                            vm.isLoading = false;
                            if(data && data.length > 0){
                                vm.reviewProjectList = data;
                                vm.noData = false;
                            }else{
                                vm.noData = true;
                            }

                        });

                    },
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            });
        }
        //S 查看专家详细


        /**
         * 导出execl功能
         */
        vm.exportToExcel = function () {
            expertSvc.exportToExcel(vm);
        }

        /**
         * 自定义报表
         */
        vm.selectHeader = function(){
            headerSvc.selectHeaderWindow(vm,vm.headerType);
        }
    }
})();
