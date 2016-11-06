package in.apra.apraclock;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import in.apra.apraclock.model.AlarmModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AlarmModelTest {
    AlarmModel m = new AlarmModel();

    @Test
    public void testDateTimeTextPM() throws Exception {
        Calendar now = Calendar.getInstance();
        m.setAlarmTime(17, 0);
        if (now.get(Calendar.HOUR_OF_DAY) > 17) {
            assertEquals("5:00 PM Tomorrow", m.getDateTimeAsString());
        } else {
            assertEquals("5:00 PM Today", m.getDateTimeAsString());
        }
    }

    @Test
    public void testDateTimeTextAM() throws Exception {
        Calendar now = Calendar.getInstance();
        m.setAlarmTime(5, 0);
        if (now.get(Calendar.HOUR_OF_DAY) > 5) {
            assertEquals("5:00 AM Tomorrow", m.getDateTimeAsString());
        } else {
            assertEquals("5:00 AM Today", m.getDateTimeAsString());
        }
    }
    @Test
    public void testLoadSave() throws Exception {
        // Context of the app under test.
        Context ctx = InstrumentationRegistry.getTargetContext();
        assertEquals("in.apra.apraclock", ctx.getPackageName());
        assertFalse("initially disabled",m.isEnabled());
        m.setAlarmTime(12,34);
        assertFalse("setting time does not change it",m.isEnabled());
        m.setAlarm(ctx,true);
        assertTrue("setting alarm enables it",m.isEnabled());
        m.save(ctx);

        {
            AlarmModel m2 = new AlarmModel();
            m2.load(ctx);
            assertEquals(m.getHour(), m2.getHour());
            assertEquals(m.getMinute(), m2.getMinute());
            assertEquals(m.isEnabled(), m2.isEnabled());
            assertEquals(m.getDateTimeAsString(), m2.getDateTimeAsString());
        }

        m.setAlarm(ctx,false);
        assertFalse("should be disabled now",m.isEnabled());

    }
}
