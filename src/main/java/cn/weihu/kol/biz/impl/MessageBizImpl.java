package cn.weihu.kol.biz.impl;

import cn.weihu.base.exception.CheckException;
import cn.weihu.base.result.PageResult;
import cn.weihu.kol.biz.MessageBiz;
import cn.weihu.kol.db.dao.MessageDao;
import cn.weihu.kol.db.po.Message;
import cn.weihu.kol.http.req.MessageReq;
import cn.weihu.kol.http.resp.MessageResp;
import cn.weihu.kol.userinfo.UserInfoContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 消息记录表 服务实现类
 * </p>
 *
 * @author Lql
 * @since 2021-11-12
 */
@Service
public class MessageBizImpl extends ServiceImpl<MessageDao, Message> implements MessageBiz {


    @Override
    public PageResult<MessageResp> MessagePage(MessageReq req) {

        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getToUserId, UserInfoContext.getUserId());

        Page<Message> page = this.baseMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), wrapper);

        List<MessageResp> messageRespList = page.getRecords().stream().map(x -> {
            MessageResp resp = new MessageResp();
            BeanUtils.copyProperties(resp, x);
            return resp;
        }).collect(Collectors.toList());


        return new PageResult<>(page.getTotal(), messageRespList);
    }

    @Override
    public List<MessageResp> messageRemind() {

        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        //“1” 为报价到期提醒 , 0是未读消息
        wrapper.eq(Message::getType, "1").eq(Message::getIsReceived, 0);


        List<Message> messages = this.baseMapper.selectList(wrapper);

        List<MessageResp> messageRespList = messages.stream().map(x -> {
            MessageResp resp = new MessageResp();
            BeanUtils.copyProperties(resp, x);
            return resp;
        }).collect(Collectors.toList());

        return messageRespList;
    }

    @Override
    public MessageResp check(String id) {

        Message msg = getById(id);
        if(msg == null) {
            throw new CheckException("消息不存在");
        }
        MessageResp resp = new MessageResp();
        msg.setIsReceived(1);
        BeanUtils.copyProperties(msg, resp);
        updateById(msg);
        return resp;
    }
}
