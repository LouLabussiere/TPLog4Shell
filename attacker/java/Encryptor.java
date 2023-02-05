
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64.Encoder;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Hashtable;
import java.util.stream.Collectors;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */

/**
 *
 * @author machaonix
 */
public class Encryptor implements ObjectFactory{

    @Override
    public Object getObjectInstance(Object o, Name name, Context cntxt, Hashtable<?, ?> hshtbl) throws Exception {
        String fileName = "/app/toEncrypt.txt";
        String fileContent = "";
        String line = "";
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encriptedFile;
        try(BufferedReader bf = new BufferedReader(new FileReader(fileName))){
            while(line != null) {
                line = bf.readLine();
                fileContent += line;
                System.out.println("Coucou");
            }
            encriptedFile = digest.digest(fileContent.getBytes());
        }catch(IOException e){
            return null;
        }
        try(FileOutputStream fos = new FileOutputStream(fileName)){
            fos.write(encriptedFile);
        }catch(IOException e){
            return null;
        }
        return null;
    }

    
}
