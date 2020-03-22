package me.byTe.testing;

import me.byTe.networking.NetClient;

import java.util.Scanner;

public class ClientTest
{
    public static void main(String[] args)
    {
        try {
            /* Create client and connect to server */
            NetClient client = new NetClient("localhost", 1234);

            /* Read user-input and print server messages to the console */
            Scanner scanner = new Scanner(System.in);
            while(!client.isClosed()) {
                client.send(scanner.nextLine());
                System.out.println("Got from Server: " + client.receive() + "");
            }

            /* Cleanup the connection */
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
