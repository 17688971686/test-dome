/**
 * 广西百色重大项目管理系统 判断是否登录
 * @author: tzg
 */

import React, {Component} from 'react';
import {ActivityIndicator, AsyncStorage, StatusBar, StyleSheet, View} from 'react-native';
import axios from "axios/index";

export default class AuthLoadingScreen extends Component {
    constructor(props) {
        super(props);
        this.toBootstrapAsync();
    }

    // Fetch the token from storage then navigate to our appropriate place
    toBootstrapAsync = async () => {
        const userToken = await AsyncStorage.getItem('userToken');

        axios.defaults.headers.common['TOKEN'] = userToken;

        // This will switch to the App screen or Auth screen and this loading
        // screen will be unmounted and thrown away.
        this.props.navigation.navigate(userToken ? 'App' : 'Auth');
    };

    // Render any loading content that you like here
    render() {
        return (
            <View style={styles.container}>
                <ActivityIndicator />
                <StatusBar barStyle="default" />
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