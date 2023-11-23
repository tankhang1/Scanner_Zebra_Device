import {
  View,
  Text,
  StatusBar,
  Pressable,
  StyleSheet,
  Alert,
  DeviceEventEmitter,
  ScrollView,
} from 'react-native';
import React, {useEffect, useState} from 'react';
import {Path, Svg} from 'react-native-svg';
import RTNDataWedge from 'rtn-datawedge/js/NativeDataWedge';
const App = () => {
  const [scans, setScans] = useState<any[]>([]);
  const [enumerateScanners, setEnumerateScanners] = useState('');

  const showAlert = (content: string) => {
    Alert.alert('Content Inside Qr Code', content, [
      {
        text: 'Cancel',
        onPress(value) {},
        style: 'cancel',
      },
      {
        text: 'Confirm',
        onPress(value) {},
        style: 'default',
      },
    ]);
  };

  useEffect(() => {
    const subscribe = DeviceEventEmitter.addListener('barcode_scan', intent =>
      setScans([...scans, intent]),
    );
    RTNDataWedge?.registerReceiver();
    return () => subscribe.remove();
  }, []);
  const toggleScan = () => {
    RTNDataWedge?.toggleScan();
  };

  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={{justifyContent: 'center', alignItems: 'center'}}>
      <View
        style={{
          height: 100,
        }}
      />
      <StatusBar
        backgroundColor={'transparent'}
        translucent
        barStyle={'dark-content'}
      />
      <Text style={styles.header}>Scan QR / Bar Code</Text>
      <Text style={styles.subTitle}>
        Place qr/bar code inside frame to scan please avoid shake to get result
        quickly
      </Text>
      <View style={styles.qrWrapper}>
        <Svg viewBox="0 0 122.61 122.88">
          <Path
            d="M26.68 26.77h25.23v25.12H26.68V26.77zM35.67 0h-12.6a22.72 22.72 0 00-8.77 1.75 23.13 23.13 0 00-7.49 5 23.16 23.16 0 00-5 7.49A22.77 22.77 0 000 23.07v15.57h10.23V23.07a12.9 12.9 0 011-4.9A12.71 12.71 0 0114 14a12.83 12.83 0 019.07-3.75h12.6V0zm63.87 0h-8.23v10.23h8.23a12.94 12.94 0 014.9 1 13.16 13.16 0 014.17 2.77l.35.36a13.07 13.07 0 012.45 3.82 12.67 12.67 0 011 4.89v15.57h10.23V23.07a22.95 22.95 0 00-6.42-15.93l-.37-.37a23.16 23.16 0 00-7.49-5A22.77 22.77 0 0099.54 0zm23.07 99.81V82.52h-10.23v17.29a12.67 12.67 0 01-1 4.89 13.08 13.08 0 01-2.8 4.17 12.8 12.8 0 01-9.06 3.78h-8.21v10.23h8.23a23 23 0 0016.29-6.78 23.34 23.34 0 005-7.49 23 23 0 001.75-8.8zm-99.54 23.07h12.6v-10.23h-12.6a12.8 12.8 0 01-9.07-3.78l-.26-.24a12.83 12.83 0 01-2.61-4.08 12.7 12.7 0 01-.91-4.74V82.52H0v17.29a22.64 22.64 0 001.67 8.57 22.86 22.86 0 004.79 7.38l.31.35a23.2 23.2 0 007.5 5 22.84 22.84 0 008.8 1.75zm66.52-33.1H96v6.33h-6.41v-6.33zm-12.36 0h6.44v6H70.8V83.47H77v-6.25h6.34V64.76h6.46v6.12h6.12v6.33H89.8v6.33H77.23v6.23zM58.14 77.12h6.23v-6.33h-6v-6.33h6v-6.33h-6.13v6.33H51.8v-6.33h6.33v-18.8h6.43v18.79h6.23v6.33h6.13v-6.33h6.43v6.33h-6.12v6.33H70.8v12.46h-6.23v12.57h-6.43V77.12zm31.35-19h6.43v6.33h-6.43v-6.33zm-50.24 0h6.43v6.33h-6.43v-6.33zm-12.57 0h6.43v6.33h-6.43v-6.33zm31.46-31.35h6.43v6.33h-6.43v-6.33zM26.58 70.88H51.8V96H26.58V70.88zM32.71 77h13v12.91h-13V77zm38-50.22h25.21v25.11H70.7V26.77zm6.13 6.1h13v12.91h-13V32.87zm-44 0h13v12.91h-13V32.87z"
            fillRule="evenodd"
            strokeWidth={2}
          />
        </Svg>
      </View>
      <Text style={{color: 'gray'}}>Scanning Code....</Text>
      <Pressable onPress={toggleScan} style={styles.button}>
        <Text style={styles.textButton}>Start Scan</Text>
      </Pressable>
    </ScrollView>
  );
};

export default App;
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'white',
  },
  header: {
    color: 'black',
    fontSize: 24,
    fontWeight: '700',
  },
  subTitle: {
    color: 'gray',
    textAlign: 'center',
    width: '75%',
    marginTop: 10,
  },
  qrWrapper: {
    width: '70%',
    height: 250,
    marginVertical: 30,
  },
  button: {
    backgroundColor: '#FF7D54',
    width: '80%',
    paddingVertical: 15,
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 20,
    marginTop: 35,
  },
  textButton: {
    color: 'white',
    fontWeight: '700',
    fontSize: 20,
  },
});
