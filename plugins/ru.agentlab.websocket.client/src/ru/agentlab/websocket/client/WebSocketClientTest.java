/**
 *
 */
package ru.agentlab.websocket.client;

import java.net.URI;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

/**
 *
 * Websocket client test.
 *
 * Server reverses and returns sended message to client.
 *
 */
public class WebSocketClientTest {

    private static final String SERVER_ADDRESS = "ws://localhost:8080/reverse";
    private static final String FIRST_MESSAGE = "ABC";
    private static final String SECOND_MESSAGE = "long message";

    public static void main(String[] args) {
        //создается вебсокет клиент
        WebSocketClient client = new WebSocketClient();
        try
        {
            //создаем сокет, который зарегистрируется в вебсокет клиенте
            ReverseClientSocket socket = new ReverseClientSocket();
            client.start();
            URI echoUri = new URI(SERVER_ADDRESS);
            //создаем и отправляем запрос на соединение клиента с сервером
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(socket, echoUri, request);

            //ждем, пока клиент найдет сервер
            socket.getLatch().await();
            //отправляем первое сообщение
            socket.sendMessage(FIRST_MESSAGE);
            //отправляем второе сообзение
            socket.sendMessage(SECOND_MESSAGE);

            //ожидаем 10 секунд
            Thread.sleep(10000l);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        finally
        {
            try
            {
                client.stop();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
