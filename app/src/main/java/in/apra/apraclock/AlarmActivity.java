/**
 * Created by apra on 9/25/2016.
 */
package in.apra.apraclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

 public class AlarmActivity extends AppCompatActivity {
     public static final String ALARM_ENABLED = "ALARM_ENABLED";
     public static final String ALARM_HH="ALARM_HH";
     public static final String ALARM_MM="ALARM_MM";
     public static final String ALARM_PREFS = "ALARM_PREFS";

     private TimePicker timePicker;
     private TextView tvMessage;
     private ToggleButton toggleButton;
     public static Ringtone ringtone=null;
     private SimpleDateFormat sdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timePicker= (TimePicker) findViewById(R.id.timePicker);
        tvMessage =(TextView)findViewById(R.id.tvMessage);
        toggleButton=(ToggleButton)findViewById(R.id.toggleButton);
        sdf= new SimpleDateFormat("EEE, dd MMM yyyy");

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                getFutureAlarmTimeMillis(hourOfDay,minute);
            }
        });
        //reload last values
        reloadLastValues();

    }

     private void reloadLastValues() {
         SharedPreferences ed= getSharedPreferences(ALARM_PREFS,MODE_PRIVATE);
         toggleButton.setChecked(ed.getBoolean(ALARM_ENABLED,false));
         timePicker.setHour(ed.getInt(ALARM_HH,timePicker.getHour()));
         timePicker.setMinute(ed.getInt(ALARM_MM,timePicker.getMinute()));

     }

     public void onAlarmToggle(View view)
    {
        Intent myIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        boolean enabled=((ToggleButton) view).isChecked();
        if (enabled) {
            long timeInMillis = getFutureAlarmTimeMillis(timePicker.getCurrentHour(),timePicker.getCurrentMinute());

            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);

        } else {
            alarmManager.cancel(pendingIntent);
            stopPlaying();
            Log.d("AlarmActivity", "Alarm Off");
        }
        savePrefs();
    }

     private long getFutureAlarmTimeMillis(int selHr, int selMin) {
         Calendar calendar = Calendar.getInstance();
         long now=calendar.getTimeInMillis();
         calendar.set(Calendar.HOUR_OF_DAY, selHr);
         calendar.set(Calendar.MINUTE, selMin);
         calendar.set(Calendar.SECOND,0);
         calendar.set(Calendar.MILLISECOND,0);
         if(now>calendar.getTimeInMillis())
         {
             //add a day
             calendar.setTimeInMillis(calendar.getTimeInMillis()+24*60*60*1000);
         }
         Log.d("AlarmActivity", "Alarm On "+calendar.getTime().toString());
         tvMessage.setText("Ring Alarm On "+sdf.format(calendar.getTime())+" at");
         return calendar.getTimeInMillis();
     }

     private void stopPlaying() {
         if(ringtone!=null)
         {
             Log.d("AlarmActivity", "stop playing");
             ringtone.stop();
             ringtone=null;
         }
     }

     @Override
     protected void onDestroy() {
         stopPlaying();
         super.onDestroy();
     }

     private void setAlarmText(String s) {
         tvMessage.setText(s);
     }

     private void savePrefs() {
         SharedPreferences.Editor ed= getSharedPreferences(ALARM_PREFS,MODE_PRIVATE).edit();
         ed.putBoolean(ALARM_ENABLED,toggleButton.isChecked());
         ed.putInt(ALARM_HH,timePicker.getHour());
         ed.putInt(ALARM_MM,timePicker.getMinute());
         ed.commit();
     }

     public static void playAlarm(Context context)
     {
         Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
         if (alarmUri == null) {
             alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
         }
         ringtone = RingtoneManager.getRingtone(context, alarmUri);
         Log.d("AlarmActivity", "Playing Ring tone");
         ringtone.play();

         // also clear the flag
         SharedPreferences.Editor ed= context.getSharedPreferences(ALARM_PREFS,MODE_PRIVATE).edit();
         ed.putBoolean(ALARM_ENABLED,false);
         ed.commit();
     }
 }
