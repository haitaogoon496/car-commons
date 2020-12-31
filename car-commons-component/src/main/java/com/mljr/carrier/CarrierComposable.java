package com.mljr.carrier;

/**
 * @description: 操作影像件组件接口
 * @Date : 2018/8/27 下午2:29
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public interface CarrierComposable {
    /**
     * 根据影像件UUID获取文件流
     * @param uuid
     * @return byte[]
     */
    byte[] getStream(String uuid);
    /**
     * 根据影像件UUID获取文件网络URL地址
     * @param uuid
     * @return
     */
    String getUrl(String uuid);
    /**
     * 根据影像件UUI下载文件后并写入共享磁盘并返回磁盘地址
     * @param fileName 存储文件名称
     * @param uuid 影像件key
     * @return 磁盘地址
     */
    String downloadFromCarrierAndWriteShare(String fileName, String uuid);
    /**
     * 把文件流写入影像中心，并返回地址
     * @param streams
     * @param streams
     * @return
     */
    String uploadStream(byte[] streams);
    /**
     * 把文件流写入影像中心，并返回地址
     * @param streams
     * @param streams
     * @return
     */
    String uploadStream(byte[] streams,String fileName);
    /**
     * 把文件流写入影像中心，并返回地址
     * @param streams
     * @return
     */
    String writeShare(String fileName, byte[] streams);
}
