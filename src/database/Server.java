package src.database;

import br.unicamp.treplica.examples.ReplicatedMap;
import java.net.*;
import java.io.*;
import java.util.Map.Entry;


public class Server {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static int port;

    // agora teremos mais um arg que sera a porta do servidor
    public static void main(String[] args) throws Exception{

        ReplicatedMap<String, String> map = new ReplicatedMap<>(Integer.parseInt(args[0]),
                                            Integer.parseInt(args[1]), args[2]);
        port = Integer.parseInt(args[3]);
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        System.out.println("Client connected!");

        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        while(true){
            String command = in.readLine();
            if (command == null || command.length() == 0) {
                continue;
            }
            if ("q".equals(command)) {
                in.close();
                out.close();
                clientSocket.close();
                serverSocket.close();
                System.exit(0);
            } else if ('g' == command.charAt(0) && command.length() > 2) {
                String key = command.split(" ")[1];
                out.println(map.get(key));
            } else if ('p' == command.charAt(0) && command.length() > 2) {
                String key = command.split(" ")[1];
                String value = command.split(" ", 3)[2];
                map.put(key, value);
                out.println("Key added.");
            } else if ('r' == command.charAt(0) && command.length() > 2) {
                String key = command.split(" ")[1];
                map.remove(key);
                out.println("Key removed.");
            } else if ("c".equals(command)) {
                map.clear();
                out.println("All keys removed.");
            } else if ("k".equals(command)) {
                map.takeCheckpoint();
                out.println("Checkpoint taken.");
            } else if ("l".equals(command)) {
                for (Entry<String, String> entry : map.entrySet()) {
                    out.println(entry.getKey() + ": " + entry.getValue());
                }
            }
        }



        
    }


}