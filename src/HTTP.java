import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class HTTP {
	public static String doRequest(String request, String hostname, int port) throws UnknownHostException, IOException {
		Socket socket = new Socket(hostname, port);
		Scanner in = new Scanner(socket.getInputStream());
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		String response = "";
		out.println(request);
		response = in.nextLine();
		socket.close();
		in.close();
		out.close();
		return response;
	}
}
