/**
 *
 */
package ru.agentlab.websocket.server;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

/**
 *
 * Server websocket. Returnes reversed message to user.
 *
 */
public class ReverseWebSocket
    extends WebSocketAdapter {

    @Override
    public void onWebSocketConnect(Session session) {
        System.out.println("Connected: " + session.getRemoteAddress().getHostString());
        super.onWebSocketConnect(session);
    }

    @Override
    public void onWebSocketText(String message) {
        System.out.println("Message received: " + message);
        try
        {
            if (getSession() != null && getSession().isOpen())
            {
                //переворачиваем принятое сообщение
                String response = new StringBuilder(message).reverse().toString();
                //отправляем в ответ
                getSession().getRemote().sendString(response);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        System.out.println("Closed: " + getSession().getRemoteAddress().getHostString());
    }

}
