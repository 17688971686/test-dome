/**
 * DatePicker
 * @author: wzb
 */
import React from "react";
import {
    AsyncStorage,
    View,
    Text,
    StyleSheet,
} from "react-native";
import DatePicker from 'react-native-datepicker'

export default class DatePickerComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data:new Date()  //默认显示当前时间，如果为自定义则为（'1991-1-1'）
        };
    }
    render() {
        const mode=this.props.mode || 'date';
        return (
            <DatePicker
                date={this.state.data}
                mode={mode}
                confirmBtnText="Confirm"
                cancelBtnText="Cancel"
                customStyles={{
                    dateIcon: {
                        position: 'absolute',
                        right: 0,
                        top: 5,
                        width:16,
                        resizeMode: 'contain',
                    },
                    dateInput: {
                        marginLeft: 36
                    }
                }}
                ref='DatePicker'
                hideText={true}  //不显示日期文本
                onDateChange={(datetime) =>alert(datetime)} />

        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex:1,
        alignItems: 'center',
        justifyContent: 'center'
    },

});