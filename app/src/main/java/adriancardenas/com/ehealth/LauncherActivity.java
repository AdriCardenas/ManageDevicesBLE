package adriancardenas.com.ehealth;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import adriancardenas.com.ehealth.Utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LauncherActivity extends AppCompatActivity {
    private final int DURATION_SPLASH = 1000;
    ImageView iconLauncher;
    TextView titleLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_launcher);

        iconLauncher = findViewById(R.id.icon_launcher);
        titleLauncher = findViewById(R.id.title_launcher);

        Animation upToDownAnimation = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        Animation downToUpAnimation = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        Utils.checkBluetoothPermission(this);

        upToDownAnimation.setAnimationListener(new Animation.AnimationListener() {
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
        iconLauncher.startAnimation(upToDownAnimation);
        titleLauncher.startAnimation(downToUpAnimation);
    }
}
