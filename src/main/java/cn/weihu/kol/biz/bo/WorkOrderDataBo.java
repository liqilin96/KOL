package cn.weihu.kol.biz.bo;

import lombok.Data;

/**
 * 工单数据对象, 全字段 39 个    逻辑字段 3 个
 */
@Data
public class WorkOrderDataBo {

        // 序号
        private String index;

        // 媒体
        private String media;

        // 账号
        private String account;

        // 账号ID或链接
        private String IDorLink;

        // 账号类型
        private String accountType;

        // 资源位置
        private String address;

        // 数量
        private String count;

        // 发布开始时间
        private String postStartTime;

        // 发布结束时间
        private String postEndTime;

        // 电商链接
        private String linkPrice;

        // @
        private String at;

        // 话题
        private String topic;

        // 电商肖像授权
        private String storeAuth;

        // 品牌双微转发授权
        private String shareAuth;

        // 微任务
        private String microTask;

        // 报备
        private String report;

        // 信息流授权
        private String msgAuth;

        // 线下探店
        private String face;

        // 星图/ 快接单
        private String star;

        // 其他
        private String other;

        // 产品提供方
        private String product;

        // 发布内容brief概述
        private String brief;

        // 星图/ 快接单平台截图
        private String img;

        // 截图时间
        private String imgTime;

        // 星图/ 快接单平台报价
        private String platPrice;

        // 粉丝数
        private String fansCount;

        // 报价
        private String price;

        // 佣金
        private String commission;

        // 备注
        private String remark;

        // 供应商
        private String supplier;

        // 档期开始时间
        private String scheduleStartTime;

        // 档期结束时间
        private String scheduleEndTime;

        // 历史报价
        private String history;

        // 反馈
        private String feedback;

        // 保价到期时间
        private String priceTime;

        // 入库时间
        private String inTime;

        // 电商授权
        private String retailAuth;

        // 肖像授权
        private String portAuth;

        // 折扣
        private String sale;

        // 仅保价一天
        private String priceOnlyDay;

        /**
         * 逻辑字段
         */

        // 是否 库内 "0" 库内  "1" 库外
        private String inbound;

        // 达人唯一标识
        private String actorSn;

        // 二选一分组使用标识
        private String compareFlag;
}
