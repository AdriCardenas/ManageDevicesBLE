package adriancardenas.com.ehealth;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import adriancardenas.com.ehealth.Utils.Utils;

public class LauncherActivity extends AppCompatActivity {
    private final int DURATION_SPLASH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_launcher);

        ImageView icon = findViewById(R.id.icon_launcher);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade);
        Utils.checkBluetoothPermission(this);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(LauncherActivity.this, ScanActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        icon.startAnimation(animation);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(LauncherActivity.this,ScanActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }, DURATION_SPLASH);
    }
}
