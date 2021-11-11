package cn.weihu.base.result;

import cn.weihu.kol.util.GsonUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class ResultBean<T> implements Serializable {
    public static final  String SUCCESS          = "0";
    public static final  String FAIL             = "-1";
    private static final long   serialVersionUID = 1L;
    private              String msg              = "success";
    private              String code             = SUCCESS;
    private              T      data;

    public ResultBean() {
        super();
    }

    public ResultBean(T data) {
        super();
        this.data = data;
    }

    public ResultBean(ErrorCode errorcode) {
        this.code = errorcode.getCode();
        this.msg = errorcode.getMsg();
    }

    public ResultBean(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public ResultBean(String code, String msg,T data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResultBean<Map<String, String>> getResultBean(String key, String value) {
        ResultBean<Map<String, String>> rb   = new ResultBean<>();
        Map<String, String>             data = new HashMap<>();
        data.put(key, value);
        rb.setData(data);
        return rb;
    }

    public static ResultBean<Object> getResultBean(String message) {
        ResultBean<Object> rb = new ResultBean<>();
        if(message.equals("success")) {
            rb.setCode(SUCCESS);
        } else {
            rb.setCode(FAIL);
        }
        rb.setMsg(message.replace("error:", ""));
        return rb;
    }

    @Override
    public String toString() {
        return GsonUtils.gson.toJson(this);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", this.getCode());
        map.put("msg", this.getMsg());
        map.put("data", this.getData());
        return map;
    }

}