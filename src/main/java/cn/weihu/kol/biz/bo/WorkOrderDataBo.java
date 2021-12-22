package cn.weihu.kol.biz.bo;

import lombok.Data;

/**
 * 工单数据对象, 全字段 39 个    逻辑字段 3 个
 */
@Data
public class WorkOrderDataBo {

        // 序号
        String index;

        // 媒体
        String media;

        // 账号
        String account;

        // 账号ID或链接
        String IDorLink;

        // 账号类型
        String accountType;

        // 资源位置
        String address;

        // 数量
        String count;

        // 发布开始时间
        String postStartTime;

        // 发布结束时间
        String postEndTime;

        // 电商链接
        String linkPrice;

        // @
        String at;

        // 话题
        String topic;

        // 电商肖像授权
        String storeAuth;

        // 品牌双微转发授权
        String shareAuth;

        // 微任务
        String microTask;

        // 报备
        String report;

        // 信息流授权
        String msgAuth;

        // 线下探店
        String face;

        // 星图/ 快接单
        String star;

        // 其他
        String other;

        // 产品提供方
        String product;

        // 发布内容brief概述
        String brief;

        // 星图/ 快接单平台截图
        String img;

        // 截图时间
        String imgTime;

        // 星图/ 快接单平台报价
        String platPrice;

        // 粉丝数
        String fansCount;

        // 报价
        String price;

        // 佣金
        String commission;

        // 备注
        String remark;

        // 供应商
        String supplier;

        // 档期开始时间
        String scheduleStartTime;

        // 档期结束时间
        String scheduleEndTime;

        // 历史报价
        String history;

        // 反馈
        String feedback;

        // 保价到期时间
        String priceTime;

        // 入库时间
        String inTime;

        // 电商授权
        String retailAuth;

        // 肖像授权
        String portAuth;

        // 折扣
        String sale;

        /**
         * 逻辑字段
         */

        // 是否 库内 "0" 库内  "1" 库外
        String inbound;

        // 达人唯一标识
        String actorSn;

        // 二选一分组使用标识
        String compareFlag;
}
