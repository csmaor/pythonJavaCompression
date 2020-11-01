package hello;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressedJsonMessageConverter implements MessageConverter {

    @Override
    public Message toMessage(Object o, Session session) throws JMSException, MessageConversionException {

        //ObjectMapper mapper = new ObjectMapper();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            //byte[] messageBytes = mapper.writeValueAsBytes(o);
            gzip = new GZIPOutputStream(new Base64OutputStream(out));
            gzip.write(new Gson().toJson(o).getBytes("UTF-8"));
            gzip.close();

        } catch (IOException e) {            e.printStackTrace();        }

        BytesMessage message = session.createBytesMessage();
        byte[] bytesToSend = out.toByteArray();
        message.writeBytes(bytesToSend);
        return message;
    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {

        ObjectMapper mapper = new ObjectMapper();
        BytesMessage bytesMessage = ((BytesMessage)message);
        SizedMessage result = null;

        //Read and decompress the data
        int arrLen = (int)bytesMessage.getBodyLength();
        byte[] readBuffer = new byte[arrLen];
        bytesMessage.readBytes(readBuffer);
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(readBuffer);
        GZIPInputStream inputStream = null;
        try {
            inputStream = new GZIPInputStream(arrayInputStream);
            int read = inputStream.read(readBuffer,0,readBuffer.length);
            inputStream.close();
            //Should hold the original (reconstructed) data
            byte[] bytesArray = Arrays.copyOf(readBuffer, read);
            // Decode the bytes into a String
            result = mapper.readValue (bytesArray, SizedMessage.class);

        } catch (Exception e) { e.printStackTrace(); }


        return result;
    }
}
