package com.mljr.common.page;

import java.io.Serializable;
import java.util.List;

/**
 * Description:分页查询VO类 <br/>
 * @version V1.0  by 石冬冬-Heil Hitler on  2017/4/27 12:57
 */
public class PageVO<T> implements Serializable {
    private static final long serialVersionUID = 1724063683524348852L;
    private int recordsTotal;
    private int recordsFiltered;
    private int draw;
    private List<T> data;
    private String error;

    public PageVO(int draw, String errorMsg) {
        this.error = errorMsg;
        this.draw = draw;
        this.recordsTotal = 0;
        this.recordsFiltered = 0;
    }

    public PageVO(int draw, int total, List<T> data){
        this.draw = draw;
        this.data = data;
        this.recordsTotal = total;
        this.recordsFiltered = total;
    }

    public PageVO(int draw, int total, List<T> data, String error){
        this.draw = draw;
        this.data = data;
        this.recordsTotal = total;
        this.recordsFiltered = total;
        this.error = error;
    }

    public PageVO(List<T> data){
        this.data = data;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
