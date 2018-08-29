import React from "react";
import {AsyncStorage, FlatList, StyleSheet, Text, View} from "react-native";
import Header from '../../component/HeaderComponent'

export default class BasicInfoScreen extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            itemData: [],
        }
    }

    componentWillMount () {
        this.getUserInfo();
    };

    getUserInfo = async () => {
        const user = JSON.parse(await AsyncStorage.getItem('userInfo'));
        this.setState({
            itemData: [{
                itemName: "用户名",
                value: "管理员"
            }, {
                itemName: "姓名",
                value: user.displayName
            }, {
                itemName: '所属机构',
                value: user.organName
            }]
        });
    };

    /*为每个item生产唯一的key*/
    _extraUniqueKey(item, index) {
        return "index" + index + item;
    }

    _renderItem(item) {
        return (
            <View style={styles.itemView}>
                <Text style={styles.itemName}>{item.itemName}</Text>
                <Text style={styles.itemValue}>{item.value}</Text>
            </View>
        )
    }

    render() {
        return (
            <View style={styles.container}>
                <Header title={'我的基本信息'} showBackTitle={true} navigation={this.props.navigation}/>
                <FlatList
                    style={{width:'100%'}}
                    keyExtractor={this._extraUniqueKey}
                    data={this.state.itemData}
                    renderItem={({item}) => this._renderItem(item)}
                />
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
    itemView: {
        width: '100%',
        height: 60,
        flexDirection:'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        paddingLeft: 15,
        paddingRight: 15,
        borderBottomWidth: 0.5,
        borderBottomColor: '#c3c3c3',
        backgroundColor: '#fff'
    },
    itemName: {
        fontSize: 16,
        color: '#000'
    },
    itemValue: {
        fontSize: 16,
        color: '#999'
    }


});