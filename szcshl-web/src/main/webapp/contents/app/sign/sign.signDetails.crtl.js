(function () {
    'use strict';

    angular.module('app').controller('signDetailsCtrl', sign);

    sign.$inject = ['$location','signSvc','$state','flowSvc'];

    function sign($location, signSvc,$state,flowSvc) {
        var vm = this;
    	vm.model = {};							//创建一个form对象   	
        vm.title = '查看详情信息';        		//标题
        vm.model.signid = $state.params.signid;	//收文ID


        active();
        function active(){
            $('#myTab li').click(function (e) {
                var aObj = $("a",this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#"+showDiv).addClass("active").addClass("in").show(500);
            })

            signSvc.initFlowPageData(vm);

            signSvc.initAssociateSigns(vm,vm.model.signid,function(response){
                if(response.data != undefined){
                    vm.associateSign = response.data;
                    var signs = response.data;
                   // console.log(signs);
                    var steps = [];
                    var html_ = '';
                    for(var i = (signs.length-1);i>=0;i--){
                        var s = signs[i];
                      /*  var htm = '<div style="position: absolute;bottom: 1px;">名称：<span style="color:red;">'+s.projectname+'</span><br/>'
                                  +s.reviewstage
                                  +'</div>';*/
                        //steps.push({title:htm});

                        var signdate = s.signdate||'';
                        html_ += '<div class="intro-list">'+
                                    '<div class="intro-list-left">'+
                                        '项目阶段：'+s.reviewstage+"<br/>签收时间："+signdate+
                                    '</div>'+
                                    '<div class="intro-list-right">'+
                                        '<span></span>'+
                                        '<div class="intro-list-content">'+
                                            '名称：<span style="color:red;">'+s.projectname+'</span><br/>'+
                                            '送件人：<span style="color:red;">'+s.sendusersign+'</span><br/>'+
                                        '</div>'+
                                    '</div>'+
                                 '</div>';

                    }
                    $('#introFlow').html(html_);
                    var step= $("#myStep").step({
                    			animate:true,
                    			initStep:1,
                    			speed:1000
                    		});

                }
            });
            if($state.params.processInstanceId){
                vm.flow = {}
                vm.flow.processInstanceId = $state.params.processInstanceId;	//流程实例ID
                flowSvc.initFlowData(vm);
            }
        }

    }
})();
