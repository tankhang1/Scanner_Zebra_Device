import {View, Text, StatusBar} from 'react-native';
import React from 'react';

const App = () => {
  return (
    <View
      style={{
        flex: 1,
        backgroundColor: 'white',
      }}>
      <StatusBar
        backgroundColor={'transparent'}
        translucent
        barStyle={'dark-content'}
      />
    </View>
  );
};

export default App;
