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
    public static final int REQUEST_ADD_WEIGHT_RESULT = 105;

    public static final int SCAN_PERIOD = 15000;

    public static final String LOCAL_PATH_PHOTOS = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ehealth/images/";
    public static final String LOCAL_APPLICATION_PATH = "adriancardenas.com.ehealth";

    public static final String PHOTO = "profile_photo";
    public static final String EXTRA_DEVICE = "extra_device";
    public static final String NAME = "user_name";
    public static final String AGE = "user_age";
    public static final String WEIGHT = "user_weight";
    public static final String STEPS_GOAL = "user_steps_goal";
    public static final String IS_CONFIGURED = "is_configured";
    public static final String HEIGHT = "user_height";
    public static final String CONNECTED = "isConnected";
    public static final String DISTANCE_CSV = "distance.csv";
    public static final String HEART_RATE_CSV = "heart.csv";
    public static final String STEPS_CSV = "steps.csv";
    public static final String WEIGHT_CSV = "weight.csv";


    public static class Basic {
        public static UUID service = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");
        public static UUID BATTERY_CHARASTERISTIC = UUID.fromString("00000006-0000-3512-2118-0009af100700");
        public static UUID Characteristic1 = UUID.fromString("00000010-0000-3512-2118-0009af100700");
        public static UUID Characteristic2 = UUID.fromString("00000008-0000-3512-2118-0009af100700");
        public static UUID REALTIME_STEPS_CHARACTERISTIC = UUID.fromString("00000007-0000-3512-2118-0009af100700");
        public static UUID ACTIVITY_DATA_CHARASTERISTIC = UUID.fromString("00000005-0000-3512-2118-0009af100700");
        public static UUID Characteristic5 = UUID.fromString("00000004-0000-3512-2118-0009af100700");
        public static UUID Characteristic6 = UUID.fromString("00000003-0000-3512-2118-0009af100700");
        public static UUID Characteristic7 = UUID.fromString("00000002-0000-3512-2118-0009af100700");
        public static UUID Characteristic8 = UUID.fromString("00000001-0000-3512-2118-0009af100700");
    }

    public static class HeartRate {
        public static UUID service = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
        public static UUID measurementCharacteristic = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
        public static UUID descriptor = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
        public static UUID controlCharacteristic = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");
    }

    public static class AlertNotification {
        public static UUID service = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");
        public static UUID alertCharacteristic = UUID.fromString("00002a06-0000-1000-8000-00805f9b34fb");
    }
}
