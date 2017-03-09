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
 * AlarmModel represents the state of a single alarm:
 * alarm time and if it is enabled or disabled
 * It also implements all the functionality relates to
 * -setting alarms
 * -core logic of converting users selection to a future alarm time
 * -creating user displayable text for alarm time
 * -storing values in shared preferences
 * -validating preferences
 *
 * Created by Apra G. on 11/6/2016.
 */

public class AlarmModel {

    static final int TODAY = 1;
    static final int TOMORROW = 2;

    //Current state of this alarm
    private Calendar alarmTime;
    private boolean isEnabled;

    /**
     * constructor assumes the alarm is for current time and disabled
     */

    public AlarmModel() {
        this.alarmTime = Calendar.getInstance();
        isEnabled = false;
    }

    /**
     * setter and getter for alarm time
     * @param time
     */
    public void setAlarmTime(Calendar time) {
        alarmTime = time;
    }
    public Calendar getAlarmTime() {
        return alarmTime;
    }

    /**
     * Keep it private as this class decides when to enable it
     * @param enabled
     */
    private void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Used to change the date and time of current alarm after user selects on GUI
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

        //if that time is in past, add 24 hours to it to make it tomorrow
        if (now > calendar.getTimeInMillis()) {
            calendar.setTimeInMillis(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000);
        }
        setAlarmTime(calendar);
    }

    /**
     * Used to know the day (today or tomorrow) for this alarm
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

    /**
     * Generates a user displayable string for an alarm
     * @return alarm time as a string
     */
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
     * save the alarm time and its status to shared preferences
     * @param ctx
     */
    public void save(Context ctx) {
        //start editing the shared preferences for this app.
        SharedPreferences.Editor ed = ctx.getSharedPreferences("ALARM_PREFS", MODE_PRIVATE).edit();
        ed.putBoolean("ALARM_ENABLED", isEnabled);
        ed.putLong("ALARM_TS", alarmTime.getTimeInMillis());
        ed.commit();

    }

    /**
     * load the alarm time and status from shared preferences
     * @param ctx
     */
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
     * Sets the alarm with Alarm manager class in android SDK
     * @param ctx the Activity in whose context this function is run
     * @param isSet true: set the alarm; false: clear the alarm
     */
    public void setAlarm(Context ctx, boolean isSet) {
        //create a intent for the receiver of this alarm (callback)
        Intent myIntent = new Intent(ctx, AlarmReceiver.class);

        //Since the alarm occurs at a different time we create a Pending intent, wrapping the actual intent
        PendingIntent pi = PendingIntent.getBroadcast(ctx, 0, myIntent, 0);

        //Get an instance of Alarm manager service
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ctx.ALARM_SERVICE);

        if (isSet) {
            //set the new alarm
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pi);
        } else {
            //clear a alarm matching this pending intent
            alarmManager.cancel(pi);
        }
        setEnabled(isSet);
    }

    //region Preferences that user can change
    public static String getPrefVal(Context ctx, String key) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(key, "");
    }

    /**
     * Validates if the user settings are correct
     * @param context The activity under whose context this function runs.
     * @return true: valid, false: invlaid
     * @throws Exception the text of exception describes whats wrong
     */
    public static boolean validatePrefs(Context context) throws Exception {
        //fetch values from preferences using private functions
        String user = getUserName(context);
        String password = getPassword(context);
        String dest_email = getDestEmails(context);
        String emailContent = getEmailContent(context).trim();

        //check password length has to be more than 0
        if (password.length() == 0) throw new Exception("Password not supplied");

        //users gmail address and destination address must match the e-mail address pattern
        if (!Patterns.EMAIL_ADDRESS.matcher(user).matches())
            throw new Exception("User name is not a valid e-mail address");
        if (!Patterns.EMAIL_ADDRESS.matcher(dest_email).matches())
            throw new Exception("Destination e-mail address is invalid");

        //the EMail content must have at least 10 characters
        if (emailContent.length() < 10) throw new Exception("E-Mail message content is too short");

        //if we did nto throw any exceptions, we are good, return true
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
