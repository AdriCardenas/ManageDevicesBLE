package adriancardenas.com.ehealth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import adriancardenas.com.ehealth.Database.DatabaseOperations;
import adriancardenas.com.ehealth.Utils.Constants;
import adriancardenas.com.ehealth.Utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

import static adriancardenas.com.ehealth.Utils.Constants.WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT;
import static adriancardenas.com.ehealth.Utils.Utils.getPhotoCode;

public class ConfigurationActivity extends AppCompatActivity {
    @BindView(R.id.name_user_layout)
    TextInputLayout nameUserLayout;
    @BindView(R.id.name_user)
    TextInputEditText nameUser;

    @BindView(R.id.age_user_layout)
    TextInputLayout ageUserLayout;
    @BindView(R.id.age_user)
    TextInputEditText ageUser;

    @BindView(R.id.weight_user_layout)
    TextInputLayout weightUserLayout;
    @BindView(R.id.weight_user)
    TextInputEditText weightUser;

    @BindView(R.id.steps_daily_user_layout)
    TextInputLayout stepsGoalLayout;
    @BindView(R.id.steps_daily_user)
    TextInputEditText stepsGoal;

    @BindView(R.id.height_user_layout)
    TextInputLayout heightUserLayout;
    @BindView(R.id.height_user)
    TextInputEditText heightUser;

    @BindView(R.id.profile_image)
    ImageView profileImage;
    @BindView(R.id.add_photo_iv)
    ImageView addPhotoIv;

    @BindView(R.id.root_cell)
    ScrollView scrollView;

    private Uri uriPhoto;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        ButterKnife.bind(this);

        initProfilePhoto();

        initFields();

        Intent intent = getIntent();

        isConnected = intent.getBooleanExtra(Constants.CONNECTED, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_configuration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.button_next:
                if (correctFields()) {
                    saveData();

                    if (isConnected) {
                        finish();
                    } else {
                        Intent intent = new Intent(ConfigurationActivity.this, ScanActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
                break;
        }
        return true;
    }

    private void saveData() {
        String name = nameUser.getText().toString();
        String goal = stepsGoal.getText().toString();
        String age = ageUser.getText().toString();
        String weight = weightUser.getText().toString();
        String height = heightUser.getText().toString();

        SharedPreferences sharedPref = getSharedPreferences(Constants.LOCAL_APPLICATION_PATH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.NAME, name);
        editor.putString(Constants.AGE, age);
        editor.putString(Constants.STEPS_GOAL, goal);
        editor.putString(Constants.HEIGHT, height);
        editor.putBoolean(Constants.IS_CONFIGURED, true);

        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        DatabaseOperations databaseOperations = DatabaseOperations.getInstance(this);
        databaseOperations.insertWeight(fDate, weight);

        editor.apply();
    }

    private boolean correctFields() {
        boolean correctField = true;
        if (nameUser.getText() == null || nameUser.getText().toString().equals("")) {
            nameUserLayout.setError(getString(R.string.introduce_name));
            correctField = false;
        }

        if (weightUser.getText() == null || weightUser.getText().toString().equals("")) {
            weightUserLayout.setError(getString(R.string.introduce_weight));
            correctField = false;
        }

        if (ageUser.getText() == null || ageUser.getText().toString().equals("")) {
            ageUserLayout.setError(getString(R.string.introduce_age));
            correctField = false;
        }

        if (stepsGoal.getText() == null || stepsGoal.getText().toString().equals("")) {
            stepsGoalLayout.setError(getString(R.string.introduce_steps_goal_error));
            correctField = false;
        }

        if (heightUser.getText() == null || heightUser.getText().toString().equals("")) {
            heightUserLayout.setError(getString(R.string.introduce_height));
            correctField = false;
        }
        return correctField;
    }

    private void initFields() {
        SharedPreferences sharedPref = this.getSharedPreferences(Constants.LOCAL_APPLICATION_PATH, Context.MODE_PRIVATE);
        DatabaseOperations operations = DatabaseOperations.getInstance(this);
        String height = sharedPref.getString(Constants.HEIGHT, "");
        Float weight = operations.getLastWeight();
        String goal = sharedPref.getString(Constants.STEPS_GOAL, "");
        String name = sharedPref.getString(Constants.NAME, "");
        String age = sharedPref.getString(Constants.AGE, "");

        if (!name.equals("")) {
            nameUser.setText(name);
        }

        if (!stepsGoal.equals("")) {
            stepsGoal.setText(goal);
        }

        if (!height.equals("")) {
            heightUser.setText(height);
        }

        if (!age.equals("")) {
            ageUser.setText(age);
        }

        if (weight != 0) {
            weightUser.setText(String.valueOf(weight));
        }
    }

    private void initProfilePhoto() {
        SharedPreferences sharedPref = this.getSharedPreferences(Constants.LOCAL_APPLICATION_PATH, Context.MODE_PRIVATE);
        String url = sharedPref.getString(Constants.PHOTO, "");

        if (!url.equals("")) {
            Glide.with(this).load(url).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(profileImage);
        }

        addPhotoIv.setOnClickListener((View) -> {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT);
                initCameraPhoto();
            } else {
                initCameraPhoto();
            }
        });
    }

    private void initCameraPhoto() {
        String photoPath = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + getPhotoCode() + ".jpg";
        File photo = new File(photoPath);

        try {
            photo.createNewFile();
            uriPhoto = FileProvider.getUriForFile(this, "adriancardenas.com.ehealth", photo);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriPhoto);
            startActivityForResult(cameraIntent, Constants.REQUEST_TAKE_PHOTO_RESULT);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("*ERROR*", "failed to create a file photo");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_TAKE_PHOTO_RESULT) {
            if (resultCode == RESULT_CANCELED) {
                Utils.showSnackbar(scrollView, getResources().getString(R.string.error_take_photo));
            } else {
                if (uriPhoto != null && uriPhoto.getPath().contains("jpg")) {
                    SharedPreferences sharedPref = this.getSharedPreferences(Constants.LOCAL_APPLICATION_PATH, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(Constants.PHOTO, "");
                    editor.apply();

                    Glide.with(this).load(uriPhoto).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(profileImage);
                    uriPhoto = null;
                } else {
                    Utils.showSnackbar(scrollView, getResources().getString(R.string.error_take_photo));
                }
            }
        }
    }
}
