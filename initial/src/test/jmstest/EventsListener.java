package jmstest;

import hello.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import utils.AutoResetEvent;

@Component
public class EventsListener implements ApplicationListener<MessageReceivedEvent> {

    private AutoResetEvent resetEvent;
    public EventsListener()
    {

    }

    public void onApplicationEvent(MessageReceivedEvent messageReceivedEvent) {
        System.out.println(String.format("message received event invoked in UT in thread: %s", Thread.currentThread().getId()));
        resetEvent.set();
    }

    public void setResetEvent(AutoResetEvent resetEvent) {
        this.resetEvent = resetEvent;
    }
}