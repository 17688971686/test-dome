
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


export default class SearchScreen extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
            <View style={styles.container}>
                <Text>哈哈哈啊</Text>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems:'center'
    },
});