/**
 * 广西百色重大项目管理系统 主页
 * @author: tzg
 */

import React from "react";
import {AsyncStorage, Image, StyleSheet, Text, TouchableOpacity, View} from "react-native";
import Icon from 'react-native-vector-icons/Ionicons';
import axios from "axios/index";

export default class MyselfScreen extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: "",
            displayName: ""
        };
    }

    componentDidMount() {
        AsyncStorage.getItem('userName',(error,result)=>{
            this.setState({
                username:result
            })
        })
    }
    render() {
        const {navigation} = this.props;
        return (
            <View style={styles.container}>
                <View style={styles.header}>
                    <Image style={styles.userIMG} source={require('../../res/images/user-img.png')}/>
                    <View style={styles.userInfo}>
                        <Text style={{color: '#fff', fontSize: 18, lineHeight: 30}}>{this.state.displayName}</Text>
                        <Text style={{color: '#fff', fontSize: 16}}>{this.state.username}</Text>
                    </View>
                </View>
                <TouchableOpacity activeOpacity={0.8} style={styles.itemView}
                                  onPress={() => navigation.navigate('BasicInfoScreen')}>
                    <Icon style={styles.itemIcon} color={'#15bc83'} name='ios-contact' size={28}/>
                    <Text style={styles.itemText}>账户基本信息</Text>
                </TouchableOpacity>
                <TouchableOpacity activeOpacity={0.8} style={styles.itemView}
                                  onPress={() => navigation.navigate('SetUpScreen')}>
                    <Icon style={styles.itemIcon} color={'#5482c2'} name='ios-cog' size={28}/>
                    <Text style={styles.itemText}>设置</Text>
                </TouchableOpacity>
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
    header: {
        height: 200,
        width: '100%',
        backgroundColor: '#1E5AAF',
        elevation: 0,
        flexDirection: 'row',
        justifyContent: 'flex-start',
        alignItems: 'center',
        paddingLeft: '10%',
        marginBottom: 10,
    },
    userIMG: {
        width: 90,
        resizeMode: 'contain',
        marginRight: 15
    },
    userInfo: {
        height: 90,
        justifyContent: 'center',

    },
    itemView: {
        width: '100%',
        height: 50,
        backgroundColor: '#fff',
        flexDirection: 'row',
        justifyContent: 'flex-start',
        alignItems: 'center',
        marginBottom: 10,
        paddingLeft: 20,
        paddingRight: 20,
    },
    itemIcon: {
        marginRight: 20
    },
    itemText: {
        color: '#000',
        fontSize: 16,
    }
});