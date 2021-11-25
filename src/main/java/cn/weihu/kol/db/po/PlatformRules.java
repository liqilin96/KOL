package cn.weihu.kol.db.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 平台筛选规则
 * </p>
 *
 * @author ${author}
 * @since 2021-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("kol_platform_rules")
public class PlatformRules implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private Long id;

    /**
     * 平台
     */
    private String platform;

    /**
     * 报价形式/资源位置
     */
    private String address;

    /**
     * 筛选字段,以分号分隔
     */
    private String screenFields;


}
