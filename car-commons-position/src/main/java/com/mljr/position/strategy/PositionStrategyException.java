package com.mljr.position.strategy;

import com.mljr.position.PositionException;

/**
 * @description: 策略异常
 * @Date : 2018/12/12 下午5:37
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class PositionStrategyException extends PositionException {
    public PositionStrategyException() {
        super();
    }

    public PositionStrategyException(String message) {
        super(message);
    }

    public PositionStrategyException(String message, Throwable cause) {
        super(message, cause);
    }

    public PositionStrategyException(Throwable cause) {
        super(cause);
    }
}
