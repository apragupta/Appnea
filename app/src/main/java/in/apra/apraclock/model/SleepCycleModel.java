package in.apra.apraclock.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Apra G. on 11/10/2016.
 */

public class SleepCycleModel {
    int CYCLE_MINS=90;
    int INITIAL_MINS=14;
    int num_cycles=6;

    public List<String> getWakeupTimes(Calendar fromTime)
    {
        List<String> options = new ArrayList<String>();
        Calendar newTime=(Calendar )fromTime.clone();

        newTime.add(Calendar.MINUTE,INITIAL_MINS);

        AlarmModel m = new AlarmModel();
        for(int i=1;i<=num_cycles;i++)
        {
            newTime.add(Calendar.MINUTE,CYCLE_MINS);
            m.setAlarmTime(newTime);
            options.add(m.getDateTimeAsString());
        }
        return options;
    }
}
