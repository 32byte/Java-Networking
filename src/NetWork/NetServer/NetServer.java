package NetWork.NetServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class NetServer implements Runnable
{
    private int maxConnections;
    private int currentConnections;
    private ServerSocket serverSocket;
    private ArrayList<Connection> connections;

    public NetServer(int port, int maxConnections) throws IOException
    {
        this.maxConnections = maxConnections;
        serverSocket = new ServerSocket(port);
        connections = new ArrayList<>();
        System.out.println("Server started!");
    }

    public void run()
    {
        System.out.println("Server running");
        while(!serverSocket.isClosed() && !Thread.interrupted() && currentConnections < maxConnections)
        {
            Connection connection = new Connection();
            if (connection.getConnection()) {
                if (! Thread.interrupted()) {
                    System.out.println("Adding first connection! IP:" + connection.getSocket().getInetAddress().getHostName());
                    connections.add(connection);
                    currentConnections++;
                }
            }
        }

    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public class Connection
    {
        private Socket socket;
        private PrintWriter printWriter;
        private BufferedReader bufferedReader;

        public boolean getConnection()
        {
            try {
                socket = serverSocket.accept();
                printWriter = new PrintWriter(socket.getOutputStream(), true);
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        public void send(String string)
        {
            printWriter.println(string);
        }

        public String receive()
        {
            try {
                return bufferedReader.readLine();
            }catch (SocketException e) {
                System.err.println("Client closed connection already!");
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void close() {
            try {
                socket.close();
                printWriter.close();
                bufferedReader.close();
            } catch (Exception e) {
            }
        }

        public Socket getSocket() {
            return socket;
        }
    }
}
