package adriancardenas.com.ehealth;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;

import adriancardenas.com.ehealth.Database.DatabaseOperations;
import adriancardenas.com.ehealth.Utils.Constants;

/**
 * Created by Adrian on 19/06/2018.
 */

@RunWith(AndroidJUnit4.class)
public class ConfigurationActivityTest {
    private String mName = "Adri";
    @Rule
    public ActivityTestRule<ConfigurationActivity> mActivityRule = new ActivityTestRule<>(ConfigurationActivity.class);
    private String mSteps = "10000";
    private String mWeight = "75";
    private String mAge = "22";
    private String mHeight = "175";
    private SharedPreferences sharedPreferences;
    private DatabaseOperations databaseOperations;
    private String mHeartRate = "70";

    @Before
    public void setUp() {
        mActivityRule.getActivity();
        sharedPreferences = mActivityRule.getActivity().getSharedPreferences(Constants.LOCAL_APPLICATION_PATH, Context.MODE_PRIVATE);
        resetSharedPreferences();
        Context appContext = InstrumentationRegistry.getTargetContext();
        databaseOperations = DatabaseOperations.getInstance(appContext);
    }

    @Test
    public void checkWeightIsInsert() {
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        databaseOperations.insertWeight(fDate, mHeight);
        assertTrue(databaseOperations.getWeights().containsKey(fDate));
        assertEquals("Weight inserted", Float.parseFloat(mHeight), databaseOperations.getLastWeight());
    }

    @Test
    public void checkHeartRateIsInsert() {
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cDate);
        databaseOperations.insertHeartRate(fDate, mHeartRate);
        assertTrue(databaseOperations.getHeartRates().containsKey(fDate));
        assertEquals("HeartRate inserted", mHeartRate, String.valueOf(databaseOperations.getHeartRates().get(fDate)));
    }

    @Test
    public void checkStepsIsInsert() {
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        databaseOperations.insertWeight(fDate, mSteps);
        assertTrue(databaseOperations.getWeights().containsKey(fDate));
        assertEquals("Weight inserted", Float.parseFloat(mSteps), databaseOperations.getWeights().get(fDate));
    }

    private void resetSharedPreferences() {
        onView(withId(R.id.steps_daily_user)).perform(replaceText(""));
        onView(withId(R.id.weight_user)).perform(replaceText(""));
        onView(withId(R.id.age_user)).perform(replaceText(""));
        onView(withId(R.id.name_user)).perform(replaceText(""));
        onView(withId(R.id.weight_user)).perform(replaceText(""));
    }

    @Test
    public void test1checkStepsFieldIsWritten() {
        onView(withId(R.id.steps_daily_user)).perform(typeText(mSteps));
        closeSoftKeyboard();
        onView(withId(R.id.steps_daily_user)).check(matches(withText(mSteps)));
    }

    @Test
    public void test2checkNameFieldIsWritten() {
        onView(withId(R.id.name_user)).perform(typeText(mName));
        closeSoftKeyboard();
        onView(withId(R.id.name_user)).check(matches(withText(mName)));
    }

    @Test
    public void test3checkWeightFieldIsWritten() {
        onView(withId(R.id.weight_user)).perform(typeText(mWeight));
        closeSoftKeyboard();
        onView(withId(R.id.weight_user)).check(matches(withText(mWeight)));
    }

    @Test
    public void test4checkAgeFieldIsWritten() {
        onView(withId(R.id.age_user)).perform(typeText(mAge));
        closeSoftKeyboard();
        onView(withId(R.id.age_user)).check(matches(withText(mAge)));
    }

    @Test
    public void test5checkHeightFieldIsWritten() {
        onView(withId(R.id.height_user)).perform(typeText(mHeight));
        closeSoftKeyboard();
        onView(withId(R.id.height_user)).check(matches(withText(mHeight)));
    }

    @Test
    public void testAllFieldsCompleted() {
        onView(withId(R.id.name_user)).perform(typeText(mName));
        closeSoftKeyboard();
        onView(withId(R.id.steps_daily_user)).perform(typeText(mSteps));
        closeSoftKeyboard();
        onView(withId(R.id.weight_user)).perform(typeText(mWeight));
        closeSoftKeyboard();
        onView(withId(R.id.age_user)).perform(typeText(mAge));
        closeSoftKeyboard();
        onView(withId(R.id.height_user)).perform(typeText(mHeight));
        closeSoftKeyboard();
        onView(withId(R.id.button_next)).perform(click());
    }

    @Test
    public void testFieldSavesInSharedPreferences() {
        onView(withId(R.id.name_user)).perform(typeText(mName));
        closeSoftKeyboard();
        onView(withId(R.id.steps_daily_user)).perform(typeText(mSteps));
        closeSoftKeyboard();
        onView(withId(R.id.weight_user)).perform(typeText(mWeight));
        closeSoftKeyboard();
        onView(withId(R.id.age_user)).perform(typeText(mAge));
        closeSoftKeyboard();
        onView(withId(R.id.height_user)).perform(typeText(mHeight));
        closeSoftKeyboard();
        onView(withId(R.id.button_next)).perform(click());
        assertEquals("Error name not equals", mName, sharedPreferences.getString(Constants.NAME, ""));
        assertEquals("Error steps not equals", mSteps, sharedPreferences.getString(Constants.STEPS_GOAL, ""));
        assertEquals("Error age not equals", mAge, sharedPreferences.getString(Constants.AGE, ""));
        assertEquals("Error height not equals", mHeight, sharedPreferences.getString(Constants.HEIGHT, ""));
    }
}
