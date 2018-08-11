package NetWork.NetClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class NetClient
{
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public NetClient(String connect_to_ip, int port) throws IOException
    {
        socket = new Socket(connect_to_ip, port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream(), true);
    }

    public void send(String string) {
        try {
            printWriter.println(string);
        } catch (Exception e) {
        }
    }

    public String receive()
    {
        try {
            return bufferedReader.readLine();
        }catch (SocketException e) {
            System.err.println("Server closed connection already!");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        try {
            bufferedReader.close();
            printWriter.close();
            socket.close();
        } catch (Exception ignored) {
        }
    }
}
