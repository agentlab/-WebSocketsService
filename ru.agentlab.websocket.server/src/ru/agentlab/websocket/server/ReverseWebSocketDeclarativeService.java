/**
 *
 */
package ru.agentlab.websocket.server;

import java.util.Hashtable;

import javax.servlet.Servlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 *
 * Instance of this class catches http server and enables websocket.
 *
 */
@Component(enabled = true, immediate = true)
public class ReverseWebSocketDeclarativeService
    extends WebSocketServlet {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Activate
    public void activate(BundleContext context) {
        //при активации компонента регистрируем сервлет на поднятом osgi hhtp сервере
        Hashtable props = new Hashtable();
        props.put("osgi.http.whiteboard.servlet.pattern", "/reverse");
        context.registerService(Servlet.class.getName(), this, props);
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(10000);
        // регистрируем сервлет сервера
        factory.register(ReverseWebSocket.class);
    }
}
