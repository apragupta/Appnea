package in.apra.apraclock;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import in.apra.apraclock.model.AlarmModel;

public class OpeningScreen extends AppCompatActivity implements SelectWakeTimeDlgFragment.SelectWakeTimeDialogListener {
    static String TAG=OpeningScreen.class.toString();
    Button btnSleepnow;
    Button BtnSleep;
    Button BtnCustomAlarm;
    TextView tvTimeDay;
    Switch swEnable;

    AlarmModel model= new AlarmModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);
        btnSleepnow = (Button) findViewById(R.id.btnSleepnow);
        BtnSleep = (Button) findViewById(R.id.BtnSleep);
        BtnCustomAlarm = (Button) findViewById(R.id.BtnCustomAlarm);
        tvTimeDay = (TextView) findViewById(R.id.tvTime);
        swEnable = (Switch)findViewById(R.id.swEnable);
        model.load(this);
        tvTimeDay.setText(model.getDateTimeAsString());
        swEnable.setChecked(model.isEnabled());
        final Context ctx=this;
        //connect a change on switch to this.onCheckedChanged(..)
        swEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.setAlarm(ctx,isChecked);
                model.save(ctx);
                showToast();
            }
        });

        if(!AlarmModel.validatePrefs(this))
        {
            startActivity(new Intent(this, SettingsActivity.class));
        }
    }

    public void sleepNow(View view) {
        Log.d(TAG  , "Sleep now clicked");
        SelectWakeTimeDlgFragment dlg = new SelectWakeTimeDlgFragment();
        dlg.show(getSupportFragmentManager(), "SelectWakeTimeDlgFragment");
    }
    @Override
    public void onWakeTimeSelected(Calendar chosen) {
        Log.d(TAG,"Selected: "+chosen.getTime());
        model.setAlarmTime(chosen);
        OnAlarmTimeSet();
    }

    public void sleepLater(View view) {
        Log.d(TAG  , "Sleep later clicked");
        Calendar now = Calendar.getInstance();
        TimePickerDialog dlg = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.d(TAG  , "Sleep at "+hourOfDay+":"+minute);
                SelectWakeTimeDlgFragment dlg = new SelectWakeTimeDlgFragment();
                dlg.setSleepTime(hourOfDay,minute);
                dlg.show(getSupportFragmentManager(), "SelectWakeTimeDlgFragment");
            }
        }, now.get(Calendar.HOUR), now.get(Calendar.MINUTE), false);
        dlg.setTitle("When will you sleep?");
        dlg.show();
    }
    public void iKnowWhenTowakeup(View view) {
        Log.d(TAG  , "I know when to wakeup clicked");
        TimePickerDialog tpDlg = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                model.setAlarmTime(hourOfDay,minute);
                OnAlarmTimeSet();
            }
        }, model.getHour(), model.getMinute(), false);
        tpDlg.setTitle("Set alarm time");
        tpDlg.show();
    }

    //to add a settings item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.acbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnPref:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return true;
    }

    //reuse code
    private void OnAlarmTimeSet() {
        model.save(this);
        tvTimeDay.setText(model.getDateTimeAsString());
        if(model.isEnabled()) {
            model.setAlarm(this, true);
            showToast();
        }
    }
    private void showToast()
    {
        if(model.isEnabled())
            Toast.makeText(this, "Set alarm for "+model.getDateTimeAsString(),Toast.LENGTH_LONG).show();
    }
}

