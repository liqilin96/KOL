package cn.weihu.kol.db.dao;

import cn.weihu.kol.db.po.Role;
import cn.weihu.kol.db.po.RoleUser;
import cn.weihu.kol.db.po.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色用户关联表 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-05-07
 */
public interface RoleUserDao extends MyMapper<RoleUser> {

    @Insert("<script> " +
            "insert into base_role_user(id,role_id,user_id,ctime) values " +
            "<foreach collection='list' item='item' separator=',' > " +
            "(#{item.id},#{item.roleId},#{item.userId},#{item.ctime}) " +
            "</foreach>" +
            "</script>")
    void saveBatch(@Param("list") List<RoleUser> list);

    @Select("select u.* from base_user u,base_role_user ru where ru.role_id = #{roleId}")
    List<User> getUsersByRoleId(@Param("roleId") String roleId);

    @Select("select u.name from base_role u,base_role_user ru where u.id=ru.role_id and ru.user_id = #{userId}")
    List<String> getRoelByUserId(@Param("userId") String userId);

    @Delete("delete from base_role_user where role_id = #{roleId}")
    void delByRoleId(@Param("roleId") String roleId);
}
