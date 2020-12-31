package com.mljr.position;

/**
 * @description: 置入规则异常
 * @Date : 2018/12/5 下午4:25
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class PositionException extends Exception {
    public PositionException() {
    }

    public PositionException(String message) {
        super(message);
    }

    public PositionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PositionException(Throwable cause) {
        super(cause);
    }
}
