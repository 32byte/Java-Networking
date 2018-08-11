package NetWork;

import NetWork.NetClient.NetClient;
import NetWork.NetServer.NetServer;

import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        try {
            NetServer netServer = new NetServer(11111, 5);
            Thread serverThread = new Thread(netServer);
            serverThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
