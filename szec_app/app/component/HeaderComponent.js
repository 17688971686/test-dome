import React from "react";
import {
    View,
    Text,
    StyleSheet,
    TouchableOpacity
} from "react-native";
import Icon from 'react-native-vector-icons/Ionicons';


export default class HeaderComponent extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        let height =parseInt(this.props.height) || 50,
            title =this.props.title || '',
            color = this.props.color || '#fff',
            showBackTitle=this.props.showBackTitle,
            backTitle= this.props.backTitle || <Icon name="ios-arrow-back" size={28} />,
            fontSize = parseInt(this.props.fontSize) || 18,
            headerRight=this.props.headerRight,
            navigation = this.props.navigation;
        return (
            <View style={[styles.container,{height:height}]}>
                <Text style={[styles.headerLeft,{height:height, lineHeight:height}]} onPress={()=>navigation.goBack()}>
                     {showBackTitle && backTitle}
                </Text>
                <Text style={{color:color, fontSize:fontSize}}>{title}</Text>
                <View style={[styles.headerRight]}>
                    {headerRight}
                </View>

            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        width:'100%',
        flexDirection:'row',
        alignItems:'center',
        justifyContent:'space-between',
        backgroundColor: '#1E5AAF',
        paddingLeft:15,
        paddingRight:15
    },
    headerLeft:{
        width:50,
        color:'#fff',
    },
    headerRight:{
        width:50
    }
});