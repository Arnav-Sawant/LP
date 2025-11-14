import java.io.*;
import java.net.*;

public class RPCServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("RPC Server started, waiting for client...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected!");

            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String methodName = in.readLine();

            if ("add".equals(methodName)) {
                int a = Integer.parseInt(in.readLine());
                int b = Integer.parseInt(in.readLine());
                int result = add(a, b);
                out.println(result);

            } else if ("subtract".equals(methodName)) {
                int a = Integer.parseInt(in.readLine());
                int b = Integer.parseInt(in.readLine());
                int result = subtract(a, b);
                out.println(result);

            } else {
                out.println("ERROR: Unknown method");
            }
            clientSocket.close();
        }
    }

    private static int add(int a, int b) {
        return a + b;
    }
    private static int subtract(int a, int b) {
        return a - b;
    }
}