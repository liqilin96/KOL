package cn.weihu.kol.db.dao;

import cn.weihu.kol.db.po.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-05-07
 */
public interface UserDao extends MyMapper<User> {

    @Select("select username from base_user where company_id = #{companyId} order by id limit 1")
    String getDefaultUserNameByCompanyId(@Param("companyId") String companyId);
}
