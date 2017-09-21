(function () {
    'use strict';

    angular.module('app').controller('assertStorageBusinessEditCtrl', assertStorageBusiness);

    assertStorageBusiness.$inject = ['$location', 'assertStorageBusinessSvc', '$state','bsWin'];

    function assertStorageBusiness($location, assertStorageBusinessSvc, $state,bsWin) {
        /* jshint validthis:true */
        var vm = this;
        vm.model = {};
        vm.conMaxIndex = 0;                   //条件号
        vm.conditions = new Array();         //条件列表
        vm.title = '添加固定资产申购流程';
        vm.isuserExist = false;
        vm.businessId = $state.params.businessId;
        vm.showFlag={
            addBooksDeatail:true,
            modBooksDetail:true,
            bookBuyApplyTr:false,
            bookBuyBzTr:true,
            bookBuyFgzrTr:true,
            bookBuyZrTr:true,
            bookBuyYsrk:true
        }
        if (vm.businessId) {
            vm.isUpdate = true;
            vm.showFlag.modBooksDetail=true;
            vm.showFlag.addBooksDeatail=false;
            vm.title = '更新图书采购申请业务信息';
            $("#businessId").val(vm.businessId);
        }

        vm.create = function () {
            assertStorageBusinessSvc.createAssertStorageBusiness(vm);
        };
        vm.update = function () {
            assertStorageBusinessSvc.updateAssertStorageBusiness(vm);
        };
        //添加资产详细信息
        vm.addCondition = function () {
            vm.condition = {};
            if(vm.showFlag.addBooksDeatail){
                vm.condition.sort = vm.conMaxIndex+1;
            }else{
                vm.conMaxIndex = vm.conditions.length;
                vm.condition.sort = vm.conditions.length+1;
            }
            vm.conditions.push(vm.condition);
            vm.conMaxIndex++;
        }
        //删除资产详细信息
        vm.removeCondition = function () {
            var isCheck = $("#conditionTable input[name='epConditionSort']:checked");
            if (isCheck.length > 0) {
                bsWin.confirm({
                    title: "询问提示",
                    message: "删除数据不可恢复，确定删除么？",
                    onOk: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < isCheck.length; i++) {
                            $.each(vm.conditions,function(c,con){
                                if (isCheck[i].value == con.sort) {
                                    if (con.id) {
                                        ids.push(con.id);
                                    }else{
                                        vm.conditions.splice(c, 1);
                                    }
                                }
                            })
                        }
                        if(ids.length > 0){
                            assertStorageBusinessSvc.deleteSelConditions(ids.join(","),vm.isCommit,function(data){
                                if(data.flag || data.reCode == 'ok'){
                                    bsWin.success("操作成功！");
                                    $.each(ids,function(i,id){
                                        $.each(vm.conditions,function(c,con){
                                            if (id == con.sort) {
                                                vm.conditions.splice(c, 1);     //没有保存抽取条件的直接删除
                                            }
                                        })
                                    })
                                }else{
                                    bsWin.error(data.reMsg);
                                }
                            });
                        }else{
                            bsWin.success("操作成功！");
                        }
                    },
                });
            }else{
                bsWin.alert("请选择要删除的抽取条件！");
            }

        }

        vm.saveCondition = function () {
            if (buildCondition(false)) {
                assertStorageBusinessSvc.saveGoodsDetailBusinessDetail(vm.conditions,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        $("#businessId").val(data.reObj.businessId);
                        bsWin.success("保存成功！");
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            } else {
                bsWin.error("请添加后再保存");
            }
        }

        function buildCondition(checkId) {
            //TODO:表单参数校验待核实
            if (vm.conditions.length > 0) {
                var validateResult = true;
                vm.conditions.forEach(function (p, number) {
                    p.goodsName = $("#goodsName" + p.sort).val();
                    p.specifications = $("#specifications" + p.sort).val();
                    p.models = $("#models" + p.sort).val();
                    p.orgCompany = $("#orgCompany" + p.sort).val();
                    p.evaluate = $("#evaluate" + p.sort).val();
                    p.goodsNumber = $("#goodsNumber" + p.sort).val();
                    p.applyDept= vm.model.applyDept;
                    p.operator= vm.model.operator;
                    p.businessId= $("#businessId").val();
                });
                return validateResult;
            } else {
                return false;
            }
        }
        //发起流程
        vm.startFlow = function(){
            /*  common.initJqValidation($('#topicform'));
             var isValid = $('#topicform').valid();*/
            bsWin.confirm({
                title: "询问提示",
                message: "发起流程后，当前页面数据将不能再修改！确认发起流程么？",
                onOk: function () {
                    if (buildCondition(false)) {
                        assertStorageBusinessSvc.startFlow(vm.conditions,vm.isCommit,function(data){
                            if(data.flag || data.reCode == 'ok'){
                                bsWin.alert("保存成功！",function(){
                                    $state.go('myAssertStorageBusiness');
                                });
                            }else{
                                bsWin.alert(data.reMsg);
                            }
                        });
                    }else{
                        bsWin.error("请添加图书信息后再发起流程！");
                    }
                }
            });
        }

        //校验数量
        vm.checkBookNum = function (sort){
            var bookNumber = "bookNumber"+sort;
            if(!isUnsignedInteger($("#"+bookNumber).val())){
                $("span[data-valmsg-for='"+bookNumber+"']").html("图书数量只能输入正整数！");
                return ;
            }
            $("span[data-valmsg-for='"+bookNumber+"']").html("");
        }

        //校验价格
        vm.checkPrice = function(sort){
            var pc = /^(-)?(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,4})?$/;    //保留4个小数点
            var priceId = "booksPrice"+sort;
            var bookNumberId = "bookNumber"+sort;
            if(!pc.test( $("#"+priceId).val())){
                $("#"+priceId).val("");
                $("span[data-valmsg-for='"+priceId+"']").html("价格只能输入数字！");
                return ;
            }
            $("span[data-valmsg-for='"+priceId+"']").html("");
            var price = $("#"+priceId).val();
            var num = $("#"+bookNumberId).val();
            $("#total"+sort).val(parseFloat(price)*num);
        }

        //检查是否为正整数
        function isUnsignedInteger(value) {
            if ((/^(\+|-)?\d+$/.test(value)) && value > 0) {
                return true;
            } else {
                return false;
            }
        }

        activate();
        function activate() {
            if (vm.isUpdate) {
                assertStorageBusinessSvc.getAssertStorageBusinessById(vm);
            }
        }
    }
})();
