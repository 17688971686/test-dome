import React from "react";
import {
    View,
    Text,
    Button,
    StyleSheet,
    FlatList,
    TouchableOpacity,
    AsyncStorage,
    DeviceEventEmitter,
    RefreshControl
} from "react-native";
import Header from '../component/HeaderComponent'
import axios from 'axios'

export default class ProRepeatScreen extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: '',
            userName: '',
            reshing:'',
            isLoading:false
        };
    }

    componentDidMount() {
        this.loadData();
        this.deEmitter = DeviceEventEmitter.addListener('Refresh', (Refresh) => {
            Refresh && this.loadData();
        });
    }
    loadData(){
        this.setState({
            isLoading:true
        });
        AsyncStorage.getItem('userName', (error, result) => {
            if (!error) {
                this.setState({
                    userName: result
                });
                axios({
                    url: "/sign/findByGetBack",
                    method: "post",
                    params: {
                        username: result
                    },
                }).then((res) => {
                    this.setState({
                        data: res.data.value,
                        isLoading:false
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
                              onPress={() => navigation.navigate('ProRepeatDetailScreen', {
                                  taskId: item.taskId,
                                  signId: item.businessKey,
                                  projectName: item.projectName,
                                  processInstanceId: item.processInstanceId,
                                  userName: this.state.userName,
                                  approve: true
                              })}>
                <Text style={styles.itemName} numberOfLines={1}>{item.projectName}</Text>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>项目名称：</Text>
                    <Text>{item.projectName}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>当前环节：</Text>
                    <Text>{item.nodeNameValue}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>处理人：</Text>
                    <Text>{item.displayName}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>项目阶段：</Text>
                    <Text>{item.reviewStage}</Text>
                </View>
            </TouchableOpacity>
        )
    }

    render() {
        return (
            <View style={styles.container}>
                <Header title={'项目重新分办'} showBackTitle={true} navigation={this.props.navigation}/>
                <View style={styles.container}>
                    <FlatList
                        style={{width: '100%', backgroundColor: '#eee'}}
                        keyExtractor={this._extraUniqueKey}
                        data={this.state.data}
                        renderItem={({item}) => this._renderItem(item)}
                        refreshControl={
                            <RefreshControl
                                title={'Loading'} //IOS
                                colors={['orange', 'green']} //android
                                tintColor={['orange']} //IOS
                                refreshing={this.state.isLoading}
                                onRefresh={() => {
                                    this.loadData();
                                }}
                            />
                        }
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