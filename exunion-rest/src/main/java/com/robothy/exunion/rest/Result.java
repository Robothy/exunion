package com.robothy.exunion.rest;

/**
 * The rest API operate result
 * @param <T> standard data type
 */
public class Result <T> {

    private Status status;

    private String code;

    private String message;

    private T data;

    private Object origin;

    /**
     * Construct a result.
     * @param status operation success or failure flag.
     * @param code operation code, in most circumstances, it represents an error code.
     * @param message message from rest API server.
     * @param data the standard feedback of the operation, cross-exchange object.
     * @param origin the original object from specific exchange.
     */
    public Result(Status status, String code, String message, T data, Object origin){
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
        this.origin = origin;
    }

    public Result(T data){
        this(Status.OK, null, null, data, null);
    }

    public Result(T data, Object origin){
        this(Status.OK, null, null, data, origin);
    }

    public Result(String code, String message){
        this(Status.ERROR, code, message, null, null);
    }

    public Result(){}

    public T get(){
        return getData();
    }

    public void set(T data){
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getOrigin() {
        return origin;
    }

    public void setOrigin(Object origin) {
        this.origin = origin;
    }

    public boolean ok(){
        return this.status == Status.OK;
    }

    public enum Status {
        OK,
        ERROR
    }
}
