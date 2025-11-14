import java.io.*;
import java.net.*;
import java.util.*;

public class NTPServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("NTP Server started on port 1234...");

        while (true) {
            Socket socket = serverSocket.accept();
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // T2 = time server received request
            long T2 = System.currentTimeMillis();

            // simulate small processing delay (for demonstration)
            try { Thread.sleep(10); } catch (InterruptedException e) {}

            // T3 = time server sends reply
            long T3 = System.currentTimeMillis();

            // Send T2 and T3 back to client
            out.writeLong(T2);
            out.writeLong(T3);

            socket.close();
        }
    }
}
