(function () {
    'use strict';

    angular.module('app').controller('bookBuyBusinessEditCtrl', bookBuyBusiness);

    bookBuyBusiness.$inject = ['$location', 'bookBuyBusinessSvc', '$state','bsWin'];

    function bookBuyBusiness($location, bookBuyBusinessSvc, $state,bsWin) {
        /* jshint validthis:true */
        var vm = this;
        vm.model = {};
        vm.conMaxIndex = 0;                   //条件号
        vm.conditions = new Array();         //条件列表
        vm.title = '图书信息录入';
        vm.isuserExist = false;
        vm.businessId = $state.params.businessId;
        vm.viewDetail = $state.params.viewDetail;
        vm.showFlag={
            addBooksDeatail:true,
            modBooksDetail:true,
            bookBuyApplyTr:false,
            bookBuyBzTr:true,
            bookBuyFgzrTr:true,
            bookBuyZrTr:true,
            bookBuyYsrk:true,
            buyChannel:false,
            isCommit:false
        }
        if (vm.businessId) {
            vm.isUpdate = true;
            vm.showFlag.modBooksDetail=true;
            vm.showFlag.addBooksDeatail=false;
            $("#businessId").val(vm.businessId);
        }else{
            vm.showFlag.modBooksDetail=false;
            vm.showFlag.addBooksDeatail=true;
        }
        if(vm.viewDetail=='1'){
            vm.showFlag.bookBuyYsrk = true;
            vm.showFlag.addBooksDeatail = true;
            vm.showFlag.modBooksDetail = true;
            vm.showFlag.bookBuyZrTr = true;
            vm.showFlag.bookBuyFgzrTr = true;
            vm.showFlag.bookBuyBzTr = true;
            vm.showFlag.bookBuyApplyTr = true;
            vm.showFlag.buyChannel = true;
            vm.showFlag.isCommit = true;
        }
        vm.create = function () {
            bookBuyBusinessSvc.createBookBuyBusiness(vm);
        };
        vm.update = function () {
            bookBuyBusinessSvc.updateBookBuyBusiness(vm);
        };
        //添加图书详细信息
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
        //删除图书详细信息
        vm.removeCondition = function () {
            var isCheck = $("#conditionTable input[name='bookInfos']:checked");
            if (isCheck.length > 0) {
                bsWin.confirm({
                    title: "询问提示",
                    message: "删除数据不可恢复，确定删除么？",
                    onOk: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < isCheck.length; i++) {
                            for(var k = 0; k < vm.conditions.length; k++){
                                var con = vm.conditions[k];
                                if (isCheck[i].value == con.sort) {
                                    if (con.id) {
                                        ids.push(con.id);
                                    }else{
                                        vm.conditions.splice(k, 1);
                                        $("#conTr"+con.sort).remove();
                                        break;
                                    }
                                }
                            }
                        }
                        if(ids.length > 0){
                            bookBuyBusinessSvc.deleteBooksConditions(ids.join(","),function(data){
                                if(data.flag || data.reCode == 'ok'){
                                    bsWin.success("操作成功！");
                                    for(var i = 0; i < ids.length; i++ ){
                                        for(var k = 0; k < vm.conditions.length; k++){
                                            var con = vm.conditions[k];
                                            if (ids[i] == con.sort) {
                                                vm.conditions.splice(k, 1);
                                                $("#conTr"+con.sort).remove();
                                                break;
                                            }
                                        }
                                    }
                              /*      $.each(ids,function(i,id){
                                        $.each(vm.conditions,function(c,con){
                                            if (id == con.sort) {
                                                vm.conditions.splice(c, 1);
                                                $("#conTr"+con.sort).remove();

                                            }
                                        })
                                    })*/
                                }else{
                                    bsWin.error(data.reMsg);
                                }
                            });
                        }
                    },
                });
            }else{
                bsWin.alert("请选择要删除的图书信息！");
            }

        }

        vm.saveCondition = function () {
            if (buildCondition()) {
                bookBuyBusinessSvc.saveBookBuyBusinessDetail(vm.conditions,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        $("#businessId").val(data.reObj.businessId);
                        bsWin.success("保存成功！");
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }
        }

        function buildCondition() {
            var buyChannel = $("#buyChannel").val();
            if(buyChannel=="" || buyChannel==="undefined"){
                bsWin.error("购买渠道没有填写，请选择后再进行保存！");
                return false;
            }
            common.initJqValidation($('#bookForm'));
            var isValid = $('#bookForm').valid();
            if (isValid) {
                if (vm.conditions.length > 0) {
                    var validateResult = true;
                    vm.conditions.forEach(function (p, number) {
                        p.booksName = $("#booksName" + p.sort).val();
                        p.booksType = $("#booksType" + p.sort).val();
                        p.publishingCompany = $("#publishingCompany" + p.sort).val();
                        p.bookNo = $("#bookNo" + p.sort).val();
                        if(p.bookNo=="" || p.bookNo==="undefined"){
                            bsWin.error("图书号不能为空请核查！");
                            validateResult = false;
                        }
                        p.author = $("#author" + p.sort).val();
                        p.bookNumber = $("#bookNumber" + p.sort).val();
                        if(p.bookNumber=="" || p.bookNumber==="undefined"){
                            bsWin.error("图书数量不能为空请核查！");
                            validateResult = false;
                        }
                        if(!isUnsignedInteger(p.bookNumber)){
                            bsWin.error("图书数量只能输入正整数请核查！");
                            validateResult = false;
                        }
                        p.storeConfirm = $("#storeConfirm" + p.sort).val();
                        p.booksPrice = $("#booksPrice" + p.sort).val();
                        var pc = /^(-)?(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,4})?$/;

                        if(!pc.test(p.booksPrice)){
                            bsWin.error("价格只能输入数字请核查！");
                            validateResult = false;
                        }
                        p.applyDept= vm.model.applyDept;
                        p.operator= vm.model.operator;
                        p.buyChannel= vm.model.buyChannel;
                        p.businessId= $("#businessId").val();
                        p.businessName = vm.model.businessName;
                        p.applyReason = vm.model.applyReason;
                    });
                    return validateResult;
                } else {
                    bsWin.error("没有分录数据，无法保存！");
                    return false;
                }
            }else{
                bsWin.alert("操作失败，有红色*号的选项为必填项，请按要求填写！");
            }
        }
        //发起流程
        vm.startFlow = function(){
                bsWin.confirm({
                    title: "询问提示",
                    message: "发起流程后，当前页面数据将不能再修改！确认发起流程么？",
                    onOk: function () {
                        if (buildCondition()) {
                            bookBuyBusinessSvc.startFlow(vm.conditions,vm.showFlag.isCommit,function(data){
                                if(data.flag || data.reCode == 'ok'){
                                    bsWin.alert("保存成功！",function(){
                                        $state.go('myBookBuyBusiness');
                                    });
                                }else{
                                    bsWin.alert(data.reMsg);
                                }
                            });
                        }else{
                            bsWin.error("没有分录数据，无法发起流程！");
                        }
                    }
                });
        }

        //校验图书数量
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
                bookBuyBusinessSvc.getBookBuyBusinessById(vm,function(data){
                    vm.model = data;
                    vm.conditions = vm.model.bookBuyList;
                    for(var i=0;i<vm.conditions.length;i++){
                        vm.conditions[i]["sort"]= (i+1);
                        vm.conditions[i]["total"] = parseFloat(vm.conditions[i].booksPrice)*(vm.conditions[i].bookNumber);
                    }
                });
            }
        }
    }
})();
