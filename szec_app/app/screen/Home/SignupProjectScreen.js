import React from 'react';
import {ScrollView, StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import Header from '../../component/HeaderComponent'
import axios from "axios";
import {Table, TableWrapper, Row, Rows, Col, Cols, Cell} from 'react-native-table-component';
import ScrollableTabView, {ScrollableTabBar, DefaultTabBar} from 'react-native-scrollable-tab-view';

export default class SignupProjectScreen extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            tabLabel: ['综合一部','项目一部', '项目二部', '概算一部', '未分办',],
            tableHead: ['部门/人员', '项目名称', '办理环节', '预签收日期'],
            tableData:[
                ['评估一部', '长圳安居工程', '确认归档', '2018-05-01'],
                ['评估一部', '长圳安居工程', '部长审批', '2018-05-01'],
                ['评估一部', '测试首页会议显示', '发文申请', '2018-05-01'],
                ['评估一部', '测试111', '发文申请', '2018-05-01'],
                ['评估一部', '测试协办人一览表显示', '发文申请', '2018-05-01'],
                ['评估一部', '测试协办人一览表显示', '发文申请', '2018-05-01'],
            ],
        };
    }

    componentDidMount() {
    }

    renderTabContent(index) {

    }

    render() {
        let tabLabel = this.state.tabLabel;
        return (
            <View style={styles.container}>
                <Header
                    title='预签收项目统计一览表'
                    showBackTitle={true}
                    navigation={this.props.navigation}/>
                <ScrollableTabView
                    initialPage={0} //初始化时被选中的Tab下标，默认是0
                    tabBarActiveTextColor='#1E5AAF'//设置选中Tab的文字颜色。
                    tabBarUnderlineStyle={{backgroundColor: '#1E5AAF', height: 2}}//设置DefaultTabBar和ScrollableTabBarTab选中时下方横线的颜 色。
                    onChangeTab={(tab) => this.renderTabContent(tab.i)} //Tab切换之后会触发此方法，包含一个参数（Object类型），这个对象有两个参数: i：被选中的Tab的下标（从0开始） ref：被选中的Tab对象（基本用不到)
                    renderTabBar={() => <ScrollableTabBar/>}>
                    {
                        tabLabel.map((item, index) => {
                            return (
                                <View tabLabel={item} key={index}>
                                    <ScrollView style={{width:'100%',marginTop:10 }}>
                                        <Table style={{flex:1}} borderStyle={{borderWidth:0.5}}>
                                            <Row
                                                data={this.state.tableHead} flexArr={[1, 2, 1, 1]}
                                                style={styles.tableHeader}
                                                textStyle={styles.text}/>
                                            <Rows
                                                data={this.state.tableData}
                                                flexArr={[1, 2, 1, 1]}
                                                style={{height:35}}
                                                textStyle={styles.rowsText}/>
                                        </Table>
                                    </ScrollView>
                                </View>
                            )
                        })
                    }
                </ScrollableTabView>
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
    tableView: {
        flex:1,
        paddingLeft:10,
        paddingRight:10
    },
    tableHeader: {
        flex:1,
        height: 30,
        backgroundColor: '#eeeeee'
    },
    text: {
        textAlign: 'center',
        fontWeight: '100',
        color:'#333'
    },
    rowsText:{
        textAlign: 'center',
        color:'#555',
        fontSize:13
    },
});