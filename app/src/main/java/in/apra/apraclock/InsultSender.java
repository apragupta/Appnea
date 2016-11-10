/**
 * Created by apra on 9/25/2016.
 */
package in.apra.apraclock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import in.apra.apraclock.model.GMailSender;


public class InsultSender extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //send the insult e-mail
        GMailSender sender = new GMailSender("apra@apra.in","use your actual password here");
        sender.sendMail("I need help with waking up",
                "Dear Mom,\n I am not able to deal with my sleepy self.\nPlease help me wake up!!",
                "apra@apra.in","gapra@tisb.ac.in");

        setResultCode(Activity.RESULT_OK);
    }

}
