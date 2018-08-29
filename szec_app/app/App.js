/**
 * 广西百色重大项目管理系统 App
 * @author: tzg
 */

import React from 'react';
import {
    createBottomTabNavigator,
    createStackNavigator,
    createSwitchNavigator,
} from 'react-navigation';
import axios from 'axios';
import * as DeviceInfo from "react-native-device-info/deviceinfo";
import AuthLoadingScreen from './screen/AuthLoadingScreen';
import SignInScreen from './screen/SignInScreen';
import HomeScreen from './screen/Home/HomeScreen';
import MyselfScreen from './screen/Myself/MyselfScreen';
import ProjectSearchScreen from "./screen/ProjectSearchScreen";
import LeadProScreen from "./screen/LeadProScreen";
import Icon from 'react-native-vector-icons/Ionicons';
import BasicInfoScreen from "./screen/Myself/BasicInfoScreen";
import SetUpScreen from "./screen/Myself/SetUpScreen";
import ProjectManagementScreen from "./screen/Home/ProjectManagementScreen";
import SignupProjectScreen from "./screen/Home/SignupProjectScreen";
import SearchScreen from "./screen/SearchScreen";


/*Tab导航器*/
const TabNavigator = createBottomTabNavigator(
    {
        Home: {
            screen: HomeScreen,
            navigationOptions: {
                title: '首页',
                tabBarIcon: ({tintColor, focused}) => (
                    <Icon
                        name={focused ? 'ios-home' : 'ios-home-outline'}
                        size={26}
                        style={{color: tintColor}}
                    />
                ),

            }
        },
        Project: {
            screen: ProjectSearchScreen,
            navigationOptions: {
                title: '项目查询',
                tabBarIcon: ({tintColor, focused}) => (
                    <Icon
                        name={focused ? 'ios-bookmarks' : 'ios-bookmarks-outline'}
                        size={26}
                        style={{color: tintColor}}
                    />
                ),
            }
        },
        leadpro: {
            screen: LeadProScreen,
            navigationOptions: {
                title: '项目审批',
                tabBarIcon: ({tintColor, focused}) => (
                    <Icon
                        name={focused ? 'ios-ribbon' : 'ios-ribbon-outline'}
                        size={26}
                        style={{color: tintColor}}
                    />
                ),
            }
        },
        My: {
            screen: MyselfScreen,
            navigationOptions: {
                title: '个人中心',
                tabBarIcon: ({tintColor, focused}) => (
                    <Icon
                        name={focused ? 'ios-person' : 'ios-person-outline'}
                        size={26}
                        style={{color: tintColor}}
                    />
                ),
            }
        },
    }, {
        tabBarOptions: {
            activeTintColor: '#1E5AAF',
        },
    }
);


/*Stack导航器*/
const RouteConfigs = {
    TabNavigator: TabNavigator,
    ProjectManagementScreen:ProjectManagementScreen,
    SignupProjectScreen:SignupProjectScreen,
    BasicInfoScreen: BasicInfoScreen,
    SetUpScreen: SetUpScreen,
    SearchScreen:SearchScreen

};

const StackNavigatorConfig = {
    navigationOptions: {
        header: null,
    },
    mode: 'card',
    headerMode: 'screen',
    cardStyle: {backgroundColor: "#f5f5f5"},

};

const AppStackNavigator = createStackNavigator(RouteConfigs, StackNavigatorConfig);


/*Switch导航器*/
const RootStack = createSwitchNavigator(
    {
        AuthLoading: AuthLoadingScreen,
        App: AppStackNavigator,
        Auth: SignInScreen
    },
);


export default class App extends React.Component {
    constructor(props) {
        super(props);
        this.initAxios();
    }

    render() {
        return <RootStack/>
    }

    initAxios = () => {
        // axios 全局配置
        axios.defaults.baseURL = 'http://test.gxsnkj.com:9090/gxbspms/api';
        // axios.defaults.baseURL = 'http://192.168.1.8:8080/gxbspms/api';
        axios.defaults.headers.common['TOKEN'] = "";
        axios.defaults.headers.common['clientId'] = DeviceInfo.getUniqueID();

        // // 添加请求拦截器
        // axios.interceptors.request.use(function (config) {
        //     console.log("request", config);
        //     // 在发送请求之前做些什么
        //     return config;
        // }, (error) => {
        //     console.log("request error", error);
        //     // 对请求错误做些什么
        //     return Promise.reject(error);
        // });

    }

}

