import React, {Component} from "react";
import {
    View,
    Text,
    StyleSheet,
    ScrollView,
    FlatList
} from "react-native";
import axios from 'axios'

export default class SignHistoryComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: ''
        }
    }

    componentWillMount() {
        const {processInstanceId} = this.props;
        axios.get('/flowApp/historyInfo', {
            params: {
                processInstanceId: processInstanceId,
            }
        })
            .then(res => {
                console.log(res);
                this.setState({
                    data: res.data.value
                })
            })
            .catch(error => {
                console.log(error);
            });
    }

    _extraUniqueKey(item, index) {
        return "index" + index + item;
    }

    _renderItem(item) {
        return (
            <View style={styles.container}>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>环节名称：</Text>
                    <Text>{item.nodeNameValue}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>处理人：</Text>
                    <Text>{item.displayName}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>结束时间：</Text>
                    <Text>{item.endTime}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>处理信息：</Text>
                    <Text>{item.message}</Text>
                </View>
            </View>
        )
    }

    render() {
        return (
            <FlatList
                style={{width: '100%', backgroundColor: '#eee'}}
                keyExtractor={this._extraUniqueKey}
                data={this.state.data}
                renderItem={({item}) => this._renderItem(item)}
            />
        )
    }

}
const styles = StyleSheet.create({
    container: {
        padding:10,
        marginBottom: 10,
        backgroundColor: '#fff'
    },
    itemsView: {
        width: '100%'
    },
    item: {
        width: '100%',
        backgroundColor: '#fff',
        marginBottom: 15,
        padding: 15,
    },
    itemName: {
        fontSize: 18,
        color: '#000',
        fontWeight: 'bold',
        marginBottom: 5,
    },
    itemView: {
        flexDirection: 'row',
        justifyContent: 'flex-start',
        height: 30,
        alignItems: 'center'
    },
    itemText: {
        fontSize: 14,
        color: '#000',
    },
});
