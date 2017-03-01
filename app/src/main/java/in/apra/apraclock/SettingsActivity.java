package in.apra.apraclock;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import in.apra.apraclock.model.AlarmModel;
import in.apra.apraclock.model.GMailSender;

/**
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    static String TAG = SettingsActivity.class.toString();
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            preference.setSummary(stringValue);
            return true;
        }
    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_email);
        bindPreferenceSummaryToValue(findPreference("username_pref"));
        bindPreferenceSummaryToValue(findPreference("dest_email_pref"));
        bindPreferenceSummaryToValue(findPreference("email_content_pref"));
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                try{
                    AlarmModel.validatePrefs(this);
                    checkAuthUI();
                }
                catch(Exception ex)
                {
                    Toast.makeText(this,ex.getMessage() ,Toast.LENGTH_LONG).show();
                }
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkAuthUI() {
        final ProgressDialog dialog = new ProgressDialog(this);
        final Activity activity=this;
        //override GMailSender to show progress bar
        GMailSender checker= new GMailSender(AlarmModel.getUserName(this),AlarmModel.getPassword(this)) {
            @Override
            protected void onPreExecute(){
                dialog.setTitle("Validating...");
                dialog.setMessage("Checking access to google account...");
                dialog.show();
            }
            @Override
            protected Boolean doInBackground(String... params) {
                Log.e(TAG,"Checking access to google account...");
                return checkAuth();
            }
            @Override
            protected void onPostExecute(Boolean result){
                dialog.dismiss();
                if(result) {
                    activity.finish();
                    Toast.makeText(activity,"Validated GMail Credentials" ,Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(activity,"Could not validate GMail credentials" ,Toast.LENGTH_LONG).show();
                }
            }
        };
        checker.execute();
    }
}
