package in.apra.apraclock;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import in.apra.apraclock.model.GMailSender;

/**
 * Created by Apra G on 11/10/2016.
 */

@RunWith(AndroidJUnit4.class)
public class MailSenderTest {
    @Test
    public void testMailSend() throws Exception {
        // Context of the app under test.
        Context ctx = InstrumentationRegistry.getTargetContext();
        GMailSender sender = new GMailSender("apra@apra.in","some_password");
        Assert.assertTrue(sender.sendMail("I need help with waking up","Dear Mom,\n I wanted to wakeup at 800 am but have not been able to. Please help ! - Apra.",
                "apra@apra.in",
                "gapra@tisb.ac.in"));
    }
    @Test
    public void testCheckAuth() throws Exception {
        // Context of the app under test.
        Context ctx = InstrumentationRegistry.getTargetContext();
        {
            GMailSender sender = new GMailSender("apra@apra.in", "badpassword");
            Assert.assertFalse("bad pass", sender.checkAuth());
        }
        {
            GMailSender sender = new GMailSender("baduser", "badpassword");
            Assert.assertFalse("bad user and pass", sender.checkAuth());
        }
        {
            GMailSender sender = new GMailSender("apra@apra.in", "some_password");
            Assert.assertTrue("proper auth", sender.checkAuth());
        }
    }
}