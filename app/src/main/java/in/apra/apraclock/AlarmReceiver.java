/**
 * Receives callback from alarm manager when alarm triggers
 * Created by apra on 9/25/2016.
 */
package in.apra.apraclock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;


public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //register for the next alarm for sending insult
        setAlarm2MinsLater(context);

        //playAlarm(context);

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),AlarmService.class.getName());

        startWakefulService(context, (intent.setComponent(comp)));

        setResultCode(Activity.RESULT_OK);
    }

    private void setAlarm2MinsLater(Context context) {
        Intent myIntent = new Intent(context, InsultSender.class);
        PendingIntent p=PendingIntent.getBroadcast(context, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        long now=calendar.getTimeInMillis();
        alarmManager.set(AlarmManager.RTC_WAKEUP, now+2*60*1000, p);
        Log.d("AlarmRecvr","setting the insult alarm");

    }
}
