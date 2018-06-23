package adriancardenas.com.ehealth;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

import adriancardenas.com.ehealth.Adapters.PagerAdapter;
import adriancardenas.com.ehealth.Database.DatabaseMetadata;
import adriancardenas.com.ehealth.Database.DatabaseOperations;
import adriancardenas.com.ehealth.Tabs.DistanceEvolutionFragment;
import adriancardenas.com.ehealth.Tabs.HeartRateEvolutionFragment;
import adriancardenas.com.ehealth.Tabs.InformationGeneralFragment;
import adriancardenas.com.ehealth.Tabs.StepsEvolutionFragment;
import adriancardenas.com.ehealth.Tabs.WeightEvolutionFragment;
import adriancardenas.com.ehealth.Utils.Constants;
import adriancardenas.com.ehealth.Utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

import static adriancardenas.com.ehealth.Utils.Constants.WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT;

public class SmartBandPagerActivity extends AppCompatActivity {
    @BindView(R.id.main_content)
    CoordinatorLayout content;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    ViewPager viewPager;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    private PagerAdapter pagerAdapter;
    private int pagePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_band_pager);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagePosition = position;
                viewPager.setCurrentItem(pagePosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setupWithViewPager(viewPager);

        setupViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_smart_band_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download_data:
                if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT);
                    exportCsvDatabases();
                } else {
                    exportCsvDatabases();
                }

                break;
            case R.id.action_settings:
                Intent intent = new Intent(SmartBandPagerActivity.this, ConfigurationActivity.class);
                intent.putExtra(Constants.CONNECTED, true);
                startActivity(intent);
                break;
//            case R.id.action_share:
//                //TODO
////                Bitmap bitmap = Utils.getScreenShot(content);
////                File file = Utils.store(this, bitmap);
////                shareImage(file);
//                break;
        }
        return true;
    }

    private void exportCsvDatabases() {
        DatabaseOperations databaseOperations = DatabaseOperations.getInstance(this);
        databaseOperations.exportDB(Constants.DISTANCE_CSV, DatabaseMetadata.TABLES.DISTANCE);
        databaseOperations.exportDB(Constants.HEART_RATE_CSV, DatabaseMetadata.TABLES.HEART);
        databaseOperations.exportDB(Constants.WEIGHT_CSV, DatabaseMetadata.TABLES.WEIGHT);
        databaseOperations.exportDB(Constants.STEPS_CSV, DatabaseMetadata.TABLES.STEPS);
    }

    private void setupViewPager(ViewPager viewPager) {
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new InformationGeneralFragment(), getString(R.string.general));
        pagerAdapter.addFragment(new StepsEvolutionFragment(), getString(R.string.steps));
        pagerAdapter.addFragment(new WeightEvolutionFragment(), getString(R.string.weight));
        pagerAdapter.addFragment(new HeartRateEvolutionFragment(), getString(R.string.heart_rate));
        pagerAdapter.addFragment(new DistanceEvolutionFragment(), getString(R.string.Distance));
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(pagerAdapter);
    }

    private void shareImage(File file) {
        Uri uri = FileProvider.getUriForFile(this, "adriancardenas.com.ehealth", file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(Intent.createChooser(shareIntent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Utils.showSnackbar(content, "No App Available");
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }
}
