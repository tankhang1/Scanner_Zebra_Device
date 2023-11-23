package com.rtndatawedge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.util.Map;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.rtndatawedge.NativeDataWedgeSpec;

public class DataWedgeModule extends NativeDataWedgeSpec implements Observer, LifecycleEventListener {

    private static String DW_ACTION = "com.rtndatawedge.ACTION";
    private static String DATAWEDGE_INTENT_SOURCE = "com.symbol.datawedge.source";
    private static String DATAWEDGE_INTENT_LABEL_TYPE = "com.symbol.datawedge.label_type";
    private static String DATAWEDGE_INTENT_DATA = "com.symbol.datawedge.data_string";


    private static String DATAWEDGE_SOFT_SCAN_TRIGGER="com.symbol.datawedge.api.SOFT_SCAN_TRIGGER";
    private static String SOFT_SCAN_ACTION="com.symbol.datawedge.api.ACTION";
    // Enumerate Scanner Receiver
    private static final String ACTION_ENUMERATE_SCANNERS = "com.symbol.datawedge.api.ACTION_ENUMERATEDSCANNERLIST";
    private static final String KEY_ENUMERATEDSCANNERLIST = "DWAPI_KEY_ENUMERATEDSCANNERLIST";
    private ReactApplicationContext reactApplicationContext;

    public static String NAME = "RTNDataWedge";

    DataWedgeModule(ReactApplicationContext context) {
        super(context);
        this.reactApplicationContext = context;
        reactApplicationContext.addLifecycleEventListener(this);
        // Register a broadcast receiver to return data back to the application
        ObservableObject.getInstance().addObserver(this);
    }

    @Override
    public void registerReceiver() {
        unregisterReceiver(myBroadcastReceiver);
        IntentFilter filter = new IntentFilter();
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        filter.addAction(DW_ACTION);
        reactApplicationContext.registerReceiver(myBroadcastReceiver, filter);

    };
    @Override
    public void toggleScan(){
        Intent intent= new Intent();
        intent.setAction(SOFT_SCAN_ACTION);
        intent.putExtra(DATAWEDGE_SOFT_SCAN_TRIGGER,"TOGGLE_SCANNING");
        reactApplicationContext.sendBroadcast(intent);
        Toast.makeText(this.reactApplicationContext, "Scan data", Toast.LENGTH_LONG).show();
    }
    private void unregisterReceiver(BroadcastReceiver receiver) {
        try {
            this.reactApplicationContext.unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            // Expected behaviour if there was not a previously register receiver;
        }
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    };

    private void unregisterReceivers() {
        unregisterReceiver(myBroadcastReceiver);
        unregisterReceiver(myEmurateScannersBroadcastReceiver);
    }

    @Override
    public void onHostResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_ENUMERATE_SCANNERS);
        reactApplicationContext.registerReceiver(myEmurateScannersBroadcastReceiver, filter);
    }

    @Override
    public void onHostPause() {
        unregisterReceivers();
    }

    @Override
    public void onHostDestroy() {

    }

    private void sendEvent(ReactContext reactContext, String eventName, WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            if (action.equals(DW_ACTION)) {
                ///
                try {
                    // displayScanResult(intent, "via Broadcast");
                    ObservableObject.getInstance().updateValue(intent);
                } catch (Exception e) {

                }
            }
        }
    };

    private BroadcastReceiver myEmurateScannersBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("Scanner App", "Received Broadcast from DataWedge API - Scanner");
            ObservableObject.getInstance().updateValue(intent);
        }
    };

    @Override
    public void update(Observable observable, Object data) {
        Intent intent = (Intent) data;

        String action = intent.getAction();
        if (action.equals(ACTION_ENUMERATE_SCANNERS)) {
            Bundle b = intent.getExtras();
            String[] scanner_list = b.getStringArray(KEY_ENUMERATEDSCANNERLIST);
            WritableArray userFriendlyScanners = new WritableNativeArray();
            for (int i = 0; i < scanner_list.length; i++) {
                userFriendlyScanners.pushString(scanner_list[i]);
            }
            try {
                WritableMap enumeratedScannersObj = new WritableNativeMap();
                enumeratedScannersObj.putArray("Scanners", userFriendlyScanners);
                sendEvent(this.reactApplicationContext, "enumerated_scanners", enumeratedScannersObj);
            } catch (Exception e) {
                Toast.makeText(this.reactApplicationContext, "Error returning scanners", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            String decodedSource = intent.getStringExtra(DATAWEDGE_INTENT_SOURCE);
            String decodedData = intent.getStringExtra(DATAWEDGE_INTENT_DATA);
            String decodedLabelType = intent.getStringExtra(DATAWEDGE_INTENT_LABEL_TYPE);

            WritableMap scanData = new WritableNativeMap();
            scanData.putString("source", decodedSource);
            scanData.putString("data", decodedData);
            scanData.putString("labelType", decodedLabelType);
            sendEvent(this.reactApplicationContext, "barcode_scan", scanData);
        }

    }

}