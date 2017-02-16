/**
 *
 */
package ru.agentlab.websocket.client;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 *
 * Client socket. Configures websocket
 *
 */
@WebSocket
public class ReverseClientSocket {
    private Session session;
    private CountDownLatch latch = new CountDownLatch(1);

    @OnWebSocketMessage
    public void onText(Session session, String message) throws IOException {
        System.out.println("Message received from server:" + message);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Connected to server");
        this.session = session;
        latch.countDown();
    }

    /**
     *
     * Sends message to server.
     *
     * @param str - message to send. Can't be <code>null</code>.
     */
    public void sendMessage(String str) {
        try
        {
            session.getRemote().sendString(str);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     * Returns latch.
     *
     * @return latch. Can't return <code>null</code>.
     */
    public CountDownLatch getLatch() {
        return latch;
    }

}
