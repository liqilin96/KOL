package cn.weihu.kol.controller;


import cn.weihu.base.result.CheckUtil;
import cn.weihu.base.result.PageResult;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.MessageBiz;
import cn.weihu.kol.http.req.MessageReq;
import cn.weihu.kol.http.resp.MessageResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 消息记录表 前端控制器
 * </p>
 *
 * @author Lql
 * @since 2021-11-12
 */
@RestController
@RequestMapping("/message")
@Api(value = "消息处理", tags = "消息处理")
public class MessageController {


    @Autowired
    private MessageBiz messageBiz;

    @ApiOperation(value = "查看历史消息", httpMethod = "GET", notes = "查看历史消息")
    @GetMapping(value = "/page")
    public ResultBean<PageResult<MessageResp>> Page(MessageReq req) {
        return new ResultBean<>(messageBiz.MessagePage(req));
    }

    @ApiOperation(value = "消息提醒", httpMethod = "GET", notes = "消息提醒")
    @GetMapping(value = "/remind")
    public ResultBean<List<MessageResp>> messageRemind() {
        return new ResultBean<>(messageBiz.messageRemind());
    }

    @ApiOperation(value = "查看消息", httpMethod = "GET", notes = "查看消息")
    @GetMapping(value = "/check")
    public ResultBean<MessageResp> check(MessageReq req) {
        CheckUtil.notNull(req.getId(), "消息序号不能为空");
        return new ResultBean<>(messageBiz.check(req.getId()));
    }


}

