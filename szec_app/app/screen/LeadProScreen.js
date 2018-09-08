import React from "react";
import {
    View,
    Text,
    Button,
    StyleSheet,
    FlatList,
    TouchableOpacity,
    AsyncStorage,
    DeviceEventEmitter
} from "react-native";
import Header from '../component/HeaderComponent'
import axios from 'axios'

export default class ProjectScreen extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: '',
            userName: '',
            reshing:''
        };
    }
    componentWillMount() {
        this.loadData();
        this.deEmitter = DeviceEventEmitter.addListener('Refresh', (Refresh) => {
            Refresh && this.loadData();
        });
    }
    loadData(){
        AsyncStorage.getItem('userName', (error, result) => {
            if (!error) {
                this.setState({
                    userName: result
                });
                axios({
                    url: "/agenda/tasks",
                    method: "post",
                    params: {
                        $skip: 1,
                        $top: 10,
                        username: result
                    },
                }).then((res) => {
                    console.log(1);
                    this.setState({
                        data: res.data.value
                    })
                })
                    .catch(error=>{
                        console.log(error)
                    })
            } else {
                console.log(error);
            }
        });
    }
    componentWillUnmount() {
        this.deEmitter.remove();
    }
    _extraUniqueKey(item, index) {
        return "index" + index + item;
    }

    _renderItem(item) {
        const {navigation} = this.props;
        return (
            <TouchableOpacity style={styles.item} activeOpacity={1}
                              onPress={() => navigation.navigate('ProDetailsScreen', {
                                  taskId: item.taskId,
                                  signId: item.businessKey,
                                  projectName: item.projectName,
                                  processInstanceId: item.processInstanceId,
                                  userName: this.state.userName,
                                  approve: true
                              })}>
                <Text style={styles.itemName} activeOpacity={1}>{item.projectName}</Text>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>项目阶段：</Text>
                    <Text>{item.reviewStage}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>当前环节：</Text>
                    <Text>{item.nodeNameValue}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>项目负责人：</Text>
                    <Text>{item.allPriUser}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>剩余工作日：</Text>
                    <Text style={{color: item.surplusDays <= 0 ? 'red' : ''}}>{item.surplusDays}</Text>
                </View>
            </TouchableOpacity>
        )
    }

    render() {
        return (
            <View style={styles.container}>
                <Header title={'项目审批'}/>
                <View style={styles.container}>
                    <FlatList
                        style={{width: '100%', backgroundColor: '#eee'}}
                        keyExtractor={this._extraUniqueKey}
                        data={this.state.data}
                        renderItem={({item}) => this._renderItem(item)}
                    />
                </View>
            </View>
        );
    }

}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        width: '100%',
        alignItems: 'center',
        justifyContent: 'flex-start'
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