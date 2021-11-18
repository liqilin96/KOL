package cn.weihu.base.result;

import java.io.Serializable;
import java.util.List;

public class PageResult<T> implements Serializable {

    private Long total;

    private List<T> records;

    //字段列表，KOL资源库使用
    private String fieldList;

    public PageResult() {
    }

    public PageResult(Long total, List<T> records) {
        this.total = total;
        this.records = records;
    }

    public PageResult(Long total, List<T> records, String fieldList) {
        this.total = total;
        this.records = records;
        this.fieldList = fieldList;
    }

    public String getFieldList() {
        return fieldList;
    }

    public void setFieldList(String fieldList) {
        this.fieldList = fieldList;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
