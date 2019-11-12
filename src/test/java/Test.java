import exunion.exchange.*;
import exunion.metaobjects.Depth;
import exunion.websocket.DeliveryBoy;

import java.math.BigDecimal;

public class Test {

    public static void main(String[] args){
        //new FCoinExchange("dbbac156cc6f4d0c8bf38eee65c4d020", "4f0bc75e05da45be8f0de1317dd7933f", false).getOrder("ETH_USDT", "9jEtXlhsx6jCrhUAFYtWiWbNQNt8Ob6uCxdGE_tJC_g=");

        BitZv2Exchange ex = new BitZv2Exchange("25ba25d0cea8831e8199e8febdd308e9", "pSP6K9JDPDKq82z6rElt4tnL1MHxRG8KYNSJqVGe9ikxLo6x2rGEOYT2kEYM99oY", true);

        ex.getOrder("BZ_BTC", "786083486");
        ex.getAccount();

        HuobiExchange huobiExchange = new HuobiExchange("","", true);


        DeliveryBoy<Depth> deliveryBoy = huobiExchange.subscribeDepth("btc");

        deliveryBoy.onMessage(System.out::println);

        //System.out.println(ex.getDepth("PNT_BTC"));
        //System.out.println(ex.getAccount().getBalances());
        //ex.order("SELL", "BZ_BTC", new BigDecimal("10"), new BigDecimal("0.01"));
        //ex.getAccount();
        //ex.getOrder("BZ_BTC", "737464060");

        //new FCoinExchange("dbbac156cc6f4d0c8bf38eee65c4d020", "4f0bc75e05da45be8f0de1317dd7933f", false)
          //      .order("SELL", "FT_USDT", new BigDecimal("3"), new BigDecimal("0.13"));

        //HadaxExchange he = new HadaxExchange("123", "31", true);
        //System.out.println(he.getDepth("PNT_BTC"));
    }

}
