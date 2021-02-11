package exunion.metaobjects;

/**
 * 订单状态
 *
 * @author robothy
 */
public class OrderStatus {


    /**
     * 新订单
     */
    public static final String NEW = "NEW";

    /**
     * 部分成交
     */
    public static final String PARTIALLY_FILLED = "PARTIALLY_FILLED";

    /**
     * 已成交
     */
    public static final String FILLED = "FILLED";

    /**
     * 已取消
     */
    public static final String CANCELED = "CANCELED";

    /**
     *
     */
    public static final String PENDING_CANCEL = "PENDING_CANCEL";

    /**
     * 已拒绝
     */
    public static final String REJECTED = "REJECTED";

    /**
     * 已过期
     */
    public static final String EXPIRED = "EXPIRED";


}
