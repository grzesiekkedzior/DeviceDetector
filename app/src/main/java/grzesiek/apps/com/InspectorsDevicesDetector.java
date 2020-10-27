package grzesiek.apps.com;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * This class represent the main logic in the application
 */
public class InspectorsDevicesDetector {

    // Here are devices names to search
    private static final String[] inspectorsDevicesNames = new String[]{"device1", "device2"};
    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BroadcastReceiver broadcastReceiver;
    private final MainActivity mainActivity;
    private boolean registeredBroadcastReceiver = false;

    public InspectorsDevicesDetector(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        initDetectionLogic();
    }

    /**
     * This method serch devices in infinitive loop
     */
    private void initDetectionLogic() {
        this.broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    action = device.getName();
                    if (action != null) {
                        for (CharSequence contains : InspectorsDevicesDetector.inspectorsDevicesNames) {
                            if (action.contains(contains)) {
                                InspectorsDevicesDetector.this.mainActivity.informAboutFindings();
                            }
                        }
                    }
                } else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                    InspectorsDevicesDetector.this.startDiscovery();
                }
            }
        };
    }

    // Register a BroadcastReceiver to be run in the main activity thread
    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter("android.bluetooth.device.action.FOUND");
        intentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
        this.mainActivity.registerReceiver(this.broadcastReceiver, intentFilter);
        this.registeredBroadcastReceiver = true;
    }

    // Unregister a previously registered BroadcastReceiver
    public void unregisterReceiver() {
        if (this.registeredBroadcastReceiver) {
            this.mainActivity.unregisterReceiver(this.broadcastReceiver);
        }
    }

    // check when adapter is switch on
    public boolean isDetectionAvailable() {
        return this.bluetoothAdapter != null;
    }

    // Return true if Bluetooth is currently enabled and ready for use.
    public boolean isDetectionEnabled() {
        return this.bluetoothAdapter.isEnabled();
    }

    // Start the remote device discovery process.
    public void startDiscovery() {
        this.bluetoothAdapter.startDiscovery();
    }

    // Create an intent with a given action
    public void requestDetectionToBeEnabled() {
        this.mainActivity.startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);

    }
}
