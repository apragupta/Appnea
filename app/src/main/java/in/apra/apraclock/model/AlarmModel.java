package in.apra.apraclock.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.apra.apraclock.AlarmReceiver;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Apra G. on 11/6/2016.
 */

public class AlarmModel {

    public static final String ALARM_ENABLED = "ALARM_ENABLED";
    public static final String ALARM_TS="ALARM_TS";
    public static final String ALARM_PREFS = "ALARM_PREFS";
    static final SimpleDateFormat sdf= new SimpleDateFormat("h:mm a");

    Calendar alarmTime=Calendar.getInstance();

    boolean isEnabled;
    enum AlarmDay{
        TODAY("Today"),
        TOMORROW("Tomorrow");
        String tag;
        AlarmDay(String tag){this.tag=tag;}
    }

    /**
     * Used to change the data and time of this alarm
     * @param hour
     * @param minute
     */
    public void setAlarmTime(int hour, int minute)
    {
        Calendar calendar = Calendar.getInstance();

        //get current time
        long now= calendar.getTimeInMillis();
        //now setup for alarm time
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        if(now>calendar.getTimeInMillis())
        {
            calendar.setTimeInMillis(calendar.getTimeInMillis()+24*60*60*1000);
        }
        alarmTime=calendar;
    }

    /**
     * Used to know the day (today or tomorrow) for this alarm
     * @return
     */
    AlarmDay getAlarmDay()
    {
        //Date now
        Calendar dateNow = Calendar.getInstance();
        clearHHMMSS(dateNow);

        if(!isEnabled)
        {
            // The alarm may have been used in past, lets set it in current context
            setAlarmTime(alarmTime.get(Calendar.HOUR_OF_DAY),alarmTime.get(Calendar.MINUTE));
        }


        //Date of alarm
        Calendar dateThen = (Calendar) alarmTime.clone();
        clearHHMMSS(dateThen);
        if(dateThen.compareTo(dateNow)>0)
            return AlarmDay.TOMORROW;
        else
            return AlarmDay.TODAY;
    }

    private void clearHHMMSS(Calendar dateNow) {
        dateNow.set(Calendar.HOUR_OF_DAY,0);
        dateNow.set(Calendar.MINUTE,0);
        dateNow.set(Calendar.SECOND,0);
        dateNow.set(Calendar.MILLISECOND,0);
    }
    public int getHour(){
        return alarmTime.get(Calendar.HOUR_OF_DAY);
    }
    public int getMinute(){
        return alarmTime.get(Calendar.MINUTE);
    }
    public String getDateTimeAsString(){
        StringBuffer sb = new StringBuffer();
        sb.append(sdf.format(alarmTime.getTime()))
                .append(" ")
                .append(getAlarmDay().tag);
        return sb.toString();
    }

    /**
     * Keep it private
     * @param enabled
     */
    private void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void save(Context ctx)
    {
        SharedPreferences.Editor ed= ctx.getSharedPreferences(ALARM_PREFS,MODE_PRIVATE).edit();
        ed.putBoolean(ALARM_ENABLED,isEnabled);
        ed.putLong(ALARM_TS,alarmTime.getTimeInMillis());
        ed.commit();

    }
    public void load(Context ctx)
    {
        SharedPreferences ed= ctx.getSharedPreferences(ALARM_PREFS,MODE_PRIVATE);
        isEnabled=ed.getBoolean(ALARM_ENABLED,false);
        Calendar c=Calendar.getInstance(); //now
        long ts=ed.getLong(ALARM_TS,c.getTimeInMillis()+60*60*1000); //an hour later
        c.setTimeInMillis(ts);
        if(isEnabled) {
            alarmTime = c;
        }else{
            setAlarmTime(c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE));
        }
    }

    /**
     *
     * @param ctx
     * @param bSet true: set the alarm, false: clear it
     */
    public void setAlarm(Context ctx, boolean bSet)
    {
        Intent myIntent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pi=PendingIntent.getBroadcast(ctx, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ctx.ALARM_SERVICE);
        if(bSet){
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pi);
        }
        else {
            alarmManager.cancel(pi);
        }
        setEnabled(bSet);
    }
}
