package cn.weihu.kol.biz.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lql
 * @date 2021/11/11 10:51
 * Description：
 */
@Getter
@Setter
public class FieldsBo implements Comparable<FieldsBo> {


    private String       title;
    private String       dataIndex;
    private String       type;
    private boolean      isRequire;
    private boolean      isFilter;
    private boolean      isEffact;
    private List<String> options;
    private Integer      id;


//    /**
//     * 序号
//     */
//    private int    index;
//    /**
//     * 参数名称
//     */
//    private String name;
//    /**
//     * 英文名称
//     */
//    private String variable;
//    /**
//     * 是否启用
//     */
//    private String startUp;
//
//    /**
//     * 是否可搜索，0否，1是
//     */
//    private String search;
//
//    /**
//     * 字段数据类型,（boolean、文本、单选、多选、时间）
//     */
//    private String type;

    @Override
    public int compareTo(FieldsBo bo) {
        return this.id - bo.getId();
    }
}
