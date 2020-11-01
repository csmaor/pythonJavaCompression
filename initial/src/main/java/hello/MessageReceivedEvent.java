package hello;

import org.springframework.context.ApplicationEvent;

public class MessageReceivedEvent extends ApplicationEvent
{
    private static final long serialVersionUID = 1L;

    private String eventType;
    private SizedMessage message;

    //Constructor's first parameter must be source
    public MessageReceivedEvent(Object source, String eventType, SizedMessage message)
    {
        //Calling this super class constructor is necessary
        super(source);
        this.eventType = eventType;
        this.message = message;
    }

    public String getEventType() {
        return eventType;
    }

    public SizedMessage getMessage() {
        return message;
    }
}