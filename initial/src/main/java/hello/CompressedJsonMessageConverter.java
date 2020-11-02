package hello;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import utils.GzipJsonCompressor;

import javax.jms.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressedJsonMessageConverter implements MessageConverter {

    GzipJsonCompressor compressor;

    public CompressedJsonMessageConverter() {
        compressor = new GzipJsonCompressor();
    }
    
    @Override
    public Message toMessage(Object o, Session session) throws JMSException, MessageConversionException {

        byte[] compressedBytes = compressor.gzipCompress(o);
        BytesMessage message = session.createBytesMessage();
        message.writeBytes(compressedBytes);
        return message;
    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {

        BytesMessage bytesMessage = ((BytesMessage)message);

        int arrLen = (int)bytesMessage.getBodyLength();
        byte[] readBuffer = new byte[arrLen];
        bytesMessage.readBytes(readBuffer);
        SizedMessage result = compressor.gzipDecompress(readBuffer, SizedMessage.class);
        return result;
    }
}
