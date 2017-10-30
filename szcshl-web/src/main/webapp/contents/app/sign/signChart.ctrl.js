(function(){
    'use strict';
    angular.module('app').controller('signChartCtrl' , signChart);
    signChart.$inject = ['signChartSvc' , 'bsWin'];
    function signChart(signChartSvc , bsWin){

        var vm = this;

        vm.startDateTime = new Date("2006/6/1 08:00");
        vm.endDateTime = new Date("2030/6/1 21:00");
        vm.startTime = "2017-06-01";
        vm.endTime = "2017-10-31";
        vm.chartType = 'histogram';//默认为柱状图
        vm.reviewStage = [];//评审阶段
        vm.appalyinvestment = [];//申报金额
        vm.authorizeValue = [];//审定金额
        vm.series = [];
        vm.resultData = [];
        vm.projectCount = [];//各阶段项目数目
        vm.capital = ['申报金额' , '审定金额'];

        /**
         * 通过开始和结束日期重新统计项目信息情况
         */
        vm.resetChart = function(){
            vm.series = [];
            vm.reviewStage = [];
            vm.appalyinvestment = [];//申报金额
            vm.authorizeValue = [];//审定金额
            vm.resultData = [];
            activate();
        }

        /**
         * 柱状图
         */
        vm.gotoHistogram =function(){
            vm.showHistogram=true;
            vm.showLineChart = false;
            vm.showPie = false;
            for(var i =0 ; i<vm.capital.length ; i++){
                vm.series.push(
                    {
                        name : vm.capital[i],
                        type : 'bar', //bar line
                        data : vm.resultData[i],
                        barWidth : 30,//设置柱子的宽度
                        itemStyle : {
                            normal: {
                                label : {
                                    show: true,
                                    position: 'top',
                                    formatter: function (params) {
                                        return params.value;
                                    }
                                }
                            }
                        },
                    }
                );
            }
            vm.initHistogram();

        }

        /**
         * 折线图
         */
        vm.gotoLineChart = function(){
            vm.showHistogram=false;
            vm.showPie = false;
            vm.showLineChart = true;
            vm.series.push(
                {
                    name : '数目',
                    type : 'line', //bar line
                    data : vm.projectCount,
                    itemStyle : {
                        normal: {
                            label : {
                                show: true,
                                position: 'top',
                                textStyle:{ //设置图表上数目的大小
                                    fontWeight : 'normal',
                                    fontSize : 15
                                },
                                formatter: function (params) {
                                    return params.value;
                                },

                            }
                        }
                    },
                }
            );
            vm.initLineChart();
        }

        /**
         * 饼图
         */
        vm.gotoPie = function(){
            vm.showHistogram=false;
            vm.showLineChart = false;
            vm.showPie = true;
            vm.seriesData = [];
            vm.stage = ['3000万以下','3000万-1亿' ,'1亿-10亿','10亿以上'];
            signChartSvc.pieData(vm , function(data){
                if(data !=undefined && data.length > 0 ){
                    for(var i =0 ; i<data.length ; i++){
                        vm.seriesData .push(
                            {
                                value : data[i],
                                name : vm.stage[i],
                                label : {
                                    normal :{
                                        formatter : ' {b} : {c}%',
                                        textStyle:{
                                            fontWeight : 'normal',
                                            fontSize : 15
                                        }
                                    }
                                }
                            }
                        )
                    }
                }else{
                    bsWin.error("结束日期必须大于开始日期！");
                }
                vm.initPie();
            });

        }

        /**
         * 初始化柱状图数据
         */
        vm.initHistogram = function(){
            var myChart = echarts.init(document.getElementById('histogram')); //只能用javaScript获取节点，如用jquery方式则找不到节点
            var option ={
                title :{
                    text :  "申报金额与审定金额统计情况",
                    subtext : '按评审阶段划分',
                    x : 'center'
                },
                tooltip:{//提示框设置
                    trigger : 'axis',
                    axisPointer :{
                        type : 'cross', //cross  line  shadow
                        label : {
                            backgroundColor : '#283b56'
                        }
                    }
                },
                legend :{ //头部显示说明，注意：data值要与series中的name一致，顺序可以不一致
                    orient : 'vertical',
                    left : 'left',
                    data : vm.capital
                },
                //工具栏设置
                toolbox :{
                    show : true,
                    x : '1000',
                    feature : {
                        dataView : {show : true ,readOnly : true},//数据统计
                        restore : {show : true },//还原
                        saveAsImage : {show : true}//下载
                    }
                },

                //设置坐标
                grid :{
                    // 这些是设置坐标边距
                    left : '10%',
                    right : '15%',
                    bottom : '10%',
                    containLabel : true,
                    //设置xy轴宽度
                    // y : 70,
                    // x : 60
                },
                xAxis :{
                    type : 'category',
                    name : '评审阶段',
                    data :vm.reviewStage,
                    axisTick :{
                        alignWithLabel :true
                    },
                    axisLabel :{
                        interval : 0,
                        rotate : 35 ,//倾斜度 -90 至 90 之间，默认为0
                        margin : 2,
                        textStyle :{
                            fontWeight : 'bolder',
                            color : '#295645'
                        }
                    }
                },
                yAxis :{
                    type : 'value',
                    name : '金额（亿元）',
                    interval : 5, //刻度值
                },
                series :vm.series,
                itemStyle : {
                    emphasis :{
                        shadowBlur : 10 ,
                        shadowOffsetX : 0,
                        shadowColor : 'rgba(0,0,0,0.5)'
                    }
                }
            };

            myChart.setOption(option);
        }//end initHistogram

        /**
         * 初始化折线图
         */
        vm.initLineChart = function(){
            var myChart = echarts.init(document.getElementById('lineChart'));
            var option = {
                title : {
                    text :'评审项目数目情况',
                    subtext : '按评审阶段划分',
                    x : 'center'
                },
                tooltip :{
                    trigger : 'item',
                    formatter : '{a} <br/> {b} : {c}'
                },
                //设置坐标
                grid :{
                    left : '10%',
                    right : '10%',
                    bottom : '10%',
                    containLabel : true,
                },
                toolbox :{
                    show : true,
                    feature : {
                        dataView : {show : true ,readOnly : true},//数据统计
                        restore : {show : true },//还原
                        saveAsImage : {show : true}//下载
                    }
                },
                legend :{
                    orient : 'vertical',
                    left : 'left',
                    data : ['数目']
                },
                xAxis : {
                    type : 'category',
                    name : '评审阶段',
                    splitLine: {show: false},
                    data :vm.reviewStage,
                    axisLabel :{
                        interval : 0,
                        rotate : 35 ,//倾斜度 -90 至 90 之间，默认为0
                        margin : 2,
                        textStyle :{
                            fontWeight : 'bolder',
                            color : '#295645'
                        }
                    }
                },
                yAxis : {
                    type : 'value',
                    name : '项目个数'
                },
                series : vm.series
            };
            myChart.setOption(option);

        }//end initLineChart

        /**
         * 初始化饼图
         */
        vm.initPie = function(){
            var myChart = echarts.init(document.getElementById('pie'));
            var  option = {
                title : {
                    text : '评审项目比例情况',
                    subtext : '按申报金额范围划分',
                    x : 'center'
                },
                tooltip :{
                    trigger : 'item',
                    formatter : "{a} <br/> {b} : {c}% "
                },
                toolbox :{
                    show : true,
                    feature : {
                        dataView : {show : true ,readOnly : true},//数据统计
                        restore : {show : true },//还原
                        saveAsImage : {show : true}//下载
                    }
                },
                legend :{
                    orient : 'vertical',
                    left : 'left',
                    data :vm.stage
                },
                series : [
                    {
                        type : 'pie',
                        radius : '45%',//半径
                        center : ['50%','50%'],
                        selectMode : 'single',
                        data : vm.seriesData,
                        itemStyle : {
                            emphasis :{
                                shadowBlur : 10 ,
                                shadowOffsetX : 0,
                                shadowColor : 'rgba(0,0,0,0.5)'
                            }
                        }

                    }
                ]

            };

            myChart.setOption(option);
        }//end initPie

        /**
         * 统计图切换
         */
        vm.radioChecked = function(){
            vm.series = [];
            if (vm.chartType == 'lineChart'){
                vm.gotoLineChart();
            }else if(vm.chartType == 'histogram'){
                vm.gotoHistogram();
            }else if(vm.chartType = 'pie'){
                vm.gotoPie();
            }
        }

        activate();
        function activate(){
            signChartSvc.findByTime(vm , function(data){
                if(data !=undefined && data.length >0){
                    for(var i =0; i<data.length ; i++){
                        $.each(data[i] , function(key , value){
                            vm.reviewStage.push( key);
                            vm.appalyinvestment.push(value[0]);
                            vm.authorizeValue.push(value[1]);
                            vm.projectCount.push(value[2]);
                        });

                    }
                    vm.resultData.push(vm.appalyinvestment , vm.authorizeValue);
                    vm.gotoHistogram();
                    // vm.gotoLineChart();
                }else{
                    bsWin.error("结束日期必须大于开始日期！");
                }

            });
        }


    }
})();