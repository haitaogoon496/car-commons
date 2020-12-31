package com.mljr.excel.export;

/**
 * @description: excel单元格编辑器
 * @Date : 2018/6/3 上午10:49
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public interface CellEditor {
	/**
	 * 获取单元格值
	 * @param val
	 * @param property
	 * @param rowCount
	 * @return
	 */
	Object getValue(Object val, String property, int rowCount);
}
