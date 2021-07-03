package org.arch.oms.common.request;

/**
 * 分页参数
 * @author junboXiang
 * @version V1.0
 * 2021-07-03
 */
public class PageInfo {

    /**
     * 当前分页页码
     */
    private long number;

    /**
     * 每页的数据大小
     */
    private long size;

    /**
     * 排序类型,默认按照主键id排序
     */
    private String sortType = "sortTypes";

    public long getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public PageInfo() {
        super();
        this.size = 10;
        this.number = 1;
    }

    public PageInfo(int number, int size, String sortType) {
        super();
        this.number = number;
        this.size = size;
        this.sortType = sortType;
    }

    public PageInfo checkPageInfo() {
        number = number < 1 ? 1 : number;
        size = size < 1 ? 10 : size > 500 ? 500 : size;
        return this;
    }

}
