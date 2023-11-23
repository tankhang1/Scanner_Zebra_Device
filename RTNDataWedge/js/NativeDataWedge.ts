import type {TurboModule} from 'react-native/Libraries/TurboModule/RCTExport';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  registerReceiver(): void;
  toggleScan(): void;
}

export default TurboModuleRegistry.get<Spec>('RTNDataWedge') as Spec | null;
