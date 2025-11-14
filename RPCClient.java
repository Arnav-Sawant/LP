import java.io.*;
import java.util.*;
import java.net.*;

public class RPCClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));
        int choice;
        System.out.println("Choose: 1(add), 2(subtract) :");
        Scanner sc = new Scanner(System.in);
        choice = sc.nextInt();

        switch (choice) {
            case 1:
                out.println("add");
                out.println(5);
                out.println(7);
                break;
            case 2:
                out.println("subtract");
                out.println(5);
                out.println(7);
                break;
            default:
                System.out.println("Invalid choice!");
                break;
        }

        String response = in.readLine();
        System.out.println("Result from server: " + response);

        socket.close();
    }
}
