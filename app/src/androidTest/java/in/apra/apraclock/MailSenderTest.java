package in.apra.apraclock;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

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
        GMailSender sender = new GMailSender("apra@apra.in","fl0w3rs");
        sender.sendMail("I need help with waking up","Dear Mom,\n I wanted to wakeup at 800 am but have not been able to. Please help ! - Apra.",
                "apra@apra.in",
                "gapra@tisb.ac.in");
    }
}