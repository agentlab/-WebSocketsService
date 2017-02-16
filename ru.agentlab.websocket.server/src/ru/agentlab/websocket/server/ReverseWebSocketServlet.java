/**
 *
 */
package ru.agentlab.websocket.server;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 *
 * Websocket servlet. Registeres socket.
 *
 */
public class ReverseWebSocketServlet
    extends WebSocketServlet {

    @Override
    public void configure(WebSocketServletFactory factory) {
        //регистрируем сервлет сервера
        factory.register(ReverseWebSocket.class);
    }

}
