package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipJsonCompressor {

    private static boolean writeMessageToFile = false;
    private ObjectMapper objectMapper ;

    public GzipJsonCompressor() {
        objectMapper = new ObjectMapper();
    }

    public byte[] gzipCompress(Object objectToCompress) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = null;

            //byte[] messageBytes = mapper.writeValueAsBytes(o);
            gzip = new GZIPOutputStream(new Base64OutputStream(out));
            String messageJson = objectMapper.writeValueAsString(objectToCompress);
            if (writeMessageToFile)
                WriteToFile(messageJson);

            byte[] bytesToCompress = messageJson.getBytes("UTF-8");

            //temp decompression
            String parsedString = new String(bytesToCompress, "UTF-8").trim();
            //temp decompression

            gzip.write(bytesToCompress);
            gzip.close();

            byte[] result = out.toByteArray();
            out.close();
            return result;

        } catch (IOException e) { e.printStackTrace(); }

        return null;
    }

    private void WriteToFile(String data){
        File file = new File("sizedMessage.txt");

        try {
            if (file.createNewFile())
                System.out.println("File created: " + file.getName());
            else
                System.out.println("File already exists.");

            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            //convert string to byte array
            byte[] bytes = data.getBytes();
            //write byte array to file
            bos.write(bytes);
            bos.close();
            fos.close();
            System.out.print("Data written to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T>T gzipDecompress(byte[] compressedBytes, Class<T> classToReturn) {
        try {

            //Read and decompress the data
            ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(compressedBytes);
            Base64InputStream base64InputStream = new Base64InputStream(arrayInputStream);
            GZIPInputStream gzipInputStream = new GZIPInputStream(base64InputStream);

            BufferedReader br = new BufferedReader(new InputStreamReader(gzipInputStream, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            gzipInputStream.close();
            arrayInputStream.close();

            String parsedString = sb.toString();
            T result = objectMapper.readValue(parsedString, classToReturn);
            return result;

        } catch (Exception e) { e.printStackTrace(); }


        return null;

    }
}
