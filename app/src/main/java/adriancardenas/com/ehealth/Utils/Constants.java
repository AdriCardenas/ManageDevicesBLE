package adriancardenas.com.ehealth.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.UUID;

/**
 * Created by brijesh on 14/4/17.
 */

public class Constants {

    public static final int REQUEST_BLUETOOTH_ENABLE_CODE = 101;
    public static final int REQUEST_LOCATION_ENABLE_CODE = 101;
    public static final int WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT = 103;
    public static final int REQUEST_TAKE_PHOTO_RESULT = 104;

    public static final int SCAN_PERIOD = 10000;

    public static final String LOCAL_PATH_PHOTOS = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ehealth/images/";
    public static final String LOCAL_APPLICATION_PATH = "adriancardenas.com.ehealth";

    public static final String PHOTO = "profile_photo";
    public static final String EXTRA_DEVICE = "extra_device";

    public static class Basic {
        public static UUID service = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");
        public static UUID batteryCharacteristic = UUID.fromString("00000006-0000-3512-2118-0009af100700");
    }

    public static class AlertNotification {
        public static UUID service = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");
        public static UUID alertCharacteristic = UUID.fromString("00002a06-0000-1000-8000-00805f9b34fb");
    }

    public static class HeartRate {
        public static UUID service = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
        public static UUID measurementCharacteristic = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
        public static UUID descriptor = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
        public static UUID controlCharacteristic = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");
    }
}
