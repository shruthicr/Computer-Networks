8

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;

//class for POST operation of client
public class WebClientPost {
    public static void main(String[] args) throws Exception {
        Socket socket;
        //specify the port number and hostname
        String hostname = "localhost";
        int port = 8080;
        //construct the urlencoded query string to server
        String query = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode("shruthi", "UTF-8");
        query += "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode("oaklanding", "UTF-8");
        //create socket and establish connection
        socket = new Socket(hostname, port);
        //Specify the filename that is requested from server as a program argument(in IntelliJ IDEA or Eclipse IDE)
        String path = args[0];
        //open outputstream
        OutputStreamWriter os = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        BufferedWriter wr = new BufferedWriter(os);
        //request file from server
        wr.write("POST " + path + " HTTP/1.1\r\n");
        wr.write("Content-Type: application/x-www-form-urlencoded\r\n");
        wr.write("Host:" + InetAddress.getLocalHost() + "\r\n");
        wr.write("QUERY:" + query + "\r\n");//post the query to server
        //wr.write("Content-Length: " + query.length() + "\r\n");//length of the query string
        wr.write("\r\n");

        wr.flush();
        //read the contents of requested file
        BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
        wr.close();
        rd.close();
    }
}




