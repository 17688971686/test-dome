(function () {
    'use strict';

    angular.module('app').controller('partyListCtrl', partyList);

    partyList.$inject = ['partySvc' , 'bsWin'];

    function partyList(partySvc , bsWin) {
        var vm = this;
        vm.title = '党员信息查询';        		//标题

        active();
        function active(){
            partySvc.partyGrid(vm);
        }


        /**
         * 查看详情
         * @param pmId
         */
        vm.partyDetail = function(pmId){
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
                open : function(){
                    partySvc.findById(pmId , function(data){
                        vm.party = data.reObj;
                    })
                },
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();

        }

        vm.formReset = function(){
            vm.party = {};
        }


        /**
         * 模糊查询
         */
        vm.queryParty = function(){
            vm.gridOptions.dataSource.read();
        }

        /**
         * 党务信息导出-word
         */
        vm.exportPartyWord = function(pmId){
            partySvc.exportPartyWord(vm , pmId)
        }

        /**
         * 删除党员信息
         * @param pmId
         */
        vm.deleteParty = function(pmId){
            bsWin.confirm("确认停用该党员？", function(){
                partySvc.deleteParty(pmId , function(data){

                    bsWin.alert("操作成功！");
                });
            })
        }

        /**
         * 导出签到表
         */
        vm.exportSignInSheet = function(){
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                bsWin.alert("请选择要删除数据！");
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                partySvc.exportSignInSheet(idStr);
            }
        }

    }
})();
