package cn.weihu.kol.biz.impl;

import cn.hutool.core.date.DateUtil;
import cn.weihu.base.exception.CheckException;
import cn.weihu.base.result.ErrorCode;
import cn.weihu.base.result.PageResult;
import cn.weihu.kol.biz.UserBiz;
import cn.weihu.kol.convert.PermissionConverter;
import cn.weihu.kol.convert.UserConverter;
import cn.weihu.kol.db.dao.PermissionDao;
import cn.weihu.kol.db.dao.RoleDao;
import cn.weihu.kol.db.dao.RoleUserDao;
import cn.weihu.kol.db.dao.UserDao;
import cn.weihu.kol.db.po.Permission;
import cn.weihu.kol.db.po.Role;
import cn.weihu.kol.db.po.RoleUser;
import cn.weihu.kol.db.po.User;
import cn.weihu.kol.http.req.LoginReq;
import cn.weihu.kol.http.req.ModifyPasswordReq;
import cn.weihu.kol.http.req.UserListReq;
import cn.weihu.kol.http.req.UserSaveReq;
import cn.weihu.kol.http.resp.LoginResp;
import cn.weihu.kol.http.resp.PermissionResp;
import cn.weihu.kol.http.resp.UserResp;
import cn.weihu.kol.redis.RedisUtils;
import cn.weihu.kol.userinfo.UserInfo;
import cn.weihu.kol.userinfo.UserInfoContext;
import cn.weihu.kol.util.MD5Util;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-05-07
 */
@Slf4j
@Service
public class UserBizImpl extends BaseBiz<UserDao, User> implements UserBiz {

    @Autowired
    private RedisUtils    redisUtils;
    @Resource
    private RoleUserDao   roleUserDao;
    @Resource
    private RoleDao       roleDao;
    @Resource
    private PermissionDao permissionDao;


    @Override
    public PageResult<UserResp> userPage(UserListReq req) {

        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class);
        if(StringUtils.isNotBlank(req.getName())) {
            wrapper.like(User::getName, req.getName() + "%");
        }
        if(StringUtils.isNotBlank(req.getUsername())) {
            wrapper.like(User::getUsername, req.getUsername() + "%");
        }

        Page<User> page = baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);
        List<UserResp> respList = page.getRecords().stream().map(user -> {
            UserResp resp = UserConverter.entity2UserResp(user);
            // 角色
            List<Role> roles = roleDao.getRolesByUserId(user.getId().toString());
            resp.setRoleIds(roles.stream().map(role -> String.valueOf(role.getId()))
                                    .collect(Collectors.joining(";")));
            resp.setRoleNames(roles.stream().map(Role::getName)
                                      .collect(Collectors.joining(";")));
            return resp;
        }).collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), respList);
    }

    @Override
    public String addUser(UserSaveReq req) {
        List<User> users = baseMapper.selectList(new LambdaQueryWrapper<>(User.class)
                                                         .eq(User::getUsername, req.getUsername()));
        if(!users.isEmpty()) {
            throw new CheckException(ErrorCode.USERNAME_ALREADY_EXISTED);
        }
        User user = UserConverter.userSaveReq2Entity(req);
        user.setPassword(MD5Util.password(req.getPassword()));
        user.setCtime(DateUtil.date());
        save(user);

        List<RoleUser> roleUsers = convert(req.getRoleIds(), user.getId().toString());
        roleUserDao.saveBatch(roleUsers);
        List<Permission>     permissions     = permissionDao.getPermissionsByUserId(user.getId().toString());
        List<PermissionResp> permissionResps = PermissionConverter.list2BoList(permissions);
        UserInfo userInfo = new UserInfo(UserInfoContext.getCompanyId(), user.getId().toString(), user.getUsername(),
                                         user.getPassword(), user.getName(), permissionResps);
        redisUtils.setUserInfoByUsername(user.getUsername(), userInfo);
        log.info(">>> 新增用户:{},roleIds:{}", req.getUsername(), req.getRoleIds());
        return user.getId().toString();
    }

    @Override
    public String editUser(String id, UserSaveReq req) {
        List<User> users = baseMapper.selectList(new LambdaQueryWrapper<>(User.class)
                                                         .eq(User::getUsername, req.getUsername())
                                                         .ne(User::getId, id));
        if(!users.isEmpty()) {
            throw new CheckException(ErrorCode.USERNAME_ALREADY_EXISTED);
        }
        User user = UserConverter.userSaveReq2Entity(req);
        user.setId(Long.parseLong(id));
        user.setUtime(DateUtil.date());
        updateById(user);

        // 删除历史用户与角色绑定记录
        roleUserDao.delete(new LambdaUpdateWrapper<>(RoleUser.class).eq(RoleUser::getUserId, id));
        List<RoleUser> roleUsers = convert(req.getRoleIds(), user.getId().toString());
        roleUserDao.saveBatch(roleUsers);
        // 删除用户登录缓存,以重新登录获取最新角色权限
        redisUtils.delUserInfoByUsername(req.getUsername());
        log.info(">>> 编辑用户:{},roleIds:{}", req.getUsername(), req.getRoleIds());
        return id;
    }

    private List<RoleUser> convert(String[] roleIds, String userId) {
        List<RoleUser> roleUsers = new ArrayList<>();
        RoleUser       roleUser;
        for(String roleId : roleIds) {
            roleUser = new RoleUser();
//            roleUser.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            roleUser.setRoleId(roleId);
            roleUser.setUserId(userId);
            roleUser.setCtime(DateUtil.date());
            roleUsers.add(roleUser);
        }
        return roleUsers;
    }

    @Override
    public String resetPassword(String id) {
        User user = getById(id);
        if(Objects.isNull(user)) {
            throw new CheckException(ErrorCode.USER_NOT_FOUND);
        }
        String newPassword = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        // 更新数据库
        user.setPassword(MD5Util.password(newPassword));
        user.setUtime(DateUtil.date());
        updateById(user);
        // 更新Redis
        UserInfo userInfo = redisUtils.getUserInfoByUsername(user.getUsername());
        userInfo.setPassword(user.getPassword());
        redisUtils.setUserInfoByUsername(user.getUsername(), userInfo);
        redisUtils.delUserInfoByToken(userInfo.getAuth());
        log.info(">>> 重置密码:{}", user.getUsername());
        return newPassword;
    }

    @Override
    public LoginResp login(LoginReq req, HttpServletRequest request) {
        if(StringUtils.isNotBlank(req.getShortName())) {
            // 座席登录
            req.setUsername(req.getUsername() + "@" + req.getShortName());
        }
        UserInfo             userInfo = redisUtils.getUserInfoByUsername(req.getUsername());
        User                 user;
        List<PermissionResp> permissionResps;
        if(Objects.isNull(userInfo)) {
            user = baseMapper.selectOne(new LambdaQueryWrapper<>(User.class).eq(User::getUsername, req.getUsername()));
            if(Objects.isNull(user)) {
                throw new CheckException(ErrorCode.USER_NOT_FOUND);
            } else {
                if(0 != user.getStatus()) {
                    throw new CheckException(ErrorCode.USER_IS_DISABLE);
                }
                if(!MD5Util.password(req.getPassword()).equals(user.getPassword())) {
                    throw new CheckException(ErrorCode.USERNAME_OR_PASSWORD_INVALID);
                }
                List<Permission> permissions = permissionDao.getPermissionsByUserId(user.getId().toString());
                permissionResps = PermissionConverter.list2BoList(permissions);
                userInfo = new UserInfo(UserInfoContext.getCompanyId(), user.getId().toString(), user.getUsername(),
                                        user.getPassword(), user.getName(), permissionResps);
                // 是否超级管理员
                userInfo.setIsAdmin("admin".equals(req.getUsername()));
            }
        } else {
            if(!MD5Util.password(req.getPassword()).equals(userInfo.getPassword())) {
                throw new CheckException(ErrorCode.USERNAME_OR_PASSWORD_INVALID);
            }
            permissionResps = userInfo.getPermissions();
        }
        String auth = UUID.randomUUID().toString();
        userInfo.setAuth(auth);
        String msgKey = userInfo.getCompanyId();
        userInfo.setMsgKey(msgKey);
        redisUtils.setUserInfoByUsername(req.getUsername(), userInfo);
        redisUtils.setUserInfoByToken(auth, userInfo);
        log.info(">>> 用户登录:{}", req.getUsername());
        request.getSession().setAttribute("userInfo", userInfo);
        return new LoginResp(userInfo.getCompanyId(), auth, msgKey, permissionResps, 0);
    }

    @Override
    public String modifyPassword(ModifyPasswordReq req) {
        UserInfo userInfo = UserInfoContext.getUserInfo();
        if(!MD5Util.password(req.getOldPassword()).equals(userInfo.getPassword())) {
            throw new CheckException(ErrorCode.OLD_PASSWORD_ERROR);
        }
        String password = MD5Util.password(req.getPassword());
        // 更新数据库
        User user = new User();
//        user.setId(userInfo.getUserId());
        user.setPassword(password);
        user.setUtime(DateUtil.date());
        updateById(user);
        // 更新Redis
        userInfo.setPassword(password);
        redisUtils.setUserInfoByUsername(userInfo.getUsername(), userInfo);
        redisUtils.delUserInfoByToken(userInfo.getAuth());
        log.info(">>> 用户修改密码:{}", userInfo.getUsername());
        return userInfo.getPassword();
    }

    @Override
    public List<UserResp> userList() {
        List<User> users = baseMapper.selectList(new LambdaQueryWrapper<>(User.class)
                                                         .eq(User::getStatus, 0));
        if(!CollectionUtils.isEmpty(users)) {
            return users.stream().map(UserConverter::entity2UserResp).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Map<String, Object> token(String accessKeyId, String accessKeySecret) {
        UserInfo userInfo = redisUtils.getUserInfoByAppid(accessKeyId);
        if(userInfo == null) {
            throw new CheckException(ErrorCode.ACCESS_KEY_INVALID);
        }
        if(!accessKeySecret.equals(userInfo.getAppsecret())) {
            throw new CheckException(ErrorCode.ACCESS_KEY_OR_SECRET_INVALID);
        }
        return tokenHandle(userInfo);
    }

    @Override
    public void delToken(HttpServletRequest request) {
        String   token    = request.getHeader("Authorization");
        UserInfo userInfo = redisUtils.getUserInfoByToken(token);
        redisUtils.delUserInfoByToken(token);
        if(userInfo != null) {
            redisUtils.delUserInfoByUsername(userInfo.getUsername());
        }
    }

    private Map<String, Object> tokenHandle(UserInfo userInfo) {
        String token = UUID.randomUUID().toString();
        userInfo.setToken(token);
        redisUtils.setUserInfoByToken(token, userInfo);
        redisUtils.setExpireOfUserInfoByToken(token, 36L, TimeUnit.HOURS);

        long                currentTime = System.currentTimeMillis();
        long                expireTime  = currentTime + 36 * 60 * 60 * 1000;
        Map<String, Object> result      = new HashMap<>(2);
        result.put("token", token);
        result.put("expireTime", expireTime);
        return result;
    }
}
