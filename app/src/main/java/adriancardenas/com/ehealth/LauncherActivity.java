package adriancardenas.com.ehealth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import adriancardenas.com.ehealth.Utils.Constants;
import adriancardenas.com.ehealth.Utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LauncherActivity extends AppCompatActivity {
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

        SharedPreferences sharedPref = getSharedPreferences(Constants.LOCAL_APPLICATION_PATH, Context.MODE_PRIVATE);

        upToDownAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(sharedPref.getBoolean(Constants.IS_CONFIGURED,false)){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(LauncherActivity.this, ScanActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(LauncherActivity.this, ConfigurationActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


        String name = sharedPref.getString(Constants.NAME, "");

        if(!name.equals("")){
            name = getString(R.string.welcome)+" "+name;
            titleLauncher.setText(name);
        }

        iconLauncher.startAnimation(upToDownAnimation);
        titleLauncher.startAnimation(downToUpAnimation);
    }
}
