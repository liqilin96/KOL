package cn.weihu.kol.db.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 字段字典表
 * </p>
 *
 * @author ${author}
 * @since 2021-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("kol_public_fields")
public class PublicFields implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 字段组名称
     */
    private String name;

    /**
     * 字段列表
     */
    private String fieldList;

//    /**
//     * 字段类型1账号类型，2报价形式
//     */
//    private Integer type;

    /**
     * 插入时间
     */
    private Date ctime;

    /**
     * 更新时间
     */
    private Date utime;

    /**
     * 创建人id
     */
    private Long createUserId;

    /**
     * 更新人id
     */
    private Long updateUserId;


}
