package adriancardenas.com.ehealth;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import adriancardenas.com.ehealth.Adapters.PagerAdapter;
import adriancardenas.com.ehealth.Tabs.InformationGeneralFragment;
import adriancardenas.com.ehealth.Tabs.StepsEvolutionFragment;
import adriancardenas.com.ehealth.Tabs.WeightEvolutionFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SmartBandPagerActivity extends AppCompatActivity {
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


    private void setupViewPager(ViewPager viewPager) {
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new StepsEvolutionFragment(), getString(R.string.steps_evolution));
        pagerAdapter.addFragment(new InformationGeneralFragment(), getString(R.string.general_information));
        pagerAdapter.addFragment(new WeightEvolutionFragment(), getString(R.string.weight_evolution));
        viewPager.setAdapter(pagerAdapter);
    }
}
