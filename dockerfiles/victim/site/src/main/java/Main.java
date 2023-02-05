import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.*;
import java.net.*;



public class Main {
    public static final Logger logger = LogManager.getLogger(Main.class);


    public static void main(String[] args) throws IOException {
        List<String> ips = new ArrayList<>();
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                ips.add(i.getHostAddress());
            }
        }

        String ip = ips.stream().filter(elIp -> elIp.startsWith("172.")).findFirst().get();

        System.out.println("Running on ip: " + ip);

        DataSource.setup();


        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);

        server.createContext("/login", new  LoginHandler()); // transforme ça end endpoint /login qui prend username password
        // qui vérifie en bdd maria db utiliser hikaricp maven


        // Un autre endpoint /accueil qui si /accueil/admin bah gg


        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        server.setExecutor(threadPoolExecutor);

        server.start();

        System.out.println(" Server started on port 8080");
    }



}
