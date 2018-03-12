(function () {
    'use strict';

    angular.module('app').controller('partyListCtrl', partyList);

    partyList.$inject = ['partySvc' , 'bsWin'];

    function partyList(partySvc , bsWin) {
        var vm = this;
        vm.title = '党员信息查询';        		//标题
        vm.party = {};

        active();
        function active(){
            partySvc.partyGrid(vm);
        }


        /**
         * 查看详情
         * @param pmId
         */
        vm.partyDetail = function(pmId){
            $('#myTab li').click(function (e) {
                var aObj = $("a",this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#"+showDiv).addClass("active").addClass("in").show(500);
            })

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

        /**
         * 模糊查询
         */
        vm.queryParty = function(){
            vm.gridOptions.dataSource.read();
        }


    }
})();
