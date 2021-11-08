package cn.weihu.kol.db.dao;

import cn.weihu.kol.db.dao.MyMapper;
import cn.weihu.kol.db.po.Permission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-05-07
 */
public interface PermissionDao extends MyMapper<Permission> {

    @Select("select * from base_permission " +
            "where id in (" +
            "select distinct rp.permission_id from base_role_permission rp,base_role_user ru " +
            "where rp.role_id = ru.role_id and ru.user_id = #{userId} " +
            ") order by level")
    List<Permission> getPermissionsByUserId(@Param("userId") String userId);
}
