(function(){
    "use strict";
    angular.module('app').controller('statisticalCtrl' , statistical);
    statistical.$inject =['$location'];
    function statistical($location){

        var vm= this;
        vm.showHistogram = true ;


        /**
         * 柱状图
         */
        vm.gotoHistogram =function(){
            vm.showHistogram=true;
            vm.showLineChart = false;
            vm.showPie = false;
            vm.initHistogram();

        }

        /**
         * 折线图
         */
        vm.gotoLineChart = function(){
            vm.showHistogram=false;
            vm.showPie = false;
            vm.showLineChart = true;
            vm.initLineChart();
        }

        /**
         * 饼图
         */
        vm.gotoPie = function(){
            vm.showHistogram=false;
            vm.showLineChart = false;
            vm.showPie = true;
            vm.initPie();
        }



        vm.initHistogram = function(){
            var myChart = echarts.init(document.getElementById('histogram')); //只能用javaScript获取节点，如用jquery方式则找不到节点
            var option ={
                title :{
                    text : "项目统计",
                    subtext : '按评审阶段进行项目统计',
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
                    data :['2014年','2015年','2016年','2017年']
                },
                //工具栏设置
                toolbox :{
                    show : true,
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
                     right : '4%',
                     bottom : '3%',
                     containLabel : true,
                    //设置xy轴宽度
                    // y : 70,
                    // x : 60
                },
                xAxis :{
                    type : 'category',
                    name : '年份',
                    data :['可行性研究报告','项目概算','项目建议书','资金申请报告'],
                    axisTick :{
                        alignWithLabel :true
                    },
                    axisLabel :{
                        interval : 0,
                        rotate : 0 ,//倾斜度
                        margin : 2,
                        textStyle :{
                            fontWeight : 'bolder',
                            color : '#295645'
                        }
                    }
                },
                yAxis :{
                    type : 'value',
                    name : '案例',
                    interval : 5,//间距
                },
                series :[
                    {
                        name : '2014年',
                        type : 'bar',
                        data : [20,15,12,10],
                        barWidth : 30,//设置柱子的宽度
                        itemStyle : {

                            normal: {
                                // color: 'gray',//设置柱子的颜色
                                label: {
                                    show: true,
                                    position: 'top',
                                    formatter: function (params) {
                                        return params.value;
                                    }
                                },
                            }
                        }
                    },
                    {
                        name :'2015年',
                        type : 'bar',
                        data :[50,20,15,30],
                        barWidth : 30,//设置柱子的宽度
                        itemStyle : {

                            normal : {
                                // color : 'gray',//设置柱子的颜色
                                label : {
                                    show : true ,
                                    position : 'top',
                                    formatter : function(params){
                                        return params.value;
                                    }
                                },
                            }
                        }

                    },
                    {
                        name : '2016年',
                        type : 'bar',
                        data : [40,20,20,20],
                        barWidth : 30,
                        itemStyle : {
                            normal : {
                                // color : 'green',
                                label : {
                                    show : true ,
                                    position : 'top',
                                    formatter : function(params){
                                        return params.value;
                                    }
                                },
                            }
                        }
                    },
                    {
                        name : '2017年',
                        type : 'bar',
                        data : [42,18,21,16],
                        barWidth : 30,
                        itemStyle : {
                            normal : {
                                // color : 'red',
                                label : {
                                    show : true ,
                                    position : 'top',
                                    formatter : function(params){
                                        return params.value;
                                    }
                                },
                            }
                        }
                    }
                ],
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



        vm.initPie = function(){
            var myChart = echarts.init(document.getElementById('pie'));

            var  option = {
                title : {
                    text : '2017年度项目统计情况',
                    subtext : '按评审计划统计占比例',
                    x : 'center'
                },
                tooltip :{
                    trigger : 'item',
                    formatter : "{a} <br/> {b} : {c} ({d}%)"
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
                    data :['可行性研究报告','项目概算' ,'项目建议书','资金申请报告']
                },
                series : [
                    {
                        type : 'pie',
                        radius : '65%',//半径
                        center : ['50%','50%'],
                        selectMode : 'single',
                        data : [
                            {
                                value : '20',
                                name : '可行性研究报告',
                                label : {
                                    normal :{
                                        formatter : ' {b} : {c} ({d}%)'
                                    }
                                }
                            },
                            {
                                value : '18',
                                name : '项目概算',
                                label : {
                                    normal :{
                                        formatter : ' {b} : {c} ({d}%)'
                                    }
                                }
                            },
                            {
                                value : '6',
                                name : '项目建议书',
                                label : {
                                    normal :{
                                        formatter : ' {b} : {c} ({d}%)'
                                    }
                                }
                            },
                            {
                                value : '12',
                                name : '资金申请报告',label : {
                                normal :{
                                    formatter : '{b} : {c} ({d}%)'
                                }
                            }
                            }
                        ],
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

        vm.initLineChart = function(){
            var myChart = echarts.init(document.getElementById('lineChart'));
            var option = {
                title : {
                    text : '2014-2017年各评审阶段项目变化情况',
                    subtext : '',
                    x : 'center'
                },
                tooltip :{
                    trigger : 'item',
                    formatter : '{a} <br/> {b} : {c}'
                },
                //设置坐标
                grid :{
                    left : '15%',
                    right : '4%',
                    bottom : '3%',
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
                    data : ['可行性研究报告','项目概算','项目建议书','资金申请报告']
                },
                xAxis : {
                    type : 'category',
                    name : '年份',
                    data :['2014年','2015年','2016年','2017年']
                },
                yAxis : {
                    type : 'value',
                    name : '案例'
                },
                series : [
                    {
                        name : '可行性研究报告',
                        type : 'line',
                        data : [20,50,40,42]
                    },
                    {
                        name : '项目概算',
                        type : 'line',
                        data : [15,20,20,18]
                    },
                    {
                        name : '项目建议书',
                        type : 'line',
                        data :[12,15,20,21]
                    },
                    {
                        name : '资金申请报告',
                        type : 'line',
                        data : [10,30,20,16]
                    }
                ]
            };
            myChart.setOption(option);

        }//end initLineChart


        activate();
        function activate(){
            vm.initHistogram();
        }

    }

})();