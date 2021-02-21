package com.robothy.exunion.huobi.trade.spot;

import com.robothy.exunion.core.trade.spot.SpotOrderDetails.Status;

public enum HuobiOrderStatus  {

    CLOSED(-1, Status.EXPIRED),

    PARTIALLY_CANCELLED(5, Status.CANCELED),

    FILLED(6, Status.FILLED),

    CANCELLED(7, Status.CANCELED),

    CANCELLING(10, Status.PENDING_CANCEL);

    private int code;

    private Status status;

    HuobiOrderStatus(int code, Status status){
        this.code = code;
        this.status = status;
    }

    public static HuobiOrderStatus of(int code){
        for(HuobiOrderStatus v : HuobiOrderStatus.values()){
            if(v.code == code) return v;
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
