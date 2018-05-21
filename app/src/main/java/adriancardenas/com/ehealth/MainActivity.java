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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;

import adriancardenas.com.ehealth.Utils.Constants;
import adriancardenas.com.ehealth.Utils.GattManager;
import adriancardenas.com.ehealth.Utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static adriancardenas.com.ehealth.Utils.Constants.WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT;
import static adriancardenas.com.ehealth.Utils.Utils.getPhotoCode;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.add_photo_iv)
    ImageView addPhotoIv;
    @BindView(R.id.constraint_layout_cell)
    ConstraintLayout constraintLayout;
    @BindView(R.id.personal_height)
    TextView personalHeight;


    private Uri uriPhoto;
    private File photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        personalHeight.setText(GattManager.device.getAddress());

        initProfilePhoto();
    }

    private void initProfilePhoto() {
        SharedPreferences sharedPref = getSharedPreferences(Constants.LOCAL_APPLICATION_PATH, Context.MODE_PRIVATE);
        String url = sharedPref.getString(Constants.PHOTO,"");

        if(!url.equals("")){
            Glide.with(getApplicationContext()).load(url).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(profileImage);
        }

        addPhotoIv.setOnClickListener((View) -> {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT);
                initCameraPhoto();
            } else {
                initCameraPhoto();
            }
        });
    }

    private void initCameraPhoto() {
        String photoPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + getPhotoCode() + ".jpg";
        photo = new File(photoPath);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_TAKE_PHOTO_RESULT) {
            if (resultCode == RESULT_CANCELED) {
                Utils.showSnackbar(constraintLayout, getResources().getString(R.string.error_take_photo));
            } else {
                if (uriPhoto != null && uriPhoto.getPath().contains("jpg")) {
                    SharedPreferences sharedPref = getSharedPreferences(Constants.LOCAL_APPLICATION_PATH, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(Constants.PHOTO,uriPhoto.toString());
                    editor.commit();

                    Glide.with(getApplicationContext()).load(uriPhoto).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(profileImage);
                    uriPhoto = null;
                } else {
                    Utils.showSnackbar(constraintLayout, getResources().getString(R.string.error_take_photo));
                }
            }
        }
    }
}
