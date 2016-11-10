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

import in.apra.apraclock.model.AlarmModel;

public class AlarmSetActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnTouchListener, TimePickerDialog.OnTimeSetListener {
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
        tvTimeDay.setOnTouchListener(this);
        swEnable.setChecked(model.isEnabled());
        swEnable.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        model.setAlarm(this,isChecked);
        model.save(this);
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
        if(model.isEnabled())
            model.setAlarm(this,true);
    }
}
