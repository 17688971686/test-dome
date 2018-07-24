/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {AppNavigator} from "./app/navigator/Navigation";


type Props = {};
export default class App extends Component<Props> {
  render() {
    return (
        <AppNavigator />
    );
  }
}

