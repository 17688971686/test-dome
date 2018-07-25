/**
 * 深圳评审中心项目评审管理系统 App
 * @author: tzg
 */

import React from 'react';
import {createSwitchNavigator, createBottomTabNavigator ,createStackNavigator,View,Text} from 'react-navigation';
import axios from 'axios';
import * as DeviceInfo from "react-native-device-info/deviceinfo";
import AuthLoadingScreen from './screen/AuthLoadingScreen';
import SignInScreen from './screen/SignInScreen';
import HomeScreen from './screen/HomeScreen';
import MyselfScreen from './screen/Myself/MyselfScreen';
import ProjectScreen from "./screen/ProjectScreen";
import LeadProScreen from "./screen/LeadProScreen";
import Icon from 'react-native-vector-icons/Ionicons';
import BasicInfoScreen from "./screen/Myself/BasicInfoScreen";
import SetUpScreen from "./screen/Myself/SetUpScreen";


/*Tab导航器*/
const TabNavigator = createBottomTabNavigator(
    {
        Home:{
            screen:HomeScreen,
            navigationOptions:{
                title:'总览',
                tabBarIcon:({tintColor,focused})=>(
                    <Icon
                        name={focused?'ios-home':'ios-home-outline'}
                        size={26}
                        style={{color:tintColor}}
                    />
                ),

            }
        } ,
        Project:{
            screen:ProjectScreen,
            navigationOptions:{
                title:'市层面重大项目',
                tabBarIcon:({tintColor,focused})=>(
                    <Icon
                        name={focused?'ios-bookmarks':'ios-bookmarks-outline'}
                        size={26}
                        style={{color:tintColor}}
                    />
                ),
            }
        },
        leadpro:{
            screen:LeadProScreen,
            navigationOptions:{
                title:'市领导联系重大项目',
                tabBarIcon:({tintColor,focused})=>(
                    <Icon
                        name={focused?'ios-ribbon':'ios-ribbon-outline'}
                        size={26}
                        style={{color:tintColor}}
                    />
                ),
            }
        },
        My:{
            screen:MyselfScreen,
            navigationOptions:{
                title:'我的',
                tabBarIcon:({tintColor,focused})=>(
                    <Icon
                        name={focused?'ios-person':'ios-person-outline'}
                        size={26}
                        style={{color:tintColor}}
                    />
                ),
            }
        }
    },{
        tabBarOptions:{
            activeTintColor: '#1E5AAF',
        },
    }
);

/*Stack导航器*/
const RouteConfigs = {
    TabNavigator:{
        screen:TabNavigator,
        navigationOptions:{
            header:null
        }
    },
    BasicInfoScreen:BasicInfoScreen,
    SetUpScreen:SetUpScreen
};

const StackNavigatorConfig = {
    navigationOptions: {
        title:'深圳市政府投资项目评审中心',
        headerTitleStyle: {fontSize: 18, color: '#fff'},
        headerStyle: {width:'100%',height: 50, backgroundColor: '#1E5AAF',elevation:0,},
    },
    mode: 'card',
    headerMode: 'screen',
    cardStyle: {backgroundColor: "#f5f5f5"},

};

const AppStackNavigator = createStackNavigator(RouteConfigs,StackNavigatorConfig);


/*Switch导航器*/
const RootStack = createSwitchNavigator(
    {
        AuthLoading: AuthLoadingScreen,
        App: AppStackNavigator,
        Auth: SignInScreen
    },
);

// axios 全局配置
axios.defaults.baseURL = 'http://test.gxsnkj.com:9090/gxbspms';
axios.defaults.headers.common['TOKEN'] = "";
axios.defaults.headers.common['clientId'] = DeviceInfo.getUniqueID();

// // 添加请求拦截器
// axios.interceptors.request.use(function (config) {
//     // 在发送请求之前做些什么
//     return config;
// }, function (error) {
//     Alert.alert("11");
//     // 对请求错误做些什么
//     return Promise.reject(error);
// });

// 添加响应拦截器
axios.interceptors.response.use((response) => {
    // 对响应数据做点什么
    console.log(response);
    return response;
}, (error) => {
    console.error(error);
    // 对响应错误做点什么
    return Promise.reject(error);
});

export default class App extends React.Component {
    render() {
        return <RootStack/>
    }
}

