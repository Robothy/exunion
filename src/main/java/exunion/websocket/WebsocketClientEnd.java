package exunion.websocket;

import exunion.metaobjects.Depth;
import exunion.metaobjects.Ticker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Websocket 客户端。
 */
public abstract class WebsocketClientEnd extends WebSocketClient {

    private Logger logger = LogManager.getLogger(WebsocketClientEnd.class);

    /**
     * 构造方法
     * @param serverUri uri字符串
     * @throws URISyntaxException 当uri字符串语法不正确时抛出此异常
     */
    public WebsocketClientEnd(String serverUri) throws URISyntaxException {
        super(new URI(serverUri));
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        logger.info("Websocket 连接 {} 已打开", uri);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        logger.info("Websocket 连接 {} 已关闭。", uri);
    }

    @Override
    public void onError(Exception ex) {
        logger.error("WebsocketClientEnd 出现错误。", ex);
    }

    public abstract DeliveryBoy<Depth> subscribeDepth(String symbol);

    public abstract void onDepth(String json);

    public abstract DeliveryBoy<Ticker> subscribeTicker(String symbol);

    public abstract void onTicker(String json);

}
