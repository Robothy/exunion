package exunion.websocket;

/**
 * 送报员，作为信息传递类。
 *
 */
public class DeliveryBoy<T> {

    private T message;

    /**
     * 当有新信息时调用此方法
     * @param handler 处理者
     */
    public void onMessage(MessageHandler<T> handler){
        handler.handle(message);
    }

    /**
     * 处理者接口，处理逻辑应实现此接口
     * @param <T> 处理的信息类型
     */
    interface MessageHandler<T> {
        void handle(T t);
    }

}