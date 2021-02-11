package exunion.metaobjects;

public class Error {

    /**
     * 错误代码
     */
    String code = null;

    /**
     * 错误代码
     */
    public String getCode() {
        return code;
    }

    /**
     * 错误代码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 错误信息
     */
    String message = null;

    /**
     * 错误信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 错误信息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 判断错误码是否为空
     *
     * @return <ul>
     * <li>true - 错误码为空，即无错误信息</li>
     * <li>false - 错误码非空，有错误信息</li>
     */
    public Boolean errorIsEmpty() {
        return (null == this.code || this.code.equals(""))
                && (null == this.message || this.code.equals(""));
    }
}
