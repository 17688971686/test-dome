(function () {
    'use strict';

    angular.module('app').controller('adminWelComeCtrl', adminWelCome).filter('FormatStrDate', function() {
        return function(input) {
            var date = new Date(input);
            var monthValue = (date.getMonth()+1) < 10 ?"0"+(date.getMonth()+1):(date.getMonth()+1);
            var dayValue = (date.getDate()) < 10 ?"0"+(date.getDate()):(date.getDate());
            var formatDate=date.getFullYear()+"/"+monthValue+"/"+dayValue;
            return formatDate
        }
    });

    adminWelCome.$inject = ['bsWin','adminSvc'];

    function adminWelCome(bsWin, adminSvc) {
        var vm = this;
        vm.title = '主页';


        /**
         * 初始化柱状图数据
         */
        vm.initHistogram = function () {
            var myChart = echarts.init(document.getElementById('histogram')); //只能用javaScript获取节点，如用jquery方式则找不到节点
            var option = {
                title: {
                    text: "在办项目数量统计情况",
                    subtext: '按评审阶段划分',
                    x: 'center'
                },
                tooltip: {//提示框设置
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross', //cross  line  shadow
                        label: {
                            backgroundColor: '#283b56'
                        }
                    }
                },
                legend: { //头部显示说明，注意：data值要与series中的name一致，顺序可以不一致
                    orient: 'vertical',
                    left: 'left',
                    data: vm.capital
                },


                //设置坐标
                grid: {
                    // 这些是设置坐标边距
                    left: '13%',
                    right: '10%',
                    bottom: '10%',
                    containLabel: true,
                    //设置xy轴宽度
                    // y : 70,
                    // x : 60
                },
                xAxis: {
                    type: 'category',
                    name: '评审阶段',
                    data: vm.review,
                    axisTick: {
                        alignWithLabel: true
                    },
                    axisLabel: {
                        interval: 0,
                        rotate: 35,//倾斜度 -90 至 90 之间，默认为0
                        margin: 2,
                        textStyle: {
                            fontWeight: 'bolder',
                            color: '#295645'
                        }
                    }
                },
                yAxis: {
                    type: 'value',
                    name: '数量',
                    min: 0,
                    // max: 1000,
                    // interval: 100, //刻度值
                },
                series: [
                    {
                        name:'数量',
                        type:'bar',
                        data: vm.signNumber
                    }
                    ],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0,0,0,0.5)'
                    }
                }
            };

            myChart.setOption(option);
        }//end initHistogram



        activate();
        function activate() {
            adminSvc.initWelComePage(function(data){
                if(data){
                    if(data.proTaskList){
                        vm.tasksList = data.proTaskList;
                    }
                    if(data.comTaskList){
                        vm.agendaTaskList = data.comTaskList;
                    }
                    if(data.endTaskList){
                        vm.endTasksList = data.endTaskList;
                    }
                    if(data.annountmentList){
                        vm.annountmentList = data.annountmentList;
                    }
                }
            });

            adminSvc.countDtasks(function (data) {
                vm.review=[];
                vm.signNumber=[];
                for(var i=0;i<data.reObj.length;i++){
                    if(data.reObj[i].REVIEWSTAGE){
                        vm.review.push(data.reObj[i].REVIEWSTAGE);
                        vm.signNumber.push(data.reObj[i].SIGNNUMBER);
                    }

                }

                vm.initHistogram();//初始化柱状图

            })
        }


        vm.testAlert = function(){
            bsWin.confirm({
                title: "询问提示",
                message: "该项目已经关联其他合并评审会关联，您确定要改为单个评审吗？",
                onOk: function () {
                    alert("点击确认！");
                },
                onCancel: function () {
                    alert("点击取消！");
                }
            });
        }
    }
})();
