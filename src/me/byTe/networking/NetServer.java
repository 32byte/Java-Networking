package me.byTe.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
* A class around the java.net.ServerSocket
* Handles connections form clients and sending/receiving data
*
* Needs to be passed to a thread
* */
public abstract class NetServer implements Runnable {
    private ServerSocket serverSocket;
    private ArrayList<Connection> connections;
    private ArrayList<Thread> connectionThreads;

    /* Initialize connection array and start the Server */
    public NetServer(int port) throws IOException {
        connections = new ArrayList<>();
        connectionThreads = new ArrayList<>();
        serverSocket = new ServerSocket(port);
    }

    /* Manages new connections */
    public void run() {
        /* Wait for new connections*/
        while(!serverSocket.isClosed() && !Thread.interrupted()) {
            Connection connection = new Connection();
            if (connection.connectionAccepted()) {
                connections.add(connection);

                /* Create a new thread to receive messages */
                Thread connectionThread = new Thread(connection);
                connectionThreads.add(connectionThread);
                connectionThread.start();
            }
        }
    }

    /*
    * This is called when the server gets a message from a connection.
    * Override this function to manage incoming messages.
     * */
    protected abstract void onMessage(Connection connection, String message);

    /* Close all the connections */
    public void stop() throws IOException {
        connectionThreads.forEach(Thread::interrupt);
        for (Connection connection : connections)
            connection.close();
        System.out.println("Closed all connections");
        serverSocket.close();
    }

    /*
    * Return the connections array.
    * This can be used to loop over all the connections and
    * manage sending/receiving data.
    * */
    public ArrayList<Connection> getConnections() {
        return connections;
    }

    /*
    * Class to handle the sending/receiving data for connections.
    * */
    public class Connection implements Runnable
    {
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;

        /* Returns false server is already closed */
        public boolean connectionAccepted() {
            try {
                socket = serverSocket.accept();
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        /* Try to receive messages from clients */
        public void run() {
            while(!socket.isClosed() && !Thread.interrupted()) {
                try {
                    /* Pass the message to the server */
                    onMessage(this, receive());
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        /* Send an object in form of a string*/
        public void send(Object object) {
            writer.println(object.toString());
        }

        /* Receive a string from the ClientConnection */
        public String receive() throws IOException{
            try {
                return reader.readLine();
            } catch (IOException e) {
                close();
            }
            return "Client connection closed!";
        }

        /* Close the reader/writer and the connection */
        public void close() throws IOException {
            writer.close();
            reader.close();
            socket.close();
        }
    }
}
