/**
 * Created by apra on 9/25/2016.
 */
package in.apra.apraclock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;


public class InsultSender extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //send the insult e-mail or face book posts
        Log.d("Insult Sender", "Sending the message to your family and friends");
        setResultCode(Activity.RESULT_OK);
    }

}
