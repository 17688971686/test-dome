import { AppRegistry } from 'react-native';
import App from './app/App';
import { YellowBox } from 'react-native';
YellowBox.ignoreWarnings(['Warning: isMounted(...) is deprecated', 'Module RCTImageLoader']);
YellowBox.ignoreWarnings(['Warning: Failed prop type: Invalid props.style key `barStyle` supplied to `View`']);
YellowBox.ignoreWarnings(['Remote debugger is in a background tab which may cause apps to perform slowly']);
AppRegistry.registerComponent('szcshlAPP', () => App);
