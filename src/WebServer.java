
import java.io.*;
import java.net.*;
import java.security.cert.CRL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public final class WebServer {
    public static void main(String args[]) throws Exception {
        //specify the port number
        int port = 8080;
        try {
            //create server socket
            ServerSocket sersoc = new ServerSocket(port);
            System.out.println("server connection" + port);
            while (true) {
                //accept connection at socket
                Socket socket = sersoc.accept();

                //set timeout for server ;close socket after the specified timeout
                sersoc.setSoTimeout(20000);
                //Construct an object to process the HTTP request message.
                HttpRequest request = new HttpRequest(socket);
                // Create a new thread to process the request

                Thread thread = new Thread(request);
                // Start the thread.
                thread.start();


            }
        } catch (Exception e) {
            System.out.println("Server timed out");
        }
    }
}

final class HttpRequest implements Runnable {
    final static String CRLF = "\r\n";
    Socket socket;

    //�Constructor
    public HttpRequest(Socket socket) throws Exception {
        this.socket = socket;
    }

    // Implement the run() method of the Runnable interface.
    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {

            processRequest();


        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void processRequest() throws Exception {

        String statusLine = null;
        String contentTypeLine = null;
        String contentLength = null;
        int len = 0;
        String entityBody = null;
        FileInputStream fis = null;
        String datetime = null;
        DateFormat dateFormat = new SimpleDateFormat("E,dd MMM yyyy HH:mm:ss z");
        Date curdate = new Date();
        //specify the path of the file in the server as shown
        String path = "";
        // Get a reference to the socket's input and output streams.
        InputStream is = socket.getInputStream();
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        // Get the request line of the HTTP request message.

        String requestLine = br.readLine();
        // Display the request line
        System.out.println();
        System.out.println(requestLine);
        // Get and display the header lines.
        String headerline = null;
        while ((headerline = br.readLine()).length() != 0) {
            System.out.println(headerline);
        }
        // Extract the filename from the request line.
        StringTokenizer token = new StringTokenizer(requestLine);
        token.nextToken();
        String filename1 = token.nextToken();

        if (filename1.length() > 1) {
            //add path to filename
            String filename = path + filename1;

            boolean fileExists = true;
            // Open the requested file.

            try {
                fis = new FileInputStream(filename);
                //length of the content being transferred
                len = (int) filename.length();

            } catch (FileNotFoundException e) {
                fileExists = false;

            }

            //if the file exists at server
            if (fileExists) {
                // Construct the response message.

                statusLine = "HTTP/1.1 200 Found " + CRLF;
                contentTypeLine = "Content-Type:" + contentType(filename) + CRLF;
                contentLength = "Content-Length: " + len + CRLF;
                datetime = "Date: " + dateFormat.format(curdate) + CRLF;


            } else {
                //if the file does not exist
                statusLine = "HTTP/1.1 404 Not Found" + CRLF;
                contentTypeLine = "no contents to be displayed\n" + CRLF;
                contentLength = "Content-Length: " + len + CRLF;
                entityBody = "<HTML>" + "<HEAD><TITLE>Not Found</TITLE></HEAD>" + "<BODY>Not Found</BODY></HTML>";
            }

            // Send the status line.
            os.writeUTF(statusLine);
            // Send the content type line.
            os.writeBytes(contentTypeLine);
            //Send content Length
            os.writeUTF(contentLength);
            os.writeBytes(datetime);
            // Send a blank line to indicate the end of the header lines.
            os.writeBytes(CRLF);

            // Send the entity body.
            if (fileExists) {
                sendBytes(fis, os);
                fis.close();

            } else {
                os.writeBytes(entityBody);
            }
        } else {
            //default page when no specific page is given in URL
            String file = "/index.html";
            String file1 = path + file;
            fis = new FileInputStream(file1);
            statusLine = "HTTP/1.1 200 Found ";
            contentTypeLine = "Content-Type:" + contentType(file1) + CRLF;
            os.writeUTF(statusLine);
            os.writeBytes(contentTypeLine);
            os.writeBytes(CRLF);
            sendBytes(fis, os);
            fis.close();
        }
        os.close();
        br.close();

        socket.close();

    }

    private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception {
        // Construct a 1K buffer to hold bytes on their way to the socket.
        byte[] buffer = new byte[1024];
        int bytes = 0;
        // Copy requested file into the socket's output stream
        while ((bytes = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
        }
        os.flush();

    }

    //type of files handled by server
    private static String contentType(String fileName) {

        if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        }
        if (fileName.endsWith(".txt")) {
            return "text/plain";
        }
        if (fileName.endsWith(".gif") || fileName.endsWith(".GIF")) {
            return "image/gif";
        }
        if (fileName.endsWith(".jpg")) {
            return "image/jpeg";
        }


        return "application/octet-stream";
    }


}