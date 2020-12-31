package com.mljr.common.page;

import java.io.Serializable;

/**
 * Description: 分页Form类<br/>
 * @version V1.0  by 石冬冬-Heil Hitler on  2017/4/27 12:48
 */
public class PageForm<T> implements Serializable {
    private static final long serialVersionUID = 1315360688901318671L;
    private int start = 0;
    private int limit = 20;
    private T form;

    public PageForm() {
    }

    public PageForm(T form) {
        this.form = form;
    }

    public PageForm(int start, int limit, T form) {
        this.start = start;
        this.limit = limit;
        this.form = form;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public T getForm() {
        return form;
    }

    public void setForm(T form) {
        this.form = form;
    }
}
