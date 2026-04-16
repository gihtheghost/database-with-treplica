package src.database;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Client {

    private String host;
    private int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    //inicializa com ip e porta do servidor
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    //cria o sockcet
    public void connect() throws IOException {
        this.socket = new Socket(this.host, this.port);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public boolean put(String key, String value){
        try {
            out.println("p" + " " + key + " " + value);
            String response = in.readLine();
            return "Key added.".equals(response);
        } catch (IOException e) {
            return false;
        }
    }

    public String get(String key){
        try {
            out.println("g" + " " + key);
            String response = in.readLine();
            return response;
        } catch (IOException e) {
            return null;
        }
    }

    public String remove(String key){
        try {
            out.println("r" + " " + key);
            String response = in.readLine();
            return response;
        } catch (IOException e) {
            return null;
        }
    }

    public String clear(){
        try {
            out.println("c");
            String response = in.readLine();
            return response;
        } catch (IOException e) {
            return null;
        }
    }

    public String checkpoint(){
        try {
            out.println("k");
            String response = in.readLine();
            return response;
        } catch (IOException e) {
            return null;
        }
    }

    public List<String> list(){
        try {
            List<String> response = new ArrayList<>();
            out.println("l");
            String line;
            while((line = in.readLine()) != null) {
                if (line.equals("END_OF_LIST"))
                    break;
                response.add(line);
            }
            return response;
        } catch (IOException e) {
            return null;
        }
    }
    
    public static void main(String[] args) throws IOException {

        Client c = new Client("localhost", 6666);
        c.connect();
        System.out.println(c.get("banana"));
        c.put("banana", "4");
        System.out.println(c.get("banana"));

    }

}

