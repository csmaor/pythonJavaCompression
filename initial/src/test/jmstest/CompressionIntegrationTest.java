package jmstest;

import static org.junit.Assert.assertEquals;

import com.google.common.base.Stopwatch;
import hello.*;
import org.junit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import utils.AutoResetEvent;

import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class, Receiver.class, Sender.class, EventsListener.class})
@EnableJms
@Component
public class CompressionIntegrationTest {

    @Autowired
    Sender sender;

    @Autowired
    Receiver receiver;

    @Autowired
    EventsListener eventsListener;

    final AutoResetEvent resetEvent = new AutoResetEvent(false);

    Stopwatch stopwatch;

    @Before
    public void BeforeEachTest() {
        receiver.resetNumOfMsgsReceived();
        resetEvent.reset();
        eventsListener.setResetEvent(resetEvent);
    }

    @Test
    public void TestSendingSimpleMessage() {

        SizedMessage message = new SizedMessage(1);
        stopwatch = Stopwatch.createStarted();
        sender.SendMessage(message);
        WaitForResetEvent();
        stopwatch.stop();
        System.out.println(String.format("it took %d mili for the message to arrive", stopwatch.elapsed(TimeUnit.MILLISECONDS)));
        assertEquals(1, receiver.getNumOfMsgsReceived());
        receiver.resetNumOfMsgsReceived();
    }

    @Test
    public void TestSendingLargeMessage() {

        SizedMessage message = new SizedMessage(50);
        stopwatch = Stopwatch.createStarted();
        sender.SendMessage(message);
        WaitForResetEvent();
        stopwatch.stop();
        System.out.println(String.format("it took %d mili for the message to arrive", stopwatch.elapsed(TimeUnit.MILLISECONDS)));
        assertEquals(1, receiver.getNumOfMsgsReceived());
        receiver.resetNumOfMsgsReceived();
    }

    private void WaitForResetEvent() {
        try { resetEvent.waitOne(); }
        catch (InterruptedException e) { Assert.fail("No Message Received"); }
    }



}
