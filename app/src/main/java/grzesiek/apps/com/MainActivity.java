package grzesiek.apps.com;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private InspectorsDevicesDetector inspectorsDevicesDetector;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);
        requestLocationAccessPermission();
        initLocationAccessUi();
        requestLocationAccessPermission();

    }

    public void onDestroy() {
        super.onDestroy();
        if (this.inspectorsDevicesDetector != null) {
            this.inspectorsDevicesDetector.unregisterReceiver();
        }
    }

    // Called when an activity you launched exits
    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1 && i2 == -1) {
            startDetection();
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (i == 1 && iArr.length > 0 && iArr[0] == 0) {
            initLocationAccessUi();
        }
    }

    // Pass MainActivity to InspectorDevicesDetector
    private void initLocationAccessUi() {
        if (isLocationAccessPermissionGranted()) {
            this.inspectorsDevicesDetector = new InspectorsDevicesDetector(this);
            checkDetectionAvailability();
        }
    }

    private boolean isLocationAccessPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0;
    }

    private void requestLocationAccessPermission() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, 1);
    }

    private void checkDetectionAvailability() {
        if (this.inspectorsDevicesDetector.isDetectionAvailable()) {
            checkIfDetectionEnabled();
        } else {
            disableInspectorsDevicesDetection();
        }
    }

    // When detection is imposible now
    private void disableInspectorsDevicesDetection() {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Disabled");
        dlgAlert.setTitle("Device");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private void checkIfDetectionEnabled() {
        if (this.inspectorsDevicesDetector.isDetectionEnabled()) {
            startDetection();
        }
    }

    // Start detection visual routines
    private void startDetection() {
        this.inspectorsDevicesDetector.registerReceiver();
        this.inspectorsDevicesDetector.startDiscovery();
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText("SEARCHING");
    }

    // When device is found the vibration is start
    public void informAboutFindings() {
        new Vibrator(this).vibrate();
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setVisibility(View.VISIBLE);
        findViewById(R.id.buttonExit).setVisibility(View.VISIBLE);
        findingTheme();
    }

    // Background color when device is found
    private void findingTheme() {
        this.getWindow().getDecorView().setBackgroundColor(Color.rgb(255, 100, 100));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E60911")));
        findViewById(R.id.buttonExit).setBackgroundColor(Color.RED);
        findViewById(R.id.imageButton).setBackgroundColor(Color.RED);
        findViewById(R.id.buttonInfo).setBackgroundColor(Color.RED);
    }

    public void onEnableBluetoothButtonClick(View view) {
        this.inspectorsDevicesDetector.requestDetectionToBeEnabled();
    }

    public void onExitApp(View view) {
        System.exit(0);
    }

    // Info message
    public void onMessageBox(View view) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Use it in the middle of a vehicle.");
        dlgAlert.setTitle("DeviceDetector v1.0.0");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
}
