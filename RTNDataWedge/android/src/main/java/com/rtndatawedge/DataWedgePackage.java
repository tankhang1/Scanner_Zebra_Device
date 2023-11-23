package com.rtndatawedge;

import androidx.annotation.Nullable;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfo;
import com.facebook.react.module.model.ReactModuleInfoProvider;
import com.rtndatawedge.DataWedgeModule;
import com.facebook.react.TurboReactPackage;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.List;

public class DataWedgePackage extends TurboReactPackage {

    @Nullable
    @Override
    public NativeModule getModule(String name, ReactApplicationContext reactContext) {
        if (name.equals(DataWedgeModule.NAME)) {
            return new DataWedgeModule(reactContext);
        } else
            return null;
    }

    @Override
    public ReactModuleInfoProvider getReactModuleInfoProvider() {
        return () -> {
            final Map<String, ReactModuleInfo> moduleInfos = new HashMap<>();
            moduleInfos.put(
                    DataWedgeModule.NAME,
                    new ReactModuleInfo(
                            DataWedgeModule.NAME,
                            DataWedgeModule.NAME,
                            false, // canOverrideExistingModule
                            false, // needsEagerInit
                            true, // hasConstants
                            false, // isCxxModule
                            true // isTurboModule
            ));
            return moduleInfos;
        };
    }
}