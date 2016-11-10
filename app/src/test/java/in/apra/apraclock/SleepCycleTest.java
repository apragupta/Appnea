package in.apra.apraclock;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;

import in.apra.apraclock.model.SleepCycleModel;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


/**
 * Created by Apra G. on 11/10/2016.
 */
public class SleepCycleTest {
    @Test
    public void testRMT() throws Exception {
        SleepCycleModel m = new SleepCycleModel();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,20);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);

        List<String> ops=m.getWakeupTimes(c);
        assertTrue(ops.get(0).startsWith("9:44 PM To"));
        assertTrue(ops.get(1).startsWith("11:14 PM To"));
        assertTrue(ops.get(2).startsWith("12:44 AM To"));
        assertTrue(ops.get(3).startsWith("2:14 AM To"));
        assertTrue(ops.get(4).startsWith("3:44 AM To"));
        assertTrue(ops.get(5).startsWith("5:14 AM To"));


    }
}
