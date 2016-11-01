/**
 * Created by apra on 9/25/2016.
 */
package in.apra.apraclock;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmService extends IntentService {
    @Override
    public void onCreate() {
        Log.d("AlarmService", "onCreate()");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d("AlarmService", "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d("AlarmService", "onStart()");
        super.onStart(intent, startId);
    }

    public AlarmService() {
        super("Alarm Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        sendNotification("Wake Up! Wake Up!");
    }


    private void sendNotification(String msg) {

                                   
        Log.d("AlarmService", "Preparing to send notification...: " + msg);


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, AlarmActivity.class), 0);

        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Alarm").setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);


        alamNotificationBuilder.setContentIntent(contentIntent);

        NotificationManager alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");
    }
}
