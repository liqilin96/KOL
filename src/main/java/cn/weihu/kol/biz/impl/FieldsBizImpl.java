package cn.weihu.kol.biz.impl;

import cn.weihu.base.result.PageResult;
import cn.weihu.kol.biz.FieldsBiz;
import cn.weihu.kol.db.dao.FieldsDao;
import cn.weihu.kol.db.po.Fields;
import cn.weihu.kol.http.req.FieldsReq;
import cn.weihu.kol.http.resp.FieldsResp;
import cn.weihu.kol.userinfo.UserInfoContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 字段组表 服务实现类
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
@Service
public class FieldsBizImpl extends ServiceImpl<FieldsDao, Fields> implements FieldsBiz {

    @Override
    public String createGroup(FieldsReq req) {

        Fields fields = new Fields();
        fields.setName(req.getName());
        fields.setFieldList(req.getFieldList());
        fields.setCtime(LocalDateTime.now());
        fields.setUtime(LocalDateTime.now());
        fields.setCreateUserId(UserInfoContext.getUserId());
        fields.setUpdateUserId(UserInfoContext.getUserId());
        this.save(fields);
        return null;
    }

    @Override
    public String updateGroup(FieldsReq req) {
        Fields fields = new Fields();
        fields.setId(Long.parseLong(req.getId()));
        if(StringUtils.isNotBlank(req.getNewName())) {
            fields.setName(req.getNewName());
        }
        if(StringUtils.isNotBlank(req.getFieldList())) {
            fields.setFieldList(req.getFieldList());
        }
        fields.setUtime(LocalDateTime.now());
        fields.setUpdateUserId(UserInfoContext.getUserId());
        updateById(fields);
        return null;
    }

    @Override
    public PageResult<FieldsResp> pageGroup(FieldsReq req) {

        LambdaQueryWrapper<Fields> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(req.getName())) {
            wrapper.eq(Fields::getName, req.getName());
        }
        wrapper.orderByDesc(Fields::getCtime);
        Page<Fields> fieldsPage = this.baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);

        List<FieldsResp> fieldsResps = fieldsPage.getRecords().stream().map(x -> {

            FieldsResp resp = new FieldsResp();
            resp.setId(x.getId().toString());
            resp.setName(x.getName());
            resp.setFieldList(x.getFieldList());
            return resp;
        }).collect(Collectors.toList());

        return new PageResult<>(fieldsPage.getTotal(), fieldsResps);
    }

    @Override
    public FieldsResp queryOne(String id) {

        Fields fields = getById(id);
        FieldsResp resp = new FieldsResp();
        resp.setId(fields.getId().toString());
        resp.setName(fields.getName());
        resp.setFieldList(fields.getFieldList());
        return resp;
    }


    @Override
    public String deleteGroup(String id) {
//        Fields fields = this.baseMapper.selectById(req.getId());
//        if(fields == null) {
//            throw new CheckException("要删除的字段组不存在");
//        }
        this.baseMapper.deleteById(id);
        return null;
    }


//    @Override
//    public String createGroup(FieldsReq req) {
//
//        Fields fields = new Fields();
//        fields.setName(req.getName());
//        fields.setType(Integer.parseInt(req.getType()));
////        fields.setFieldList(req.getFieldList());
//        fields.setCtime(LocalDateTime.now());
//        fields.setUtime(LocalDateTime.now());
//        fields.setCreateUserId(Long.valueOf(UserInfoContext.getUserId() == null ? "-1" : UserInfoContext.getUserId()));
//        fields.setUpdateUserId(Long.valueOf(UserInfoContext.getUserId() == null ? "-1" : UserInfoContext.getUserId()));
//        this.save(fields);
//        return null;
//    }
//
//    @Override
//    public String deleteGroup(FieldsReq req) {
//
//        Fields fields = this.baseMapper.selectById(req.getId());
//        if(fields == null) {
//            throw new CheckException("要删除的字段组不存在");
//        }
//        this.baseMapper.deleteById(req.getId());
//        return null;
//    }
//
//    @Override
//    public String updateGroup(FieldsReq req) {
//
//        Fields fields = new Fields();
//        fields.setId(Long.parseLong(req.getId()));
//        fields.setName(req.getNewName());
//        updateById(fields);
//        return null;
//    }
//
//
//    @Override
//    public PageResult<FieldsResp> pageGroup(FieldsReq req) {
//
//        LambdaQueryWrapper<Fields> wrapper = new LambdaQueryWrapper<>();
//        if(StringUtils.isNotBlank(req.getName())) {
//            wrapper.eq(Fields::getName, req.getName());
//        }
//        wrapper.eq(Fields::getType, req.getType());
//        wrapper.orderByDesc(Fields::getCtime);
//        Page<Fields> fieldsPage = this.baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);
//
//        List<FieldsResp> fieldsResps = fieldsPage.getRecords().stream().map(x -> {
//
//            FieldsResp resp = new FieldsResp();
//            resp.setId(x.getId().toString());
//            resp.setName(x.getName());
//            return resp;
//        }).collect(Collectors.toList());
//
//        return new PageResult<>(fieldsPage.getTotal(), fieldsResps);
//    }
//
//    @Override
//    public String create(FieldsReq req) {
//
//        //指定字段组
//        if(StringUtils.isNotBlank(req.getId())) {
//            Fields fields = this.baseMapper.selectById(req.getId());
//            if(fields != null) {
//                throw new CheckException("字段组不存在");
//            }
//            if(StringUtils.isNotBlank(fields.getFieldList())) {
//                //获取原有字段列表
//                List<FieldsBo> fieldsBos = GsonUtils.gson.fromJson(fields.getFieldList(), new TypeToken<ArrayList<FieldsBo>>() {
//                }.getType());
//
//                List<FieldsBo> bos = GsonUtils.gson.fromJson(req.getFieldList(), new TypeToken<ArrayList<FieldsBo>>() {
//                }.getType());
//
//                fieldsBos.addAll(bos);
//                fields.setFieldList(GsonUtils.gson.toJson(fieldsBos));
//                fields.setUtime(LocalDateTime.now());
//                fields.setUpdateUserId(Long.valueOf(UserInfoContext.getUserId() == null ? "-1" : UserInfoContext.getUserId()));
//                this.updateById(fields);
//
//            } else {
//                //新建字段
//                fields.setFieldList(req.getFieldList());
//                fields.setType(Integer.parseInt(req.getType()));
//                fields.setCtime(LocalDateTime.now());
//                fields.setUtime(LocalDateTime.now());
//                fields.setCreateUserId(Long.valueOf(UserInfoContext.getUserId() == null ? "-1" : UserInfoContext.getUserId()));
//                fields.setUpdateUserId(Long.valueOf(UserInfoContext.getUserId() == null ? "-1" : UserInfoContext.getUserId()));
//                this.save(fields);
//            }
//        }
//
//        return null;
//    }
//
//    @Override
//    public String updateField(FieldsReq req) {
//        Fields fields = this.baseMapper.selectById(req.getId());
//        if(fields != null) {
//            throw new CheckException("字段组不存在");
//        }
//
//        if(StringUtils.isNotBlank(fields.getName())){
//
//        }
//
//        if(StringUtils.isNotBlank(req.getGroupId())) {
//
//        }
//
//        return null;
//    }
}
