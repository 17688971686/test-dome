(function () {
    'expert strict';

    angular.module('app').controller('expertEditCtrl', expert);

    expert.$inject = ['bsWin', 'projectExpeSvc', 'workExpeSvc', 'expertSvc', 'expertOfferSvc', 'expertTypeSvc', '$state','$rootScope'];

    function expert(bsWin, projectExpeSvc, workExpeSvc, expertSvc, expertOfferSvc, expertTypeSvc, $state,rootScope) {
        var vm = this;
        vm.model = {};
        vm.data = {};
        vm.title = '专家信息编辑';
        vm.isuserExist = false;
        vm.expertID = $state.params.expertID;
        //一些参数
        vm.showSS = true;
        vm.showSC = false;
        vm.showWS = true;
        vm.showWC = false;
        vm.MW = false;
        vm.MS = false;
        vm.expertList = new Array(10); // 控制空白行

        activice();
        function activice() {
            //初始化专家信息
            if (vm.expertID) {
                expertSvc.getExpertById(vm.expertID, function (data) {
                    vm.model = data;
                    vm.showSS = false;
                    vm.showSC = true;
                    vm.showWS = false;
                    vm.showWC = true;
                    initUpload(vm);
                    $("#expertPhotoSrc").attr("src", rootPath + "/expert/transportImg?expertId=" + vm.model.expertID + "&t=" + Math.random());
                });
            }
        }

        /**
         * 查看专家评审过的项目列表
         * @param expertId
         */
        vm.selReviewProject = function () {
            if(vm.model.expertID){
                $("#reviewProject").kendoWindow({
                    width: "75%",
                    height: "600px",
                    title: "评审项目列表",
                    visible: false,
                    open:function(){
                        vm.isLoading = true;
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
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "close"]
                }).data("kendoWindow").center().open();
            }
        }

        //查看流程详细
        vm.queryDetail = function(signId , processInstanceId){
            $("#reviewProject").data("kendoWindow").close();
            $state.go('signDetails' ,{signid : signId , processInstanceId :  processInstanceId} );

        }

        //S_initUpload
        function initUpload(vm) {
            var projectfileoptions = {
                language: 'zh',
                allowedPreviewTypes: ['image'],
                allowedFileExtensions: ['jpg', 'png', 'gif'],
                maxFileSize: 2000,
                showRemove: false,
                uploadUrl: rootPath + "/expert/uploadPhoto",
                uploadExtraData: {expertId: vm.model.expertID}
            };
            $("#expertphotofile").fileinput(projectfileoptions).on("filebatchselected", function (event, files) {

            }).on("fileuploaded", function (event, data) {
                $("#expertPhotoSrc").removeAttr("src");
                $("#expertPhotoSrc").attr("src", rootPath + "/expert/transportImg?expertId=" + vm.model.expertID + "&t=" + Math.random());
            });
        }//E_initUpload

        vm.showUploadWin = function () {
            if (vm.model.expertID) {
                $("#uploadWin").kendoWindow({
                    width: "660px",
                    height: "360px",
                    title: "上传头像",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            } else {
                common.alert({
                    vm: vm,
                    msg: "先保存数据在执行操作！"
                })
            }
        }

        /**
         * 保存专家信息
         */
        vm.saveEP = function () {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.birthDay = $('#birthDay').val();
                vm.model.graduateDate = $('#graduateDate').val();
                expertSvc.saveExpert(vm.model, vm.isSubmit, function (data) {
                    vm.isSubmit = false;
                    if (data.flag || data.reCode == 'ok') {
                        vm.model.expertID = data.reObj.expertID;
                        vm.model.expertNo = data.reObj.expertNo;
                        bsWin.success("保存成功！")
                    } else {
                        bsWin.error(data.reMsg);
                    }
                });
            } else {
                bsWin.alert("专家信息没有填写完整");
            }
        }

        vm.gotoWPage = function () {
            if (vm.model.expertID) {
                $("#wrwindow").kendoWindow({
                    width : "680px",
                    height : "420px",
                    title : "工作简历",
                    visible : false,
                    modal : true,
                    closable : true,
                    actions : [ "Pin", "Minimize", "Maximize", "Close" ]
                }).data("kendoWindow").center().open();
            } else {
                bsWin.alert("请先保存专家信息！");
            }

        }

        /**
         * 保存工作简历信息
         */
        vm.saveWorks = function () {
            common.initJqValidation($('#workForm'));
            var isValid = $('#workForm').valid();
            if (isValid) {
                if (!vm.work.expertID) {
                    vm.work.expertID = vm.model.expertID;
                }
                workExpeSvc.saveWork(vm.work, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        if(!vm.work.weID){
                            if (!vm.model.workDtoList) {
                                vm.model.workDtoList = [];
                            }
                            vm.model.workDtoList.push(data.reObj);
                        }else{
                            $.each(vm.model.workDtoList,function (index,tl) {
                                if(tl.weID == vm.work.weID){
                                    tl = data.reObj;
                                }
                            })
                        }
                        bsWin.success(data.reMsg, function () {
                            vm.work = {};
                            vm.onWClose();
                        });
                    } else {
                        bsWin.error(data.reMsg);
                    }
                });
            }
        }

        /**
         * 更新工作简历
         */
        vm.updateWork = function(){
            var isCheck = $("input[name='checkwr']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择要更改的项目经历信息！");
            } else if (isCheck.length > 1) {
                bsWin.alert("每次只能更改一条记录！");
            } else {
                var editId = isCheck.val();
                $.each(vm.model.workDtoList,function (index,tl) {
                    if(tl.weID == editId){
                        vm.work = tl;
                    }
                })
                vm.gotoWPage();
            }
        }

        /**
         * 删除专家工作简历
         */
        vm.deleteWork = function () {
            var isCheck = $("input[name='checkwr']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择要删除的工作简历");
            } else {
                var ids = [];
                $.each(isCheck, function (i, obj) {
                    ids.push(obj.value);
                });
                workExpeSvc.deleteWork(ids.join(","),function(){
                    $.each(ids,function (i,id) {
                        $.each(vm.model.workDtoList,function (index,tl) {
                            if(tl && tl.weID == id){
                                vm.model.workDtoList.splice(index,1);
                            }
                        })
                    })
                    bsWin.alert("操作成功！");
                })
            }
        }

        vm.onWClose = function () {
            window.parent.$("#wrwindow").data("kendoWindow").close();
        }

        /**
         * 跳转到项目经历页面
         */
        vm.gotoJPage = function () {
            if (vm.model.expertID) {
                $("#pjwindow").kendoWindow({
                    width: "690px",
                    height: "330px",
                    title: "添加项目经验",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin","Minimize","Maximize","Close"]
                }).data("kendoWindow").center().open();
            }else{
                bsWin.alert("请先保存专家信息！");
            }
        }

        /**
         * 新增项目经历
         */
        vm.saveProject = function () {
            common.initJqValidation($('#ProjectForm'));
            var isValid = $('#ProjectForm').valid();
            if (isValid) {
                if (!vm.project.expertID) {
                    vm.project.expertID = vm.model.expertID;
                }
                vm.project.projectbeginTime = $('#projectbeginTime').val();
                vm.project.projectendTime = $('#projectendTime').val();
                projectExpeSvc.saveProject(vm.project,function(data){
                    if (data.flag || data.reCode == 'ok') {
                        if(!vm.project.peID){
                            if (!vm.model.projectDtoList) {
                                vm.model.projectDtoList = [];
                            }
                            vm.model.projectDtoList.push(data.reObj);
                        }else{
                            $.each(vm.model.projectDtoList,function (index,tl) {
                                if(tl.peID == vm.project.peID){
                                    tl = data.reObj;
                                }
                            })
                        }
                        bsWin.success(data.reMsg, function () {
                            vm.project = {};
                            vm.onPClose();
                        });
                    } else {
                        bsWin.error(data.reMsg);
                    }
                });
            }
        }

        /**
         * 更改项目经历
         */
        vm.updateProjectPage = function(){
            var isCheck = $("input[name='checkpj']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择要更改的项目经历信息！");
            } else if (isCheck.length > 1) {
                bsWin.alert("每次只能更改一条记录！");
            } else {
                var editId = isCheck.val();
                $.each(vm.model.projectDtoList,function (index,tl) {
                    if(tl.peID == editId){
                        vm.project = tl;
                    }
                })
                vm.gotoJPage();
            }
        }

        /**
         * 删除项目经历
         */
        vm.delertProject = function () {
            common.initJqValidation();
            var isCheck = $("input[name='checkpj']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择要删除的操作对象!");
            } else {
                var ids = [];
                $.each(isCheck, function (i, obj) {
                    ids.push(obj.value);
                });
                projectExpeSvc.delertProject(ids.join(","),function(){
                    $.each(ids,function (i,id) {
                        $.each(vm.model.projectDtoList,function (index,tl) {
                            if(tl && tl.peID == id){
                                vm.model.projectDtoList.splice(index,1);
                            }
                        })
                    })
                    bsWin.alert("操作成功！");
                });
            }
        }

        vm.onPClose = function () {
            window.parent.$("#pjwindow").data("kendoWindow").close();
        }

        /**
         * 新增专家专业类别
         */
        vm.gotoExpertType = function () {
            if (vm.model.expertID) {
                $("#addExpertType").kendoWindow({
                    width: "680px",
                    height: "400px",
                    title: "专家类型",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            } else {
                bsWin.alert("请先保存专家信息！");
            }
        }

        /**
         * 关闭专家类别弹窗
         */
        vm.onETlose = function () {
            expertTypeSvc.cleanValue();
            window.parent.$("#addExpertType").data("kendoWindow").close();
        }

        /**
         * 保存专家类别信息
         */
        vm.saveExpertType = function () {
            common.initJqValidation($('#expertTypeForm'));
            var isValid = $('#expertTypeForm').valid();
            if (isValid) {
                if (!vm.expertType.expertID) {
                    vm.expertType.expertID = vm.model.expertID;
                }
                expertTypeSvc.saveExpertType(vm.expertType, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        if(!vm.expertType.id){
                            if (!vm.model.expertTypeDtoList) {
                                vm.model.expertTypeDtoList = [];
                            }
                            vm.model.expertTypeDtoList.push(data.reObj);
                        }else{
                            $.each(vm.model.expertTypeDtoList,function (index,tl) {
                                if(tl.id == vm.expertType.id){
                                    tl = data.reObj;
                                }
                            })
                        }
                        bsWin.success(data.reMsg, function () {
                            vm.expertType = {};
                            vm.onETlose();
                        });
                    } else {
                        bsWin.error(data.reMsg);
                    }
                });
            }
        }

        /**
         * 更新专业类型
         */
        vm.updateProjectType = function () {
            var isCheck = $("input[name='checkEType']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择要更改的专业信息！");
            } else if (isCheck.length > 1) {
                bsWin.alert("每次只能更改一条记录！");
            } else {
                var editId = isCheck.val();
                $.each(vm.model.expertTypeDtoList,function (index,tl) {
                    if(tl.id == editId){
                        vm.expertType = tl;
                    }
                })
                vm.expertType.majobSmallDicts = rootScope.topSelectChange(vm.expertType.maJorBig,rootScope.DICT.MAJOR.dicts);
                vm.gotoExpertType();
            }
        }

        /**
         * 删除转接类别信息
         */
        vm.delertProjectType = function () {
            var isCheck=$("input[name='checkEType']:checked");
            if(isCheck.length<1){
                bsWin.alert("请选择删除的专家类别信息！");
            }else {
                var ids = [];
                $.each(isCheck, function (i, obj) {
                    ids.push(obj.value);
                });
                expertTypeSvc.deleteExpertType(ids.join(","),function () {
                    $.each(ids,function (i,id) {
                        $.each(vm.model.expertTypeDtoList,function (index,tl) {
                            if(tl && tl.id == id){
                                vm.model.expertTypeDtoList.splice(index,1);
                            }
                        })
                    })
                    bsWin.alert("操作成功！");
                });
            }
        }

        //专家聘书弹窗
        vm.gotoOfferPage = function () {
            if (vm.model.expertID) {
                $("#ep_offer_div").kendoWindow({
                    width: "820px",
                    height: "600px",
                    title: "专家聘书",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            } else {
                bsWin.alert("先保存专家信息！");
            }
        }

        //保存聘书信息
        vm.saveOffer = function () {
            common.initJqValidation($("#expert_offer_form"));
            var isValid = $('#expert_offer_form').valid();
            if (isValid) {
                vm.expertOffer.expertId = vm.model.expertID
                expertOfferSvc.saveOffer(vm.expertOffer, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        if(!vm.expertOffer.id){
                            if (!vm.model.expertOfferDtoList) {
                                vm.model.expertOfferDtoList = [];
                            }
                            vm.model.expertOfferDtoList.push(data.reObj);
                        }else{
                            $.each(vm.model.expertOfferDtoList,function (index,tl) {
                                if(tl.id == vm.expertOffer.id){
                                    tl = data.reObj;
                                }
                            })
                        }
                        bsWin.success(data.reMsg, function () {
                            vm.expertOffer = {};
                            vm.onETlose();
                        });
                    } else {
                        bsWin.error(data.reMsg);
                    }
                });
            }
        }
        //关闭窗口信息
        vm.closeOffer = function () {
            window.parent.$("#ep_offer_div").data("kendoWindow").close();
        }
        //查看专家聘书
        vm.showOffer = function () {
            var isCheck=$("input[name='checkps']:checked");
            if(isCheck.length<1){
                bsWin.alert("请选择要查看的聘书信息！");
            }else if(isCheck.length>1){
                bsWin.alert("每次只能查看一条记录！");
            }
            else {
                var id = isCheck.val();
                $.each(vm.model.expertOfferDtoList,function(index ,o){
                    if (o.id == id) {
                        vm.expertOffer = o;
                        return;
                    }
                });
                vm.showProjectOffer = true;
                vm.gotoOfferPage();
            }
        }

        vm.chooseMW = function () {
            vm.MW = true;
            vm.showWS = true;
            vm.showWC = false;
        }
        vm.cancelMW = function () {
            vm.showWS = false;
            vm.showWC = true;
        }
        vm.sureMW = function () {
            if (!vm.majorWork) {
                common.alert({
                    vm: vm,
                    msg: "您未选择专业，请选择！",
                    fn: function () {
                        $('.alertDialog').modal('hide');
                        $('.modal-backdrop').remove();
                    }
                })
            } else {
                vm.model.majorWork = vm.majorWork;
                vm.showWS = false;
                vm.showWC = true;
            }
        }
        vm.chooseMS = function () {
            vm.MS = true;
            vm.showSS = true;
            vm.showSC = false;
        }
        vm.cancelMS = function () {
            vm.showSS = false;
            vm.showSC = true;
        }
        vm.sureMS = function () {
            if (!vm.majorStudy) {
                common.alert({
                    vm: vm,
                    msg: "您未选择专业，请选择！",
                    fn: function () {
                        $('.alertDialog').modal('hide');
                        $('.modal-backdrop').remove();
                    }
                })
            } else {
                vm.model.majorStudy = vm.majorStudy;
                vm.showSS = false;
                vm.showSC = true;
            }
        }

        vm.queryDetail = function(signId , processInstanceId){
            $("#reviewProject").data("kendoWindow").close();
            $state.go('signDetails' ,{signid : signId , processInstanceId :  processInstanceId} );

        }


        //项目签收编辑模板打印
        vm.editPrint = function () {
           expertSvc.expertPrint(vm);
        }

    }
})();
