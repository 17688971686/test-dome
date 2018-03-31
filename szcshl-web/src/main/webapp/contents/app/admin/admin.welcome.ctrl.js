(function () {
    'use strict';

    angular.module('app').controller('adminWelComeCtrl', adminWelCome).filter('FormatStrDate', function () {
        return function (input) {
            var date = new Date(input);
            var monthValue = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1);
            var dayValue = (date.getDate()) < 10 ? "0" + (date.getDate()) : (date.getDate());
            var formatDate = date.getFullYear() + "/" + monthValue + "/" + dayValue;
            return formatDate;
        }
    });

    adminWelCome.$inject = ['bsWin', 'adminSvc', '$state'];

    function adminWelCome(bsWin, adminSvc, $state) {
        var vm = this;
        vm.title = '主页';
        //默认用户是普通员工
        vm.isdisplays = false;
        /**
         * 初始化柱状图数据
         */
        vm.initHistogram = function () {
            var myChart = echarts.init(document.getElementById('histogram')); //只能用javaScript获取节点，如用jquery方式则找不到节点

            var option = {
                title: {
                    text: "在办项目数量统计情况",
                    subtext: '',
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
                    name: '人员/部门',
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
                        name: '数量',
                        type: 'bar',
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

        /**
         * 初始化折线图
         */
        vm.initLineChart = function () {
            //当页面两个id相同时会发生冲突不显示。所以用两个id来分别显示部长以上和普通员工的显示
            var myChart = echarts.init(document.getElementById('lineChart'));
            var option = {
                title: {
                    text: '项目办理情况',
                    subtext: '',
                    x: 'center'
                },
                tooltip: {
                    trigger: 'axis',
                    formatter: function (param) {
                        //option.series[param[0].seriesIndex].rawdate[param[0].dataIndex]是rawdate传的值
                        //先进行切割，获取到项目名称
                        var ssArray = option.series[param[0].seriesIndex].rawdate[param[0].dataIndex].split(",");
                        var res = '项目名称：' + param[0].name + '<br/>' + '剩余工作日:' + ssArray[0];
                        return res;
                    }
                },

                //设置坐标
                grid: {
                    left: '13%',
                    right: '10%',
                    bottom: '10%',
                    containLabel: true,
                },
                /*   legend: {
                    orient: 'vertical',
                    left: 'left',
                    data: vm.projectType
                    },*/
                xAxis: {
                    show: false,
                    type: 'category',
                    boundaryGap: false,
                    name: '项目名称',
                    axisLine: {onZero: false},
                    data: vm.reviewdate,
                },
                yAxis: {
                    type: 'value',
                    name: '剩余工作日',
                    min: -3,
                    max: 15,
                },
                series: [
                    {
                        name: '剩余工作日',
                        type: 'line',
                        markPoint: {
                            data: [
                                {type: 'max', name: '最大值'},
                                {type: 'min', name: '最小值'}
                            ]
                        },
                        markLine: {
                            data: [
                                {type: 'average', name: '平均值'}
                            ]
                        },
                        data: vm.linedatas,
                        rawdate: vm.name//自定义参数
                    }
                ],
                lineStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0,0,0,0.5)'
                    }
                }
            };
            myChart.setOption(option);
            myChart.on('click', function (param) {
                //进行分割。获取到signid
                var ssArray = option.series[param.seriesIndex].rawdate[param.dataIndex].split(",");
                $state.go('signDetails', {signid: ssArray[1], processInstanceId: ssArray[2]});
            });
        }//end initLineChart
        activate();

        function activate() {
            adminSvc.initWelComePage(function (data) {
                if (data) {
                    if (data.proTaskList) {
                        vm.tasksList = data.proTaskList;
                    }
                    if (data.comTaskList) {
                        vm.agendaTaskList = data.comTaskList;
                    }
                    if (data.endTaskList) {
                        vm.endTasksList = data.endTaskList;
                    }
                    if (data.annountmentList) {
                        vm.annountmentList = data.annountmentList;
                    }
                    console.log(data);
                    //是否显示图表
                    vm.isdisplays = data.isdisplay;
                    if(!data.isdisplay){
                        //线性图数据
                        vm.linedatas = [];//纵轴(剩余工作日)
                        vm.reviewdate = [];//横轴(项目名称)
                        vm.name = [];
                        if(data.lineSign){
                            var lineList = data.lineSign;
                            for (var i = 0,l=lineList.length; i < l; i++) {
                                //赋值给横轴需要的数据
                                var day = lineList[i].surplusDays;
                                if (day < -3) {
                                    day = -3;
                                }
                                if (day > 15) {
                                    day = 15;
                                }
                                vm.linedatas.push(day);
                                vm.reviewdate.push(lineList[i].projectName);
                                //自定义传参，先进行拼接需要的数据。后再拆分
                                vm.name.push(lineList[i].surplusDays + "," + lineList[i].businessKey + "," + lineList[i].processInstanceId);
                            }
                        }
                        vm.initLineChart();//初始化折线图

                        //柱状图
                        vm.review = data.histogram_x;  //横轴(人员名称/部门)
                        vm.signNumber = data.histogram_y;//纵轴(数量)
                        vm.initHistogram();//初始化柱状图
                    }

                    //显示柱状图信息
                }
            });
        }

    }
})();
