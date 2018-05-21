package adriancardenas.com.ehealth.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by brijesh on 14/4/17.
 */

public class Constants {

    public static final int REQUEST_BLUETOOTH_ENABLE_CODE = 101;
    public static final int REQUEST_LOCATION_ENABLE_CODE = 101;
    public static final int WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT = 103;
    public static final int REQUEST_TAKE_PHOTO_RESULT = 104;

    public static final int SCAN_PERIOD = 20000;

    public static final String LOCAL_PATH_PHOTOS = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ehealth/images/";
    public static final String LOCAL_APPLICATION_PATH = "adriancardenas.com.ehealth";

    public static final String PHOTO = "profile_photo";
    public static final String DEVICE = "device";
    public static final String EXTRA_DEVICE = "extra_device";

}
