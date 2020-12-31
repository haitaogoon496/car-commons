package com.mljr.excel.export;

/**
 * @description: excel表格字段信息
 * @Date : 2018/6/3 上午10:48
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class Column {
	/**
	 * 属性
	 */
	private String property;
	/**
	 * 标题
	 */
    private String title;
	/**
	 * excel单元格编辑器
	 */
	private CellEditor cellEditor;
    
    public Column(String property){
    	this.property = property;
    }
    
	public String getProperty() {
		return property;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Column title(String title) {
		this.title = title;
		return this;
	}
	
	public CellEditor getCellEditor() {
		return cellEditor;
	}

	public void setCellEditor(CellEditor cellEditor) {
		this.cellEditor = cellEditor;
	}

	public Column cellEditor(CellEditor editor) {
    	setCellEditor(editor);
    	return this;
	}
	
}
