import React, {Component} from 'react';
import {View, Text, ScrollView, WebView, Dimensions, StyleSheet, TouchableOpacity} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import Header from '../../component/HeaderComponent'
import ChartComponent from "../../component/ECharts/ChartComponent";
import axios from 'axios';


export default class ListScreen extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            barOPtions: {},
            pieOptions: {}
        };
    }

    componentDidMount() {
        axios({
            url: "/agenda/getHomeProjInfo",
            method:"post",
            params: {
                username: '张一帆'
            },
        })
            .then((res) => {
                console.log(res);
            })
            .catch(error => {
                console.log(error)
            });
        /*this.setState({
            barOPtions: {
                title: {
                    text: '',
                },
                tooltip: {},
                legend: {
                    data: ['在办项目']
                },
                xAxis: {
                    data: ["综合一部", "项目一部", "项目二", "概算一部", "未分办"]
                },
                yAxis: {},
                series: [{
                    name: '在办项目',
                    type: 'bar',
                    data: [10, 5, 20, 36, 10]
                }]
            },
            pieOptions: {
                series: [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius: '55%',
                        data: [
                            {value: 20, name: '未分办'},
                            {value: 15, name: '综合一部'},
                            {value: 18, name: '项目一部'},
                            {value: 30, name: '项目二部'},
                            {value: 35, name: '概算一部'}
                        ]
                    }
                ]
            }
        })*/
    }

    render() {
        const {navigation} = this.props;
        return (
            <View style={styles.container}>
                <Header title={'评审系统'}/>
                <View style={styles.headerBg}/>
                <View style={styles.header}>
                    <TouchableOpacity activeOpacity={0.8} style={styles.headItem}>
                        <View style={styles.iconView}>
                            <Icon name='ios-list-box' size={35} color={'#ff9700'}/>
                        </View>
                        <Text>待办项目</Text>
                    </TouchableOpacity>
                    <TouchableOpacity activeOpacity={0.8} style={styles.headItem}>
                        <View style={styles.iconView}>
                            <Icon name='ios-list-box' size={35} color={'#15BC83'}/>
                        </View>
                        <Text>待办任务</Text>
                    </TouchableOpacity>
                    <TouchableOpacity activeOpacity={0.8} style={styles.headItem}>
                        <View style={styles.iconView}>
                            <Icon name='ios-list-box' size={35} color={'#1E5AAF'}/>
                        </View>
                        <Text>项目查询</Text>
                    </TouchableOpacity>
                    <TouchableOpacity activeOpacity={0.8} style={styles.headItem}>
                        <View style={styles.iconView}>
                            <Icon name='ios-list-box' size={35} color={'#FF4800'}/>
                        </View>
                        <Text>项目统计</Text>
                    </TouchableOpacity>
                </View>
                <ScrollView>
                    <View style={styles.chart}>
                        <View style={styles.chartTitle}>
                            <View style={styles.line}/>
                            <Text style={styles.titleStyle}>项目办理情况</Text>
                            <TouchableOpacity onPress={() => navigation.navigate('ProjectManagementScreen')}>
                                <Text style={styles.more}>查看更多</Text>
                            </TouchableOpacity>
                        </View>
                        <View style={styles.chartView}>
                            <ChartComponent
                                ref="charts"
                                option={this.state.barOPtions}
                            />
                        </View>
                    </View>
                    <View style={styles.chart}>
                        <View style={styles.chartTitle}>
                            <View style={styles.line}/>
                            <Text style={styles.titleStyle}>预签收项目统计</Text>
                            <TouchableOpacity onPress={() => navigation.navigate('SignupProjectScreen')}>
                                <Text style={styles.more}>查看更多</Text>
                            </TouchableOpacity>
                        </View>
                        <View style={styles.chartView}>
                            <ChartComponent
                                ref="charts"
                                option={this.state.pieOptions}
                            />
                        </View>
                    </View>
                </ScrollView>
            </View>

        );
    }
}
const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'flex-start'
    },
    headerBg: {
        width: '100%',
        height: 60,
        backgroundColor: '#1E5AAF',
        marginBottom: 70
    },
    header: {
        width: '90%',
        height: 120,
        backgroundColor: '#fff',
        position: 'absolute',
        top: 50,
        elevation: 5,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-around',
    },
    iconView: {
        height: 60,
        width: 60,
        borderRadius: 60,
        backgroundColor: '#F7F6F6',
        alignItems: 'center',
        justifyContent: 'center',
        marginBottom: 15
    },
    chart: {
        width: '100%',
        backgroundColor: '#fff',
        elevation: 5,
        marginBottom: 10,
    },
    chartTitle: {
        height: 35,
        borderBottomWidth: 0.8,
        borderBottomColor: '#1E5AAF',
        paddingLeft: 25,
        justifyContent: 'space-between',
        flexDirection: 'row',
        alignItems: 'center',
        paddingRight: 15
    },
    titleStyle: {
        fontWeight: 'bold'
    },
    chartView: {
        paddingTop: 10,
        height: 230
    },
    line: {
        position: 'absolute',
        width: 5,
        height: 17,
        backgroundColor: '#1956AD',
        left: 12
    },
    more: {
        fontSize: 12,
        color: '#999'
    }
});