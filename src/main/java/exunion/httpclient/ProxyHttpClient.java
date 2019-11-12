package exunion.httpclient;

import java.util.ArrayList;
import java.util.List;

/**
 * 走代理的 HTTP #exunion.httpclient.Client
 */
public class ProxyHttpClient {

    private static List<Client> clients = new ArrayList<>();

    private static int idx = 0;

    static {
        List<ProxyPool.ProxyServer> servers = ProxyPool.getProxyPool();
        servers.forEach(e -> clients.add(new Client(e.getIpAddress(), e.getPort())));
    }

    /**
     * 判断 ProxyHttpClient 是否包含走代理的 Client
     * @return true 表示存在 ProxyHttpClient
     */
    public static boolean isExists(){
        return clients.size() > 0;
    }

    /**
     * 获取一个走代理的 Client，所有走代理的 Client 轮流返回
     * @return 一个走代理的 #exunion.httpclient.Client
     */
    public static Client next(){
        if(idx == clients.size()){
            idx = 0;
        }
        return clients.get(idx++);
    }
}
