package com.mljr.exception;

/**
 * @description: Gps设备验证异常
 * @Date : 2019/1/28 下午5:39
 * @Author : 石冬冬-Seig Heil
 */
public class GpsDeviceException extends RuntimeException {
    public GpsDeviceException() {
    }

    public GpsDeviceException(String message) {
        super(message);
    }

    public GpsDeviceException(String message, Throwable cause) {
        super(message, cause);
    }

    public GpsDeviceException(Throwable cause) {
        super(cause);
    }
}
