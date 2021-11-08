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
 * 角色权限关联表
 * </p>
 *
 * @author ${author}
 * @since 2021-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("base_role_permission")
public class RolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 权限id
     */
    private String permissionId;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 插入时间
     */
    private Date ctime;

    /**
     * 更新时间
     */
    private Date utime;


}
