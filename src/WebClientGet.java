

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;


/**
 * Created by shruthi on 27-10-2015.
 */
//class for GET operation of client
public class WebClientGet {
    public static void main(String[] args) throws Exception {
        Socket socket;
        //specify the port number and hostname
        String hostname = "localhost";
        int port = 8080;
        //create socket
        socket = new Socket(hostname, port);
        //open input stream
        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
        //Specify the filename that is requested from server as a program argument(in IntelliJ IDEA or Eclipse IDE)
        String path = args[0];
        //open outputstream
        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
        //request for the file
        wr.write("GET" + " " + path + " " + "HTTP/1.1\r\n");
        wr.write("Host:" + InetAddress.getLocalHost() + "\r\n");
//            wr.write("User-Agent:"+"Mozilla/5.0 (Windows NT 6.1; WOW64) Chrome/46.0.2490.80 Safari/537.36"+"\r\n");
//            wr.write("Accept-Encoding:"+"gzip, deflate, sdch"+"\r\n");
//            wr.write("Accept-Language:"+"en-GB, en-US;q=0.8,en;q=0.6"+"\r\n");
        wr.write("\r\n");

        wr.flush();
        //read the contents of file
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);

        }
        //close the input and outputstream
        wr.close();
        br.close();

    }
}






