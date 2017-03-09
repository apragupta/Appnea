package in.apra.apraclock.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * SleepCycleModel encapsulates the functionality of computing propsoed sleep times based on SleepCycle research
 *
 * Created by Apra G. on 11/10/2016.
 */

public class SleepCycleModel {
    int CYCLE_MINS=90;
    int INITIAL_MINS=14;
    int num_cycles=6;

    public List<String> getWakeupTimeStr(Calendar fromTime)
    {
        List<Calendar> options=getWakeupTimes(fromTime);
        List<String> ret = new ArrayList<String>();

        AlarmModel m = new AlarmModel();
        for(Calendar c:options)
        {
            m.setAlarmTime(c);
            ret.add(m.getDateTimeAsString());
        }
        return ret;
    }

    /**
     * Returns a list of 6 proposed wakeup times for a given "go to bed" time.
     * @param fromTime
     * @return List of 6 proposed times
     */
    public List<Calendar> getWakeupTimes(Calendar fromTime)
    {
        List<Calendar> options = new ArrayList<Calendar>();
        //make a copy of fromTime to be a running cumulative time
        Calendar newTime=(Calendar )fromTime.clone();
        //add initial 14 minutes of falling sleep
        newTime.add(Calendar.MINUTE,INITIAL_MINS);
        // in a loop of 6, add 90 mins and add to the list
        for(int i=1;i<=num_cycles;i++)
        {
            newTime.add(Calendar.MINUTE,CYCLE_MINS);
            options.add(newTime);
            newTime=(Calendar )newTime.clone();
        }
        return options;
    }
}
