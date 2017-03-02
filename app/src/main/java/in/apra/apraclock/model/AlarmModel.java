package in.apra.apraclock.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Patterns;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.apra.apraclock.AlarmReceiver;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Apra G. on 11/6/2016.
 */

public class AlarmModel {

    static final int TODAY = 1;
    static final int TOMORROW = 2;

    //my properties
    private Calendar alarmTime;
    private boolean isEnabled;

    public AlarmModel() {
        this.alarmTime = Calendar.getInstance();
        isEnabled = false;
    }

    /**
     * Used to change the data and time of this alarm
     *
     * @param hour
     * @param minute
     */
    public void setAlarmTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        //get current time
        long now = calendar.getTimeInMillis();

        //now setup for alarm time
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (now > calendar.getTimeInMillis()) {
            calendar.setTimeInMillis(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000);
        }
        setAlarmTime(calendar);
    }

    public void setAlarmTime(Calendar time) {
        alarmTime = time;
    }

    public Calendar getAlarmTime() {
        return alarmTime;
    }

    /**
     * Used to know the day (today or tomorrow) for this alarm
     *
     * @return TODAY or TOMORROW
     */
    private int getAlarmDay() {
        //Date now
        Calendar dateNow = Calendar.getInstance();
        setToBeginningOfDay(dateNow);

        if (isEnabled == false) {
            // The alarm may have been used few days ago, lets set it in current context
            setAlarmTime(alarmTime.get(Calendar.HOUR_OF_DAY), alarmTime.get(Calendar.MINUTE));
        }
        //Date of alarm
        Calendar dateThen = (Calendar) alarmTime.clone();
        setToBeginningOfDay(dateThen);

        if (dateThen.compareTo(dateNow) > 0)
            return TOMORROW;
        else
            return TODAY;
    }

    private void setToBeginningOfDay(Calendar dateNow) {
        dateNow.set(Calendar.HOUR_OF_DAY, 0);
        dateNow.set(Calendar.MINUTE, 0);
        dateNow.set(Calendar.SECOND, 0);
        dateNow.set(Calendar.MILLISECOND, 0);
    }

    public int getHour() {
        return alarmTime.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        return alarmTime.get(Calendar.MINUTE);
    }

    public String getDateTimeAsString() {
        StringBuffer sb = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");

        sb.append(sdf.format(alarmTime.getTime()))
                .append(" ");

        if (getAlarmDay() == TODAY)
            sb.append("Today");
        else
            sb.append("Tomorrow");

        return sb.toString();
    }

    /**
     * Keep it private
     *
     * @param enabled
     */
    private void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void save(Context ctx) {
        //start editing the shared preferences for this app.
        SharedPreferences.Editor ed = ctx.getSharedPreferences("ALARM_PREFS", MODE_PRIVATE).edit();
        ed.putBoolean("ALARM_ENABLED", isEnabled);
        ed.putLong("ALARM_TS", alarmTime.getTimeInMillis());
        ed.commit();

    }

    public void load(Context ctx) {
        SharedPreferences ed = ctx.getSharedPreferences("ALARM_PREFS", MODE_PRIVATE);

        Calendar c = Calendar.getInstance(); //now
        long one_hour_from_now = c.getTimeInMillis() + 60 * 60 * 1000;//an hour later

        isEnabled = ed.getBoolean("ALARM_ENABLED", false);

        long ts = ed.getLong("ALARM_TS", one_hour_from_now);


        c.setTimeInMillis(ts);

        if (isEnabled) {
            alarmTime = c;
        } else {
            setAlarmTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
        }
    }

    /**
     * @param ctx
     * @param isSet true: set the alarm; false: clear it
     */
    public void setAlarm(Context ctx, boolean isSet) {
        Intent myIntent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(ctx, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ctx.ALARM_SERVICE);
        if (isSet) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pi);
        } else {
            alarmManager.cancel(pi);
        }
        setEnabled(isSet);
    }

    //region Preferences that user can change
    public static String getPrefVal(Context ctx, String key) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(key, "");
    }

    public static boolean validatePrefs(Context context) throws Exception {
        String user = getUserName(context);
        String password = getPassword(context);
        String dest_email = getDestEmails(context);
        String emailContent = getEmailContent(context).trim();

        if (password.length() == 0) throw new Exception("Password not supplied");
        if (!Patterns.EMAIL_ADDRESS.matcher(user).matches())
            throw new Exception("User name is not a valid e-mail address");
        if (!Patterns.EMAIL_ADDRESS.matcher(dest_email).matches())
            throw new Exception("Destination e-mail address is invalid");
        if (emailContent.length() < 10) throw new Exception("E-Mail message content is too short");

        return true;
    }

    public static String getPassword(Context context) {
        return getPrefVal(context, "password_pref");
    }

    public static String getUserName(Context context) {
        return getPrefVal(context, "username_pref");
    }

    public static String getDestEmails(Context context) {
        return getPrefVal(context, "dest_email_pref");
    }

    public static String getEmailContent(Context context) {
        return getPrefVal(context, "email_content_pref");
    }
    //endregion
}
