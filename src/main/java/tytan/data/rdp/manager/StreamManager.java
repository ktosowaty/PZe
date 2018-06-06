package tytan.data.rdp.manager;

import tytan.data.rdp.specification.web_socket.MessageHandler;

import javax.websocket.*;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ClientEndpoint
public class StreamManager {

    private URI uri;
    private String clientId;
    private StreamDataListener streamDataListener;
    private Session userSession = null;
    private MessageHandler messageHandler;

    private StreamManager(URI uri, String clientId, StreamDataListener streamDataListener) {
        this.uri = uri;
        this.clientId = clientId;
        this.streamDataListener = streamDataListener;
        initialize();
    }

    public void initialize() {
        try {
            System.out.println("StreamManager uri: " + uri.toString());
            WebSocketContainer container = ContainerProvider
                    .getWebSocketContainer();
//            container.connectToServer(endpoint, new EndpointConfig(), uri);
            container.connectToServer(this, uri);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("StreamManager onOpen");
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("StreamManager onClose");
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null)
            this.messageHandler.handleMessage(message);
        streamDataListener.onStreamReceived(message);
    }


    public static String byteBuffer2String(ByteBuffer buf, Charset charset) {
        byte[] bytes;
        if (buf.hasArray()) {
            bytes = buf.array();
        } else {
            buf.rewind();
            bytes = new byte[buf.remaining()];
        }
        return new String(bytes, charset);
    }

    public static ByteBuffer string2ByteBuffer(String msg, Charset charset){
        return ByteBuffer.wrap(msg.getBytes(charset));
    }

    public static class Builder {

        private static URI uri;
        private static String clientId;
        private static StreamDataListener streamDataListener;

        public Builder setURI(URI endpointURI) {
            uri = endpointURI;
            return this;
        }

        public Builder setClientId(String id) {
            clientId = id;
            return this;
        }

        public Builder setStreamListener(StreamDataListener streamListener) {
            streamDataListener = streamListener;
            return this;
        }

        public StreamManager build() {
            return new StreamManager(uri, clientId, streamDataListener);
        }
    }

    public interface StreamDataListener {

        void onStreamReceived(String stream);

    }

}
