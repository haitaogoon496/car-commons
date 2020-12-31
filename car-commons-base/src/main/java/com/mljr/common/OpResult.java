package com.mljr.common;

/**
 * @description: 用于Ajax传输使用
 * @Date : 上午11:49 2017/9/29
 * @Author : 石冬冬-Heil Hitler(dongdong.shi@mljr.com)
 */
public class OpResult<T> {

    private final static int SUCCESS_CODE = 1;
    private final static int FAILURE_CODE = 0;
    private final static String SUCCESS_MSG = "操作成功";
    private final static String FAILURE_MSG = "操作失败";
    /**
     * 是否执行成功
     */
    private boolean success;
    /**
     * 包体
     */
    private T data;
    /**
     * 执行操作code
     */
    private int code;

    public OpResult() {
    }

    public OpResult(boolean success, T data) {
        this.success = success;
        this.data = data;
        this.code = SUCCESS_CODE;
    }

    public OpResult(boolean success, T data, int code) {
        this(success,data);
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 静态方法，返回执行成功
     * @return
     */
    public static OpResult suc(){
        return suc(SUCCESS_MSG);
    }

    /**
     * 静态方法，返回执行成功
     * @param t
     * @param <T>
     * @return
     */
    public static <T> OpResult suc(T t){
        OpResult opResult = new OpResult(Boolean.TRUE,t);
        return opResult;
    }

    /**
     * 静态方法，返回执行失败
     * @return
     */
    public static OpResult fail(){
        return fail(FAILURE_MSG);
    }

    /**
     * 静态方法，返回执行失败
     * @param t
     * @param <T>
     * @return
     */
    public static <T> OpResult fail(T t){
        OpResult opResult = new OpResult(Boolean.FALSE,t);
        opResult.setCode(FAILURE_CODE);
        return opResult;
    }
}