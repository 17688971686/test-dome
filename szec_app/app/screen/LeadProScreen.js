/**
 * 广西百色重大项目管理系统 市领导联系重大项目（事项）信息
 * @author: tzg
 */

import React from "react";
import {View,Text,StyleSheet,TouchableOpacity} from "react-native";
import ProjectListComponent from '../component/ProjectListComponent'
import Header from '../component/HeaderComponent';
import DatePicker from '../component/DatePickerComponent'

export default class LeadProScreen extends React.Component {
    state={
        projectData:[],
        totalNum:'',
    };
    componentWillMount() {
        this.setState({
            totalNum:10,
            projectData:[{
                itemName:'田阳地税局新建项目1',
                itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
                totalInvestment:'500万',
                startDate:'2018-08-08',
            },{
                itemName:'田阳地税局新建项目2',
                itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
                totalInvestment:'800万',
                startDate:'2018-09-08',
            },{
                itemName:'田阳地税局新建项目3',
                itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
                totalInvestment:'805万',
                startDate:'2018-12-08',
            },{
                itemName:'田阳地税局新建项目4',
                itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
                totalInvestment:'805万',
                startDate:'2018-12-08',
            },{
                itemName:'田阳地税局新建项目5',
                itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
                totalInvestment:'805万',
                startDate:'2018-12-08',
            }]
        })
    }
    _filter(){
        return(
            <View style={styles.filterView}>
                <DatePicker ref='DatePicker'/>
                <Text style={styles.filterText} onPress={()=>this.refs.DatePicker.refs.DatePicker.onPressDate()}>筛选</Text>
            </View>
        )
    };
    //上拉加载更多方法
    loadMore=()=>{
        return projectData=[{
            itemName:'田阳地税局新建项目11',
            itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
            totalInvestment:'500万',
            startDate:'2018-08-08',
        },{
            itemName:'田阳地税局新建项目21',
            itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
            totalInvestment:'800万',
            startDate:'2018-09-08',
        },{
            itemName:'田阳地税局新建项目31',
            itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
            totalInvestment:'805万',
            startDate:'2018-12-08',
        },{
            itemName:'田阳地税局新建项目41',
            itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
            totalInvestment:'805万',
            startDate:'2018-12-08',
        },{
            itemName:'田阳地税局新建项目51',
            itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
            totalInvestment:'805万',
            startDate:'2018-12-08',
        }]
    };
    //刷新数据方法
    onRefresh=()=>{
        return onRefreshData=[{
            itemName:'新数据11',
            itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
            totalInvestment:'500万',
            startDate:'2018-08-08',
        },{
            itemName:'新数据1121',
            itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
            totalInvestment:'800万',
            startDate:'2018-09-08',
        },{
            itemName:'新数据1131',
            itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
            totalInvestment:'805万',
            startDate:'2018-12-08',
        },{
            itemName:'新数据1141',
            itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
            totalInvestment:'805万',
            startDate:'2018-12-08',
        },{
            itemName:'新数据1151',
            itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
            totalInvestment:'805万',
            startDate:'2018-12-08',
        }]
    };
    render() {
        return (
            <View style={[styles.container,]}>
                <Header title={'项目审批'} headerRight={this._filter()}/>
                <View style={styles.container}>
                    <ProjectListComponent
                        projectData={this.state.projectData}
                        onRefresh={this.onRefresh}
                        loadMore={this.loadMore}
                        totalNum={this.state.totalNum}
                    />
                </View>
            </View>

        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        width:'100%',
        alignItems: 'center',
        justifyContent: 'flex-start',
    },
    filterView:{
        flexDirection:'row',
        justifyContent:'flex-end',
        alignItems:'center'
    },
    filterText:{
        color:'#fff',
        lineHeight:50,
    },
    filterContentView:{
        height:'100%',
    },
});