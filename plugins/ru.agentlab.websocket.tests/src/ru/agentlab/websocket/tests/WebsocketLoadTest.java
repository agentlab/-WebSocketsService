/**
 *
 */
package ru.agentlab.websocket.tests;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.Test;

import ru.agentlab.websocket.client.ReverseClientSocket;

/**
 *
 * Websocket load test. Creates threads with websocket clients and sends messages.
 *
 */
public class WebsocketLoadTest {

    private static final int TIME_TO_SPAWN_USER = 100;
    private static final int TIMEOUT = 20;
    private static final String SERVER_ADDRESS = "ws://localhost:8080/reverse";
    private static final String FIRST_MESSAGE = "ABC";
    private static final int USERS_COUNT = 200;

    @Test
    public void wsLoadTest() throws Exception {
        WebSocketClient client = new WebSocketClient();
        client.start();

        ExecutorService executorService = Executors.newFixedThreadPool(USERS_COUNT);
        for (int userIndex = 0; userIndex < USERS_COUNT; userIndex++)
        {
            executorService.execute(new OneMessageClient(client));

            Thread.sleep(TIME_TO_SPAWN_USER);
        }

        executorService.shutdown();
        executorService.awaitTermination(TIMEOUT, TimeUnit.SECONDS);

        executorService = Executors.newFixedThreadPool(USERS_COUNT);
        for (int userIndex = 0; userIndex < USERS_COUNT; userIndex++)
        {
            executorService.execute(new ContiniousMessageClients(client));

            Thread.sleep(TIME_TO_SPAWN_USER);
        }

        executorService.shutdown();
    }

    private static class OneMessageClient
        implements Runnable {

        private WebSocketClient client;

        public OneMessageClient(WebSocketClient client) {
            this.client = client;
        }

        @Override
        public void run() {
            try
            {
                ReverseClientSocket socket = new ReverseClientSocket();
                URI echoUri = new URI(SERVER_ADDRESS);
                ClientUpgradeRequest request = new ClientUpgradeRequest();
                client.connect(socket, echoUri, request);

                socket.getLatch().await();
                socket.sendMessage(FIRST_MESSAGE);
            }
            catch (Throwable t)
            {
                t.printStackTrace();
            }
        }
    }

    private static class ContiniousMessageClients
        implements Runnable {

        private static final int MESSAGES_COUNT = 100;

        private WebSocketClient client;

        public ContiniousMessageClients(WebSocketClient client) {
            this.client = client;
        }

        @Override
        public void run() {
            try
            {
                ReverseClientSocket socket = new ReverseClientSocket();
                URI echoUri = new URI(SERVER_ADDRESS);
                ClientUpgradeRequest request = new ClientUpgradeRequest();
                client.connect(socket, echoUri, request);

                socket.getLatch().await();

                for (int messageIndex = 0; messageIndex < MESSAGES_COUNT; messageIndex++)
                {
                    socket.sendMessage(FIRST_MESSAGE + " (" + messageIndex + ')');
                }
            }
            catch (Throwable t)
            {
                t.printStackTrace();
            }
        }
    }
}
