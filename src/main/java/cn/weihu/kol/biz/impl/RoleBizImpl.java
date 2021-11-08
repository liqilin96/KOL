package cn.weihu.kol.biz.impl;

import cn.hutool.core.date.DateUtil;
import cn.weihu.base.exception.CheckException;
import cn.weihu.base.result.ErrorCode;
import cn.weihu.base.result.PageResult;
import cn.weihu.kol.biz.RoleBiz;
import cn.weihu.kol.convert.RoleConverter;
import cn.weihu.kol.db.dao.RoleDao;
import cn.weihu.kol.db.dao.RolePermissionDao;
import cn.weihu.kol.db.dao.RoleUserDao;
import cn.weihu.kol.db.po.Role;
import cn.weihu.kol.db.po.RolePermission;
import cn.weihu.kol.db.po.User;
import cn.weihu.kol.http.req.RoleListReq;
import cn.weihu.kol.http.req.RoleSaveReq;
import cn.weihu.kol.http.resp.RoleResp;
import cn.weihu.kol.redis.RedisUtils;
import cn.weihu.kol.userinfo.UserInfoContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-05-07
 */
@Service
public class RoleBizImpl extends BaseBiz<RoleDao, Role> implements RoleBiz {

    @Autowired
    private RedisUtils        redisUtils;
    @Resource
    private RoleUserDao       roleUserDao;
    @Resource
    private RolePermissionDao rolePermissionDao;

    @Override
    public PageResult<RoleResp> rolePage(RoleListReq req) {
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery(Role.class);
        wrapper.eq(Role::getCompanyId, UserInfoContext.getCompanyId());
        if(StringUtils.isNotBlank(req.getName())) {
            wrapper.like(Role::getName, "%" + req.getName() + "%");
        }
        wrapper.orderByDesc(Role::getId);
        Page<Role> page = baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);
        List<RoleResp> respList = page.getRecords().stream()
                .map(role -> {
                    RoleResp resp = RoleConverter.entity2RoleResp(role);
                    resp.setPermissionIds(rolePermissionDao.getPermissionIdsByRoleId(role.getId()));
                    return resp;
                })
                .collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), respList);
    }

    @Override
    public List<RoleResp> roleAll() {
        List<Role> roles = baseMapper.selectList(new LambdaQueryWrapper<>(Role.class)
                                                         .eq(Role::getCompanyId, UserInfoContext.getCompanyId()));
        List<RoleResp> respList = null;
        if(!roles.isEmpty()) {
            respList = roles.stream()
                    .map(RoleConverter::entity2RoleResp)
                    .collect(Collectors.toList());
        }
        return respList;
    }

    @Override
    public String roleAdd(RoleSaveReq req) {
        // 校验名称
        Role role = getOne(new LambdaQueryWrapper<>(Role.class)
                                   .eq(Role::getCompanyId, UserInfoContext.getCompanyId())
                                   .eq(Role::getName, req.getName()));
        if(Objects.nonNull(role)) {
            throw new CheckException(ErrorCode.ROLE_NAME_IS_EXIST);
        }
        role = new Role();
        role.setCompanyId(UserInfoContext.getCompanyId());
        role.setName(req.getName());
        role.setCtime(DateUtil.date());
        role.setUtime(DateUtil.date());
        save(role);
        if(!CollectionUtils.isEmpty(req.getPermissionIds())) {
            // 添加角色与权限的绑定关系记录
            bindPermissions(role.getId(), req.getPermissionIds());
        }
        return role.getId();
    }

    @Override
    @Transactional
    public String roleEdit(String id, RoleSaveReq req) {
        // 校验名称
        Role role = getOne(new LambdaQueryWrapper<>(Role.class)
                                   .eq(Role::getCompanyId, UserInfoContext.getCompanyId())
                                   .eq(Role::getName, req.getName())
                                   .ne(Role::getId, id));
        if(Objects.nonNull(role)) {
            throw new CheckException(ErrorCode.ROLE_NAME_IS_EXIST);
        }
        role = new Role();
        role.setId(id);
        role.setName(req.getName());
        role.setUtime(DateUtil.date());
        updateById(role);
        // 删除角色与权限的历史绑定关系记录
        rolePermissionDao.delByRoleId(id);
        if(!CollectionUtils.isEmpty(req.getPermissionIds())) {
            // 添加角色与权限的绑定关系记录
            bindPermissions(role.getId(), req.getPermissionIds());
        }
        // 删除此角色下的所有用户登录缓存,以重新登录获取最新角色权限
        List<User> users = roleUserDao.getUsersByRoleId(id);
        users.forEach(user -> redisUtils.delUserInfoByUsername(user.getUsername()));
        return id;
    }

    private void bindPermissions(String roleId, List<String> permissionIds) {
        List<RolePermission> list = new ArrayList<>();
        RolePermission       rolePermission;
        for(String permissionId : permissionIds) {
            rolePermission = new RolePermission();
            rolePermission.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermission.setCtime(DateUtil.date());
            rolePermission.setUtime(DateUtil.date());
            list.add(rolePermission);
        }
        rolePermissionDao.insertList(list);
    }

    @Override
    public String roleDel(String id) {
        return null;
    }
}
