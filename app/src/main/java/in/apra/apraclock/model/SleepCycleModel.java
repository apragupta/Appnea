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
    public List<Calendar> getWakeupTimes(Calendar fromTime)
    {
        List<Calendar> options = new ArrayList<Calendar>();
        Calendar newTime=(Calendar )fromTime.clone();

        newTime.add(Calendar.MINUTE,INITIAL_MINS);
        for(int i=1;i<=num_cycles;i++)
        {
            newTime.add(Calendar.MINUTE,CYCLE_MINS);
            options.add(newTime);
            newTime=(Calendar )newTime.clone();
        }
        return options;
    }
}
