package <%= package %>.web.response;

/**
 * @author <%= user %>
 * @date 2019-08-08 10:11:08
 */
public class Result<T> {
    /**
     * 错误码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 具体内容
     */
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 请求成功返回
     *
     * @param object
     * @return
     */
    public static Result success(Object object) {
        Result msg = new Result();
        msg.setCode(200);
        msg.setMsg("请求成功");
        msg.setData(object);
        return msg;
    }

    public static Result success() {
        return success(null);
    }

    public static Result error(Integer code, String resultmsg) {
        Result msg = new Result();
        msg.setCode(code);
        msg.setMsg(resultmsg);
        return msg;
    }

}
