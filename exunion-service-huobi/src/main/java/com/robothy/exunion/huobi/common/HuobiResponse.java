package com.robothy.exunion.huobi.common;

import com.google.api.client.util.Key;

public class HuobiResponse {

    @Key(Keys.STATUS)
    private String status;

    @Key(Keys.ERR_CODE)
    private String errCode;

    @Key(Keys.ERR_MSG)
    private String errMsg;

    @Key(Keys.DATA)
    private Object data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public interface Keys {
        String STATUS = "status";
        String ERR_CODE = "err-code";
        String ERR_MSG = "err-msg";
        String DATA = "data";
    }

    public interface Status {
        String ERROR = "error";
        String OK = "ok";
    }

}
