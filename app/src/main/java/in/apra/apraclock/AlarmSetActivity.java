package in.apra.apraclock;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import in.apra.apraclock.model.AlarmModel;

public class AlarmSetActivity extends AppCompatActivity implements View.OnTouchListener,
        CompoundButton.OnCheckedChangeListener, TimePickerDialog.OnTimeSetListener {
    TextView tvTimeDay;
    Switch swEnable;
    AlarmModel model= new AlarmModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_set);
        tvTimeDay = (TextView) findViewById(R.id.tvTime);
        swEnable = (Switch)findViewById(R.id.swEnable);
        model.load(this);
        tvTimeDay.setText(model.getDateTimeAsString());
        swEnable.setChecked(model.isEnabled());

        //connect a touch event on tvTimeDay to this.OnTouch(..)
        tvTimeDay.setOnTouchListener(this);

        //connect a change on switch to this.onCheckedChanged(..)
        swEnable.setOnCheckedChangeListener(this);
    }
    void showToast()
    {
        if(model.isEnabled())
            Toast.makeText(this, "Set alarm for "+model.getDateTimeAsString(),Toast.LENGTH_LONG).show();
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        model.setAlarm(this,isChecked);
        model.save(this);
        showToast();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //user touched the TimeDay TV
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            TimePickerDialog tpDlg = new TimePickerDialog(this, this, model.getHour(), model.getMinute(), false);
            tpDlg.setTitle("Set alarm time");
            tpDlg.show();
            return true;
        }
        return false;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        model.setAlarmTime(hourOfDay,minute);
        model.save(this);
        tvTimeDay.setText(model.getDateTimeAsString());
        if(model.isEnabled()) {
            model.setAlarm(this, true);
            showToast();
        }
    }
}
