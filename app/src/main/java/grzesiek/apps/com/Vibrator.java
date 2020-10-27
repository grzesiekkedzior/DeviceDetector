package grzesiek.apps.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build.VERSION;
import android.os.VibrationEffect;

/**
 *  This class is responsible for start phone vibration when device is detected
 */
public class Vibrator {
    private final long[] pattern = new long[]{0, 1000, 1000};
    private final android.os.Vibrator vibrator;

    @SuppressLint("WrongConstant")
    public Vibrator(Activity activity) {
        this.vibrator = (android.os.Vibrator) activity.getSystemService("vibrator");
    }

    public void vibrate() {
        if (VERSION.SDK_INT >= 26) {
            this.vibrator.vibrate(VibrationEffect.createWaveform(this.pattern, 0));
        } else {
            this.vibrator.vibrate(this.pattern, 0);
        }
    }
}
