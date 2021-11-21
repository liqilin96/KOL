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
 * 达人报价表
 * </p>
 *
 * @author ${author}
 * @since 2021-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("kol_quote")
public class Quote implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 达人编号
     */
    private String actorSn;

    /**
     * 达人信息
     */
    private String actorData;

    /**
     * 佣金%
     */
    private Integer commission;

    /**
     * 价格
     */
    private Double price;

    /**
     * 供应商
     */
    private String provider;

    /**
     * 保价到期时间
     */
    private Date insureEndtime;

    /**
     * 是否启用,0:否,1:是
     */
    private Integer enableFlag;

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
