package me.byTe.testing;

import me.byTe.networking.NetServer;
import java.io.IOException;

/*
* Example class for how to use the NetServer.
* */
public class EchoServer extends NetServer
{
    /* Start the server on port 1234 */
    public EchoServer() throws IOException {
        super(1234);
        System.out.println("Server started..");
    }

    /* Broadcast the message to all the other connections */
    protected void onMessage(Connection connection, String message) {
        System.out.println("Got message: " + message + "");

        /* Stop the server if a client send "stop" */
        if(message.equals("stop")) {
            System.out.println("Stopping server..");
            try {
                stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /* Since this is an echo server just send the same message back */
        getConnections().forEach(con -> {
            /* The check is here to avoid sending the message back to the same connection */
             con.send(message);
        });
    }

    /* Main function to start the EchoServer */
    public static void main(String[] args) {
        try {
            /* Create new instance of this test-class */
            EchoServer server = new EchoServer();

            /* Start the server */
            Thread thread = new Thread(server);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
