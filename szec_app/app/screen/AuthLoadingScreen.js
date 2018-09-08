/**
 * 广西百色重大项目管理系统 判断是否已获取登录凭证
 * @author: tzg
 */

import React, {Component} from 'react';
import {ActivityIndicator, AsyncStorage, StatusBar, StyleSheet, View} from 'react-native';
import axios from "axios/index";
import SplashScreen from 'react-native-splash-screen';

export default class AuthLoadingScreen extends Component {
    constructor(props) {
        super(props);
        this.toBootstrapAsync();
    }

    componentDidMount() {
        SplashScreen.hide();//关闭启动屏幕
    }

    toBootstrapAsync = async () => {
        // AsyncStorage.setItem("userToken", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1MzUwOTg3NTgsInVzZXJuYW1lIjoiYWRtaW4ifQ.ojfsx-bzXV4Qh2fIlGHe7cZr_9h23WbVMZuZ8rxCLkY");
        const userToken = await AsyncStorage.getItem('userToken'), me = this;

        axios.defaults.headers.common['sysToken'] = userToken || "";

        // 添加响应拦截器
        axios.interceptors.response.use((response) => {
            // 对响应数据做点什么
            // console.log(response);
            return response;
        }, (error) => {
            if (error.response) {
                // console.log(error.response);
                // 授权超时或未授权
                if (error.response.status == 401) {
                    me.toSignOutAsync();
                }
            } else {
                console.log('Error', error.message);
            }
            // console.log(error.config);
            // 对响应错误做点什么
            return Promise.reject(error);
        });

        // this.getUserInfo();

        this.props.navigation.navigate(userToken ? 'App' : 'Auth');
    };

    toSignOutAsync = async () => {
        await AsyncStorage.clear();
        this.props.navigation.navigate('Auth');
    };

    // Render any loading content that you like here
    render() {
        return (
            <View style={styles.container}>
                <ActivityIndicator/>
                <StatusBar barStyle="default"/>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
    },
});