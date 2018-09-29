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
import ProjectSearchScreen from "./screen/ProjectSearch/ProjectSearchScreen";
import LeadProScreen from "./screen/LeadProScreen";
import Icon from 'react-native-vector-icons/Ionicons';
import BasicInfoScreen from "./screen/Myself/BasicInfoScreen";
import SetUpScreen from "./screen/Myself/SetUpScreen";
import ProjectManagementScreen from "./screen/Home/ProjectManagementScreen";
import SignupProjectScreen from "./screen/Home/SignupProjectScreen";
import ProDetailsScreen from "./screen/ProDetailsScreen";
import ApproveScreen from "./screen/ApproveScreen";


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
    ProjectManagementScreen: ProjectManagementScreen,
    SignupProjectScreen: SignupProjectScreen,
    BasicInfoScreen: BasicInfoScreen,
    SetUpScreen: SetUpScreen,
    ProDetailsScreen: ProDetailsScreen,
    ApproveScreen:ApproveScreen
};

const StackNavigatorConfig = {
    navigationOptions: {
        header: null,
    },
    mode: 'card',
    headerMode: 'screen',
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
        /*评审内网测试地址*/
        axios.defaults.baseURL = 'http://172.30.36.217:9090/api';

       /* axios.defaults.baseURL = 'http://192.168.1.20:8080/szcshl-web/api';*/
       /* axios.defaults.baseURL = 'http://192.168.0.170:8080/szcshl-web/api';*/
        axios.defaults.headers.common['clientId'] = DeviceInfo.getUniqueID();
    }

}

