import React from "react";
import {
    View,
    Text,
    StyleSheet,
} from "react-native";
import Header from '../component/HeaderComponent'


export default class ProDetailsScreen extends React.Component {
    constructor(props){
        super(props);
    }
    render() {
        return (
            <View style={styles.container}>
                <Header title={'市层面重大项目'} headerRight={this._filter()}/>
            </View>
        );
    }

}

const styles = StyleSheet.create({
    container: {
        flex:1,
        alignItems: 'center',
        justifyContent: 'flex-start'
    },
    filterView:{
        flexDirection:'row',
        justifyContent:'flex-end',
        alignItems:'center'
    },
    filterText:{
        color:'#fff',
        lineHeight:50,
    },
});