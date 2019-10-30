package WebSockets;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@WebSocket
public class WebSocketHandler {

    // Sessions of current users
    private static final Queue<Session> sessions = new ConcurrentLinkedDeque<>();
    private static final Logger log = LoggerFactory.getLogger(WebSocketHandler.class);

    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        log.info(session.getLocalAddress().getHostString() +" Sent: "+message);
        session.getRemote().sendString(message); // and send it back
    }

    public static void broadcast (String message) {

        // Transformer
        Gson gson = new Gson();

        // Send message to all sessions
        sessions.stream()
                .filter(Session::isOpen)
                .forEach(session -> {
                    try {
                        session.getRemote().sendString(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

}
