package me.byTe.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
* Simple class around the java.net.Socket class
* Handles sending and receiving data in form of strings
* */
public class NetClient {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    /* Initiate class and try to connect to the ip */
    public NetClient(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    /* Send an object in form of a string*/
    public void send(Object object) {
        writer.println(object.toString());
    }

    /* Receive a string from the Server */
    public String receive() throws IOException {
        if(socket.isClosed()) return null;

        String message = reader.readLine();

        /* Close the connection client-side */
        if(message == null) close();
        return message;
    }

    /* Returns the state of the client */
    public boolean isClosed() {
        return socket.isClosed();
    }

    /* Close the reader/writer and the connection */
    public void close() throws IOException {
        if(socket.isClosed()) return;

        reader.close();
        writer.close();
        socket.close();
    }
}
