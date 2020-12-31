package com.mljr.excel.util;

import com.mljr.excel.export.Column;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.List;
/**
 * @description: Excel工具类
 * @Date : 2018/6/3 下午8:37
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class ExcelUtil {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);
	/**
	 * 根据List生成Excel
	 * @param columns
	 * @param list
	 * @return
	 */
	public static HSSFWorkbook generateExcel(List<Column> columns, List<?> list){
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sht = hwb.createSheet();
		HSSFRow row;

		int rows = list.size(), cols = columns.size();// 工作表数据记录数
		Object val;
		for (int c = 0; c < cols; c++) {
			row = getRow(sht, 0);
			getCell(row, c).setCellValue(columns.get(c).getTitle());// 设置表头
			for (int r = 0; r < rows; r++) {// 设置数据
				val = getBeanVal(list.get(r), columns.get(c).getProperty());
				if (null != columns.get(c).getCellEditor()) {
					val = columns.get(c).getCellEditor().getValue(val, columns.get(c).getProperty(), r);
				}
				if (null != val) {
					row = getRow(sht, r + 1);
					if (val instanceof Number) {
						Double number = Double.valueOf(val.toString());
						getCell(row, c).setCellValue(number);
					} else {
						getCell(row, c).setCellValue(val.toString());
					}
				}
			}
		}
		
		//数据全部加载完毕后，调整一下各列的宽度
//		for(int c = 0; c < cols; c++){
//			sht.autoSizeColumn(c);
//		}
		return hwb;
	}
	
	 /**
	 * 保存工作表到磁盘
	 * @Title: makeDir  
	 * @param @param dir   
	 * @author jxwu
	 * @date 2015-12-16 上午11:18:39  
	  */
	public static boolean save(OutputStream os, HSSFWorkbook book){
	    try {
		  book.write(os);
	      os.flush();
	      os.close();
	      return true;
	    } catch (Exception e) {
	      LOGGER.error("保存excel文件异常", e);
	      return false;
	    } 
	}
	
	/**
	 * 获取工作表中指定的行
	 * @param sht 工作表
	 * @param index 工作行索引
	 * @return
	 */
	private static HSSFRow getRow(HSSFSheet sht, int index){
		HSSFRow row = sht.getRow(index);
		if(null == row){
			row=sht.createRow(index);
		}
		return row;
	}
	/**
	 * 获取工作表中指定的列
	 * @param row 工作行
	 * @param index 工作列索引
	 * @return
	 */
	private static HSSFCell getCell(HSSFRow row, int index){
		HSSFCell cell = row.getCell((short)index);
		if(null == cell){
			cell = row.createCell((short)index, Cell.CELL_TYPE_STRING);
		}
		return cell;
	}
	
	/**
	 * 根据属性字段获取数据
	 * @param bean 数据实体
	 * @param prop 属性
	 * @return
	 */
	private static Object getBeanVal(Object bean, String prop){
		Object itemValue = null;
		try {
			itemValue = PropertyUtils.getProperty(bean, prop);
		} catch (Exception e) {
			LOGGER.debug("获取数据出现异常，beanName=" + bean.getClass().getName() + "\tprop=" + prop, e);
		}
		return itemValue;
	}
	
}
