package in.apra.apraclock;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import in.apra.apraclock.model.AlarmModel;
import in.apra.apraclock.model.SleepCycleModel;

/**
 A DialogFragment for showing the wakeup time options
 * https://developer.android.com/guide/topics/ui/dialogs.html
 * Created by Apra G on 2/12/2017.
 *
 */

public class SelectWakeTimeDlgFragment extends DialogFragment implements DialogInterface.OnClickListener  {
    static String TAG=SelectWakeTimeDlgFragment.class.toString();

    /**
     * This interface is invoked when a selection is made
     * Activities using this dialg should implement this interface
     * to receive a callback on selection
     */
    public interface SelectWakeTimeDialogListener {
        public void onWakeTimeSelected(Calendar time);
    }
    private SelectWakeTimeDialogListener mListener;

    //time options shown in this dialog
    List<Calendar> timeOptions;
    //sleep time to compute the options
    Calendar sleepTime;

    /**
     * Constructor assumes "sleep now" hence uses current time
     */
    public SelectWakeTimeDlgFragment() {
        super();
        sleepTime=Calendar.getInstance(); //now
    }

    /**
     * set the sleep time for "sleep later" use case
     * @param hourOfDay to sleep
     * @param minute to sleep
     */
    public void setSleepTime(int hourOfDay, int minute)
    {
        //use AlarmModel to find the right time: it may be tomorrow !
        AlarmModel m = new AlarmModel();
        m.setAlarmTime(hourOfDay,minute);
        sleepTime=m.getAlarmTime();
        Log.d(TAG,"Sleeping at "+sleepTime.getTime().toString());
    }

    /**
     * Builds the options dialog and returns to its caller
     * @param savedInstanceState
     * @return new Dialog built
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //create a new SleepCycleModel to compute wakeup options
        SleepCycleModel sleepCycleModel= new SleepCycleModel();
        timeOptions=sleepCycleModel.getWakeupTimes(sleepTime);

        //Get list of wakeup times as string
        List<String> options= sleepCycleModel.getWakeupTimeStr(sleepTime);
        //make a new builder for building our dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.wakeDlgTitle);
        //set the list of options
        builder.setItems(options.toArray(new String[options.size()]),this);
        //set that nothing happens on cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        return builder.create();
    }

    /**
     * Called only on OK button press,
     * invokes the SelectWakeTimeDialogListener callback with chosen wakeup time
     * @param dialog
     * @param which
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        Calendar chosen=timeOptions.get(which);
        Log.d(TAG,"Selected: "+chosen.getTime());
        mListener.onWakeTimeSelected(chosen);
    }

    //to be able to pass events back to activity: h
    // https://developer.android.com/guide/topics/ui/dialogs.html#PassingEvents
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SelectWakeTimeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement SelectWakeTimeDialogListener");
        }
    }
}
