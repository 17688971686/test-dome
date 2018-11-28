(function () {
    'use strict';

    angular.module('app').controller('partyListCtrl', partyList);

    partyList.$inject = ['partySvc', 'bsWin', '$state'];

    function partyList(partySvc, bsWin, $state) {
        var vm = this;
        vm.title = '党员信息查询';        		//标题

        active();
        function active() {
            partySvc.partyGrid(vm);
        }


        /**
         * 查看详情
         * @param pmId
         */
        vm.partyDetail = function (pmId) {
            /* $('#myTab li').click(function (e) {
             var aObj = $("a",this);
             e.preventDefault();
             aObj.tab('show');
             var showDiv = aObj.attr("for-div");
             $(".tab-pane").removeClass("active").removeClass("in");
             $("#"+showDiv).addClass("active").addClass("in").show(500);
             })*/

            $("#partyDetail").kendoWindow({
                width: "1000px",
                height: "600px",
                title: "党员信息表",
                visible: false,
                open: function () {
                    partySvc.findById(pmId, function (data) {
                        vm.party = data.reObj;
                    })
                },
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();

        }

        vm.formReset = function () {
            vm.party = {};
        }


        /**
         * 模糊查询
         */
        vm.queryParty = function () {
            vm.gridOptions.dataSource.read();
        }

        /**
         * 党务信息导出-word
         */
        vm.exportPartyWord = function (pmId) {
            partySvc.exportPartyWord(vm, pmId)
        }

        /**
         * 删除党员信息
         * @param pmId
         */
        vm.deleteParty = function (pmId) {
            bsWin.confirm("删除的数据无法恢复，确定删除？", function () {
                partySvc.deleteParty(pmId, function (data) {
                    bsWin.alert("删除成功！");
                    vm.gridOptions.dataSource.read();
                });
            })
        }

        /**
         * 导出签到表
         */
        vm.exportSignInSheet = function () {
            /*var selectIds = common.getKendoCheckId('.grid');
             if (selectIds.length == 0) {
             bsWin.alert("请选择要导出数据！");
             } else {
             var ids = [];
             for (var i = 0; i < selectIds.length; i++) {
             ids.push(selectIds[i].value);
             }
             var idStr = ids.join(',');
             partySvc.exportSignInSheet(idStr);
             }*/
            partySvc.exportSignInSheet();
        }

        /**
         * 批量导入弹框
         */
        vm.importExcel = function () {
            $("importFile").val('');
            $("#importDiv").kendoWindow({
                width: "700px",
                height: "300px",
                title: "导入文件",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        /**
         * 重置按钮
         */
        vm.formReset = function () {
            var tab = $("#partyform").find('input,select');
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }

        /**
         * 导入数据
         */
        vm.importFile = function () {
            var file = $("#importFile").val();
            var fileStr = file.split('.');
            if (file != "" && fileStr[1] != undefined && ( fileStr[1] == 'xls' || fileStr[1] == 'xlsx')) {
                /* var downForm = $("#importForm");
                 downForm.attr("target", "");
                 downForm.attr("method", "post");
                 downForm.attr("enctype" , 'multipart/form-data');
                 downForm.attr("action", rootPath + "/partyManager/importFile");
                 downForm.submit();//表单提交
                 // downForm.attr("success" , function(data){
                 //     location.href = "#/partyList";
                 // })*/
                vm.isImport = true;
                var formData = new FormData();
                var file = document.querySelector('input[type=file]').files[0];
                formData.append("file", file);
                $.ajax({
                    type: "post",
                    url: rootPath + "/partyManager/importFile",
                    data: formData,
                    enctype: "multipart/form-data",
                    // headers: {'Content-Type':undefined},
                    contentType: false,
                    cache: false,
                    processData: false
                }).success(function (data) {
                    vm.isImport = false;
                    var msrg = data.reMsg;
                    bsWin.alert(msrg, function () {
                        window.parent.$("#importDiv").data("kendoWindow").close();
                        vm.gridOptions.dataSource.read();
                        // location.href = "#/partyList";
                    });
                    //成功提交
                })

            } else if (file == "") {
                bsWin.alert("请上传导入文件！");
            } else {
                bsWin.alert("上传文件类型不匹配，请重新上传！");
            }
        }

        /**
         * 导出党员信息表-excel
         */
        vm.exportPartyInfo = function () {
            partySvc.exportPartyInfo();
        }

    }
})();
