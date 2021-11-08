package cn.weihu.kol.db.dao;

import cn.weihu.kol.db.po.RolePermission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色权限关联表 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-05-07
 */
public interface RolePermissionDao extends MyMapper<RolePermission> {

    @Insert("<script> " +
            "insert into base_role_permission(id,permission_id,role_id,ctime,utime) values " +
            "<foreach collection='list' item='item' separator=',' > " +
            "(#{item.id},#{item.permissionId},#{item.roleId},#{item.ctime},#{item.utime}) " +
            "</foreach>" +
            "</script>")
    void insertList(@Param("list") List<RolePermission> list);

    @Delete("delete from base_role_permission where role_id = #{roleId}")
    void delByRoleId(@Param("roleId") String roleId);

    @Select("select permission_id from base_role_permission where role_id = #{roleId}")
    List<String> getPermissionIdsByRoleId(@Param("roleId") String roleId);
}
