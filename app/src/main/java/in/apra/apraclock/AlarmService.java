/**
 * Service launches the notification with ringing sound
 * Created by apra on 9/25/2016.
 */
package in.apra.apraclock;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import in.apra.apraclock.model.AlarmModel;

public class AlarmService extends IntentService {
    public AlarmService() {
        super("Alarm Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        sendNotification(this,"Wake Up! Tap here to start solving math puzzles");
    }


    static void sendNotification(Context ctx, String msg) {

        Log.d("AlarmService", "Preparing to send notification...: " + msg);

        Intent intent = new Intent(ctx, RandomTaskActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                intent, 0);


        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                ctx).setContentTitle("Alarm").setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        alamNotificationBuilder.setContentIntent(contentIntent);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        alamNotificationBuilder.setSound(alarmUri, AudioManager.STREAM_ALARM);

        NotificationManager alarmNotificationManager = (NotificationManager) ctx
                .getSystemService(Context.NOTIFICATION_SERVICE);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");

        //since it is served, disable it
        AlarmModel m = new AlarmModel();
        m.setAlarm(ctx,false);
        m.save(ctx);
    }
}
