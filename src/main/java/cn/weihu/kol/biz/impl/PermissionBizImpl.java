package cn.weihu.kol.biz.impl;

import cn.weihu.kol.biz.PermissionBiz;
import cn.weihu.kol.convert.PermissionConverter;
import cn.weihu.kol.db.dao.PermissionDao;
import cn.weihu.kol.db.po.Permission;
import cn.weihu.kol.http.resp.PermissionResp;
import cn.weihu.kol.userinfo.UserInfoContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-05-07
 */
@Service
public class PermissionBizImpl extends BaseBiz<PermissionDao, Permission> implements PermissionBiz {

    @Override
    public List<PermissionResp> permissionList() {
        List<Permission> permissions;
        if(UserInfoContext.getUserInfo().getIsAdmin() == null ? false : UserInfoContext.getUserInfo().getIsAdmin()) {
            permissions = baseMapper.selectList(new LambdaQueryWrapper<>(Permission.class).orderByAsc(Permission::getLevel));
        } else {
            permissions = baseMapper.getPermissionsByUserId(UserInfoContext.getUserId());
        }
        List<PermissionResp> result = null;
        if(!CollectionUtils.isEmpty(permissions)) {
            result = PermissionConverter.list2BoList(permissions);
        }
        return result;
    }
}
