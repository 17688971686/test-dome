/**
 * 深圳 App
 * @author: tzg
 */

import React from 'react';
import {createSwitchNavigator, createBottomTabNavigator ,createStackNavigator,View,Text} from 'react-navigation';
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
        title:'深圳政府投资项目评审中心项目评审管理系统',
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

export default class App extends React.Component {
    render() {
        return <RootStack/>
    }
}

