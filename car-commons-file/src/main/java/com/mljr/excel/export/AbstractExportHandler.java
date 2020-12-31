package com.mljr.excel.export;
/**
 * @description: 抽象导出处理器
 * @Date : 2018/6/3 上午10:50
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public abstract class AbstractExportHandler implements ExportHandler{
	//文件名称
	private String fileName = "export";
	//一个Excel存放的数据
	private int sheetRows = 5000;
	//每次取数据的条数
	private int limit = 1000;
	//总记录数
	private int totalSize = -1;
	//取数据开始位置
	private int start = 0;
	//最后一条数据主键
	private Object lastKey;
	
	/**
	 * 实例
	 * @param fileName
	 */
	public AbstractExportHandler(String fileName){
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public int getSheetRows() {
		return sheetRows;
	}

	void setSheetRows(int sheetRows) {
		this.sheetRows = sheetRows;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getStart() {
		return start;
	}

	void setStart(int start) {
		this.start = start;
	}

	public int getTotalSize() {
		return totalSize;
	}

	void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public Object getLastKey() {
		return lastKey;
	}

	protected void setLastKey(Object lastKey) {
		this.lastKey = lastKey;
	}
}
