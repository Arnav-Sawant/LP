import java.io.*;
import java.net.*;

public class NTPClient {
    public static void main(String[] args) throws IOException {
        String serverAddress = "localhost";
        int port = 1234;

        // T1 = client send time
        long T1 = System.currentTimeMillis();

        Socket socket = new Socket(serverAddress, port);
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // T4 = client receive time (after getting response)
        long T2 = in.readLong(); // server receive time
        long T3 = in.readLong(); // server send time
        long T4 = System.currentTimeMillis();

        // Calculate offset and delay
        long delay = (T4 - T1) - (T3 - T2);
        double offset = ((T2 - T1) + (T3 - T4)) / 2.0;

        System.out.println("T1 (Client Send): " + T1);
        System.out.println("T2 (Server Receive): " + T2);
        System.out.println("T3 (Server Send): " + T3);
        System.out.println("T4 (Client Receive): " + T4);

        System.out.println("\nRound-trip delay = " + delay + " ms");
        System.out.println("Clock offset = " + offset + " ms");

        System.out.println("\nAdjusted client time: " + (T4 + offset));

        socket.close();
    }
}
