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
 * 用户表
 * </p>
 *
 * @author ${author}
 * @since 2021-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("base_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户状态：0正常，1禁用
     */
    private Integer status;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 所属企业id
     */
    private String companyId;

    /**
     * 插入时间
     */
    private Date ctime;

    /**
     * 更新时间
     */
    private Date utime;


}
