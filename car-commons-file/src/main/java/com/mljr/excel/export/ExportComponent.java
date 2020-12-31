package com.mljr.excel.export;

import com.mljr.spring.SpringUtil;
import com.mljr.util.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @description: 数据导出组件
 * @Date : 2018/6/3 上午10:54
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class ExportComponent {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ExportComponent.class);
	/**
	 * 导出处理对象
	 */
	private AbstractExportHandler handler;
	
	public AbstractExportHandler getHandler() {
		return handler;
	}
	/**
	 *
	 * 初始化导出组件@param params
	 * @throws ExportException
	 */
	public void init(Map<String, String> params) throws ExportException{
		String beanName = params.get("beanName");
    	if (StringTools.isEmpty(beanName)){
    		throw new ExportException("参数解析错误，输入参数错误！");
    	}
		//获取bean对象
    	try {
    		handler = SpringUtil.getBean(beanName,AbstractExportHandler.class);
		} catch (Exception e) {
			throw new ExportException("参数解析错误，输入参数错误！", e);
		}
		int sheetRows = Integer.valueOf(Optional.ofNullable(params.get("sheetRows")).orElse("0"));
    	int limit = Integer.valueOf(Optional.ofNullable(params.get("limit")).orElse("0"));
    	if (sheetRows != 0){
    		handler.setSheetRows(sheetRows);
    	}
    	if (limit != 0 ){
    		handler.setLimit(limit);
    	}
    	List<Column> columns = handler.getColumns();
        if (columns.isEmpty() || handler.getSheetRows() % handler.getLimit() != 0){
        	throw new ExportException("参数解析错误，输入参数错误！");
        }
        try{
        	handler.setTotalSize(handler.getCount(params));//获取总记录数
        }catch (Exception e) {
			throw new ExportException("文件导出数据加载失败！", e);
		}
        LOGGER.debug("文件导出开始beanName={}",beanName);
	}
	/**
	 * 获取单个Excel的数据
	 * @param params
	 * @param type
	 * @return
	 * @throws ExportException
	 */
	public List<?> getListData(Map<String, ? extends Object> params, ExportEnum type) throws ExportException {
		List<Object> list = new ArrayList<>();
		switch (type){
			case ZIP:
				int i = 0;// 计数器
				do {
					if (i == handler.getSheetRows() || handler.getStart() >= handler.getTotalSize()) {
						break;
					} else {
						list.addAll(handler.getList(params));
						handler.setStart(handler.getStart() + handler.getLimit());
						i += handler.getLimit();
					}
				} while (true);
				break;
			case XLS:
				list.addAll(handler.getList(params));
				break;
			default:
				throw new ExportException("未知的文件导出类型！");
		}
		return list;
	}
 
}
