package cn.weihu.kol.db.dao;

import cn.weihu.kol.db.po.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-05-07
 */
public interface RoleDao extends MyMapper<Role> {

    @Select("SELECT r.id,r.`name` FROM base_role r,base_role_user ru WHERE r.id = ru.role_id AND ru.user_id = #{userId}")
    List<Role> getRolesByUserId(@Param("userId") String userId);
}
