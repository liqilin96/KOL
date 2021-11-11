package cn.weihu.base.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Pages<T> implements Serializable {

    private static final long    serialVersionUID = 1L;
    //当前页
    private              int     pageNo;
    //每页的数量
    private              int     pageSize;
    //当前页的数量
    private              int     size;
    //总记录数
    private              long    total;
    //总页数
    private              int     pages;
    //结果集
    private              List<T> list;

    public Pages(int pageNo, int pageSize, long total, List<T> list) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
    }
}
