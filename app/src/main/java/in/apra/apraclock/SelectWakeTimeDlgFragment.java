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
 * Created by Apra G on 2/12/2017.
 * https://developer.android.com/guide/topics/ui/dialogs.html
 */

public class SelectWakeTimeDlgFragment extends DialogFragment implements DialogInterface.OnClickListener  {
    static String TAG=SelectWakeTimeDlgFragment.class.toString();

    public interface SelectWakeTimeDialogListener {
        public void onWakeTimeSelected(Calendar time);
    }
    private SelectWakeTimeDialogListener mListener;

    List<Calendar> timeOptions;
    Calendar sleepTime;
    public SelectWakeTimeDlgFragment() {
        super();
        sleepTime=Calendar.getInstance(); //now
    }

    public void setSleepTime(int hourOfDay, int minute)
    {
        //use AlarmModel to find the right time: it may be tomorrow !
        AlarmModel m = new AlarmModel();
        m.setAlarmTime(hourOfDay,minute);
        sleepTime=m.getAlarmTime();
        Log.d(TAG,"Sleeping at "+sleepTime.getTime().toString());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        SleepCycleModel sleepCycleModel= new SleepCycleModel();
        timeOptions=sleepCycleModel.getWakeupTimes(sleepTime);
        //GUI build below
        List<String> options= sleepCycleModel.getWakeupTimeStr(sleepTime);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.wakeDlgTitle);
        builder.setItems(options.toArray(new String[options.size()]),this);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

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
