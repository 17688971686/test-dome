/**
 * 判断是否已获取登录凭证
 * @author: tzg
 */

import React, {Component} from 'react';
import {ActivityIndicator, AsyncStorage, StatusBar, StyleSheet, View} from 'react-native';
import axios from "axios/index";
import SplashScreen from 'react-native-splash-screen';
import qs from 'qs';

export default class AuthLoadingScreen extends Component {
    constructor(props) {
        super(props);
        this.toBootstrapAsync();
    }
    componentDidMount() {
        SplashScreen.hide();//关闭启动屏幕
    }

    toBootstrapAsync = async () => {
        const userToken = await AsyncStorage.getItem('userToken'), me = this;
        const userName = await AsyncStorage.getItem('userName');
        axios.defaults.headers.common['sysToken'] = userToken || "";
        // 添加请求拦截器
       /* axios.interceptors.request.use(function (config) {
            if(config.method === 'post') {
                let data = qs.parse(config.data);
                config.data = qs.stringify({
                    username:userName,
                    ...data
                })
            } else if(config.method === 'get') {
                config.params = {
                    username:userName,
                    ...config.params
                }
            }
            return config;
        }, function (error) {
            return Promise.reject(error);
        });*/

        // 添加响应拦截器
        axios.interceptors.response.use((response) => {
            return response;
        }, (error) => {
            if (error.response) {
                if (error.response.status === 401) {
                    me.toSignOutAsync();
                }
            } else {
                console.log('Error', error.message);
            }
            return Promise.reject(error);
        });
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