package in.apra.apraclock;

import org.junit.Test;

import in.apra.apraclock.tasks.RandomMathTask;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RandomTaskTest {
    @Test
    public void testRMT() throws Exception {
        RandomMathTask rmt = new RandomMathTask();

        //add
        rmt.init(10,20,0);
        assertTrue(rmt.checkAnswer(10+20));
        assertEquals("10 + 20",rmt.toString());

        //subs
        rmt.init(10,20,1);
        assertTrue(rmt.checkAnswer(10-20));
        assertEquals("10 - 20",rmt.toString());

        //mult
        rmt.init(10,20,2);
        assertTrue(rmt.checkAnswer(10*20));
        assertEquals("10 * 20",rmt.toString());

    }
}