package adriancardenas.com.ehealth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import adriancardenas.com.ehealth.Database.DatabaseOperations;
import adriancardenas.com.ehealth.Utils.Constants;
import adriancardenas.com.ehealth.Utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddWeightActivity extends AppCompatActivity {
    @BindView(R.id.root_cell)
    ConstraintLayout cellBg;

    @BindView(R.id.weight_user_layout)
    TextInputLayout weightUserLayout;
    @BindView(R.id.weight_user)
    TextInputEditText weightUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weight);

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
                } else {
                    Utils.showSnackbar(cellBg, getString(R.string.introduce_weight));
                }
                break;
        }
        return true;
    }

    private void saveData() {
        String weight = weightUser.getText().toString();
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        DatabaseOperations databaseOperations = DatabaseOperations.getInstance(this);
        Intent resultIntent = new Intent();
        if(databaseOperations.insertWeight(fDate, weight)){
            setResult(Activity.RESULT_OK, resultIntent);
        }else{
            setResult(Activity.RESULT_CANCELED, resultIntent);
        }
        finish();
    }

    private boolean correctFields() {
        boolean correctField = true;

        if (weightUser.getText() == null || weightUser.getText().toString().equals("")) {
            weightUserLayout.setError(getString(R.string.introduce_weight));
            correctField = false;
        }
        return correctField;
    }
}
