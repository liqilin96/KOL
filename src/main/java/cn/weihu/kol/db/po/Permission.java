package cn.weihu.kol.db.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author ${author}
 * @since 2021-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("base_permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限链接地址
     */
    private String url;

    /**
     * 图标
     */
    private String icon;

    /**
     * 是否有效：0：正常，1无效
     */
    private Integer status;

    /**
     * 插入时间
     */
    private Date ctime;

    /**
     * 更新时间
     */
    private Date utime;

    /**
     * 权限类型：1：目录，2菜单，3按钮
     */
    private Integer type;

    /**
     * 上级权限id
     */
    private String parentId;

    /**
     * 顺序
     */
    private Integer level;
}
