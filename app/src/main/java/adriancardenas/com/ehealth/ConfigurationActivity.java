package adriancardenas.com.ehealth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Date;

import adriancardenas.com.ehealth.Database.DatabaseOperations;
import adriancardenas.com.ehealth.Utils.Constants;
import adriancardenas.com.ehealth.Utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        ButterKnife.bind(this);
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

                    Intent intent = new Intent(ConfigurationActivity.this, ScanActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
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
}
