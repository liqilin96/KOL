package cn.weihu.kol.biz;

import cn.weihu.base.result.PageResult;
import cn.weihu.kol.db.po.Message;
import cn.weihu.kol.http.req.MessageReq;
import cn.weihu.kol.http.resp.MessageResp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 消息记录表 服务类
 * </p>
 *
 * @author Lql
 * @since 2021-11-12
 */
public interface MessageBiz extends IService<Message> {

    PageResult<MessageResp> MessagePage(MessageReq req);

    List<MessageResp> messageRemind();

    MessageResp check(String id);
}
