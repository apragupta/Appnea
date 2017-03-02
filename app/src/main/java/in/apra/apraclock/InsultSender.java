/**
 * Created by apra on 9/25/2016.
 */
package in.apra.apraclock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import in.apra.apraclock.model.AlarmModel;
import in.apra.apraclock.model.GMailSender;


public class InsultSender extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //fetch values from shared prefs
        String user= AlarmModel.getUserName(context);
        String password= AlarmModel.getPassword(context);
        String dest_email= AlarmModel.getDestEmails(context);
        String emailContent= AlarmModel.getEmailContent(context);
        //send the insult e-mail
        GMailSender sender = new GMailSender(user,password);
        sender.execute("I need help with waking up",emailContent,
                user,dest_email);
        AlarmService.sendNotification(context,"Sent e-mail insult as you failed to wake up!");
        setResultCode(Activity.RESULT_OK);
    }
}
