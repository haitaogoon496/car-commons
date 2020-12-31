package com.mljr.excel.export;

import java.util.List;
import java.util.Map;
/**
 * @description: 数据导出处理接口
 * @Date : 2018/6/3 上午10:45
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public interface ExportHandler {
	/**
	 * 获得数据的List
	 * @param params 请求参数
	 * @return
	 */
	List<?> getList(Map<String, ? extends Object> params);
	/**
	 * 获取总记录数
	 * @param params 请求参数
	 * @return
	 */
	int getCount(Map<String, ? extends Object> params);
	/**
	 * 获取表头
	 * key 为显示标题
	 * val 为字段属性
	 */
	List<Column> getColumns();
}