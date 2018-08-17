/**
 * 广西百色重大项目管理系统 市层面重大项目信息
 * @author: tzg
 */
import React from "react";
import {
    View,
    Text,
    Button,
    StyleSheet,
    FlatList,
    RefreshControl,
    ActivityIndicator,
    TouchableOpacity
} from "react-native";
import ProjectListComponent from '../component/ProjectListComponent'


type Props = {};
export default class ProjectScreen extends React.Component {
    constructor(props){
        super(props);
        this.state={
            projectData:[]
        }
    }
    componentWillMount() {
        this.setState({
            projectData:[{
                itemName:'(市层面)田阳地税局新建项目1',
                itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
                totalInvestment:'500万',
                startDate:'2018-08-08',
            },{
                itemName:'(市层面)田阳地税局新建项目2',
                itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
                totalInvestment:'800万',
                startDate:'2018-09-08',
            },{
                itemName:'(市层面)田阳地税局新建项目3',
                itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
                totalInvestment:'805万',
                startDate:'2018-12-08',
            },{
                itemName:'(市层面)田阳地税局新建项目4',
                itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
                totalInvestment:'805万',
                startDate:'2018-12-08',
            },{
                itemName:'(市层面)田阳地税局新建项目5',
                itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
                totalInvestment:'805万',
                startDate:'2018-12-08',
            }]
        })
    }
    render() {
        return (
            <View style={styles.container}>
                <View style={{height: 50,width:'100%', backgroundColor: '#1E5AAF',elevation:0,alignItems:'center'}}>
                    <Text style={{fontSize: 18, color: '#fff',lineHeight:50}}>市层面重大项目</Text>
                </View>
                <View style={styles.container}>
                    <ProjectListComponent
                        projectData={this.state.projectData}
                    />
                </View>
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
});