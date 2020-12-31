package com.mljr.excel.handler;

/**
 * @description: Excel导出处理器接口
 * @Date : 2018/7/17 下午2:28
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public interface ExcelExportHandle {
    /**
     * 文件过期时间2小时
     */
    long expire =  2 * 60 * 60 * 1000;

    /**
     * 判断是否持有锁
     * 调用方可以自定义实现策略
     * @return
     */
    default boolean hasLock(){
        return false;
    }

    /**
     * 释放锁
     * 调用方可以自定义实现策略
     */
    default void releaseLock(){

    }

    /**
     * 文件失效时间
     * @return
     */
    default long fileExpire(){
        return  expire;
    }

    /**
     * 客户端回调函数
     * @return
     */
    default String callbackClient(){
        return "<script>alert('{0}');history.go(-1);</script>";
    }
}
