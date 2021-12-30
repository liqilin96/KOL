package cn.weihu.kol.biz.bo;

import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class FieldsBo implements Comparable<FieldsBo> {


    private String    title;
    private String    dataIndex;
    private String    type;
    private boolean   isRequire;
    private boolean   isFilter;
    private boolean   isEffect;
    private List<Opt> options;
    private Integer   id;


    @Override
    public int compareTo(FieldsBo bo) {
        return this.id - bo.getId();
    }
}

@Setter
@Getter
class Opt {
    private String value;
}
