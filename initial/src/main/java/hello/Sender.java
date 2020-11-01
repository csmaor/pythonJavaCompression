package hello;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender implements ApplicationContextAware {

    private ApplicationContext appContext;

    public void SendMessage(SizedMessage message){
        JmsTemplate jmsTemplate = appContext.getBean(JmsTemplate.class);

        // Send a message with a POJO - the template reuse the message converter
        System.out.println(String.format("Sending  %s", message));
        jmsTemplate.convertAndSend("mailbox", message);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    };
}
