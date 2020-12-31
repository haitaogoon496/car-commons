package com.mljr.excel.export;

/**
 * @description: 导出异常
 * @Date : 2018/6/3 上午10:47
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class ExportException extends Throwable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6655185580023533990L;

	public ExportException() {
		super();
	}

	public ExportException(String message) {
		super(message);
	}

	public ExportException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExportException(Throwable cause) {
		super(cause);
	}
}
