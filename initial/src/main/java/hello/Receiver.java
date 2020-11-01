package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class Receiver {

    private int numberOfMessagesReceived;
    public int getNumOfMsgsReceived() {return numberOfMessagesReceived;}
    public void resetNumOfMsgsReceived() {numberOfMessagesReceived=0;}

    private ApplicationEventPublisher publisher;

    @Autowired
    public Receiver(ApplicationEventPublisher eventPublisher){
        publisher = eventPublisher;
    }

    @JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public void receiveMessage(SizedMessage message) {
        System.out.println("Received <" + message + ">");
        ++numberOfMessagesReceived;

        //System.out.println(String.format("Invoking event from thread: %s", Thread.currentThread().getId()));
        publisher.publishEvent(new MessageReceivedEvent(this, "MessageReceived", message));
    }


}