package com.pan.society.Common;

import com.alibaba.fastjson.JSON;

import java.util.LinkedHashMap;

/**
 * @author maqch
 * @date 2019/4/11 17:22
 */
public class JsonResponse extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = -3957696416833580484L;
    public static final int SUCCESS = 1;
    public static final int FAILED = 0;
    public static final int NOTFINISHED = 2;
    public static final String STATUS = "status";
    public static final String MESSAGE = "msg";
    public static final String DATA = "data";

    public JsonResponse() {
    }

    public JsonResponse(int flag, String msg) {
        this.put("status", flag);
        this.put("msg", msg);
    }

    public void failed() {
        this.put("status", 0);
    }

    public void failed(String msg) {
        this.put("status", 0);
        this.put("msg", msg);
    }

    public void success() {
        this.put("status", 1);
    }

    public void success(String msg) {
        this.put("status", 1);
        this.put("msg", msg);
    }

    public void success(String key, Object value) {
        this.put("status", 1);
        this.put(key, value);
    }

    public void success(String msg, String key, Object value) {
        this.put("status", 1);
        this.put("msg", msg);
        this.put(key, value);
    }

    public void setStatus(int status) {
        this.put("status", status);
    }

    public void setMessage(String msg) {
        this.put("msg", msg);
    }

    public boolean isfailed() {
        return this.get("status") != null && (Integer)this.get("status") == 0;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}
