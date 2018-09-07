/**
 * 广西百色重大项目管理系统 用户登录
 * @author: tzg
 */
import React from "react";
import {
    AsyncStorage,
    StyleSheet,
    View,
    Text,
    TextInput,
    Alert,
    ImageBackground,
    Image,
    TouchableOpacity,
    KeyboardAvoidingView
} from "react-native";
import axios from "axios";
import Icon from 'react-native-vector-icons/Ionicons';
import JSEncrypt from '../res/lib/JSEncrypt';


export default class SignInScreen extends React.Component {
    constructor(props) {
        super(props);
        const me = this;
        me.state = {username: '', password: '', rsaKey: ''};
    }

    render() {
        return (
            <KeyboardAvoidingView style={{flex:1}}>
                <ImageBackground style={styles.container} source={require('../res/images/loginBG.jpg')}>
                    <Image style={styles.logo} source={require('../res/images/logo.png')}/>
                    <View style={styles.inputView}>
                        <View style={styles.inputContainer}>
                            <Icon name='md-person' size={25} style={styles.iconStyle}/>
                            <TextInput
                                style={styles.textInputStyle}
                                clearButtonMode="while-editing"
                                placeholder="请输入用户名"
                                onChangeText={(username) => this.setState({username})}
                                underlineColorAndroid='transparent'
                                value={this.state.username}
                            />
                        </View>
                        <View style={styles.inputContainer}>
                            <Icon name='ios-lock' size={25} style={styles.iconStyle}/>
                            <TextInput
                                style={styles.textInputStyle}
                                clearButtonMode="while-editing"
                                secureTextEntry
                                placeholder="请输入密码"
                                onChangeText={(password) => this.setState({password})}
                                underlineColorAndroid='transparent'
                                value={this.state.password}
                            />
                        </View>
                    </View>
                    <TouchableOpacity activeOpacity={0.8} style={styles.btnView} onPress={this.toSignInAsync}>
                        <Text style={styles.btnStyle}>登录</Text>
                    </TouchableOpacity>
                </ImageBackground>
            </KeyboardAvoidingView>
        );
    }

    toSignInAsync = () => {
        const un = this.state.username,
            pd = this.state.password;
        if (!un || !pd) {
            Alert.alert('账号或密码不能为空');
            return false;
        }

        const encrypt = new JSEncrypt();
        const me = this;
        encrypt.setPublicKey(me.state.rsaKey || "");
        axios({
            url: "/login/signin",
            method: "post",
            params: {
                username: un,
                password: pd
            }
        }).then((res) => {
            const data = res.data || {};
            const userToken = data.reObj || "";
            if (res.status == 200 && data.reCode == 'ok') {
                AsyncStorage.setItem('userToken', userToken);
                me.props.navigation.navigate('AuthLoading');
            } else {
                Alert.alert(data.reMsg);
            }
        }).catch((error) => {
            console.error(error);
            Alert.alert("登录失败！");
        })
    };
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent:'flex-start',
    },
    logo: {
        width:'80%',
        alignSelf: 'center',
        resizeMode: 'contain',
        marginBottom: 50,
        marginTop:50,
    },
    inputView: {
        width: '80%',
        alignSelf: 'center'
    },
    inputContainer: {
        borderBottomWidth: 0.5,
        borderBottomColor: '#8A8A8A',
        flexDirection: 'row',
        height: 40,
        alignItems: 'center',
        paddingLeft: 10,
        marginBottom: 25,
    },
    textInputStyle: {
        fontSize: 15,
        flex: 1,
        paddingLeft: 15
    },
    iconStyle: {
        width: 25,
        color: '#8A8A8A',
    },
    btnView: {
        width: '80%',
        alignSelf: 'center',
        justifyContent: 'center',
        backgroundColor: '#1E5AAF',
        height: 40,
        borderRadius: 50,
        marginTop: 30,
    },
    btnStyle: {
        color: '#fff',
        alignSelf: 'center',
        fontSize: 16,
        fontWeight: 'bold',
        letterSpacing: 5,
    }
});