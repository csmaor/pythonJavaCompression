package jmstest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import hello.SizedMessage;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Test;
import utils.GzipJsonCompressor;

import javax.jms.BytesMessage;
import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static junit.framework.TestCase.failNotEquals;
import static org.junit.Assert.assertEquals;

public class CompressionUT {



    @Test
    public void TestGZIPCompression() {
        GzipJsonCompressor compressor = new GzipJsonCompressor();
        SizedMessage messageToCompress = new SizedMessage(1);
        byte[] compressedBytes = compressor.gzipCompress(messageToCompress);
        SizedMessage uncompressedMessage = compressor.gzipDecompress(compressedBytes, SizedMessage.class);

        assertEquals("Test Failed", messageToCompress, uncompressedMessage);
    }




}
